package servicetest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apiguardian.api.API;

import adapters.EventPublisher;
import adapters.RabbitMqEventPublisher;
import adapters.RabbitMqFacade;
import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import dto.AccountTokensDto;
import dto.RegistrationDto;
import dto.Report;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import service.CorrelationId;
import service.CustomerFacadeService;

public class CustomerSteps {
    MessageQueue mockedQueue;
    MessageQueue queue;
    EventPublisher publisher;
    CustomerFacadeService service;
    RabbitMqFacade facade;

    String resultOfCall;
    AccountTokensDto resultOfCallTokens;
    List<Report> resultOfCallReport;

    @Before
    public void setUp() { 
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();
        publisher = new RabbitMqEventPublisher(mockedQueue);
        service = new CustomerFacadeService(publisher);
        facade = new RabbitMqFacade(queue, service);
    }

    @Given("an API call for registering a customer")
    public void apiCallToRegister() throws InterruptedException {
        new Thread(() -> {
            RegistrationDto dto = new RegistrationDto();
            resultOfCall = service.create(dto);
            
        }).start();
        Thread.sleep(1500);
    }

    @Then("the CustomerRegistrationRequested event is emitted")
    public void CustomerRegistrationRequestedEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerRegistrationRequested")));
    }

    @And("when the corresponding CustomerRegistered is received")
    public void CustomerRegisteredReceived() {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event("AccountRegistered",  new Object[] { "customerId", new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("the customer id is returned")
    public void customerIdIsReturned() {
        assertEquals("customerId", resultOfCall);
    }

    ///
    /// 
    /// 
    /// 


    @And("when the corresponding AccountRegistrationFailed is received with payload {string}")
    public void CustomerRegisteredReceived(String expectedPayload) {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event("AccountRegistrationFailed",  new Object[] { expectedPayload, new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("the payload {string} is returned")
    public void customerIdIsReturned(String expectedPayload) {
        assertEquals(expectedPayload, resultOfCall);
    }


    ///
    /// 
    /// 
    /// 
    

    @Given("an API call to delete a customer")
    public void an_api_call_to_delete_a_customer() throws InterruptedException {
        new Thread(() -> {
            resultOfCall = service.deregister("customerId");
        }).start();
        Thread.sleep(1500);
    }
    
    @Then("the CustomerDeregistrationRequested event is emitted")
    public void the_customer_deregistration_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerDeregistrationRequested")));
    }
    @Then("when the correspoding AccountDeregistered event is received with payload {string}")
    public void when_the_correspoding_account_deregistered_event_is_received_with_payload(String expectedPayload) {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event("AccountDeregistered",  new Object[] { expectedPayload, new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }


    ///
    /// 
    /// 
    /// 


    @Given("an API call to lookup a customer")
    public void an_api_call_to_lookup_a_customer() throws InterruptedException {
        new Thread(() -> {
            resultOfCallTokens = service.get("customerId");
        }).start();
        Thread.sleep(1500);
    }

    @Then("the CustomerRetrievalRequested event is emitted")
    public void the_customer_retrieval_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerRetrievalRequested")));
    }

    @Then("when the correspoding CustomerRetrieved event is received")
    public void when_the_correspoding_customer_retrieved_event_is_received() {
        var correlations = service.getAllCorrelations().stream().toList();
        var expectedPayload = new AccountTokensDto();
        expectedPayload.setAccountId("customerId");
        expectedPayload.setTokens(new ArrayList<>());
        expectedPayload.getTokens().add("token1");

        queue.publish(new Event("CustomerRetrieved",  new Object[] { expectedPayload, new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("the customer tokens are returned")
    public void the_customer_tokens_are_returned() {
        assertEquals("token1", resultOfCallTokens.getTokens().get(0));
    }


    ///
    /// 
    /// 
    /// 


    @Given("an API call to generate tokens for customer")
    public void an_api_call_to_generate_tokens_for_customer() throws InterruptedException {
        new Thread(() -> {
            resultOfCall = service.createTokens("customerId", "4");
        }).start();
        Thread.sleep(1500);
    }

    @Then("the CustomerTokensRequested event is emitted")
    public void the_customer_tokens_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerTokensRequested")));
    }

    @Then("when the correspoding {string} event is received")
    public void when_the_correspoding_tokens_created_event_is_received(String eventName) {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event(eventName,  new Object[] { new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("a {string} status is returned")
    public void a_status_is_returned(String status) throws InterruptedException {
        Thread.sleep(1000);
        assertEquals(status, resultOfCall);
    }


    ///
    /// 
    /// 
    /// 


    @Given("an API call to get the customer report")
    public void an_api_call_to_get_the_customer_report() throws InterruptedException {
        new Thread(() -> {
            resultOfCallReport = service.getReport("customerId");
        }).start();
        Thread.sleep(1500);
    }

    @Then("the CustomerReportRequested event is emitted")
    public void the_customer_report_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerReportRequested")));
    }

    @Then("when the correspoding CustomerReportSent event is received")
    public void when_the_correspoding_customer_report_sent_event_is_received() {
        var correlations = service.getAllCorrelations().stream().toList();

        Report dto = new Report();
        dto.setAmount(10);
        dto.setMerchantId("merchantId");
        dto.setUsedToken("token");

        var list = new ArrayList<>();
        list.add(dto);

        queue.publish(new Event("CustomerReportSent",  new Object[] { list, new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("a report is returned")
    public void a_report_is_returned() {
        assertEquals(1, resultOfCallReport.size());
    }
}

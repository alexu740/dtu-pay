package servicetest;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import adapters.EventPublisher;
import adapters.RabbitMqEventPublisher;
import adapters.RabbitMqFacade;
import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import dto.PaymentDto;
import dto.RegistrationDto;
import dto.Report;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import service.CorrelationId;
import service.MerchantFacadeService;

public class MerchantSteps {
   MessageQueue mockedQueue;
    MessageQueue queue;
    EventPublisher publisher;
    MerchantFacadeService service;
    RabbitMqFacade facade;

    String resultOfCall;
    List<Report> resultOfCallReport;

    @Before
    public void setUp() { 
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();
        publisher = new RabbitMqEventPublisher(mockedQueue);
        service = new MerchantFacadeService(publisher);
        facade = new RabbitMqFacade(queue, service);
    }
 

    @Given("an API call for registering a merchant")
    public void apiCallToRegister() throws InterruptedException {
        new Thread(() -> {
            RegistrationDto dto = new RegistrationDto();
            resultOfCall = service.create(dto);
            
        }).start();
        Thread.sleep(1500);
    }

    @Then("the MerchantRegistrationRequested event is emitted")
    public void MerchantRegistrationRequestedEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("MerchantRegistrationRequested")));
    }

    @And("when the corresponding MerchantRegistered is received")
    public void MerchantRegisteredReceived() {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event("AccountRegistered",  new Object[] { "merchantId", new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("the merchant id is returned")
    public void merchantIdIsReturned() {
        assertEquals("merchantId", resultOfCall);
    }

    @Then("the payload {string} is returned")
    public void merchantIdIsReturned(String expectedPayload) {
        assertEquals(expectedPayload, resultOfCall);
    }


    ///
    /// 
    /// 
    /// 
    

    @Given("an API call to delete a merchant")
    public void an_api_call_to_delete_a_merchant() throws InterruptedException {
        new Thread(() -> {
            resultOfCall = service.deregister("merchantId");
        }).start();
        Thread.sleep(1500);
    }
    
    @Then("the MerchantDeregistrationRequested event is emitted")
    public void the_merchant_deregistration_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("MerchantDeregistrationRequested")));
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
    


    @Given("an API call to get the merchant report")
    public void an_api_call_to_get_the_merchant_report() throws InterruptedException {
        new Thread(() -> {
            resultOfCallReport = service.getReport("merchantId");
        }).start();
        Thread.sleep(1500);
    }

    @Then("the MerchantReportRequested event is emitted")
    public void the_merchant_report_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("MerchantReportRequested")));
    }

    @Then("when the correspoding MerchantReportSent event is received")
    public void when_the_correspoding_merchant_report_sent_event_is_received() {
        var correlations = service.getAllCorrelations().stream().toList();

        Report dto = new Report();
        dto.setAmount(10);
        dto.setUsedToken("token");

        var list = new ArrayList<>();
        list.add(dto);

        queue.publish(new Event("MerchantReportSent",  new Object[] { list, new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("a report is returned")
    public void a_report_is_returned() {
        assertEquals(1, resultOfCallReport.size());
    }

   //
   //
   //
   //


    @Given("an API call initiate payment")
    public void an_api_call_initiate_payment() throws InterruptedException {
        new Thread(() -> {
            PaymentDto pay = new PaymentDto();
            pay.setAmount(10);
            pay.setCustomerId("customerId");
            pay.setMerchantId("merchantId");
            pay.setToken("token");

            resultOfCall = service.initialisePayment(pay);
        }).start();
        Thread.sleep(1500);
    }

    @Then("the PaymentRequested event is emitted")
    public void the_payment_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentRequested")));
    }

    @Then("when the correspoding PaymentSucceeded event is received")
    public void when_the_correspoding_payment_succeeded_event_is_received() {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event("PaymentSucceeded",  new Object[] { "", new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("when the correspoding PaymentFailed event is received")
    public void when_the_correspoding_payment_failed_event_is_received() {
        var correlations = service.getAllCorrelations().stream().toList();
        queue.publish(new Event("PaymentFailed",  new Object[] { new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("the payment is successful")
    public void the_payment_is_successful() {
        assertEquals("successful", resultOfCall);
    }

    @Then("the payment is failed")
    public void the_payment_is_failed() {
        assertEquals("failed", resultOfCall);
    }
}

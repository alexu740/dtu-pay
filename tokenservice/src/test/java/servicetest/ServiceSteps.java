package servicetest;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import boilerplate.implementations.RabbitMqQueue;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tokenservice.adapters.EventPublisher;
import tokenservice.adapters.RabbitMqEventPublisher;
import tokenservice.adapters.RabbitMqFacade;
import tokenservice.repositories.TokenRepository;
import tokenservice.service.CorrelationId;
import tokenservice.service.TokenService;
/**
 * @author: Senhao Zou, s242606
 */
public class ServiceSteps {
    MessageQueue mockedQueue;
    MessageQueue queue;

    TokenRepository repo;

    EventPublisher publisher;
    TokenService service;
    TokenService mockedService;
    RabbitMqFacade facade;
    
    Event event;
    CorrelationId correlationId;

    @Before
    public void setUp() {
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();

        publisher = new RabbitMqEventPublisher(mockedQueue);
        repo = new TokenRepository();

        service = new TokenService(publisher, repo);
        mockedService = mock(TokenService.class);
        facade = new RabbitMqFacade(queue, mockedService);
        correlationId = CorrelationId.randomId();
    }

    @Given("a PaymentInitialised event is received")
    public void aPaymentInitialisedEventIsReceived() {
        event = new Event("PaymentInitialised", new Object[] { "customerId", "token", correlationId, "transaction" });
        queue.publish(event);
    }

    @Then("the application service executes a handlePaymentInitialised command")
    public void aPaymentInitializeCommandIsExecuted() {
        verify(mockedService).handlePaymentInitialised(any(String.class), any(String.class), any(CorrelationId.class), any(String.class));
        service.handlePaymentInitialised("customerId", "token", correlationId, "transaction");
    }

    @And("a CustomerHasTokenCheckRequested event is emitted")
    public void paymentIsInitialised() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerHasTokenCheckRequested")));
    }

    //

    @Given("a CustomerHasTokenChecked event is received")
    public void CustomerHasTokenCheckedEventIsReceived() {
        event = new Event("CustomerHasTokenChecked", new Object[] { "customerId", "token", true, correlationId, "transaction" });
        queue.publish(event);
    }

    @Then("the application service executes a handleCustomerHasTokenChecked command")
    public void ahandleCustomerHasTokenCheckedCommandIsExecuted() {
        verify(mockedService).handleCustomerHasTokenChecked(any(String.class), any(String.class), anyBoolean(), any(CorrelationId.class), any(String.class));
        repo.put("token", "validating");
        service.handleCustomerHasTokenChecked("customerId", "token", true, correlationId, "transaction");
    }

    @And("a TokenValidated event is emitted")
    public void TokenValidatedIsEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokenValidated")));
    }

    @And("a TokenUsed event is emitted")
    public void TokenUsedIsEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokenUsed")));
    }

    //

    @Then("the application service executes a handleCustomerHasTokenChecked command where the token is invalid")
    public void ahandleCustomerHasTokenCheckedCommandWithInvalidTokenIsExecuted() {
        verify(mockedService).handleCustomerHasTokenChecked(any(String.class), any(String.class), anyBoolean(), any(CorrelationId.class), any(String.class));
        repo.put("token", "validating");
        service.handleCustomerHasTokenChecked("customerId", "token", false, correlationId, "transaction");
    }

    @And("a TokenValidationFailed event is emitted")
    public void TokenValidationFailedIsEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokenValidationFailed")));
    }
}

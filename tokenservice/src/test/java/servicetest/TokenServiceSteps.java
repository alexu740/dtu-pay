package servicetest;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.mockito.internal.matchers.Null;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import boilerplate.implementations.RabbitMqQueue;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tokenservice.adapters.EventPublisher;
import tokenservice.adapters.RabbitMqEventPublisher;
import tokenservice.adapters.RabbitMqFacade;
import tokenservice.service.CorrelationId;
import tokenservice.service.TokenService;

public class TokenServiceSteps {
    MessageQueue queue = mock(RabbitMqQueue.class);

    EventPublisher publisher = new RabbitMqEventPublisher(queue);
    TokenService service = new TokenService(publisher);
    RabbitMqFacade facade = new RabbitMqFacade(queue, service);

    CorrelationId correlationId = new CorrelationId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d"));
    Event event; 

    @When("a {string} event is received to initiate token validation")
    public void PaymentInitialised_event_without_token_is_received(String string) {
        facade.handlePaymentInitialised(new Event(string, new Object[] { "customerId", "token", correlationId, "transactionId" }));
    }
    @Then("a {string} event is sent to check if customer has token")
    public void CustomerHasTokenCheckRequest_is_sent(String string) {
        verify(queue).publish(argThat(event -> 
            event.getType().equals(string) &&
            event.getArgument(0, String.class).equals("customerId") &&
            event.getArgument(1, String.class).equals("token") &&
            event.getArgument(3, String.class).equals("transactionId")
        ));
    }
    
    @When("a token is marked as validating")
    public void token_marked_as_validating(){
        service.handlePaymentInitialised("random", "token", correlationId, "random");
    }

    @Then("a {string} event is sent to fail the token validation")
    public void TokenValidationFailed_is_sent(String string) {
        verify(queue).publish(argThat(event -> 
            event.getType().equals(string)&&
            event.getArgument(0, String.class).equals("transactionId")
        ));
    }




    @When("a {string} event is received with token present on customer account")
    public void CustomerHasTokenChecked_is_received_with_isPresent_true(String string) {
        facade.handleCustomerHasTokenChecked(new Event(string, new Object[] { "customerId", "token", true, correlationId, "transactionId" }));
    }
    @Then("a {string} is sent to validate token")
    public void TokenValidated_is_sent(String string) {
        verify(queue).publish(argThat(event -> 
            event.getType().equals(string) &&
            event.getArgument(0, String.class).equals("customerId") &&
            event.getArgument(1, String.class).equals("token") &&
            event.getArgument(3, String.class).equals("transactionId")
        ));
    }
    @Then("a {string} event is sent to mark token as used")
    public void TokenUsed_event_is_sent(String string) {
        verify(queue).publish(argThat(event ->
            event.getType().equals(string) &&
            event.getArgument(0, String.class).equals("customerId") &&
            event.getArgument(1, String.class).equals("token")
        ));
    }
    
    

    @When("a {string} event is received with token not present on customer account")
    public void CustomerHasTokenChecked_is_received_with_isPresent_false(String string) {
        facade.handleCustomerHasTokenChecked(new Event(string, new Object[] { "customerId", "token", false, correlationId, "transactionId" }));

    }
    @Then("a {string} event sent to indicate validation failure")
    public void TokenValidateFailure_event_sent_isPresent_false(String string) {
        verify(queue).publish(argThat(event -> 
            event.getType().equals(string)&&
            event.getArgument(0, String.class).equals("transactionId")
        ));
    }


}

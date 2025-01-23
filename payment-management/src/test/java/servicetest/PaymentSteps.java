package servicetest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;
import micro.adapters.EventPublisher;
import micro.adapters.ExternalBank;
import micro.adapters.FastMoneyBank;
import micro.adapters.RabbitMqEventPublisher;
import micro.adapters.RabbitMqFacade;
import micro.aggregate.Payment;
import micro.commands.CommandFactory;
import micro.commands.InitializePaymentCommand;
import micro.dto.PaymentDto;
import micro.events.PaymentResolved;
import micro.repositories.PaymentReadModelRepository;
import micro.repositories.PaymentRepository;
import micro.service.CorrelationId;
import micro.service.PaymentManagementService;

public class PaymentSteps {
    MessageQueue mockedQueue;
    MessageQueue queue;

    PaymentRepository repo;

    PaymentManagementService service;
    PaymentManagementService mockedService;
    RabbitMqEventPublisher pub;

    ExternalBank bank;

    RabbitMqFacade facade;
    Event event;
    CorrelationId correlationId;

    PaymentDto dto;
    Payment paymentTransaction;
    @Before
    public void setUp() {
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();
        
        repo = new PaymentRepository(mockedQueue);
		PaymentReadModelRepository rm = new PaymentReadModelRepository(queue);
		pub = new RabbitMqEventPublisher(mockedQueue);
		
        bank = mock(FastMoneyBank.class);

		service = new PaymentManagementService(repo, rm, pub, bank);
        mockedService = mock(PaymentManagementService.class);

        facade = new RabbitMqFacade(queue, mockedService);
        correlationId = CorrelationId.randomId();
    }

    // 
    @Given("a payment is requested with the PaymentRequested event")
    public void aPaymentIsRequested() {
        dto = new PaymentDto();
        dto.setAmount(10);
        dto.setCustomerId("null");
        dto.setMerchantId("null");
        //dto.setPaymentId("null");
        dto.setToken("null");

        event = new Event("PaymentRequested", new Object[] { dto, correlationId });
        queue.publish(event);
    }

    @Then("the application service executes a PaymentInitializationCommand")
    public void aPaymentInitializeCommandIsExecuted() {
        verify(mockedService).handlePaymentInitializationCommand(any(InitializePaymentCommand.class), any(CorrelationId.class));
        service.handlePaymentInitializationCommand(CommandFactory.createInitializePaymentCommand(dto), correlationId);
    }

    @And("a PaymentInitialised event is emitted")
    public void paymentIsInitialised() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentInitialised")));
    }
    //

    @Given("a TokenValidated event is received")
    public void tokenValidateEventReceived() {
        event = new Event("TokenValidated", new Object[] { "customerid", "token", correlationId, "transaction" });
        queue.publish(event);
    }

    @Then("the application service executes a handleTokenValidated command")
    public void tokenValidatedCommandExecuted() {
        verify(mockedService).handleTokenValidated(any(String.class),any(String.class),any(CorrelationId.class),any(String.class));
    }

    @And("an internal PaymentTokenValidated and outgoing PaymentInformationResolutionRequested events are emitted") 
    public void paymentTokenValidatedAndPaymentInfoResolution() {
        var pay = Payment.create("customerId", "merchantId", "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", 10, correlationId);
        repo.save(pay);

        service.handleTokenValidated("customerId", "merchantId", correlationId, pay.getTransactionId());
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentTokenValidated")));

        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentInformationResolutionRequested")
        && event.getArgument(1, String.class).equals("customerId")
        && event.getArgument(2, String.class).equals("merchantId")));
    }

    // 

    @Given("a TokenValidationFailed event is received")
    public void aTokenValidationFailedEventIsReceived() {
        event = new Event("TokenValidationFailed", new Object[] { "transaction", correlationId });
        queue.publish(event);
    }

    @Then("the application service executes a handlePaymentTokenValidationFailed command")
    public void handlePaymentTokenValidationFailedCommandExecuted() {
        verify(mockedService).handlePaymentTokenValidationFailed(any(String.class),any(CorrelationId.class));
    }

    @And("then a PaymentFailed event is emitted")
    public void paymentFailedIsEmitted() {
        var pay = Payment.create("customerId", "merchantId", "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", 10, correlationId);
        repo.save(pay);

        service.handlePaymentTokenValidationFailed(pay.getTransactionId(), correlationId);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentFailed")));
    }

    //
    @Given("a PaymentInformationResolved event is received")
    public void aPaymentInformationResolvedEventIsReceived() {
        event = new Event("PaymentInformationResolved", new Object[] { "transaction", "customerBank", "merchantBank", correlationId });
        queue.publish(event);
    }

    @Then("the application service executes a handlePaymentInformationResolved command")
    public void handlePaymentInformationResolvedCommandExecuted() {
        verify(mockedService).handlePaymentInformationResolved(any(String.class),any(String.class),any(String.class),any(CorrelationId.class));
    }

    @And("then the payment is updated with the bank information")
    public void paymentIsUpdated() {
        var pay = Payment.create("customerId", "merchantId", "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", 10, correlationId);
        repo.save(pay);

        service.handlePaymentInformationResolved(pay.getTransactionId(), "customerBank", "merchantBank", correlationId);
        
        var updatedPay = repo.getById(pay.getTransactionId());
        assertEquals("customerBank", updatedPay.getCustomerBankAccount());
        assertEquals("merchantBank", updatedPay.getMerchantBankAccount());
    }

    @And("a PaymentResolved event is emitted")
    public void paymentResolvedIsEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentResolved")));
    }

    //

    @Given("a PaymentResolved event is received")
    public void aPaymentResolvedEventIsReceived() {
        event = new PaymentResolved("transaction", "customerBank", "merchantBank", correlationId);
        queue.publish(event);
    }

    @Then("the application service executes a handlePaymentTransaction command")
    public void handlePaymentTransactionCommandExecuted() {
        verify(mockedService).handlePaymentTransaction(any(String.class), any(CorrelationId.class));
        paymentTransaction = Payment.create("customerId", "merchantId", "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", 10, correlationId);
        paymentTransaction.getAppliedEvents().add(new PaymentResolved(paymentTransaction.getTransactionId(), "customerBank", "merchantBank", correlationId));
        repo.save(paymentTransaction);
    }

    @And("if the bank accepts the payment")
    public void bankAcceptsPayment() {
        when(bank.pay("customerBank", "merchantBank", 10, paymentTransaction.generatePaymentNote())).thenReturn(true);
    }

    @And("if the bank rejects the payment")
    public void bankRejectedPayment() {
        when(bank.pay("customerBank", "merchantBank", 10, paymentTransaction.generatePaymentNote())).thenReturn(false);
    }

    @And("the payment is processed")
    public void paymentIsProcessed() {
        service.handlePaymentTransaction(paymentTransaction.getTransactionId(), correlationId);
    }

    @Then("a PaymentSucceeded event is emitted")
    public void paymentSucceededIsEmitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentSucceeded")));
    }

    @Then("a PaymentFailed event is emitted")
    public void paymentFailedIsEmitted2() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("PaymentFailed")));
    }

}

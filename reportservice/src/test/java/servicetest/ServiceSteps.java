package servicetest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import reportservice.adapters.EventPublisher;
import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;
import reportservice.dto.Payment;
import reportservice.repositories.ReadModelRepository;
import reportservice.repositories.ReportRepository;
import reportservice.services.CorrelationId;
import reportservice.services.Service;

public class ServiceSteps {
    MessageQueue mockedQueue;
    MessageQueue queue;

    ReportRepository repo;
    ReadModelRepository readModel;

    EventPublisher publisher;
    Service service;
    Service mockedService;
    
    RabbitMqFacade facade;
    
    Event event;
    CorrelationId correlationId;

    Payment payment;
    @Before
    public void setUp() {
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();
        
        publisher = new RabbitMqEventPublisher(mockedQueue);
        repo = new ReportRepository(queue);
        readModel = new ReadModelRepository(queue);

        service = new Service(repo, readModel, publisher);
        mockedService = mock(Service.class);

        facade = new RabbitMqFacade(queue, mockedService);
        correlationId = CorrelationId.randomId();


        payment = new Payment();
        payment.setAmount(10);
        payment.setCustomerId("customerId");
        payment.setMerchantId("merchantId");
        payment.setCustomerToken("token");
        payment.setPaymentId("payId");
    }

    @Given("a PaymentSucceeded event is received")
    public void aPaymentInitialisedEventIsReceived() {        
        event = new Event("PaymentSucceeded", new Object[] { payment });
        queue.publish(event);
    }

    @Then("the application service executes a handlePaymentReceived command")
    public void ahandlePaymentReceivedCommandIsExecuted() {
        verify(mockedService).handlePaymentReceived(any(Payment.class));
        var TempService = new Service(new ReportRepository(mockedQueue), readModel, publisher);
        TempService.handlePaymentReceived(payment);
    }

    @And("the payment is saved")
    public void paymentIsInitialised() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("ReportGenerated")));
    }

    // 

    @Given("a CustomerReportRequested event is received")
    public void aCustomerReportRequestedEventIsReceived() {    
        event = new Event("CustomerReportRequested", new Object[] { "customerId", correlationId });
        queue.publish(event);
    }

    @Then("the application service executes a handleCustomerReportRequested query")
    public void handleCustomerReportRequestedQueryIsExecuted() {
        verify(mockedService).handleCustomerReportRequested(any(CorrelationId.class), any(String.class));

        service.handlePaymentReceived(payment);
        service.handleCustomerReportRequested(correlationId, "customerId");
    }

    @And("the CustomerReportSent event is emitted")
    public void CustomerReportSentIsEmitted() {
        assertEquals(1, readModel.getPaymentsByCustomerID("customerId").size());
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("CustomerReportSent")));
    }

    // 

    @Given("a MerchantReportRequested event is received")
    public void aMerchantReportRequestedEventIsReceived() {    
        event = new Event("MerchantReportRequested", new Object[] { "merchantId", correlationId });
        queue.publish(event);
    }

    @Then("the application service executes a handleMerchantReportRequested query")
    public void handleMerchantReportRequestedQueryIsExecuted() {
        verify(mockedService).handleMerchantReportRequested(any(CorrelationId.class), any(String.class));

        service.handlePaymentReceived(payment);
        service.handleMerchantReportRequested(correlationId, "merchantId");
    }

    @And("the MerchantReportSent event is emitted")
    public void MerchantReportSentIsEmitted() {
        assertEquals(1, readModel.getPaymentsByMerchantID("merchantId").size());
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("MerchantReportSent")));
    }

    // 

    @Given("a ManagerReportRequested event is received")
    public void aManagerReportRequestedEventIsReceived() {    
        event = new Event("ManagerReportRequested", new Object[] { correlationId });
        queue.publish(event);
    }

    @Then("the application service executes a handleManagerReportRequested query")
    public void handlePaymentReportRequestedQueryIsExecuted() {
        verify(mockedService).handlePaymentReportRequested(any(CorrelationId.class));

        service.handlePaymentReceived(payment);
        service.handlePaymentReportRequested(correlationId);
    }

    @And("the ManagerReportSent event is emitted")
    public void ManagerReportSentIsEmitted() {
        assertEquals(1, readModel.getManagerReport().size());
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("ManagerReportSent")));
    }
}

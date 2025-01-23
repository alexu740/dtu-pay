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
import dto.Report;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import service.CorrelationId;
import service.ManagerFacadeService;

public class ManagerSteps {
    MessageQueue mockedQueue;
    MessageQueue queue;
    EventPublisher publisher;
    ManagerFacadeService service;
    RabbitMqFacade facade;

    String resultOfCall;
    List<Report> resultOfCallReport;

    @Before
    public void setUp() { 
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();
        publisher = new RabbitMqEventPublisher(mockedQueue);
        service = new ManagerFacadeService(publisher);
        facade = new RabbitMqFacade(queue, service);
    }
 

    @Given("an API call to get the manager report")
    public void an_api_call_to_get_the_manager_report() throws InterruptedException {
        new Thread(() -> {
            resultOfCallReport = service.getReport();
        }).start();
        Thread.sleep(1500);
    }

    @Then("the ManagerReportRequested event is emitted")
    public void the_manager_report_requested_event_is_emitted() {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("ManagerReportRequested")));
    }

    @Then("when the correspoding ManagerReportSent event is received")
    public void when_the_correspoding_manager_report_sent_event_is_received() {
        var correlations = service.getAllCorrelations().stream().toList();

        Report dto = new Report();
        dto.setAmount(10);
        dto.setUsedToken("token");
        dto.setCustomerId("customerId");
        dto.setMerchantId("ManagerId");

        var list = new ArrayList<>();
        list.add(dto);

        queue.publish(new Event("ManagerReportSent",  new Object[] { list, new CorrelationId(UUID.fromString(correlations.get(0))) }));
    }

    @Then("a report is returned")
    public void a_report_is_returned() throws InterruptedException {
        Thread.sleep(1500);
        assertEquals(1, resultOfCallReport.size());
    }
}

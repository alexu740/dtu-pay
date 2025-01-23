package servicetest;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.UUID;


import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.RabbitMqQueue;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;
import reportservice.dto.Payment;
import reportservice.repositories.ReadModelRepository;
import reportservice.repositories.ReportRepository;
import reportservice.repositories.valueobjects.CustomerReport;
import reportservice.repositories.valueobjects.ManagerReport;
import reportservice.repositories.valueobjects.MerchantReport;
import reportservice.services.CorrelationId;
import reportservice.services.Service;

public class ReportServiceSteps {
    MessageQueue queue = mock(RabbitMqQueue.class);

    ReportRepository repo = new ReportRepository(queue);
    ReadModelRepository readRepo = new ReadModelRepository(queue);
    RabbitMqEventPublisher publisher = new RabbitMqEventPublisher(queue);

    Service service = new Service(repo, readRepo, publisher);
    RabbitMqFacade facade = new RabbitMqFacade(queue, service);
    CorrelationId correlationId = CorrelationId.randomId();


    @When("a {string} event is received to indicate a successful payment")
    public void PaymentSucceeded_is_received_to_indicate_a_successful_payment(String string) {
        var payment = new Payment();

        payment.setCustomerId("customer-1");
        payment.setCustomerToken("token");
        payment.setPaymentId("payment-1");;
        payment.setMerchantId("merchant-1");
        payment.setAmount(1000);
        var event = new Event(string, new Object[] { payment });
        queue.publish(event);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    @Then("a {string} event is sent to show that the report was generated")
    public void ReportGenerated_is_sent_to_show_that_the_report_was_generated(String string) {

        var payment = new Payment();

        payment.setCustomerId("customer-1");
        payment.setCustomerToken("token");
        payment.setPaymentId("payment-1");;
        payment.setMerchantId("merchant-1");
        payment.setAmount(1000);

        verify(queue).publish(argThat(event -> 
        event.getType().equals(string) &&
        event.getArgument(0, Payment.class).equals(payment)
    ));
    }
    
        
    @Given("that a successful payment occured in the past")
    public void that_a_successful_payment_occured_in_the_past() {
        var payment = new Payment();

        payment.setCustomerId("customer-1");
        payment.setCustomerToken("token");
        payment.setPaymentId("payment-1");;
        payment.setMerchantId("merchant-1");
        payment.setAmount(1000);
        var event = new Event("PaymentSucceeded", new Object[] { payment });
        queue.publish(event);

        payment.setCustomerId("customer-1");
        payment.setCustomerToken("token2");
        payment.setPaymentId("payment-2");;
        payment.setMerchantId("merchant-2");
        payment.setAmount(2000);

        event = new Event("PaymentSucceeded", new Object[] { payment });
        queue.publish(event);

        payment.setCustomerId("customer-2");
        payment.setCustomerToken("token3");
        payment.setPaymentId("payment-3");;
        payment.setMerchantId("merchant-1");
        payment.setAmount(3000);

        event = new Event("PaymentSucceeded", new Object[] { payment });
        queue.publish(event);
    }
    
      
    @When("a {string} event is received from a merchant requesting a report")
    public void MerchantReportRequested_is_received(String string) {
        var event = new Event(string, new Object[] { correlationId, "merchant-1" });
        queue.publish(event);

    }

    @Then("a {string} event is sent to show the token and amount of that past payment")
    public void MerchantReportSent_is_sent(String string) {
        var report = new MerchantReport[3];
        report[0] = new MerchantReport();
        report[1] = new MerchantReport();
        report[2] = new MerchantReport();
        report[0].setUsedToken("token");
        report[0].setAmount(1000);
        report[2].setUsedToken("token3");
        report[2].setAmount(3000);

        // verify(queue).publish(argThat(event -> 
        //     event.getType().equals(string) &&
        //     event.getArgument(0, Payment.class).equals(report)
        // ));

        verify(queue).publish(new Event(string, new Object[] { report, correlationId}));
    }
          
    @When("a {string} event is received from a customer requesting a report")
    public void CustomerReportRequested_is_received(String string) {
        var event = new Event(string, new Object[] { correlationId, "customer-1"});
        queue.publish(event);
    }

    @Then("a {string} event is sent to show the token, merchantId and amount of that past payment")
    public void CustomerReportSent_is_sent(String string) {
        var report = new CustomerReport[3];
        report[0] = new CustomerReport();
        report[1] = new CustomerReport();
        report[2] = new CustomerReport();
        report[0].setUsedToken("token");
        report[0].setMerchantId("merchant-1");
        report[0].setAmount(1000);
        report[1].setUsedToken("token2");
        report[1].setMerchantId("merchant-2");
        report[1].setAmount(2000);

        verify(queue).publish(argThat(event -> 
            event.getType().equals(string) &&
            event.getArgument(0, CustomerReport.class).equals(report)
        ));
    }
    
        
    @When("a {string} event is received from a manager requesting a report")
    public void ManagerReportRequested_is_received(String string) {
        var event = new Event(string, new Object[] { correlationId });
        queue.publish(event);
    }

    @Then("a {string} event is sent to show the customerId, token, merchantId and amount of that past payment")
    public void ManagerReportSent_is_sent(String string) {
        var report = new ManagerReport[3];
        report[0] = new ManagerReport();
        report[1] = new ManagerReport();
        report[2] = new ManagerReport();


        report[0].setCustomerId("customer-1");
        report[0].setTokenUsed("token");
        report[0].setMerchantId("merchant-1");
        report[0].setAmount(1000);
        report[1].setCustomerId("customer-1");
        report[1].setTokenUsed("token2");
        report[1].setMerchantId("merchant-2");
        report[1].setAmount(2000);
        report[2].setCustomerId("customer-2");
        report[2].setTokenUsed("token3");
        report[2].setMerchantId("merchant-1");
        report[2].setAmount(3000);

        verify(queue).publish(argThat(event -> 
            event.getType().equals(string) &&
            event.getArgument(0, ManagerReport.class).equals(report)
        ));
   }
}

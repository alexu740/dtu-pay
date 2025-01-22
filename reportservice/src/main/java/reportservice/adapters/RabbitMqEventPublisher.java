package reportservice.adapters;

import reportservice.repositories.valueobjects.CustomerReport;
import reportservice.repositories.valueobjects.ManagerReport;
import reportservice.repositories.valueobjects.MerchantReport;
import reportservice.services.CorrelationId;

import java.util.List;

import boilerplate.Event;
import boilerplate.MessageQueue;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    @Override
    public void emitManagerReportGenerated(List<ManagerReport> report, CorrelationId correlationId) {
        Event ev = new Event("ManagerReportSent", new Object[] { report, correlationId });
        this.queue.publish(ev);
    }

    @Override
    public void emitMerchantReportGenerated(List<MerchantReport> report, CorrelationId correlationId) {
        Event ev = new Event("MerchantReportSent", new Object[] { report, correlationId });
        this.queue.publish(ev);
    } 

    @Override
    public void emitCustomerReportGenerated(List<CustomerReport> report, CorrelationId correlationId) {
        Event ev = new Event("CustomerReportSent", new Object[] { report, correlationId });
        this.queue.publish(ev);
    }
}

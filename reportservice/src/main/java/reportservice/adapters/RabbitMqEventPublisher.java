package reportservice.adapters;

import reportservice.adapters.EventPublisher;
import reportservice.boilerplate.Event;
import reportservice.boilerplate.MessageQueue;
import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

import java.util.List;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitPaymentStorageSucceededEvent(CorrelationId correlationId) {
        Event ev = new Event("payment.storage.succeeded", new Object[] { correlationId });
        queue.publish(ev);
    }

    public void emitPaymentStorageFailedEvent(CorrelationId correlationId,Exception e) {
        Event ev = new Event("payment.storage.failed", new Object[] { correlationId,e });
        queue.publish(ev);
    }

    public void emitPaymentReportSucceededEvent(CorrelationId correlationId,List<Payment> transactions) {
        Event  ev = new Event("payments.report.succeeded", new Object[] { correlationId, transactions });
        queue.publish(ev);
    }

    public void emitPaymentReportFailedEvent(CorrelationId correlationId,Exception e) {
        Event ev = new Event("payments.report.failed", new Object[] { correlationId,e });
        queue.publish(ev);
    }
    public void emitMerchantReportSucceededEvent(CorrelationId correlationId,List<Payment> transactions) {
        Event  ev = new Event("payments.report.succeeded", new Object[] { correlationId, transactions });
        queue.publish(ev);
    }
    public void emitMerchantReportFailedEvent(CorrelationId correlationId,Exception e) {
        Event  ev = new Event("payments.report.failed", new Object[] { correlationId, e });
        queue.publish(ev);
    }





}

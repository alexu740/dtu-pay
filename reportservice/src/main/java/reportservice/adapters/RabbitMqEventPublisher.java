package reportservice.adapters;

import reportservice.dto.Payment;
import reportservice.models.CustomerPaymentViewModel;
import reportservice.models.MerchantPaymentViewModel;
import reportservice.services.CorrelationId;

import java.util.List;

import boilerplate.Event;
import boilerplate.MessageQueue;

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

    public void emitPaymentReportSucceededEvent(CorrelationId correlationId,List<Payment> payments) {
        Event  ev = new Event("payments.report.succeeded", new Object[] { correlationId, payments });
        queue.publish(ev);
    }

    public void emitPaymentReportFailedEvent(CorrelationId correlationId,Exception e) {
        Event ev = new Event("payments.report.failed", new Object[] { correlationId,e });
        queue.publish(ev);
    }
    public void emitMerchantReportSucceededEvent(CorrelationId correlationId,List<MerchantPaymentViewModel> payments) {
        Event  ev = new Event("payments.report.succeeded", new Object[] { correlationId, payments });
        queue.publish(ev);
    }
    public void emitMerchantReportFailedEvent(CorrelationId correlationId,Exception e) {
        Event  ev = new Event("payments.report.failed", new Object[] { correlationId, e });
        queue.publish(ev);
    }
    public void emitCustomerReportSucceededEvent(CorrelationId correlationId,List<CustomerPaymentViewModel> payments) {
        Event  ev = new Event("payments.report.succeeded", new Object[] { correlationId, payments });
        queue.publish(ev);
    }
    public void emitCustomerReportFailedEvent(CorrelationId correlationId,Exception e) {
        Event  ev = new Event("payments.report.failed", new Object[] { correlationId, e });
        queue.publish(ev);
    }






}

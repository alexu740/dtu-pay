package adapters;

import adapters.EventPublisher;
import boilerplate.Event;
import boilerplate.MessageQueue;
import dto.PaymentDto;
import dto.RegistrationDto;
import service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitCreateUserEvent(RegistrationDto payload, CorrelationId correlationId) {
        Event event = new Event("MerchantRegistrationRequested", new Object[] { payload, correlationId });
        queue.publish(event);
    }

    public void emitUnregisterUserEvent(String merchantId, CorrelationId correlationId) {
        Event event = new Event("MerchantDeregistrationRequested", new Object[] { merchantId, correlationId });
        queue.publish(event);
    }

    public void emitInitialisePayment(PaymentDto payload, CorrelationId correlationId) {
        Event event = new Event("PaymentRequested", new Object[] { payload, correlationId });
        queue.publish(event);
    }

    public void emitMerchantReportRequested(String merchantId, CorrelationId correlationId) {
        Event event = new Event("MerchantReportRequested", new Object[] { merchantId, correlationId });
        queue.publish(event);
    }
}

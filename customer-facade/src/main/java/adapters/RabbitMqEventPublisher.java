package adapters;

import adapters.EventPublisher;
import boilerplate.Event;
import boilerplate.MessageQueue;
import dto.RegistrationDto;
import service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitRetrieveUserEvent(String id, CorrelationId correlationId) {
        Event event = new Event("CustomerRetrievalRequested", new Object[] { id, correlationId });
        queue.publish(event);
    }

    public void emitCreateUserEvent(RegistrationDto payload, CorrelationId correlationId) {
        Event event = new Event("CustomerRegistrationRequested", new Object[] { payload, correlationId });
        queue.publish(event);
    }

    public void emitCreateTokensEvent(String customerId, String tokenNumber, CorrelationId correlationId) {
        Event event = new Event("CustomerTokensRequested", new Object[] { customerId, tokenNumber, correlationId });
        queue.publish(event);
    }

    public void emitUnregisterUserEvent(String customerId, CorrelationId correlationId) {
        Event event = new Event("CustomerDeregistrationRequested", new Object[] { customerId, correlationId });
        queue.publish(event);
    }
    public void emitReportRequestEvent(String customerId, CorrelationId correlationId) {
        Event event = new Event("CustomerReportRequested", new Object[] { customerId, correlationId });
        queue.publish(event);
    }

    @Override
    public void emitCustomerReportRequested(String customerId, CorrelationId correlationId) {
        Event event = new Event("CustomerReportRequested", new Object[] { customerId, correlationId });
        queue.publish(event);
    }
}

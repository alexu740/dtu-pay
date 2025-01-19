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

    public void emitUnregisterUserEvent() {

    }
}

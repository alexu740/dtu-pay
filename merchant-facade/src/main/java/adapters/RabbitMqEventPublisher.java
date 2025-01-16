package adapters;

import adapters.EventPublisher;
import messaging.Event;
import messaging.MessageQueue;
import service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitCreateUserEvent(String payload, CorrelationId correlationId) {
        Event event = new Event("TestEvent", new Object[] { payload, correlationId });
        queue.publish(event);
    }

    public void emitUnregisterUserEvent() {

    }
}

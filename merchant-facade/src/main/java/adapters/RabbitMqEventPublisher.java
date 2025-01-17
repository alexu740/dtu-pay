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

    public void emitCreateUserEvent(RegistrationDto payload, CorrelationId correlationId) {
        Event event = new Event("MerchantRegistrationRequested", new Object[] { payload, correlationId });
        queue.publish(event);
    }

    public void emitUnregisterUserEvent() {

    }
}

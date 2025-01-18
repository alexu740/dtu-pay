package adapters;

import adapters.EventPublisher;
import boilerplate.Event;
import boilerplate.MessageQueue;
import dto.Payment;
import impl.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitCreateUserEvent() {
        //Event event = new Event("MerchantRegistrationRequested", new Object[] { payload, correlationId });
        //queue.publish(event);
    }

    public void emitUnregisterUserEvent() {

    }
}

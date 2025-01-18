package reportservice.adapters;

import reportservice.adapters.EventPublisher;
import reportservice.boilerplate.Event;
import reportservice.boilerplate.MessageQueue;
import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

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

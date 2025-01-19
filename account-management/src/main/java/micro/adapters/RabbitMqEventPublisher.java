package micro.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import micro.repositories.viewmodel.TokenViewModel;
import micro.service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;

    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitAccountTokensRequestedEvent(TokenViewModel vm, CorrelationId correlationId) {
        queue.publish(new Event("CustomerRetrieved", new Object[] { vm, correlationId }));
    }

    public void emitAccountTokensRequestRejectedEvent(CorrelationId correlationId) {
        queue.publish(new Event("CustomerRetrieved", new Object[] { correlationId }));
    }
}

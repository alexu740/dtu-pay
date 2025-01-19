package tokenservice.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import tokenservice.service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;

    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitCustomerHasTokenCheckRequested(String customerId, String token, CorrelationId correlationId, String transactionId) {
        queue.publish(new Event("CustomerHasTokenCheckRequested", new Object[] { customerId, token, correlationId, transactionId}));
    }

    public void emitTokenValidated(String customerId, String token, CorrelationId correlationId, String transactionId) {
        queue.publish(new Event("TokenValidated", new Object[] { customerId, token, correlationId, transactionId}));
    }
}

package tokenservice.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import tokenservice.service.CorrelationId;
/**
 * @author: Senhao Zou, s242606
 */
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

    public void emitTokenValidationFailed(String transactionId, CorrelationId correlationId) {
        queue.publish(new Event("TokenValidationFailed", new Object[] { transactionId, correlationId}));
    }

    public void emitTokenUsed(String customerId, String token) {
        queue.publish(new Event("TokenUsed", new Object[] { customerId, token}));
    }
}

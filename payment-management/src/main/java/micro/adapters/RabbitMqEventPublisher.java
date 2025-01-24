package micro.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import micro.service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;

    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitPaymentInformationResolutionRequested(String transactionId, String customerId, String merchantId, CorrelationId correlationId) {
        queue.publish(new Event("PaymentInformationResolutionRequested", new Object[] { transactionId, customerId, merchantId, correlationId }));
    }
}

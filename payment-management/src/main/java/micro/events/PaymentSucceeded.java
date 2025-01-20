package micro.events;

import micro.service.CorrelationId;

public class PaymentSucceeded extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724489L;

    public PaymentSucceeded(String transactionId, CorrelationId correlationId) {
        super("PaymentSucceeded", new Object[] { correlationId }, transactionId);
    }
}

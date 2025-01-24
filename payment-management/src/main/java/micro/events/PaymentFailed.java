package micro.events;

import micro.service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class PaymentFailed extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724489L;

    public PaymentFailed(String transactionId, CorrelationId correlationId) {
        super("PaymentFailed", new Object[] { correlationId }, transactionId);
    }
}

package micro.events;

import micro.dto.PaymentDto;
import micro.service.CorrelationId;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class PaymentSucceeded extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724489L;

    public PaymentSucceeded(String transactionId, PaymentDto payment, CorrelationId correlationId) {
        super("PaymentSucceeded", new Object[] { payment, correlationId }, transactionId);
    }
}

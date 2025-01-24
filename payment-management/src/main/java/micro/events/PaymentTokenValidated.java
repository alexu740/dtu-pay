package micro.events;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class PaymentTokenValidated extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724487L;

    public PaymentTokenValidated(String transactionId) {
        super("PaymentTokenValidated", new Object[] { }, transactionId);
    }
}

package micro.repositories;

import boilerplate.MessageQueue;
import micro.aggregate.Payment;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class PaymentRepository {
	
	private EventStore eventStore;

	public PaymentRepository(MessageQueue bus) {
		eventStore = new EventStore(bus);
	}

	public Payment getById(String transactionId) {
		return Payment.createFromEvents(eventStore.getEventsFor(transactionId));
	}
	
	public void save(Payment payment) {
		eventStore.addEvents(payment.getTransactionId(), payment.getAppliedEvents());
		payment.clearAppliedEvents();
	}
}

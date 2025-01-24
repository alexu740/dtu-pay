package micro.repositories;

import boilerplate.Event;
import boilerplate.MessageQueue;
import micro.events.PaymentInitialised;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class PaymentReadModelRepository {
	public PaymentReadModelRepository(MessageQueue eventQueue) {
		eventQueue.addHandler("PaymentInitialised", this::applyPaymentInitialised);
	}
	
	public void applyPaymentInitialised(Event e) {
		var event = (PaymentInitialised) e;
	}
}

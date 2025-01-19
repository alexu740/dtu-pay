package micro.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import boilerplate.Event;
import boilerplate.Message;
import boilerplate.MessageQueue;
import micro.events.PaymentInitialised;

public class PaymentReadModelRepository {
	//private Map<String, TokenViewModel> tokens = new HashMap<>();
	//private Map<String, PaymentInstrumentViewModel> paymentInstruments = new HashMap<>();

	public PaymentReadModelRepository(MessageQueue eventQueue) {
		eventQueue.addHandler("PaymentInitialised", this::applyPaymentInitialised);
	}
	
	public void applyPaymentInitialised(Event e) {
		var event = (PaymentInitialised) e;
		var accountId = event.getTransactionId();
	}
}

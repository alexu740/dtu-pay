package micro.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import boilerplate.MessageQueue;
import micro.events.DomainEvent;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class EventStore {
	private Map<String, List<DomainEvent>> store = new ConcurrentHashMap<>();

	private MessageQueue eventBus;

	public EventStore(MessageQueue bus) {
		this.eventBus = bus;
	}

	public void addEvent(DomainEvent event) {
		System.out.println("Adding new event to payment store");
		var transactionId = event.getTransactionId();
		if (!store.containsKey(transactionId)) {
			store.put(transactionId, new ArrayList<DomainEvent>());
		}
		store.get(transactionId).add(event);
		System.out.println("Pushing event to queue");
		eventBus.publish(event);
	}
	
	public Stream<DomainEvent> getEventsFor(String id) {
		if (!store.containsKey(id)) {
			store.put(id, new ArrayList<DomainEvent>());
		}
		return store.get(id).stream();
	}

	public void addEvents(String transactionId, List<DomainEvent> appliedEvents) {
		appliedEvents.stream().forEach(e -> addEvent(e));
	}
}

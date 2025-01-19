package micro.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import lombok.NonNull;
import boilerplate.MessageQueue;
import micro.events.DomainEvent;

public class EventStore {
	private Map<String, List<DomainEvent>> store = new ConcurrentHashMap<>();

	private MessageQueue eventBus;

	public EventStore(MessageQueue bus) {
		this.eventBus = bus;
	}

	public void addEvent(DomainEvent event) {
		System.out.println("Adding new event to store");
		var accountId = event.getAccountId().getUuid().toString();
		if (!store.containsKey(accountId)) {
			store.put(accountId, new ArrayList<DomainEvent>());
		}
		store.get(accountId).add(event);
		System.out.println("Pushing event to queue");
		eventBus.publish(event);
	}
	
	public Stream<DomainEvent> getEventsFor(String id) {
		if (!store.containsKey(id)) {
			store.put(id, new ArrayList<DomainEvent>());
		}
		return store.get(id).stream();
	}

	public void addEvents(@NonNull String accountid, List<DomainEvent> appliedEvents) {
		appliedEvents.stream().forEach(e -> addEvent(e));
	}
}

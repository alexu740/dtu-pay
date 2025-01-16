package micro.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import lombok.NonNull;
import messaging.MessageQueue;
import micro.aggregate.AccountId;
import micro.events.Event;

public class EventStore {

	private Map<AccountId, List<Event>> store = new ConcurrentHashMap<>();

	private MessageQueue eventBus;

	public EventStore(MessageQueue bus) {
		this.eventBus = bus;
	}

	public void addEvent(AccountId id, Event event) {
		if (!store.containsKey(event.getAccountId())) {
			store.put(event.getAccountId(), new ArrayList<Event>());
		}
		store.get(event.getAccountId()).add(event);
		//eventBus.publish(event);
	}
	
	public Stream<Event> getEventsFor(AccountId id) {
		if (!store.containsKey(id)) {
			store.put(id, new ArrayList<Event>());
		}
		return store.get(id).stream();
	}

	public void addEvents(@NonNull AccountId accountid, List<Event> appliedEvents) {
		appliedEvents.stream().forEach(e -> addEvent(accountid, e));
	}

}

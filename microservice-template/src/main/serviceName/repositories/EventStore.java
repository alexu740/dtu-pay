package usermanagement.domain.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import lombok.NonNull;
import messaging.MessageQueue;
import usermanagement.domain.aggregate.UserId;
import usermanagement.domain.events.Event;

public class EventStore {

	private Map<UserId, List<Event>> store = new ConcurrentHashMap<>();

	private MessageQueue eventBus;

	public EventStore(MessageQueue bus) {
		this.eventBus = bus;
	}

	public void addEvent(UserId id, Event event) {
		if (!store.containsKey(event.getUserId())) {
			store.put(event.getUserId(), new ArrayList<Event>());
		}
		store.get(event.getUserId()).add(event);
		eventBus.publish(event);
	}
	
	public Stream<Event> getEventsFor(UserId id) {
		if (!store.containsKey(id)) {
			store.put(id, new ArrayList<Event>());
		}
		return store.get(id).stream();
	}

	public void addEvents(@NonNull UserId userid, List<Event> appliedEvents) {
		appliedEvents.stream().forEach(e -> addEvent(userid, e));
	}

}

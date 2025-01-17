package micro.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import lombok.NonNull;
import boilerplate.MessageQueue;
import micro.aggregate.AccountId;
import boilerplate.Event;

public class EventStore {

	private Map<String, List<Event>> store = new ConcurrentHashMap<>();

	private MessageQueue eventBus;

	public EventStore(MessageQueue bus) {
		this.eventBus = bus;
	}

	public void addEvent(Event event) {
		System.out.println("adding event");
		if (!store.containsKey(event.getArgument(0, String.class))) {
			store.put(event.getArgument(0, String.class), new ArrayList<Event>());
		}
		store.get(event.getArgument(0, String.class)).add(event);
		System.out.println("Pushing event to queue" + event.getType());
		eventBus.publish(event);
	}
	
	public Stream<Event> getEventsFor(AccountId id) {
		if (!store.containsKey(id.getUuid())) {
			store.put(id.getUuid(), new ArrayList<Event>());
		}
		return store.get(id.getUuid()).stream();
	}

	public void addEvents(@NonNull AccountId accountid, List<Event> appliedEvents) {
		appliedEvents.stream().forEach(e -> addEvent(e));
	}

}

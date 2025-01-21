package boilerplate.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import boilerplate.Event;
import boilerplate.MessageQueue;

public class MessageQueueSync implements MessageQueue  {

	private Map<String,List<Consumer<Event>>> subscribers;

	public MessageQueueSync() {
		subscribers = new ConcurrentHashMap<>();
	}

	private void notifySubscribers(Event m) {
		subscribers.getOrDefault(m.getType(), new ArrayList<Consumer<Event>>())
			.forEach(a -> a.accept(m));
	}

	@Override
	public void publish(Event event) {
		notifySubscribers(event);
	}

	@Override
	public void addHandler(String topic, Consumer<Event> handler) {
		if (!subscribers.containsKey(topic)) {
			subscribers.put(topic, new ArrayList<Consumer<Event>>());
		}
		subscribers.get(topic).add(handler);
	}
}

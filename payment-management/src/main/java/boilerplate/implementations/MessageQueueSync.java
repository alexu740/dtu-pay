package boilerplate.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import boilerplate.Message;
import boilerplate.MessageQueue;

public class MessageQueueSync /*implements MessageQueue */ {
/*
	private Map<Class<?>,List<Consumer<Message>>> subscribers = new ConcurrentHashMap<>();

	private void notifySubscribers(Message m) {
		subscribers.getOrDefault(m.getClass(), new ArrayList<Consumer<Message>>())
			.forEach(a -> a.accept(m));
	}

	@Override
	public void publish(Message message) {
		notifySubscribers(message);
	}

	@Override
	public void addHandler(Class<? extends Message> event, Consumer<Message> handler) {
		if (!subscribers.containsKey(event)) {
			subscribers.put(event, new ArrayList<Consumer<Message>>());
		}
		subscribers.get(event).add(handler);
	}
		 */
}

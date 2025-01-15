package messaging.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import messaging.Message;
import messaging.MessageQueue;

public class MessageQueueAsync implements MessageQueue {

	private Map<Class<?>,List<Consumer<Message>>> subscribers = new HashMap<>();
	private final BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
	private Thread notificationThread = null;
	
	public MessageQueueAsync() {
		notificationThread = new Thread(() -> {
			notifySubscribers();
		});
		notificationThread.start();
	}

	@Override
	public void addHandler(Class<? extends Message> event, Consumer<Message> handler) {
		if (!subscribers.containsKey(event)) {
			subscribers.put(event, new ArrayList<Consumer<Message>>());
		}
		subscribers.get(event).add(handler);
	}

	@Override
	public void publish(Message m) {
		try {
			queue.put(m);
		} catch (InterruptedException e) {
			throw new Error();
		}
	}
	
	private void notifySubscribers() {
		while (true) {
			Message m;
			try {
				m = queue.take();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
			subscribers.getOrDefault(m.getClass(), new ArrayList<Consumer<Message>>())
				.forEach(a -> a.accept(m));
		}
	}
	

}

package micro.startup;

import java.io.IOException;
import java.util.function.Consumer;
import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import boilerplate.Event;
import boilerplate.implementations.RabbitMqQueue;
import micro.events.AccountRegistered;
import micro.events.TokenAdded;
import micro.events.TokenRemoved;

public class EnrichedRabbitMqQueue extends RabbitMqQueue {
    public EnrichedRabbitMqQueue(String hostname) {
        super(hostname);
    }
    
    @Override
	public void addHandler(String topic, Consumer<Event> handler) {
		var chan = super.setUpChannel();
		try {
			String queueName = chan.queueDeclare().getQueue();
			chan.queueBind(queueName, super.EXCHANGE_NAME, topic);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");

				Event event;
				if (topic.equals("AccountRegistered")) {
					event = new Gson().fromJson(message, AccountRegistered.class);
				} 
				else if (topic.equals("TokenAdded")) {
					event = new Gson().fromJson(message, TokenAdded.class);
				}
				else if (topic.equals("TokenRemoved")) {
					event = new Gson().fromJson(message, TokenRemoved.class);
				}
				else {
					event = new Gson().fromJson(message, Event.class);
				}
				handler.accept(event);
			};
			chan.basicConsume(queueName, true, deliverCallback, consumerTag -> {
			});
		} catch (IOException e1) {
			throw new Error(e1);
		}
	}
}

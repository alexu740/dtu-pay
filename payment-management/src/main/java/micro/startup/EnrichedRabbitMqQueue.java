package micro.startup;

import java.io.IOException;
import java.util.function.Consumer;
import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import boilerplate.Event;
import boilerplate.implementations.RabbitMqQueue;
import micro.events.PaymentInitialised;
import micro.events.PaymentResolved;
import micro.events.PaymentTokenValidated;

/**
 * @author: Alex Ungureanu (s225525)
 */

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
				if (topic.equals("PaymentInitialised")) {
					event = new Gson().fromJson(message, PaymentInitialised.class);
				} else 
				if (topic.equals("PaymentTokenValidated")) {
					event = new Gson().fromJson(message, PaymentTokenValidated.class);
				} else 
				if (topic.equals("PaymentResolved")) {
					event = new Gson().fromJson(message, PaymentResolved.class);
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

package reportservice.startup;

import boilerplate.implementations.RabbitMqQueue;
import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;
import reportservice.repositories.Repository;
import reportservice.services.Service;

public class ApplicationFactory {
    public static RabbitMqFacade createApplication(String queueName) {
		var mq = new RabbitMqQueue(queueName);
		Repository repo = new Repository();
		var publisher = new RabbitMqEventPublisher(mq);
		var service = new Service(repo,publisher);

		return new RabbitMqFacade(mq, service);
    }
}

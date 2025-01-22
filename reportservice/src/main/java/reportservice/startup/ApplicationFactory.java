package reportservice.startup;

import boilerplate.implementations.RabbitMqQueue;
import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;
import reportservice.repositories.ReadModelRepository;
import reportservice.repositories.ReportRepository;
import reportservice.services.Service;

public class ApplicationFactory {
    public static RabbitMqFacade createApplication(String queueName) {
		var mq = new RabbitMqQueue(queueName);
		ReportRepository repo = new ReportRepository(mq);
		ReadModelRepository readRepo = new ReadModelRepository(mq);

		var publisher = new RabbitMqEventPublisher(mq);
		var service = new Service(repo, readRepo, publisher);

		return new RabbitMqFacade(mq, service);
    }
}

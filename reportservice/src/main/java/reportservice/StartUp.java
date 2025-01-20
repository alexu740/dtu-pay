package reportservice;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.RabbitMqQueue;
import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;
import reportservice.lib.IRepository;
import reportservice.repositories.Repository;
import reportservice.services.Service;

public class StartUp {

	private Service service;
	private RabbitMqFacade facade;

	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {

		System.out.println("STARTING the Report Service");
		Thread.sleep(10000);
		var mq = new RabbitMqQueue("rabbitMq");
		Repository repo = new Repository();
		var publisher = new RabbitMqEventPublisher(mq);

		service = new Service(repo,publisher);

		facade = new RabbitMqFacade(mq, service);

		Thread.currentThread().join();

	}
}

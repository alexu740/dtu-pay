package reportservice;

import reportservice.boilerplate.implementations.RabbitMqQueue;
import reportservice.boilerplate.MessageQueue;
import reportservice.boilerplate.Event;

import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;

import reportservice.impl.Repository;
import reportservice.impl.Service;

import reportservice.lib.IRepository;

public class StartUp {

	private Service service;
	private RabbitMqFacade facade;

	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {

		System.out.println("STARTING the Account Management Service");
		Thread.sleep(10000);
		var mq = new RabbitMqQueue("rabbitMq");
		IRepository repo = new Repository();
		var publisher = new RabbitMqEventPublisher(mq);

		service = new Service(mq, repo,publisher);

		facade = new RabbitMqFacade(mq, service);

		Thread.currentThread().join();

	}
}

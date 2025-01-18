package reportservice;

import boilerplate.implementations.RabbitMqQueue;

import reportservice.adapters.RabbitMqEventPublisher;
import reportservice.adapters.RabbitMqFacade;

import reportservice.impl.Repository;
import reportservice.impl.Service;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;

import reportservice.lib.Factory;

public class StartUp {
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

		return service;
	}
}

package micro;

import boilerplate.implementations.RabbitMqQueue;
import micro.adapters.RabbitMqFacade;
import micro.repositories.AccountRepository;
import micro.service.*;

public class StartUp {
	private static RabbitMqFacade facade;
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("STARTING the Account Management Service");
		Thread.sleep(10000);
		var mq = new RabbitMqQueue("rabbitMq");
		AccountRepository repo = new AccountRepository(mq);
		var service = new AccountManagementService(repo, null);
		facade = new RabbitMqFacade(mq, service);
		Thread.currentThread().join();
	}
}
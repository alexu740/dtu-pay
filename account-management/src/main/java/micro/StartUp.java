package micro;

import messaging.implementations.RabbitMqQueue;
import micro.adapters.RabbitMqFacade;
import micro.service.*;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("STARTING the Account Management Service");
		var mq = new RabbitMqQueue("dtupay");
		var service = new AccountManagementService(null, null);
		new RabbitMqFacade(mq, service);
	}
}
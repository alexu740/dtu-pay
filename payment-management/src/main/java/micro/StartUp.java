package micro;

import micro.adapters.RabbitMqFacade;
import micro.startup.ApplicationFactory;

public class StartUp {
	private static RabbitMqFacade facade;
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("STARTING the Payment Management Service");
		facade = ApplicationFactory.createApplication("rabbitMq");
	}
}
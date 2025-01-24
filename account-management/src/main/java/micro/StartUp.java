package micro;

import micro.adapters.RabbitMqFacade;
import micro.startup.ApplicationFactory;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class StartUp {
	private static RabbitMqFacade facade;
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("STARTING the Account Management Service");
		facade = ApplicationFactory.createApplication("rabbitMq");
	}
}
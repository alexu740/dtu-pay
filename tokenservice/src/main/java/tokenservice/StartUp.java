package tokenservice;

import tokenservice.adapters.RabbitMqFacade;
import tokenservice.startup.ApplicationFactory;
/**
 * @author: Senhao Zou, s242606
 */
public class StartUp {
	RabbitMqFacade facade;
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("STARTING the token validation service");
		facade = ApplicationFactory.createApplication();
	}
}

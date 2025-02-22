package reportservice;

import reportservice.adapters.RabbitMqFacade;
import reportservice.startup.ApplicationFactory;
/**
 * @author: Nicolas Venizelou, s232523
 */
public class StartUp {
	private RabbitMqFacade facade;

	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("STARTING the Report Service");
		facade = ApplicationFactory.createApplication("rabbitMq");
	}
}

package reportservice;

import reportservice.lib.Factory;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		new Factory().getService();


	}
}

package tokenservice;

import tokenservice.lib.Factory;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		Thread.sleep(20000);
		new Factory().getService();
	}
}

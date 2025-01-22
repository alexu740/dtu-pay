package micro.repositories;

import boilerplate.MessageQueue;
import micro.aggregate.Account;
import micro.aggregate.CustomerAccount;

public class AccountRepository {
	
	private EventStore eventStore;

	public AccountRepository(MessageQueue bus) {
		eventStore = new EventStore(bus);
	}

	public Account getById(String userId) {
		return CustomerAccount.createFromEvents(eventStore.getEventsFor(userId));
	}
	
	public void save(Account account) {
		eventStore.addEvents(account.getAccountid().getUuid().toString(), account.getAppliedEvents());
		account.clearAppliedEvents();
	}
}

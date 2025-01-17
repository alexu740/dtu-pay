package micro.repositories;

import boilerplate.MessageQueue;
import micro.aggregate.Account;
import micro.aggregate.AccountId;

public class AccountRepository {
	
	private EventStore eventStore;

	public AccountRepository(MessageQueue bus) {
		eventStore = new EventStore(bus);
	}

	public Account getById(AccountId userId) {
		return Account.createFromEvents(eventStore.getEventsFor(userId));
	}
	
	public void save(Account account) {
		eventStore.addEvents(account.getAccountid(), account.getAppliedEvents());
		account.clearAppliedEvents();
	}
}

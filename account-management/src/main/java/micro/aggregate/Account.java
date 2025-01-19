package micro.aggregate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import micro.events.AccountCreated;
import micro.service.CorrelationId;
import boilerplate.Event;


public class Account {
	private AccountId accountid;
	private AccountOwnerDetails ownerDetails;
	private AccountFinancialDetails financialDetails;
	private List<Event> appliedEvents = new ArrayList<Event>();

	private Map<Class<? extends Serializable>, Consumer<Serializable>> handlers = new HashMap<>();

	public static Account create(String firstName, String lastName, String cpr, String bankAccount, CorrelationId correlationId) {
		var accountId = new AccountId(UUID.randomUUID());
		var ownerDetails = new AccountOwnerDetails(firstName, lastName, cpr);
		var financialDetails = new AccountFinancialDetails(bankAccount, null);

		var account = new Account();
		account.accountid = accountId;
		account.ownerDetails = ownerDetails;
		account.financialDetails = financialDetails;

		var event = new AccountCreated(accountId, correlationId);
		event.firstName = firstName;
		event.lastName = lastName;
		event.cpr = cpr;
		event.bankAccount = bankAccount;

		account.appliedEvents.add(event);

		return account;
	}

	public static Account createFromEvents(Stream<Event> events) {
		Account account = new Account();
		account.applyEvents(events);
		return account;
	}

	public Account() {
		registerEventHandlers();
	}

	public AccountId getAccountid() {
		return accountid;
	}

	public List<Event> getAppliedEvents() {
		return appliedEvents;
	}

	private void registerEventHandlers() {
		handlers.put(AccountCreated.class, e -> apply((AccountCreated) e));
	}

	private void applyEvents(Stream<Event> events) throws Error {
		events.forEachOrdered(e -> {
			if (e instanceof AccountCreated)
				this.apply((AccountCreated) e);
		});
		if (this.getAccountid() == null) {
			throw new Error("user does not exist");
		}
	}

	private void apply(AccountCreated event) {
		this.accountid = event.accountId;
		this.ownerDetails = new AccountOwnerDetails(event.firstName, event.lastName, event.cpr);
		this.financialDetails = new AccountFinancialDetails(event.bankAccount, null);
	}

	public void clearAppliedEvents() {
		appliedEvents.clear();
	}
}

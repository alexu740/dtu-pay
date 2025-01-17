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

import boilerplate.Message;
import micro.aggregate.AccountFinancialDetails;
import micro.aggregate.AccountOwnerDetails;
import boilerplate.Event;


public class Account {
	private AccountId accountid;
	private AccountOwnerDetails ownerDetails;
	private AccountFinancialDetails financialDetails;

	public AccountId getAccountid() {
		return accountid;
	}

	//private Set<Contact> contacts = new HashSet<>();
	//private Set<Address> addresses = new HashSet<>();
	
	private List<Event> appliedEvents = new ArrayList<Event>();

	public List<Event> getAppliedEvents() {
		return appliedEvents;
	}

	private Map<Class<? extends Serializable>, Consumer<Serializable>> handlers = new HashMap<>();

	public static Account create(String firstName, String lastName, String cpr, String bankAccount, String correlationId) {
		var accountId = new AccountId(UUID.randomUUID());
		var ownerDetails = new AccountOwnerDetails(firstName, lastName, cpr);
		var financialDetails = new AccountFinancialDetails(bankAccount, null);

		var account = new Account();
		account.accountid = accountId;
		account.ownerDetails = ownerDetails;
		account.financialDetails = financialDetails;

		var event = new Event("AccountCreated", new Object[] { accountId.getUuid(), account, correlationId });
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

	private void registerEventHandlers() {
		//handlers.put(AccountCreated.class, e -> apply((AccountCreated) e));
	}

	/* Business Logic */
	//public void update(Set<Contact> contacts, Set<Address> addresses) {
		//removeOldAdresses(addresses);
		//addNewContacts(contacts);
		//applyEvents(appliedEvents.stream());
	//}

	//private void removeOldAdresses(Set<Address> addresses) {
		//var events = getAddresses().stream().filter(a -> !addresses.contains(a))
		//		.map(address -> (Event)new UserAddressRemoved(userid, address.getCity(), address.getState(),
		//				address.getPostcode()))
		//		.collect(Collectors.toList());
		//appliedEvents.addAll(events);
	//}

	//private void addNewContacts(Set<Contact> contacts) {
		//var events = contacts.stream().filter(c -> !getContacts().contains(c))
		//		.map(contact -> (Event)new UserContactAdded(userid, contact.getType(), contact.getDetail()))
		//		.collect(Collectors.toList());
		//appliedEvents.addAll(events);
	//}
	
	/* Event Handling */

	private void applyEvents(Stream<Event> events) throws Error {
		events.forEachOrdered(e -> {
			this.applyEvent(e);
		});
		if (this.getAccountid() == null) {
			throw new Error("user does not exist");
		}
	}

	private void applyEvent(Event e) {
		//handlers.getOrDefault(e.getClass(), this::missingHandler).accept(e);
	}

	private void missingHandler(Message e) {
		throw new Error("handler for event "+e+" missing");
	}

	private void apply(Event event) {
		//accountid = event.getAccountId();
		//firstname = event.getFirstName();
		//lastname = event.getLastName();
	}

	public void clearAppliedEvents() {
		appliedEvents.clear();
	}
}

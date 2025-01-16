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

import messaging.Message;
import micro.events.AccountCreated;
import micro.events.Event;


public class Account {
	private AccountId accountid;
	public AccountId getAccountid() {
		return accountid;
	}

	private String firstname;
	private String lastname;
	//private Set<Contact> contacts = new HashSet<>();
	//private Set<Address> addresses = new HashSet<>();
	
	private List<Event> appliedEvents = new ArrayList<Event>();

	public List<Event> getAppliedEvents() {
		return appliedEvents;
	}

	private Map<Class<? extends Serializable>, Consumer<Serializable>> handlers = new HashMap<>();

	public static Account create(String firstName, String lastName) {
		var accountId = new AccountId(UUID.randomUUID());
		AccountCreated event = new AccountCreated();
		var account = new Account();
		account.accountid = accountId;
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

	private void apply(AccountCreated event) {
		accountid = event.getAccountId();
		//firstname = event.getFirstName();
		//lastname = event.getLastName();
	}

	public void clearAppliedEvents() {
		appliedEvents.clear();
	}
}

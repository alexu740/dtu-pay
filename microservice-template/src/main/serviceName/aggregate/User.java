package usermanagement.domain.aggregate;

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import messaging.Message;
import usermanagement.domain.events.Event;
import usermanagement.domain.events.UserAddressAdded;
import usermanagement.domain.events.UserAddressRemoved;
import usermanagement.domain.events.UserContactAdded;
import usermanagement.domain.events.UserContactRemoved;
import usermanagement.domain.events.UserCreated;

@Getter
public class User {
	private UserId userid;
	private String firstname;
	private String lastname;
	private Set<Contact> contacts = new HashSet<>();
	private Set<Address> addresses = new HashSet<>();
	
	@Setter(AccessLevel.NONE)
	private List<Event> appliedEvents = new ArrayList<Event>();

	private Map<Class<? extends Message>, Consumer<Message>> handlers = new HashMap<>();

	public static User create(String firstName, String lastName) {
		var userId = new UserId(UUID.randomUUID());
		UserCreated event = new UserCreated(userId, firstName, lastName);
		var user = new User();
		user.userid = userId;
		user.appliedEvents.add(event);
		return user;
	}

	public static User createFromEvents(Stream<Event> events) {
		User user = new User();
		user.applyEvents(events);
		return user;
	}

	public User() {
		registerEventHandlers();
	}

	private void registerEventHandlers() {
		handlers.put(UserCreated.class, e -> apply((UserCreated) e));
		handlers.put(UserContactAdded.class, e -> apply((UserContactAdded) e));
		handlers.put(UserContactRemoved.class, e -> apply((UserContactRemoved) e));
		handlers.put(UserAddressAdded.class, e -> apply((UserAddressAdded) e));
		handlers.put(UserAddressRemoved.class, e -> apply((UserAddressRemoved) e));
	}

	/* Business Logic */
	public void update(Set<Contact> contacts, Set<Address> addresses) {
		addNewAddresses(addresses);
		removeOldAdresses(addresses);
		addNewContacts(contacts);
		removeOldContacts(contacts);
		applyEvents(appliedEvents.stream());
	}

	private void addNewAddresses(Set<Address> addresses) {
		var events = addresses.stream().filter(a -> !getAddresses().contains(a))
				.map(address -> (Event)new UserAddressAdded(userid, address.getCity(), address.getState(),
						address.getPostcode()))
				.collect(Collectors.toList());
		appliedEvents.addAll(events);
	}

	private void removeOldAdresses(Set<Address> addresses) {
		var events = getAddresses().stream().filter(a -> !addresses.contains(a))
				.map(address -> (Event)new UserAddressRemoved(userid, address.getCity(), address.getState(),
						address.getPostcode()))
				.collect(Collectors.toList());
		appliedEvents.addAll(events);
	}

	private void addNewContacts(Set<Contact> contacts) {
		var events = contacts.stream().filter(c -> !getContacts().contains(c))
				.map(contact -> (Event)new UserContactAdded(userid, contact.getType(), contact.getDetail()))
				.collect(Collectors.toList());
		appliedEvents.addAll(events);
	}

	private void removeOldContacts(Set<Contact> contacts) {
		var events = getContacts().stream().filter(c -> !contacts.contains(c))
				.map(contact -> (Event)new UserContactRemoved(userid, contact.getType(), contact.getDetail()))
				.collect(Collectors.toList());
		appliedEvents.addAll(events);
	}
	
	/* Event Handling */

	private void applyEvents(Stream<Event> events) throws Error {
		events.forEachOrdered(e -> {
			this.applyEvent(e);
		});
		if (this.getUserid() == null) {
			throw new Error("user does not exist");
		}
	}

	private void applyEvent(Event e) {
		handlers.getOrDefault(e.getClass(), this::missingHandler).accept(e);
	}

	private void missingHandler(Message e) {
		throw new Error("handler for event "+e+" missing");
	}

	private void apply(UserCreated event) {
		userid = event.getUserId();
		firstname = event.getFirstName();
		lastname = event.getLastName();
	}

	private void apply(UserContactAdded event) {
		var contact = new Contact(event.getContactType(), event.getContactDetails());
		contacts.add(contact);
	}

	private void apply(UserContactRemoved event) {
		var contact = new Contact(event.getContactType(), event.getContactDetails());
		contacts.remove(contact);
	}

	private void apply(UserAddressAdded event) {
		var address = new Address(event.getCity(), event.getState(), event.getPostCode());
		addresses.add(address);
	}

	private void apply(UserAddressRemoved event) {
		var address = new Address(event.getCity(), event.getState(), event.getPostCode());
		addresses.remove(address);
	}

	public void clearAppliedEvents() {
		appliedEvents.clear();
	}

}

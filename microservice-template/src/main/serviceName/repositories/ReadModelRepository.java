package usermanagement.domain.repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import messaging.Message;
import messaging.MessageQueue;
import usermanagement.domain.aggregate.Address;
import usermanagement.domain.aggregate.Contact;
import usermanagement.domain.aggregate.UserId;
import usermanagement.domain.events.UserAddressAdded;
import usermanagement.domain.events.UserAddressRemoved;
import usermanagement.domain.events.UserContactAdded;
import usermanagement.domain.events.UserContactRemoved;
import usermanagement.domain.events.UserCreated;

public class ReadModelRepository {

	private Map<UserId, Set<Address>> addresses = new HashMap<>();

	private Map<UserId, Set<Contact>> contacts = new HashMap<>();

	public ReadModelRepository(MessageQueue eventQueue) {
		eventQueue.addHandler(UserCreated.class, e -> apply((UserCreated) e));
		eventQueue.addHandler(UserContactAdded.class, e -> apply((UserContactAdded) e));
		eventQueue.addHandler(UserContactRemoved.class, e -> apply((UserContactRemoved) e));
		eventQueue.addHandler(UserAddressAdded.class, e -> apply((UserAddressAdded) e));
		eventQueue.addHandler(UserAddressRemoved.class, e -> apply((UserAddressRemoved) e));
	}

	public Set<Address> getUserAddressesByRegion(UserId userId, String region) {
		return addresses.getOrDefault(userId, new HashSet<Address>()).stream().filter(a -> region.equals(a.getState()))
				.collect(Collectors.toSet());
	}

	public Set<Contact> getUserContactsByType(UserId userId, String type) {
		return contacts.getOrDefault(userId, new HashSet<Contact>()).stream().filter(a -> type.equals(a.getType()))
				.collect(Collectors.toSet());
	}

	public void apply(UserAddressAdded event) {
		var addressesByUser = addresses.getOrDefault(event.getUserId(), new HashSet<Address>());
		addressesByUser.add(new Address(event.getCity(), event.getState(), event.getPostCode()));
		addresses.put(event.getUserId(), addressesByUser);
	}

	public void apply(UserAddressRemoved event) {
		var addressesByUser = addresses.getOrDefault(event.getUserId(), new HashSet<Address>());
		addressesByUser.remove(new Address(event.getCity(), event.getState(), event.getPostCode()));
		addresses.put(event.getUserId(), addressesByUser);
	}

	public void apply(UserContactAdded event) {
		var contactsByUser = contacts.getOrDefault(event.getUserId(), new HashSet<Contact>());
		contactsByUser.add(new Contact(event.getContactType(), event.getContactDetails()));
		contacts.put(event.getUserId(), contactsByUser);
	}

	public void apply(UserContactRemoved event) {
		var contactsByUser = contacts.getOrDefault(event.getUserId(), new HashSet<Contact>());
		contactsByUser.remove(new Contact(event.getContactType(), event.getContactDetails()));
		contacts.put(event.getUserId(), contactsByUser);
	}

	public void apply(UserCreated event) {
	}
}

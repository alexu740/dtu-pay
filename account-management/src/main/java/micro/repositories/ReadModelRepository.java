package micro.repositories;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import boilerplate.Message;
import boilerplate.MessageQueue;
import micro.aggregate.AccountId;
import micro.events.AccountCreated;

public class ReadModelRepository {

	//private Map<UserId, Set<Address>> addresses = new HashMap<>();
	//private Map<UserId, Set<Contact>> contacts = new HashMap<>();

	public ReadModelRepository(MessageQueue eventQueue) {
		//eventQueue.addHandler(AccountCreated.class, e -> apply((AccountCreated) e));
	}
/**
	public Set<Address> getUserAddressesByRegion(UserId userId, String region) {
		return addresses.getOrDefault(userId, new HashSet<Address>()).stream().filter(a -> region.equals(a.getState()))
				.collect(Collectors.toSet());
	}

	public Set<Contact> getUserContactsByType(UserId userId, String type) {
		return contacts.getOrDefault(userId, new HashSet<Contact>()).stream().filter(a -> type.equals(a.getType()))
				.collect(Collectors.toSet());
	}
*/
	public void apply(AccountCreated event) {
		//fx
		/**
		var contactsByUser = contacts.getOrDefault(event.getUserId(), new HashSet<Contact>());
		contactsByUser.remove(new Contact(event.getContactType(), event.getContactDetails()));
		contacts.put(event.getUserId(), contactsByUser);
		 */
	}
}

package usermanagement.domain.repositories;

import messaging.MessageQueue;
import usermanagement.domain.aggregate.User;
import usermanagement.domain.aggregate.UserId;

public class UserRepository {
	
	private EventStore eventStore;

	public UserRepository(MessageQueue bus) {
		eventStore = new EventStore(bus);
	}

	public User getById(UserId userId) {
		return User.createFromEvents(eventStore.getEventsFor(userId));
	}
	
	public void save(User user) {
		eventStore.addEvents(user.getUserid(),user.getAppliedEvents());
		user.clearAppliedEvents();
	}
}

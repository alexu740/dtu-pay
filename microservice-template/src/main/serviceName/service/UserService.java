package usermanagement.domain.service;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import usermanagement.domain.aggregate.Address;
import usermanagement.domain.aggregate.Contact;
import usermanagement.domain.aggregate.User;
import usermanagement.domain.aggregate.UserId;
import usermanagement.domain.repositories.ReadModelRepository;
import usermanagement.domain.repositories.UserRepository;

public class UserService {

	private UserRepository repository;
	private ReadModelRepository readRepository;

	public UserService(UserRepository repository, ReadModelRepository readRepository) {
		this.readRepository = readRepository;
		this.repository = repository;
	}
	
	/* Command Operations */

	public UserId createUser(String firstname, String lastname) throws InterruptedException, ExecutionException {
		User user = User.create(firstname,lastname);
		repository.save(user);
		return user.getUserid();
	}

	public void updateUser(UserId userId, Set<Address> addresses, Set<Contact> contacts) {
		User user = repository.getById(userId);
		user.update(contacts, addresses);
		repository.save(user);
	}
	
	/* Query Operations */

	public Set<Contact> contactByType(UserId userId, String type) throws Exception {
    	return readRepository.getUserContactsByType(userId, type);
	}

	public Set<Address> addressByRegion(UserId userId, String region) throws Exception {
    	return readRepository.getUserAddressesByRegion(userId, region);
	}

}

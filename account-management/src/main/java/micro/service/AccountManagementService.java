package micro.service;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import micro.aggregate.Account;
import micro.aggregate.AccountId;
import micro.commands.AccountCreationCommand;
import micro.repositories.ReadModelRepository;
import micro.repositories.AccountRepository;

public class AccountManagementService {

	private AccountRepository repository;
	private ReadModelRepository readRepository;

	public AccountManagementService(AccountRepository repository, ReadModelRepository readRepository) {
		this.readRepository = readRepository;
		this.repository = repository;
	}
	
	/* Command Operations */
	public void handleCreateAccount(AccountCreationCommand command) {
		
	}
	/*
	public AccountId createAccount(String firstname, String lastname) throws InterruptedException, ExecutionException {
		Account account = Account.create(firstname,lastname);
		repository.save(account);
		return account.getAccountid();
	}

	public void updateAccount(AccountId userId, Set<Address> addresses, Set<Contact> contacts) {
		Account user = repository.getById(userId);
		user.update(contacts, addresses);
		repository.save(user);
	}
	 */
	/* Query Operations 

	public Set<Contact> contactByType(AccountId userId, String type) throws Exception {
    	return readRepository.getAccountContactsByType(userId, type);
	}

	public Set<Address> addressByRegion(AccountId userId, String region) throws Exception {
    	return readRepository.getAccountAddressesByRegion(userId, region);
	}
	*/
}

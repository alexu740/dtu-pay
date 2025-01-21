package micro.service;

import micro.adapters.EventPublisher;
import micro.aggregate.Account;
import micro.aggregate.CustomerAccount;
import micro.commands.AccountCreationCommand;
import micro.commands.AccountDeletionCommand;
import micro.commands.AccountGetQuery;
import micro.commands.AccountTokenCreationCommand;
import micro.exception.BusinessValidationException;
import micro.repositories.AccountReadModelRepository;
import micro.repositories.AccountRepository;

public class AccountManagementService {
	private AccountRepository repository;
	private AccountReadModelRepository readRepository;
	private EventPublisher publisher;

	public AccountManagementService(AccountRepository repository, AccountReadModelRepository readRepository, EventPublisher publisher) {
		this.readRepository = readRepository;
		this.repository = repository;
		this.publisher = publisher;
	}
	
	/* Command Operations */
	public void handleCreateAccount(AccountCreationCommand command, CorrelationId correlationId) {
		System.out.println("Creating new account object");
		Account account;
		if(command.isCustomer) {
			account = CustomerAccount.create(command.firstName, command.lastName, command.cpr, command.bankAccount, correlationId);
		}
		else {
			account = Account.create(command.firstName, command.lastName, command.cpr, command.bankAccount, false,  correlationId);
		}
		System.out.println("Created new account object");
		this.repository.save(account);
	}

	public void handleCreateTokens(AccountTokenCreationCommand command, CorrelationId correlationId) {
		var account = this.repository.getById(command.getAccountId());
		try {
			if (!(account instanceof CustomerAccount))  throw new BusinessValidationException("Cannot add tokens for non-customer accounts!");
			((CustomerAccount)account).createTokens(command.getNumberOfTokens());
			this.repository.save(account);
			publisher.emitTokensCreatedEvent(correlationId);
		} catch(BusinessValidationException ex) {
			publisher.emitTokensCreateFailedEvent(correlationId);
		}
	}

	public void handleGetAccount(AccountGetQuery query, CorrelationId correlationId) {
		System.out.println("Requesting tokens for account " + query.accountId);
		if(query.isCustomerAccount) {
			var acc = readRepository.getCustomerTokens(query);
			publisher.emitAccountCustomerRetrievedEvent(acc, correlationId);
		} else {
			publisher.emitAccountMerchantRetrievedEvent(correlationId);
		}
	}

	public void handleCheckTokenPresent(String accountId, String token, CorrelationId correlationId, String transactionId) {
		publisher.emitCheckTokenPresent(accountId, token, readRepository.tokenIsPresent(accountId, token), correlationId, transactionId);
	}

	public void handlePaymentInformationResolutionQuery(String transactionId, String customerId, String merchantId, CorrelationId correlationId) {
		var vm = readRepository.getPaymentInformation(customerId, merchantId);
		publisher.emitPaymentInformationResolved(transactionId, vm, correlationId);
	} 
	
	public void handleTokenUserCommand(String customerId, String token) {
		var account = this.repository.getById(customerId);
		if ((account instanceof CustomerAccount)) {
			((CustomerAccount)account).removeToken(token);
			this.repository.save(account);
		}
	}

	public void handleDeleteAccount(AccountDeletionCommand command, CorrelationId correlationId) {

	}
}

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
import micro.events.DomainEvent;
import micro.service.CorrelationId;

public class Account {
	private AccountId accountid;
	private AccountOwnerDetails ownerDetails;
	private AccountFinancialDetails financialDetails;

	private List<DomainEvent> appliedEvents = new ArrayList<DomainEvent>();

    public static Account create(String firstName, String lastName, String cpr, String bankAccount, boolean isCustomerAccount, CorrelationId correlationId) {
		var accountId = new AccountId(UUID.randomUUID());
		var ownerDetails = new AccountOwnerDetails(firstName, lastName, cpr);
		var financialDetails = new AccountFinancialDetails(bankAccount, null);

		var account = new Account();
		if(isCustomerAccount) {
			account = new CustomerAccount();
		}
		
		account.setAccountid(accountId);
		account.setOwnerDetails(ownerDetails);
		account.setFinancialDetails(financialDetails);

		var event = new AccountCreated(accountId, correlationId);

		event.firstName = firstName;
		event.lastName = lastName;
		event.cpr = cpr;
		event.bankAccount = bankAccount;
		event.isCustomerAccountType = isCustomerAccount;

		account.getAppliedEvents().add(event);

		return account;
	}

	public static Account createFromEvents(Stream<DomainEvent> events) {
		Account account = new Account();
		account.applyEvents(events);
		return account;
	}

	protected void applyEvents(Stream<DomainEvent> events) throws Error {
		events.forEachOrdered(e -> {
			if (e instanceof AccountCreated)
				this.apply((AccountCreated) e);
		});
		if (this.getAccountid() == null) {
			throw new Error("user does not exist");
		}
	}

	protected void apply(AccountCreated event) {
		this.accountid = event.getAccountId();
		this.ownerDetails = new AccountOwnerDetails(event.firstName, event.lastName, event.cpr);
		this.financialDetails = new AccountFinancialDetails(event.bankAccount, null);
	}

	public void clearAppliedEvents() {
		appliedEvents.clear();
	}

	public List<DomainEvent> getAppliedEvents() {
		return appliedEvents;
	}

	public void setAccountid(AccountId accountid) {
		this.accountid = accountid;
	}
	
	public AccountId getAccountid() {
		return accountid;
	}

	public AccountOwnerDetails getOwnerDetails() {
		return ownerDetails;
	}

	public void setOwnerDetails(AccountOwnerDetails ownerDetails) {
		this.ownerDetails = ownerDetails;
	}

	public AccountFinancialDetails getFinancialDetails() {
		return financialDetails;
	}

	public void setFinancialDetails(AccountFinancialDetails financialDetails) {
		this.financialDetails = financialDetails;
	}
}

package micro.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import micro.events.AccountRegistered;
import micro.events.AccountDeregistered;
import micro.events.DomainEvent;
import micro.service.CorrelationId;

/**
 * @author: Alex Ungureanu (s225525)
 */

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

		var event = new AccountRegistered(accountId, correlationId);

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
			if (e instanceof AccountRegistered)
				this.apply((AccountRegistered) e);
		});
		if (this.getAccountid() == null) {
			throw new Error("user does not exist");
		}
	}

	protected void apply(AccountRegistered event) {
		this.accountid = event.getAccountId();
		this.ownerDetails = new AccountOwnerDetails(event.firstName, event.lastName, event.cpr);
		this.financialDetails = new AccountFinancialDetails(event.bankAccount, null);
	}

	public void delete(CorrelationId correlationId) {
		var event = new AccountDeregistered(this.getAccountid(), correlationId);
		appliedEvents.add(event);
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

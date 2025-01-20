package micro.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import micro.events.AccountCreated;
import micro.events.DomainEvent;
import micro.events.TokenAdded;
import micro.events.TokenRemoved;
import micro.exception.BusinessValidationException;
import micro.service.CorrelationId;

public class CustomerAccount extends Account {
	private List<String> tokens;
	
	public static Account create(String firstName, String lastName, String cpr, String bankAccount, CorrelationId correlationId) {
		var account = Account.create(firstName, lastName, cpr, bankAccount, true, correlationId);
		((CustomerAccount)account).setTokens(new ArrayList<String>());
		return account;
	}

	public CustomerAccount() {
		this.tokens = new ArrayList<>();
	}

	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}

	public void createTokens(int tokenNumber) {
		if(tokens.size() > 1) throw new BusinessValidationException("Cannot add new tokens when there are existing tokens.");
		if (tokenNumber > 5 || tokenNumber < 1) throw new BusinessValidationException("Can only add between 1 and 5 tokens");

		for(int i = 0; i < tokenNumber; i++) {
			var newToken = UUID.randomUUID().toString();
			this.getAppliedEvents().add(new TokenAdded(getAccountid(), newToken));
			tokens.add(newToken);
		}
	}

	public void removeToken(String token) {
		if(tokens.contains(token)) {
			this.getAppliedEvents().add(new TokenRemoved(getAccountid(), token));
			tokens.remove(token);
		}
	}

	public static Account createFromEvents(Stream<DomainEvent> events) {
		List<DomainEvent> eventList = events.collect(Collectors.toList());

		AccountCreated creatingEvent = (AccountCreated) eventList.stream()
			.filter(e -> e instanceof AccountCreated)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("AccountCreated event not found"));
		
		if(creatingEvent.isCustomerAccountType()) {
			Account account = new CustomerAccount();
			account.applyEvents(eventList.stream());
			return account;
		} else {
			return Account.createFromEvents(eventList.stream());
		}
	}

	@Override
	protected void applyEvents(Stream<DomainEvent> events) throws Error {
		events.forEachOrdered(e -> {
			if (e instanceof AccountCreated) {
				super.apply((AccountCreated) e);
				this.apply((AccountCreated) e);
			}
			if(e instanceof TokenAdded) 
				this.apply((TokenAdded) e);
			if(e instanceof TokenRemoved) 
				this.apply((TokenRemoved) e);
		});
		if (this.getAccountid() == null) {
			throw new Error("user does not exist");
		}
	}

	private void apply(TokenAdded e) {
		var tokens = this.getTokens();
		tokens.add(e.newToken);
		this.setTokens(tokens);
	}

	private void apply(TokenRemoved e) {
		var tokens = this.getTokens();
		tokens.remove((e.getToken()));
		this.setTokens(tokens);
	}
}

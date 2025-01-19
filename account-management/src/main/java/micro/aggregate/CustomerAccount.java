package micro.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import micro.events.AccountCreated;
import micro.service.CorrelationId;

public class CustomerAccount extends Account {
	private List<String> tokens;
	
	public static Account create(String firstName, String lastName, String cpr, String bankAccount, CorrelationId correlationId) {
		var account = Account.create(firstName, lastName, cpr, bankAccount, true, correlationId);
		((CustomerAccount)account).setTokens(new ArrayList<String>());
		return account;
	}

	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
}

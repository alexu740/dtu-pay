package micro.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import boilerplate.Event;
import boilerplate.Message;
import boilerplate.MessageQueue;
import micro.aggregate.AccountId;
import micro.aggregate.CustomerAccount;
import micro.commands.AccountGetQuery;
import micro.events.AccountCreated;
import micro.events.TokenAdded;
import micro.repositories.viewmodel.PaymentInstrumentViewModel;
import micro.repositories.viewmodel.TokenViewModel;

public class AccountReadModelRepository {

	private Map<String, TokenViewModel> tokens = new HashMap<>();
	private Map<String, PaymentInstrumentViewModel> paymentInstruments = new HashMap<>();

	//private Map<UserId, Set<Contact>> contacts = new HashMap<>();

	public AccountReadModelRepository(MessageQueue eventQueue) {
		eventQueue.addHandler("AccountRegistered", this::applyAccountRegistered);
		eventQueue.addHandler("TokenAdded", this::applyTokenAdded);
	}

	public TokenViewModel getCustomerTokens(AccountGetQuery query) {
		System.out.println("Getting customer tokens " + tokens.get(query.accountId));
		System.out.println("this" + this);
		return tokens.get(query.accountId);
	}

	public PaymentInstrumentViewModel getCustomerPaymentInstruments() {
		return null;
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
	public void applyAccountRegistered(Event e) {
		var event = (AccountCreated) e;
		var accountId = event.getAccountId().getUuid().toString();
		System.out.println("Updating read model with event: AccountCreated for account " + accountId);
		if(event.isCustomerAccountType()) {
			var tokenvm = new TokenViewModel();
			tokenvm.setAccountId(accountId);
			tokenvm.setTokens(new ArrayList<String>());
			tokens.put(accountId, tokenvm);
		}

		var payvm = new PaymentInstrumentViewModel();
		payvm.setAccountId(accountId);
		payvm.setBankAccount(event.bankAccount); 

		paymentInstruments.put(accountId, payvm);
	}

	public void applyTokenAdded(Event e) {
		var event = (TokenAdded) e;
		var accountId = event.getAccountId().getUuid().toString();
		var vm = tokens.get(accountId);
		var userTokens = vm.getTokens();
		userTokens.add(event.getNewToken());
		vm.setTokens(userTokens);
	}
}

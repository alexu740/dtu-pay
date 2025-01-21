package micro.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import boilerplate.Event;
import boilerplate.MessageQueue;
import micro.commands.AccountGetQuery;
import micro.events.AccountRegistered;
import micro.events.TokenAdded;
import micro.events.TokenRemoved;
import micro.repositories.viewmodel.PaymentInstrumentViewModel;
import micro.repositories.viewmodel.TokenViewModel;

public class AccountReadModelRepository {

	private Map<String, TokenViewModel> tokens = new HashMap<>();
	private Map<String, String> paymentInstruments = new HashMap<>();

	public AccountReadModelRepository(MessageQueue eventQueue) {
		eventQueue.addHandler("AccountRegistered", this::applyAccountRegistered);
		eventQueue.addHandler("TokenAdded", this::applyTokenAdded);
		eventQueue.addHandler("TokenRemoved", this::applyTokenRemoved);
	}

	public TokenViewModel getCustomerTokens(AccountGetQuery query) {
		System.out.println("Getting customer tokens " + tokens.get(query.accountId));
		return tokens.get(query.accountId);
	}

	public void applyAccountRegistered(Event e) {
		var event = (AccountRegistered) e;
		var accountId = event.getAccountId().getUuid().toString();
		System.out.println("Updating read model with event: AccountCreated for account " + accountId);
		if(event.isCustomerAccountType()) {
			var tokenvm = new TokenViewModel();
			tokenvm.setAccountId(accountId);
			tokenvm.setTokens(new ArrayList<String>());
			tokens.put(accountId, tokenvm);
		}

		paymentInstruments.put(accountId, event.bankAccount);
	}

	public void applyTokenAdded(Event e) {
		var event = (TokenAdded) e;
		var accountId = event.getAccountId().getUuid().toString();
		var vm = tokens.get(accountId);
		var userTokens = vm.getTokens();
		userTokens.add(event.getNewToken());
		vm.setTokens(userTokens);
	}

	public void applyTokenRemoved(Event e) {
		var event = (TokenRemoved) e;
		var accountId = event.getAccountId().getUuid().toString();
		var vm = tokens.get(accountId);
		var userTokens = vm.getTokens();
		userTokens.remove(event.getToken());
		vm.setTokens(userTokens);
	}

	public boolean tokenIsPresent(String customerId, String token) {
		var custTokens = tokens.get(customerId);
		if(custTokens != null) {
			return custTokens.getTokens().contains(token);
		}
		return false;
	}

	public PaymentInstrumentViewModel getPaymentInformation(String customerId, String merchantId) {
		var vm = new PaymentInstrumentViewModel();
		vm.setCustomerAccountId(customerId);
		vm.setMerchantAccountId(merchantId);
		vm.setCustomerBankAccount(paymentInstruments.get(customerId));
		vm.setMerchantBankAccount(paymentInstruments.get(merchantId));
		return vm;
	}
}

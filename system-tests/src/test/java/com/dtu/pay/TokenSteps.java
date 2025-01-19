package com.dtu.pay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Service.MobileAppApiHelper;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

public class TokenSteps {
	BankService bank = new BankServiceService().getBankServicePort();
    List<String> accounts = new ArrayList<>();

    private Customer customer;
    private MobileAppApiHelper appApi = new MobileAppApiHelper();
    private Map<String,String> userIds = new HashMap<>();
    private Map<String,String> tokenRequest = new HashMap<>();

    @Given("a registered customer with name {string}, cpr {string} and {int} tokens")
	public void aRegisteredCustomerWithName(String firstName, String cpr, Integer tokens) throws BankServiceException_Exception {
		var customer = new Customer(firstName, "Last name", cpr);
        var bankaccount = registerBankAccount(customer.getFirstName(), customer.getLastName(), customer.getCpr(), 1000);
        accounts.add(bankaccount);
        var result = appApi.register(customer, bankaccount);
        userIds.put(firstName, result);
	}

    @When("When requesting {int} new tokens for {string}")
    public void requestingNewTokens(Integer numberOfTokens, String name) {
       var tokenResult = appApi.requestTokens(userIds.get(name), numberOfTokens);
       tokenRequest.put(name, tokenResult);
	}

    @Then("the customer {string} has {int} tokens")
    public void theCustomerHasTokens(String name, Integer tokenNumber) {
        Customer cust = appApi.getCustomer(userIds.get(name));
        assertEquals(cust.getTokens().size(), tokenNumber.intValue());
	}

    @Then("the request for {string} is denied")
    public void theRequestIsDenied(String name) {
        assertEquals(tokenRequest.get(name), "denied");
	}

    public String registerBankAccount(String firstName, String lastName, String cpr, int intialBalance) throws BankServiceException_Exception {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setCprNumber(cpr);

		String account = bank.createAccountWithBalance(user, new BigDecimal(intialBalance));
		System.out.println("SOAP Account: " + account);
		return account;
	}

    @io.cucumber.java.After
    public void cleanupBankAccounts() throws BankServiceException_Exception {
        for (String account : accounts) {
            bank.retireAccount(account);
        }
	}
}

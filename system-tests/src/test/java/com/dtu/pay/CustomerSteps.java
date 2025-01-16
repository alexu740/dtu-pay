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
import java.util.List;
import java.util.UUID;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Service.MobileAppApiHelper;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

public class CustomerSteps {
	BankService bank = new BankServiceService().getBankServicePort();
    List<String> accounts = new ArrayList<>();

    private Customer customer;
    private MobileAppApiHelper appApi = new MobileAppApiHelper();
    private String result;

    @Given("a customer with name {string}")
	public void aCustomerWithName(String firstName) {
		customer = new Customer(firstName, "Last name", "010201-0201");
	}

    @When("the customer is registered")
    public void theCustomerIsRegistered() throws BankServiceException_Exception {
        var bankaccount = registerBankAccount(customer.getFirstName(), customer.getLastName(), customer.getCpr(), 1000);
        accounts.add(bankaccount);
        result = appApi.register(customer, bankaccount);
	}

    @Then("the user id is returned")
    public void theOperationIsSuccessful() {
        try {
            UUID.fromString(result);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertTrue(false);
        }
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

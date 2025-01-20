package com.dtu.pay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

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
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Model.Dto.CustomerTokensResponseDto;
import com.dtu.pay.Service.MobileAppApiHelper;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

public class PaymentSteps {
	BankService bank = new BankServiceService().getBankServicePort();
    List<String> accounts = new ArrayList<>();

    private Customer customer;
    private Merchant merchant;
    private String payResult;

    private MobileAppApiHelper appApi = new MobileAppApiHelper();
    private Map<String,String> userIds = new HashMap<>();
    private Map<String,String> bankAccounts = new HashMap<>();
    private Map<String,String> tokenRequest = new HashMap<>();
    private Map<String,String> tokens = new HashMap<>();

    @Given("a customer with name {string}, last name {string}, and CPR {string}")
	public void aRegisteredCustomerWithName(String firstName, String lastName, String cpr) {
		customer = new Customer(firstName, lastName, cpr);
	}

    @Given("a customer with name {string}, who is registered with DTU Pay")
	public void aRegisteredCustomerWithName2(String firstName) throws BankServiceException_Exception {
		customer = new Customer(firstName, "Test", "071285-6249");
        var bankaccount = registerBankAccount(customer.getFirstName(), customer.getLastName(), customer.getCpr(), 1000);
        bankAccounts.put(customer.getFirstName(), bankaccount);
        accounts.add(bankaccount);
        String result = appApi.register(customer, bankAccounts.get(customer.getFirstName()));
        userIds.put(customer.getFirstName(), result);
        appApi.requestTokens(userIds.get(customer.getFirstName()), 1);
        CustomerTokensResponseDto cust = appApi.getCustomer(userIds.get(customer.getFirstName()));
        tokens.put(customer.getFirstName(), cust.getTokens().getFirst());
	}

    @And("the customer is registered with the bank with an initial balance of {int} kr") 
    public void theCustomerIsRegisteredWithBank(Integer balance) throws BankServiceException_Exception {
        var bankaccount = registerBankAccount(customer.getFirstName(), customer.getLastName(), customer.getCpr(), balance);
        bankAccounts.put(customer.getFirstName(), bankaccount);
        accounts.add(bankaccount);
    }

    @And("the customer is registered with DTU Pay using their bank account")
    public void theCustomerIsRegisteredWithDtuPay() {
        String result = appApi.register(customer, bankAccounts.get(customer.getFirstName()));
        userIds.put(customer.getFirstName(), result);
    }

    @And("the customer has a valid token")
    public void theCustomerHasToken() {
        appApi.requestTokens(userIds.get(customer.getFirstName()), 1);
        CustomerTokensResponseDto cust = appApi.getCustomer(userIds.get(customer.getFirstName()));
        tokens.put(customer.getFirstName(), cust.getTokens().getFirst());
    }

    @And("the customer has a invalid token")
    public void theCustomerHasInvalidToken() {
        tokens.put(customer.getFirstName(), UUID.randomUUID().toString());
    }

    @And("a merchant with name {string}, last name {string}, and CPR {string}")
    public void theMerchantWithName(String firstName, String lastName, String cpr) {
        merchant = new Merchant(firstName, lastName, cpr);
    }

    @Given("a merchant with name {string}, who is registered with DTU Pay")
    public void theMerchantWithName(String firstName) throws BankServiceException_Exception {
        merchant = new Merchant(firstName, "Test", "060262-7143");
        var bankaccount = registerBankAccount(merchant.getFirstName(), merchant.getLastName(), merchant.getCpr(), 1000);
        bankAccounts.put(merchant.getFirstName(), bankaccount);
        accounts.add(bankaccount);
        String result = appApi.register(merchant, bankAccounts.get(merchant.getFirstName()));
        userIds.put(merchant.getFirstName(), result);
    }
    
    @And("the merchant is registered with the bank with an initial balance of {int} kr")
    public void theMerchantIsRegisteredWithBank(Integer balance) throws BankServiceException_Exception {
        var bankaccount = registerBankAccount(merchant.getFirstName(), merchant.getLastName(), merchant.getCpr(), balance);
        bankAccounts.put(merchant.getFirstName(), bankaccount);
        accounts.add(bankaccount);
    }

    @And("the merchant is registered with DTU Pay using their bank account")
    public void theMerchantIsRegisteredWithDtuPay() {
        String result = appApi.register(merchant, bankAccounts.get(merchant.getFirstName()));
        userIds.put(merchant.getFirstName(), result);
    }

    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantStartsPayment(Integer paymentAmount) {
       payResult = appApi.pay(userIds.get(customer.getFirstName()),  userIds.get(merchant.getFirstName()), tokens.get(customer.getFirstName()), paymentAmount);
	}

    @When("the merchant initiates a payment for {int} kr using customer id {string}")
    public void theMerchantStartsPaymentForNonExistentCustomer(Integer paymentAmount, String givenCustId) {
        payResult = appApi.pay(givenCustId, userIds.get(merchant.getFirstName()), UUID.randomUUID().toString(), paymentAmount);
    }

    @When("a payment for {int} kr is initiated by a corrupted merchant with id {string}")
    public void thePaymentIsInitiatedByCorruptedMerchant(Integer paymentAmount, String givenMerch) {
        payResult = appApi.pay(userIds.get(customer.getFirstName()), givenMerch, tokens.get(customer.getFirstName()), paymentAmount);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
        assertEquals("successful", payResult);
	}

    @Then("the payment is failed")
    public void thePaymentIsFailed() {
        assertEquals("failed", payResult);
	} 

    @And("the token is removed")
    public void theTokenIsRemoved() {
        appApi.requestTokens(userIds.get(customer.getFirstName()), 1);
        CustomerTokensResponseDto cust = appApi.getCustomer(userIds.get(customer.getFirstName()));
        var usedToken = tokens.get(customer.getFirstName());

        assertFalse(cust.getTokens().contains(usedToken));
	}

    @And("the balance of the customer at the bank is {int} kr")
	public void theCustomerBalanceIsUpdate(Integer expectedBalance) {
		try {
			Integer actualBalance = bank.getAccount(bankAccounts.get(customer.getFirstName())).getBalance().intValue();
			assertEquals(expectedBalance, actualBalance);
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	@And("the balance of the merchant at the bank is {int} kr")
	public void theMerchantBalanceIsUpdate(Integer expectedBalance) {
		try {
			Integer actualBalance = bank.getAccount(bankAccounts.get(merchant.getFirstName())).getBalance().intValue();
			assertEquals(expectedBalance, actualBalance);
		} catch(Exception e) {
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

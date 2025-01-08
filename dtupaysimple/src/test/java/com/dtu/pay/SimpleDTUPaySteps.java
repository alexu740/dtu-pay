package com.dtu.pay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Service.SimpleDtuPay;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

public class SimpleDTUPaySteps {
	BankService bank = new BankServiceService().getBankServicePort();
	private Customer customer;
	private String customerBankAccountNumber;

	private Merchant merchant;
	private String merchantBankAccountNumber;

	private String customerId, merchantId;
	private SimpleDtuPay dtupay = new SimpleDtuPay();
	private boolean successful = false;

	@Given("a customer with name {string}, last name {string}, and CPR {string}")
	public void aCustomerWithName(String firstName, String lastName, String cpr) {
		customer = new Customer(firstName, lastName, cpr);
	}

	@Given("the customer is registered with the bank with an initial balance of {int} kr")
	public void theCustomerIsRegisteredWithBankAccount(Integer balance) {
		try {
			customerBankAccountNumber = registerBankAccount(customer.getFirstName(), customer.getLastName(), customer.getCpr(), balance);
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	@Given("the customer is registered with Simple DTU Pay using their bank account")
	public void theCustomerIsRegisteredWithSimpleDTUPay() {
		customerId = dtupay.register(customer, customerBankAccountNumber);
	}
	
	@Given("a merchant with name {string}, last name {string}, and CPR {string}")
	public void aMerchantWithName(String firstName, String lastName, String cpr) {
		merchant = new Merchant(firstName, lastName, cpr);
	}

	@Given("the merchant is registered with the bank with an initial balance of {int} kr")
	public void theMerchantIsRegisteredWithBankAccount(Integer balance) {
		try {
			merchantBankAccountNumber = registerBankAccount(merchant.getFirstName(), merchant.getLastName(), merchant.getCpr(), balance);
		} catch(Exception e) {
			assertTrue(false);
		}
	}
	
	@Given("the merchant is registered with Simple DTU Pay using their bank account")
	public void theMerchantIsRegisteredWithSimpleDTUPay() {
		merchantId = dtupay.register(merchant, merchantBankAccountNumber);
	}
	
	@When("the merchant initiates a payment for {int} kr by the customer")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(Integer amount) {
		successful = dtupay.pay(amount,customerId,merchantId);
	}
	
	@Then("the payment is successful")
	public void thePaymentIsSuccessful() {
		assertTrue(successful);
	}

	@Then("the payment is failed")
	public void thePaymentIsFailed() {
		assertFalse(successful);
	}

	@Then("the balance of the customer at the bank is {int} kr")
	public void theCustomerBalanceIsUpdate(Integer expectedBalance) {
		try {
			Integer actualBalance = bank.getAccount(customerBankAccountNumber).getBalance().intValue();
			assertEquals(expectedBalance, actualBalance);
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	@Then("the balance of the merchant at the bank is {int} kr")
	public void theMerchantBalanceIsUpdate(Integer expectedBalance) {
		try {
			Integer actualBalance = bank.getAccount(merchantBankAccountNumber).getBalance().intValue();
			assertEquals(expectedBalance, actualBalance);
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	@io.cucumber.java.After
    public void cleanupBankAccounts() throws BankServiceException_Exception {
		if(customerBankAccountNumber != null) {
			bank.retireAccount(customerBankAccountNumber);
		}
		if(merchantBankAccountNumber != null) {
			bank.retireAccount(merchantBankAccountNumber);
		}

		dtupay.unregister(customerId, "customer");
		dtupay.unregister(merchantId, "merchant");
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
}
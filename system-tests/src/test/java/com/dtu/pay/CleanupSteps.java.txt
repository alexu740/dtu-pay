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

public class CleanupSteps {
	BankService bank = new BankServiceService().getBankServicePort();
	private String testAccount;

	public String registerBankAccount(String firstName, String lastName, String cpr, int intialBalance) throws BankServiceException_Exception {
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setCprNumber(cpr);

		String account = bank.createAccountWithBalance(user, new BigDecimal(intialBalance));
		System.out.println("SOAP Account: " + account);
		return account;
	}

	@Given("a bank account is created")
	public void aBankAccountIsCreated() {
		try {
			testAccount = registerBankAccount("Test", "Test", "010101-0101", 1000);
		} catch(Exception e) {
			assertTrue(false);
		}
	}

	@Then("the account is cleaned up correctly upon deletion")
	public void theBankAccountIsCleanedup() {
		try {
			bank.retireAccount(testAccount);
		} catch(Exception e) {
			assertTrue(false);
		}
		try {
			Account deletedAccount = bank.getAccount(testAccount);
			assertTrue(false);
		} catch(Exception e) {
			assertTrue(true);
		}
	}
}
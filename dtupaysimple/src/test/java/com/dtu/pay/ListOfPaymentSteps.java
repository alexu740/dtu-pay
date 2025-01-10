package com.dtu.pay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Model.Payment;
import com.dtu.pay.Service.SimpleDtuPay;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;

public class ListOfPaymentSteps {
	BankService bank = new BankServiceService().getBankServicePort();
	private Customer customer;
	private String customerBankAccountNumber;

	private Merchant merchant;
	private String merchantBankAccountNumber;

	private String customerId, merchantId;

	private SimpleDtuPay dtupay = new SimpleDtuPay();
	private boolean successful = false;
	private Map<String, Payment> paymentMap;

	@Given("a customer with name {string}, who is registered with Simple DTU Pay")
	public void aCustomerWithName(String name) {
		try {
			customer = new Customer(name, "LastName", "010101-0101");
			customerBankAccountNumber = registerBankAccount(customer.getFirstName(), customer.getLastName(), customer.getCpr(), 1000);
			customerId = dtupay.register(customer, customerBankAccountNumber);
		} catch(Exception e) {
			assertTrue(false);
		}
	}
	
	@And("a merchant with name {string}, who is registered with Simple DTU Pay")
	public void aMerchantWithName(String name) {
		try {
			merchant = new Merchant(name,"LastName","020202-0202");
			merchantBankAccountNumber = registerBankAccount(merchant.getFirstName(), merchant.getLastName(), merchant.getCpr(), 1000);
			merchantId = dtupay.register(merchant, merchantBankAccountNumber);
		} catch(Exception e) {
			assertTrue(false);
		}
	}
	
	@Given("a successful payment of {int} kr from the customer to the merchant")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer(Integer amount) {
		successful = dtupay.pay(amount,customerId,merchantId);
		assertTrue(successful);
	}
	
	@When("the manager asks for a list of payments")
	public void thePaymentIsSuccessful() {
		paymentMap = dtupay.getListOfPayments();
	}

	@Then("the list contains a payments where customer {string} paid {int} kr to merchant {string}")
	public void thePaymentExists(String customer, Integer amount, String merchant) {
		AtomicBoolean found = new AtomicBoolean(false);
		paymentMap.forEach((id, payment) -> {
			if(payment.getCustomer().equals(customerId) && payment.getMerchant().equals(merchantId) && payment.getAmount() == amount) {
				found.set(true);
			}
		});
		assertTrue(found.get());
	}

	@io.cucumber.java.After
    public void cleanupBankAccounts() throws BankServiceException_Exception {
		if(customerBankAccountNumber != null) {
			bank.retireAccount(customerBankAccountNumber);
		}
		if(merchantBankAccountNumber != null) {
			bank.retireAccount(merchantBankAccountNumber);
		}

		dtupay.unregister(customer, customerId);
		dtupay.unregister(merchant, merchantId);
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
package com.dtu.pay;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Model.Payment;
import com.dtu.pay.Service.SimpleDtuPay;

public class ListOfPaymentSteps {
	private Customer customer;
	private Merchant merchant;
	private String customerId, merchantId;
	private SimpleDtuPay dtupay = new SimpleDtuPay();
	private boolean successful = false;
	private Map<String, Payment> paymentMap;

	@Given("a customer with name {string}, who is registered with Simple DTU Pay")
	public void aCustomerWithName(String name) {
		customer = new Customer(name, "", "");
		customerId = dtupay.register(customer,"");
	}
	
	@And("a merchant with name {string}, who is registered with Simple DTU Pay")
	public void aMerchantWithName(String name) {
		merchant = new Merchant(name,"","");
		merchantId = dtupay.register(merchant,"");
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
}
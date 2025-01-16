package com.dtu.pay;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dtu.pay.Model.Customer;
import com.dtu.pay.Model.Merchant;
import com.dtu.pay.Service.SimpleDtuPay;

public class MerchantNotKnownSteps {
	private Customer customer;
	private Merchant merchant;
	private String customerId, merchantId;
	private SimpleDtuPay dtupay = new SimpleDtuPay();
	private boolean successful = false;

	@Given("Given a customer with name {string}, who is registered with Simple DTU Pay")
	public void aCustomerWithName(String name) {
		customer = new Customer(name,"","");
		customerId = dtupay.register(customer,"");
	}
	
	@When("the customer initiates a payment for {int} kr using merchant id {string}")
	public void aMerchantWithName(Integer amount, String merchant) {
		successful = dtupay.pay(amount,customerId,merchantId);
	}
	
	@Then("the payment is not successful")
	public void theMerchantIsRegisteredWithSimpleDTUPay() {
		assertFalse(successful);
	}
	
	@Then("an error message is returned saying \"customer with id \\\"non-existent-id\\\" is unknow")
	public void theMerchantInitiatesAPaymentForKrByTheCustomer() {
		assertFalse(successful);
	}
}
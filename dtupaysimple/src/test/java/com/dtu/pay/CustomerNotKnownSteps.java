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

public class CustomerNotKnownSteps {
	private Customer customer;
	private Merchant merchant;
	private String customerId, merchantId;
	private SimpleDtuPay dtupay = new SimpleDtuPay();
	private boolean successful = false;

	@Given("Given a merchant with name {string}, who is registered with Simple DTU Pay")
	public void aCustomerWithName(String name) {
		customer = new Customer(name, "","");
		customerId = dtupay.register(customer,"");
	}
	
	@When("the merchant initiates a payment for {int} kr using customer id {string}")
	public void aCustWithName(Integer amount, String merchant) {
		successful = dtupay.pay(amount,customerId,merchantId);
	}
	
	@Then("[cust]the payment is not successful")
	public void theCustIsRegisteredWithSimpleDTUPay() {
		assertFalse(successful);
	}
	
	@Then("[cust]an error message is returned saying \"customer with id \\\"non-existent-id\\\" is unknow")
	public void theCustInitiatesAPaymentForKrByTheCustomer() {
		assertFalse(successful);
	}
}
package micro.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import micro.events.PaymentInitialised;
import micro.events.PaymentResolved;
import micro.events.PaymentSucceeded;
import micro.events.PaymentTokenValidated;
import micro.dto.PaymentDto;
import micro.events.DomainEvent;
import micro.events.PaymentFailed;
import micro.service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class Payment {
	private String transactionId;
	private String customerId;
	private String customerBankAccount;

	private String merchantId;
	private String merchantBankAccount;

	private int amount;
	private String token;

	private boolean tokenValidated;
	private boolean allPaymentInformationResolved;

	private boolean paymentSuccessful;
	private String paymentNote;

	private List<DomainEvent> appliedEvents = new ArrayList<DomainEvent>();

    public static Payment create(String customerId, String merchantId, String token, int amount, CorrelationId correlationId) {
		var transactionId = UUID.randomUUID().toString();

		var payment = new Payment();

		payment.setTransactionId(transactionId);
		payment.setCustomerId(customerId);
		payment.setMerchantId(merchantId);
		payment.setAmount(amount);
		payment.setToken(token);

		var event = new PaymentInitialised(customerId, transactionId, token, correlationId);

		event.setCustomerId(customerId);
		event.setMerchantId(merchantId);
		event.setAmount(amount);
		event.setToken(token);

		payment.getAppliedEvents().add(event);

		return payment;
	}

	public static Payment createFromEvents(Stream<DomainEvent> events) {
		Payment payment = new Payment();
		payment.applyEvents(events);
		return payment;
	}

	protected void applyEvents(Stream<DomainEvent> events) throws Error {
		events.forEachOrdered(e -> {
			if (e instanceof PaymentInitialised)
				this.apply((PaymentInitialised) e);
			if (e instanceof PaymentTokenValidated)
				this.apply((PaymentTokenValidated) e);
			if (e instanceof PaymentResolved)
				this.apply((PaymentResolved) e);
			if (e instanceof PaymentSucceeded)
				this.apply((PaymentSucceeded) e);
			if (e instanceof PaymentFailed)
				this.apply((PaymentFailed) e);
		});
		if (this.getTransactionId() == null) {
			throw new Error("payment does not exist");
		}
	}

	protected void apply(PaymentInitialised event) {
		this.transactionId = event.getTransactionId();
		this.customerId = event.getCustomerId();
		this.merchantId = event.getMerchantId();
		this.amount = event.getAmount();
		this.token = event.getToken();
	}

	protected void apply(PaymentTokenValidated event) {
		this.tokenValidated = true; 
	}

	protected void apply(PaymentResolved event) {
		this.customerBankAccount = event.getCustomerBankAccount();
		this.merchantBankAccount = event.getMerchantBankAccount();
		this.allPaymentInformationResolved = true;
	}

	protected void apply(PaymentSucceeded event) {
		this.paymentSuccessful = true;
	}

	protected void apply(PaymentFailed event) {
		this.paymentSuccessful = false;
	}
	
	public void updatePaymentTokenValidated() {
		var event = new PaymentTokenValidated(transactionId);
		this.apply(event);
		this.appliedEvents.add(event);
	}

	public void updateTokenInvalid(CorrelationId correlationId) {
		var event = new PaymentFailed(this.getTransactionId(), correlationId);
		this.apply(event);
		this.appliedEvents.add(event);
	}

	public void update(String customerBank, String merchantBank, CorrelationId correlationId) {
		if(customerBank == null || merchantBank == null) {
			updateTokenInvalid(correlationId);
		} else {
			var event = new PaymentResolved(transactionId, customerBank, merchantBank, correlationId);
			this.apply(event);
			this.appliedEvents.add(event);
		}
	}

	public void markAsCompleted(boolean successful, CorrelationId correlationId) {
		PaymentDto dto = new PaymentDto();
		dto.setPaymentId(transactionId);
		dto.setAmount(amount);
		dto.setCustomerId(customerId);
		dto.setMerchantId(merchantId);
		dto.setToken(token);

		if(successful) {
			var event = new PaymentSucceeded(this.getTransactionId(), dto, correlationId);
			this.apply(event);
			this.appliedEvents.add(event);
		} 
		else {
			updateTokenInvalid(correlationId);
		}
	}

	public void clearAppliedEvents() {
		appliedEvents.clear();
	}

	public List<DomainEvent> getAppliedEvents() {
		return appliedEvents;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerBankAccount() {
		return customerBankAccount;
	}

	public void setCustomerBankAccount(String customerBankAccount) {
		this.customerBankAccount = customerBankAccount;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantBankAccount() {
		return merchantBankAccount;
	}

	public void setMerchantBankAccount(String merchantBankAccount) {
		this.merchantBankAccount = merchantBankAccount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isTokenValidated() {
		return tokenValidated;
	}

	public void setTokenValidated(boolean tokenValidated) {
		this.tokenValidated = tokenValidated;
	}

	public boolean isAllPaymentInformationResolved() {
		return allPaymentInformationResolved;
	}

	public void setAllPaymentInformationResolved(boolean allPaymentInformationResolved) {
		this.allPaymentInformationResolved = allPaymentInformationResolved;
	}

	public void setAppliedEvents(List<DomainEvent> appliedEvents) {
		this.appliedEvents = appliedEvents;
	}

	public boolean isPaymentSuccessful() {
		return paymentSuccessful;
	}

	public void setPaymentSuccessful(boolean paymentSuccessful) {
		this.paymentSuccessful = paymentSuccessful;
	}

	public String getPaymentNote() {
		return paymentNote;
	}

	public void setPaymentNote(String paymentNote) {
		this.paymentNote = paymentNote;
	}

	public String generatePaymentNote() {
		return "Payment " + this.transactionId;
	}
}

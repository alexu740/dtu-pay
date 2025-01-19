package micro.events;

import micro.service.CorrelationId;
import boilerplate.Event;

public class PaymentInitialised extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724482L;

	private String customerId;
	private String merchantId;
	private int amount;
	private String token;

    public PaymentInitialised(String customerId, String transactionId, String token, CorrelationId correlationId) {
        super("PaymentInitialised", new Object[] { customerId, token, correlationId, transactionId }, transactionId);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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
}

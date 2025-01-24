package micro.events;

import micro.service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class PaymentResolved extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724486L;
    private String customerBankAccount;
    private String merchantBankAccount;

    public PaymentResolved(String transactionId, String customerBankAccount, String merchantBankAccount, CorrelationId correlationId) {
        super("PaymentResolved", new Object[] { correlationId }, transactionId);
        this.customerBankAccount = customerBankAccount;
        this.merchantBankAccount = merchantBankAccount;
    }

    public String getCustomerBankAccount() {
        return customerBankAccount;
    }

    public void setCustomerBankAccount(String customerBankAccount) {
        this.customerBankAccount = customerBankAccount;
    }

    public String getMerchantBankAccount() {
        return merchantBankAccount;
    }

    public void setMerchantBankAccount(String merchantBankAccount) {
        this.merchantBankAccount = merchantBankAccount;
    }
}

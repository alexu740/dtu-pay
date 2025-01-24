package micro.repositories.viewmodel;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class PaymentInstrumentViewModel {
    private String customerAccountId;
    private String merchantAccountId;
    private String customerBankAccount;
    private String merchantBankAccount;
    
    public String getCustomerAccountId() {
        return customerAccountId;
    }
    public void setCustomerAccountId(String customerAccountId) {
        this.customerAccountId = customerAccountId;
    }
    public String getMerchantAccountId() {
        return merchantAccountId;
    }
    public void setMerchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
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

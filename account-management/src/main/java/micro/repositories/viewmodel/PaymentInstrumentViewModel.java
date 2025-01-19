package micro.repositories.viewmodel;

public class PaymentInstrumentViewModel {
    private String accountId;
    private String bankAccount;
    
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}

package reportservice.models;


public class CustomerPaymentViewModel {
    private String customerToken;
    private String merchantID;
    private int amount;

    public String getCustomerToken() {
        return this.customerToken;
    }
    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }
    public String getMerchantID() {
        return this.merchantID;
    }
    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}



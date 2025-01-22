package reportservice.models;

public class MerchantPaymentViewModel {
    private String customerToken;
    private int amount;

    public String getCustomerToken() {
        return this.customerToken;
    }
    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

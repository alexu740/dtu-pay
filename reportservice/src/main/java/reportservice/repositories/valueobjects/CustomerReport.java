package reportservice.repositories.valueobjects;

public class CustomerReport {
    private int amount;
    private String merchantId;
    private String usedToken;
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public String getUsedToken() {
        return usedToken;
    }
    public void setUsedToken(String usedToken) {
        this.usedToken = usedToken;
    }
}

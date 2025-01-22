package dto;

public class Report {
    private String merchantId;
    private String customerId;
    private String usedToken;
    private int amount;
    
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getUsedToken() {
        return usedToken;
    }
    public void setUsedToken(String usedToken) {
        this.usedToken = usedToken;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

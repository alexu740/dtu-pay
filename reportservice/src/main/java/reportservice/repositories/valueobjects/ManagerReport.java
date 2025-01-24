package reportservice.repositories.valueobjects;
/**
 * @author: Nicolas Venizelou, s232523
 */
public class ManagerReport {
    private String merchantId;
    private String customerId;
    private String tokenUsed;
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
    public String getTokenUsed() {
        return tokenUsed;
    }
    public void setTokenUsed(String tokenUsed) {
        this.tokenUsed = tokenUsed;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

package reportservice.repositories.valueobjects;
/**
 * @author: Nicolas Venizelou, s232523
 */
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

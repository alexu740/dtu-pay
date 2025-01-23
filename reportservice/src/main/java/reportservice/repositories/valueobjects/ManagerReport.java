package reportservice.repositories.valueobjects;

import java.util.Objects;

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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; 
        ManagerReport that = (ManagerReport) obj;
        return amount == that.amount &&
               Objects.equals(merchantId, that.merchantId) &&
               Objects.equals(customerId, that.customerId) &&
               Objects.equals(tokenUsed, that.tokenUsed); 
    }

}

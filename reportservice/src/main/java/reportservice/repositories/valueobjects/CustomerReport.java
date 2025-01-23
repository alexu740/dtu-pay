package reportservice.repositories.valueobjects;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomerReport that = (CustomerReport) obj;
        return amount == that.amount &&
               Objects.equals(merchantId, that.merchantId) &&
               Objects.equals(usedToken, that.usedToken);
    }
    
}


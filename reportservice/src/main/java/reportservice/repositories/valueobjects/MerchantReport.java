package reportservice.repositories.valueobjects;

import java.util.Objects;

public class MerchantReport {
    private String usedToken;
    private int amount;
    public String getUsedToken() {
        return usedToken;
    }
    public void setUsedToken(String tokenUsed) {
        this.usedToken = tokenUsed;
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
    MerchantReport that = (MerchantReport) obj;
    return amount == that.amount &&
           Objects.equals(usedToken, that.usedToken);
}

}

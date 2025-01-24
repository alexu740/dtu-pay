package dto;
/**
 * @author: Lukas Åkefeldt, s242204
 */
public class Report {
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
}

package micro.commands;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class AccountHasTokenQuery {
    private String customerId;
    private String token;
    private String transactionId;

    public AccountHasTokenQuery(String customerId, String token, String transactionId) {
        this.customerId = customerId;
        this.token = token;
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}

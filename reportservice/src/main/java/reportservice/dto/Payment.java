package reportservice.dto;
/**
 * @author: Nicolas Venizelou, s232523
 */
public class Payment {
    private String paymentId;
    private String customerId;
    private String token;
    private String merchantId;
    private int amount;
    
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getCustomerToken() {
        return this.token;
    }
    public void setCustomerToken(String customerToken) {
        this.token = customerToken;
    }
    public String getMerchantId() {
        return this.merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

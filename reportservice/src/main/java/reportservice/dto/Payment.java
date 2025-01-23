package reportservice.dto;

import java.util.Objects;

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
        this.token = customerId;
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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Payment that = (Payment) obj;
        return amount == that.amount &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(token, that.token) &&
            Objects.equals(merchantId, that.merchantId);
}

}

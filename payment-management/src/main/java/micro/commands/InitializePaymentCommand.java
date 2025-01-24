package micro.commands;

import micro.dto.PaymentDto;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class InitializePaymentCommand {
    private String customerId;
    private String merchantId;
    private String token;
    private int amount;

    public InitializePaymentCommand(PaymentDto dto) {
        this.customerId = dto.getCustomerId();
        this.merchantId = dto.getMerchantId();
        this.token = dto.getToken();
        this.amount = dto.getAmount();
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

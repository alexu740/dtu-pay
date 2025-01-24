package com.dtu.pay.Model.Dto;
/**
 * @author: Alex Ungureanu (s225525)
 */
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
}

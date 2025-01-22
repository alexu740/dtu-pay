package com.rest.start.Model.valueobjects;

public class MerchantReport {
    private String tokenUsed;
    private int amount;
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
}

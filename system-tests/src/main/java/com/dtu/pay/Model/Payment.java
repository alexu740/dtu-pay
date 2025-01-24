package com.dtu.pay.Model;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class Payment {
    private String customer;
    private String merchant;
    private int amount;
    
    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getMerchant() {
        return merchant;
    }
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}

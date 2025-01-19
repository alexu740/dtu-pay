package com.dtu.pay.Model;

import java.util.List;

public class Customer extends User {
    private List<String> tokens;

    public Customer(String firstName, String lastName, String cpr) {
        super(firstName, lastName, cpr);
    }
    
    public List<String> getTokens() {
        return tokens;
    }
    
    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}

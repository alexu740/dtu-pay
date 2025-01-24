package com.dtu.pay.Model.Dto;

import java.util.List;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class CustomerTokensResponseDto {
        private String accountId;
    private List<String> tokens;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getTokens() {
        return tokens;
    }
    
    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}

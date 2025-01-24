package com.dtu.pay.Model.Dto;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class TokenRequestDto {
    private String userId;
    private Integer numberOfTokens;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getNumberOfTokens() {
        return numberOfTokens;
    }
    public void setNumberOfTokens(Integer numberOfTokens) {
        this.numberOfTokens = numberOfTokens;
    }
}

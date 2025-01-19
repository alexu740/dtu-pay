package micro.repositories.viewmodel;

import java.util.List;

public class TokenViewModel {
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

package micro.commands;

public class AccountTokenCreationCommand {
    private String accountId;
    private int numberOfTokens;

    public AccountTokenCreationCommand(String accountId, int numberOfTokens) {
        this.accountId = accountId;
        this.numberOfTokens = numberOfTokens;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getNumberOfTokens() {
        return numberOfTokens;
    }

    public void setNumberOfTokens(int numberOfTokens) {
        this.numberOfTokens = numberOfTokens;
    }
}

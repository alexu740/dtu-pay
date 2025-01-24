package micro.commands;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class AccountDeletionCommand {
    private String accountId;

    public AccountDeletionCommand(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}

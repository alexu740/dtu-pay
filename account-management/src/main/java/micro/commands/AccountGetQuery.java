package micro.commands;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class AccountGetQuery{
    public boolean isCustomerAccount;
    public String accountId;

    public AccountGetQuery(boolean isCustomerAccount, String accountId) {
        this.isCustomerAccount = isCustomerAccount;
        this.accountId = accountId;
    }
}

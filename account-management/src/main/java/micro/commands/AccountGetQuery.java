package micro.commands;

public class AccountGetQuery{
    public boolean isCustomerAccount;
    public String accountId;

    public AccountGetQuery(boolean isCustomerAccount, String accountId) {
        this.isCustomerAccount = isCustomerAccount;
        this.accountId = accountId;
    }
}

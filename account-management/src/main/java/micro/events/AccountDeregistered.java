package micro.events;

import micro.aggregate.AccountId;
import micro.service.CorrelationId;

public class AccountDeregistered extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724482L;

    private AccountId accountid;

    public AccountDeregistered(String accountId, CorrelationId correlationId) {
        super("AccountDeregistered", new Object[] { accountId, correlationId }, accountId);
    }

    public String getAccountId() {
        return accountId;
    }

    public String setAccountId(String accountId) {
        this.accountId = new AccountId(accountId);
    }

}

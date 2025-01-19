package micro.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import micro.aggregate.AccountId;
import micro.service.CorrelationId;
import boilerplate.Event;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountCreated extends Event {

	private static final long serialVersionUID = -1599019626118724482L;
	public AccountId accountId;
    public String firstName;
    public String lastName;
    public String cpr;
    public String bankAccount;

    public AccountId getAccountId() {
        return accountId;
    }

    public AccountCreated(AccountId accountId, CorrelationId correlationId) {
        super("AccountRegistered", new Object[] { accountId.getUuid(), correlationId });
    }
}

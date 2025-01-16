package micro.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import micro.aggregate.AccountId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountCreated extends Event {

	private static final long serialVersionUID = -1599019626118724482L;
	private AccountId accountId;
    private String firstName;
    private String lastName;

    public AccountId getAccountId() {
        return accountId;
    }
}

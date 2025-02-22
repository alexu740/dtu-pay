package micro.events;

import micro.aggregate.AccountId;
import micro.service.CorrelationId;

/**
 * @author: Senhao Zou, s242606
 */

public class AccountDeregistered extends DomainEvent {
	private static final long serialVersionUID = -1599019626118724482L;

    public AccountDeregistered(AccountId accountId, CorrelationId correlationId) {
        super("AccountDeregistered", new Object[] { accountId.getUuid(), correlationId }, accountId);
    }

}

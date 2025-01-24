package micro.events;

import java.time.Instant;
import boilerplate.Event;

import micro.aggregate.AccountId;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class DomainEvent extends Event {
	private static final long serialVersionUID = -8571080289905090781L;
	
	private long timestamp;
	private AccountId accountId;

	public DomainEvent(String topic, Object[] arguments, AccountId accountId) {
		super(topic, arguments);
		this.accountId = accountId;
		this.timestamp = Instant.now().toEpochMilli();
	}
	
    public AccountId getAccountId() {
		return this.accountId;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

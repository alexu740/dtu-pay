package micro.events;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import boilerplate.Event;

import micro.aggregate.AccountId;

public class DomainEvent extends Event {
	private static final long serialVersionUID = -8571080289905090781L;
	private static long versionCount = 1;	

	long timestamp;
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

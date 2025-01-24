package micro.events;

import java.time.Instant;

import boilerplate.Event;

/**
 * @author: Alex Ungureanu (s225525)
 */
public class DomainEvent extends Event {
	private static final long serialVersionUID = -8571080289905090781L;
	private static long versionCount = 1;	

	long timestamp;
	private String transactionId;

	public DomainEvent(String topic, Object[] arguments, String transactionId) {
		super(topic, arguments);
		this.transactionId = transactionId;
		this.timestamp = Instant.now().toEpochMilli();
	}
	
    public String getTransactionId() {
		return this.transactionId;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

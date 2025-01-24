package micro.service;

import java.util.UUID;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class CorrelationId {
	private UUID id;
	public CorrelationId(UUID id) {
		this.id = id;
	}
	public static CorrelationId randomId() {
		return new CorrelationId(UUID.randomUUID());
	}

	public String get() {
		return id.toString();
	}
}

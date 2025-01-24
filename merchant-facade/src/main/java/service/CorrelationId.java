package service;

import java.util.UUID;
/**
 * @author: Lukas Åkefeldt, s242204
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

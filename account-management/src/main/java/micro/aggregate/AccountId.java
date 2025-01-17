package micro.aggregate;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class AccountId implements Serializable{
	private static final long serialVersionUID = -1455308747700082116L;
	private UUID uuid;
	public AccountId(UUID uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid.toString();
	}
}

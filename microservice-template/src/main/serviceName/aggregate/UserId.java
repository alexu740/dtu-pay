package usermanagement.domain.aggregate;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserId implements Serializable{
	private static final long serialVersionUID = -1455308747700082116L;
	private UUID uuid;
}

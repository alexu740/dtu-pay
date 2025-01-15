package usermanagement.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import usermanagement.domain.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserCreated extends Event {

	private static final long serialVersionUID = -1599019626118724482L;
	private UserId userId;
    private String firstName;
    private String lastName;
}

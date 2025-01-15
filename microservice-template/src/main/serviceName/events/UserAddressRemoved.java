package usermanagement.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import usermanagement.domain.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserAddressRemoved extends Event {

	private static final long serialVersionUID = 1596683920706802940L;
	private UserId userId;
    private String city;
    private String state;
    private String postCode;
}

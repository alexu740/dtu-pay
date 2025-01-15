package usermanagement.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import usermanagement.domain.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserContactRemoved extends Event {
   
	private static final long serialVersionUID = 3644914234210847153L;
	private UserId userId;
    private String contactType;
    private String contactDetails;
}

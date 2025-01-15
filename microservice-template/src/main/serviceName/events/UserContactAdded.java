package usermanagement.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import usermanagement.domain.aggregate.UserId;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserContactAdded extends Event {
    
	private static final long serialVersionUID = 6473986354743309952L;
	private UserId userId;
    private String contactType;
    private String contactDetails;
}

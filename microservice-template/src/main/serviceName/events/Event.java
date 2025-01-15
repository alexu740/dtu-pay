package usermanagement.domain.events;

import java.io.Serializable;

import lombok.Getter;
import messaging.Message;
import usermanagement.domain.aggregate.UserId;

public abstract class Event implements Message, Serializable {

	private static final long serialVersionUID = -8571080289905090781L;

	private static long versionCount = 1;
	
	@Getter
	private final long version = versionCount++;
			
    public abstract UserId getUserId();

}

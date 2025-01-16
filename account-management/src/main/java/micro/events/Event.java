package micro.events;

import java.io.Serializable;

import lombok.Getter;
import micro.aggregate.AccountId;

public abstract class Event implements Serializable {

	private static final long serialVersionUID = -8571080289905090781L;

	private static long versionCount = 1;
	
	@Getter
	private final long version = versionCount++;
			
    public abstract AccountId getAccountId();

}

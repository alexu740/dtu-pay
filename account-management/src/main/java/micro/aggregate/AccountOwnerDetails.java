package micro.aggregate;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class AccountOwnerDetails {
    private String firstName;
    private String lastName;
    private String cpr;

	public AccountOwnerDetails(String firstName, String lastName, String cpr) {
		this.firstName = firstName;
        this.lastName = lastName;
        this.cpr = cpr;
	}
}

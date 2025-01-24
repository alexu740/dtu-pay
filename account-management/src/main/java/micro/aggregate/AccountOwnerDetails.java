package micro.aggregate;
import lombok.Value;

/**
 * @author: Alex Ungureanu (s225525)
 */

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

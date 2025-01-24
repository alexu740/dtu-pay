package micro.aggregate;

import java.util.List;
import lombok.Value;

/**
 * @author: Alex Ungureanu (s225525)
 */

@Value
public class AccountFinancialDetails {
    private String bankAccount;
    private List<String> validTokens;

    public AccountFinancialDetails(String bankAccount, List<String> tokens) {
	    this.bankAccount = bankAccount;
        this.validTokens = tokens;
	}
}

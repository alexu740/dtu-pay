package micro.aggregate;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class AccountFinancialDetails {
    private String bankAccount;
    private List<String> validTokens;

    public AccountFinancialDetails(String bankAccount, List<String> tokens) {
		  this.bankAccount = bankAccount;
      this.validTokens = tokens;
	  }
}

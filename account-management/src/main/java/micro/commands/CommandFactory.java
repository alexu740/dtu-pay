package micro.commands;

/**
 * @author: Alex Ungureanu (s225525)
 */

import boilerplate.Event;
import micro.dto.RegistrationDto;

public class CommandFactory {
    public static AccountCreationCommand createAccountCreationCommand(RegistrationDto dto, boolean isCustomer) {
        return new AccountCreationCommand(dto, isCustomer);
    }

    public static AccountTokenCreationCommand createAccountTokenCreationCommand(Event e) {
        var custId = e.getArgument(0, String.class);
        var numberOfTokens = e.getArgument(1, String.class);
        return new AccountTokenCreationCommand(custId, Integer.parseInt(numberOfTokens));
    }
}

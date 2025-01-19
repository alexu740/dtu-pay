package micro.commands;

import boilerplate.Event;
import micro.dto.RegistrationDto;

public class CommandFactory {
    public static AccountCreationCommand createAccountCreationCommand(RegistrationDto dto, boolean isCustomer) {
        return new AccountCreationCommand(dto, isCustomer);
    }
}

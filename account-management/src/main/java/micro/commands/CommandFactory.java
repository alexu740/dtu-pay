package micro.commands;

import micro.dto.RegistrationDto;

public class CommandFactory {
    public static AccountCreationCommand createAccountCreationCommand(RegistrationDto dto, boolean isCustomer) {
        return new AccountCreationCommand(dto, isCustomer);
    }
}

package micro.commands;

import micro.dto.RegistrationDto;

public class AccountCreationCommand {
    public String firstName;
    public String lastName;
    public String cpr;
    public String bankAccount;
    public boolean isCustomer;

    public AccountCreationCommand(RegistrationDto eventPayload, boolean isCustomer) {
        this.firstName = eventPayload.firstName;
        this.lastName = eventPayload.lastName;
        this.cpr = eventPayload.cpr;
        this.bankAccount = eventPayload.bankAccount;
        this.isCustomer = isCustomer;
    }
}

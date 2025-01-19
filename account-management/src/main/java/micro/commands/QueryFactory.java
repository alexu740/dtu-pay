package micro.commands;

import boilerplate.Event;
import micro.dto.RegistrationDto;

public class QueryFactory {
    public static AccountGetQuery createAccountGetCommand(Event event, boolean isCustomer) {
        var accId = event.getArgument(0, String.class);
        return new AccountGetQuery(true, accId);
    }
}

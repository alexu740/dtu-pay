package micro.commands;

import boilerplate.Event;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class QueryFactory {
    public static AccountGetQuery createAccountGetCommand(Event event, boolean isCustomer) {
        var accId = event.getArgument(0, String.class);
        return new AccountGetQuery(true, accId);
    }

    public static AccountHasTokenQuery createAccountHasTokenQuery(Event e) {
        var customerId = e.getArgument(0, String.class);
        var token = e.getArgument(1, String.class);
        var transactionId = e.getArgument(3, String.class);

        return new AccountHasTokenQuery(customerId, token, transactionId);
    }
}

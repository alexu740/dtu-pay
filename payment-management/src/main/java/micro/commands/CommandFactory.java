package micro.commands;

import micro.dto.PaymentDto;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class CommandFactory {
    public static InitializePaymentCommand createInitializePaymentCommand(PaymentDto dto) {
        return new InitializePaymentCommand(dto);
    }
}

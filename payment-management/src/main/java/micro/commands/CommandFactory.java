package micro.commands;

import micro.dto.PaymentDto;

public class CommandFactory {
    public static InitializePaymentCommand createInitializePaymentCommand(PaymentDto dto) {
        return new InitializePaymentCommand(dto);
    }
}

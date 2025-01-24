package micro.adapters;

import micro.repositories.viewmodel.PaymentInstrumentViewModel;
import micro.repositories.viewmodel.TokenViewModel;
import micro.service.CorrelationId;

/**
 * @author: Alex Ungureanu (s225525)
 */

public interface EventPublisher {
    public void emitAccountCustomerRetrievedEvent(TokenViewModel vm, CorrelationId correlationId);
    public void emitAccountMerchantRetrievedEvent(CorrelationId correlationId);
    public void emitTokensCreatedEvent(CorrelationId correlationId);
    public void emitTokensCreateFailedEvent(CorrelationId correlationId);
    public void emitCheckTokenPresent(String accountId, String token, boolean present, CorrelationId correlationId, String transactionId);
    public void emitPaymentInformationResolved(String transactionId, PaymentInstrumentViewModel vm, CorrelationId correlationId);
}

package micro.adapters;

import micro.repositories.viewmodel.TokenViewModel;
import micro.service.CorrelationId;

public interface EventPublisher {
    public void emitAccountTokensRequestedEvent(TokenViewModel vm, CorrelationId correlationId);
    public void emitAccountTokensRequestRejectedEvent(CorrelationId correlationId);
    public void emitTokensCreatedEvent(CorrelationId correlationId);
    public void emitTokensCreateFailedEvent(CorrelationId correlationId);
    public void emitCheckTokenPresent(String accountId, String token, boolean present, CorrelationId correlationId, String transactionId);
}

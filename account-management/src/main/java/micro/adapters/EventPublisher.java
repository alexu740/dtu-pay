package micro.adapters;

import micro.repositories.viewmodel.TokenViewModel;
import micro.service.CorrelationId;

public interface EventPublisher {
    public void emitAccountTokensRequestedEvent(TokenViewModel vm, CorrelationId correlationId);
    public void emitAccountTokensRequestRejectedEvent(CorrelationId correlationId);
}

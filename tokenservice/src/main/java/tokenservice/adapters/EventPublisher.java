package tokenservice.adapters;

import tokenservice.service.CorrelationId;

public interface EventPublisher {
    public void emitCustomerHasTokenCheckRequested(String customerId, String token, CorrelationId correlationId, String transactionId);
    public void emitTokenValidated(String customerId, String token, CorrelationId correlationId, String transactionId);
    public void emitTokenValidationFailed(String transactionId, CorrelationId correlationId);
    public void emitTokenUsed(String customerId, String token);
}

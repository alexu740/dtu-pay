package micro.adapters;

import micro.service.CorrelationId;

public interface EventPublisher {
    public void emitPaymentInformationResolutionRequested(String transactionId, String customerId, String merchantId, CorrelationId correlationId);
}

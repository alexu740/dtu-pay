package micro.adapters;

import micro.service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public interface EventPublisher {
    public void emitPaymentInformationResolutionRequested(String transactionId, String customerId, String merchantId, CorrelationId correlationId);
}

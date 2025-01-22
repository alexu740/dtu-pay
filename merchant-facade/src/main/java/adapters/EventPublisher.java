package adapters;

import dto.PaymentDto;
import dto.RegistrationDto;
import service.CorrelationId;

public interface EventPublisher {
    public void emitCreateUserEvent(RegistrationDto payload, CorrelationId correlationId);
    public void emitInitialisePayment(PaymentDto payload, CorrelationId correlationId);
    public void emitUnregisterUserEvent(String merchantId, CorrelationId correlationId);
    public void emitMerchantReportRequested(String merchantId, CorrelationId correlationId);
}

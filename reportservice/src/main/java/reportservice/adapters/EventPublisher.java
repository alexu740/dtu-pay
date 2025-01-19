package reportservice.adapters;

import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

import java.util.List;

public interface EventPublisher {
    public void emitPaymentStorageSucceededEvent(CorrelationId correlationId);
    public void emitPaymentStorageFailedEvent(CorrelationId correlationId,Exception e);
    public void emitPaymentReportSucceededEvent(CorrelationId correlationId,List<Payment> transactions);
    public void emitPaymentReportFailedEvent(CorrelationId correlationId,Exception e);
    public void emitMerchantReportSucceededEvent(CorrelationId correlationId,List<Payment> transactions);
    public void emitMerchantReportFailedEvent(CorrelationId correlationId,Exception e);
}

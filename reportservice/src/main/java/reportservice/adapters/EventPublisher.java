package reportservice.adapters;

import reportservice.dto.Payment;
import reportservice.models.CustomerPaymentViewModel;
import reportservice.models.MerchantPaymentViewModel;
import reportservice.services.CorrelationId;

import java.util.List;

public interface EventPublisher {
    public void emitPaymentStorageSucceededEvent(CorrelationId correlationId);
    public void emitPaymentStorageFailedEvent(CorrelationId correlationId,Exception e);
    public void emitPaymentReportSucceededEvent(CorrelationId correlationId,List<Payment> payments);
    public void emitPaymentReportFailedEvent(CorrelationId correlationId,Exception e);
    public void emitMerchantReportSucceededEvent(CorrelationId correlationId,List<MerchantPaymentViewModel> payments);
    public void emitMerchantReportFailedEvent(CorrelationId correlationId,Exception e);
    public void emitCustomerReportSucceededEvent(CorrelationId correlationId,List<CustomerPaymentViewModel> payments);
    public void emitCustomerReportFailedEvent(CorrelationId correlationId,Exception e);
}

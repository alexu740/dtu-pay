package reportservice.lib;

import reportservice.dto.Payment;
import reportservice.services.CorrelationId;

public interface IService {

    void handlePaymentReceived(CorrelationId correlationId, Payment payment);

    void handlePaymentReportRequested(CorrelationId correlationId);

    void handleMerchantReportRequested(CorrelationId correlationId,String merchantID);

    void handleCustomerReportRequested(CorrelationId correlationId,String customerID);

}

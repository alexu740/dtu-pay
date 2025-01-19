package reportservice.lib;

import reportservice.boilerplate.implementations.RabbitMqQueue;
import reportservice.boilerplate.MessageQueue;
import reportservice.boilerplate.Event;

import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

public interface IService {

    void handlePaymentReceived(Payment payment,CorrelationId correlationId);

    void handleCustomerReportRequested(Event ev);

    void handleMerchantReportRequested(CorrelationId correlationId,String id);

}

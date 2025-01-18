package reportservice.lib;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;

public interface IService {

    void handlePaymentReceived(Event ev);

    void handleCustomerReportRequested(Event ev);

    void handleMerchantReportRequested(Event ev);

}

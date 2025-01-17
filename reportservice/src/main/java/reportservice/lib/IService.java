package reportservice.lib;

import messaging.Event;

public interface IService {

    void handlePaymentReceived(Event ev);

    void handleCustomerReportRequested(Event ev);

    void handleMerchantReportRequested(Event ev);

}

package reportservice.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import reportservice.dto.Payment;
import reportservice.services.CorrelationId;
import reportservice.services.Service;
/**
 * @author: Nicolas Venizelou, s232523
 */
public class RabbitMqFacade {
    Service service;

    public RabbitMqFacade(MessageQueue queue, Service service) {
        queue.addHandler("MerchantReportRequested", this::handleMerchantReportRequested);
        queue.addHandler("CustomerReportRequested", this::handleCustomerReportRequested);
        queue.addHandler("ManagerReportRequested", this::handlePaymentsReportRequested);
        queue.addHandler("PaymentSucceeded", this::handlePaymentCompleted);

        this.service = service;
    }

    private void handlePaymentCompleted(Event ev) {
        var payment = ev.getArgument(0, Payment.class);
        service.handlePaymentReceived(payment);
    }

    private void handlePaymentsReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        service.handlePaymentReportRequested(correlationId);
    }
    private void handleMerchantReportRequested(Event ev) {
        var id = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        service.handleMerchantReportRequested(correlationId, id);
    }
    private void handleCustomerReportRequested(Event ev) {
        var id = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        service.handleCustomerReportRequested(correlationId, id);
    }
}

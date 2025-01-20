package reportservice.adapters;

import reportservice.dto.Payment;
import reportservice.services.CorrelationId;
import reportservice.services.Service;
import boilerplate.Event;
import boilerplate.MessageQueue;

public class RabbitMqFacade {
    Service service;

    public RabbitMqFacade(MessageQueue queue, Service service) {
        queue.addHandler("payments.report.merchant.requested", this::handleMerchantReportRequested);
        queue.addHandler("payments.report.customer.requested", this::handleCustomerReportRequested);
        queue.addHandler("payment.storage.requested", this::handlePaymentReceived);
        queue.addHandler("payments.report.requested", this::handlePaymentsReportRequested);
        this.service = service;
    }

    private void handlePaymentReceived(Event ev) {
        var payment = ev.getArgument(1, Payment.class);
        var correlationId = ev.getArgument(0, CorrelationId.class);
        service.handlePaymentReceived(correlationId, payment);
    }

    private void handlePaymentsReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        service.handlePaymentReportRequested(correlationId);
    }
    private void handleMerchantReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        var id = ev.getArgument(1, String.class);
        service.handleMerchantReportRequested(correlationId, id);
    }
    private void handleCustomerReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        var id = ev.getArgument(1, String.class);
        service.handleCustomerReportRequested(correlationId, id);
    }
}

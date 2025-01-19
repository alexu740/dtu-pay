package reportservice.adapters;

import reportservice.boilerplate.Event;
import reportservice.boilerplate.MessageQueue;
import reportservice.impl.Service;
import reportservice.impl.CorrelationId;
import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

public class RabbitMqFacade {
    Service service;

    public RabbitMqFacade(MessageQueue queue, Service service) {
        queue.addHandler("payments.report.merchant.requested", this::handleMerchantReportRequested);
        queue.addHandler("payment.storage.requested", this::handlePaymentReceived);
        queue.addHandler("payments.report.requested", this::handlePaymentsReportRequested);
        this.service = service;
    }

    private void handlePaymentReceived(Event ev) {
        var payment = ev.getArgument(1, Payment.class);
        var correlationId = ev.getArgument(0, CorrelationId.class);
        service.handlePaymentReceived(payment, correlationId);
    }

    private void handlePaymentsReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        service.handlePaymentsReportRequested(correlationId);
    }
    private void handleMerchantReportRequested(Event ev) {
        var correlationId = ev.getArgument(0, CorrelationId.class);
        var id = ev.getArgument(1, String.class);
        service.handleMerchantReportRequested(correlationId, id);
    }
}

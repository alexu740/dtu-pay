package adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import impl.Service;
import impl.CorrelationId;
import dto.Payment;
import impl.CorrelationId;

public class RabbitMqFacade {
    Service service;

    public RabbitMqFacade(MessageQueue queue, Service service) {
        queue.addHandler("customer.report.customer.requested", this::handleCustomerReportRequested);
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

    private void handleMerchantRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }
    private void handleMerchantRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }
    private void handleMerchantRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }
}

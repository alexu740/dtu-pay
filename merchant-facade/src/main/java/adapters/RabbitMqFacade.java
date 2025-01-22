package adapters;

import java.util.ArrayList;
import java.util.List;

import boilerplate.Event;
import boilerplate.MessageQueue;
import service.MerchantFacadeService;
import service.CorrelationId;
import dto.Report;

public class RabbitMqFacade {
    MerchantFacadeService service;

    public RabbitMqFacade(MessageQueue queue, MerchantFacadeService service) {
        queue.addHandler("AccountRegistered", this::handleMerchantRegistration);
        queue.addHandler("AccountDeregistered", this::handleAccountDeregistred);
        queue.addHandler("PaymentSucceeded", this::handlePaymentSucceeded);
        queue.addHandler("PaymentFailed", this::handlePaymentFailed);
        queue.addHandler("MerchantReportSent", this::handleMerchantReportSent);

        this.service = service;
    }

    private void handleMerchantRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }
    
    public void handleAccountDeregistred(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeDeregisteration(eventPayload, correlationid, true);
    }

    private void handlePaymentSucceeded(Event e) {
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completePaymentTransaction(true, correlationid);
    }

    private void handlePaymentFailed(Event e) {
        var correlationid = e.getArgument(0, CorrelationId.class);
        service.completePaymentTransaction(false, correlationid);
    }

    public void handleMerchantReportSent(Event e) { 
        var report = e.getArgument(0, List.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeReportRequest(report, correlationid);
    }
}

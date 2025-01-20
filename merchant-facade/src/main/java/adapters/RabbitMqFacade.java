package adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import service.MerchantFacadeService;
import service.CorrelationId;

public class RabbitMqFacade {
    MerchantFacadeService service;

    public RabbitMqFacade(MessageQueue queue, MerchantFacadeService service) {
        queue.addHandler("AccountRegistered", this::handleMerchantRegistration);
        queue.addHandler("PaymentSucceeded", this::handlePaymentSucceeded);
        queue.addHandler("PaymentFailed", this::handlePaymentFailed);
        
        
        this.service = service;
    }

    private void handleMerchantRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }

    private void handlePaymentSucceeded(Event e) {
        var correlationid = e.getArgument(0, CorrelationId.class);
        service.completePaymentTransaction(true, correlationid);
    }

    private void handlePaymentFailed(Event e) {
        var correlationid = e.getArgument(0, CorrelationId.class);
        service.completePaymentTransaction(false, correlationid);
    }
}

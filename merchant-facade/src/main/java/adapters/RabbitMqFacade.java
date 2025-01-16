package adapters;

import messaging.Event;
import messaging.MessageQueue;
import service.MerchantFacadeService;
import service.CorrelationId;

public class RabbitMqFacade {
    MerchantFacadeService service;

    public RabbitMqFacade(MessageQueue queue, MerchantFacadeService service) {
        queue.addHandler("TestEvent", this::handleMerchantRegistration);
        this.service = service;
    }

    private void handleMerchantRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }
}

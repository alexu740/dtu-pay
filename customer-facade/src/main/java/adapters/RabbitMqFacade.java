package adapters;

import messaging.Event;
import messaging.MessageQueue;
import service.CustomerFacadeService;
import service.CorrelationId;

public class RabbitMqFacade {
    CustomerFacadeService service;

    public RabbitMqFacade(MessageQueue queue, CustomerFacadeService service) {
        queue.addHandler("TestEvent", this::handleCustomerRegistration);
        this.service = service;
    }

    private void handleCustomerRegistration(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid);
    }
}

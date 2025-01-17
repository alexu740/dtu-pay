package adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import service.CustomerFacadeService;
import service.CorrelationId;

public class RabbitMqFacade {
    CustomerFacadeService service;

    public RabbitMqFacade(MessageQueue queue, CustomerFacadeService service) {
        queue.addHandler("AccountRegistered", this::handleAccountRegistred);
        queue.addHandler("AccountRegistrationFailed", this::handleAccountRegistrationFailed);
        this.service = service;
    }

    public void handleAccountRegistred(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid, true);
    }

    public void handleAccountRegistrationFailed(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeRegistration(eventPayload, correlationid, false);
    }
}

package adapters;

import java.util.List;

import boilerplate.Event;
import boilerplate.MessageQueue;
import dto.AccountTokensDto;
import service.CustomerFacadeService;
import service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class RabbitMqFacade {
    CustomerFacadeService service;

    public RabbitMqFacade(MessageQueue queue, CustomerFacadeService service) {
        queue.addHandler("AccountRegistered", this::handleAccountRegistred);
        queue.addHandler("AccountRegistrationFailed", this::handleAccountRegistrationFailed);
        queue.addHandler("AccountDeregistered", this::handleAccountDeregistred);
        queue.addHandler("CustomerRetrieved", this::handleCustomerRetrieved);
        queue.addHandler("TokensCreated", this::handleTokensCreated);
        queue.addHandler("TokensCreateFailed", this::handleTokensCreated);
        queue.addHandler("CustomerReportSent", this::handleCustomerReportSent);


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

    public void handleAccountDeregistred(Event e) {
        var eventPayload = e.getArgument(0, String.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeDeregisteration(eventPayload, correlationid, true);
    }

    public void handleCustomerRetrieved(Event e) { 
        var eventPayload = e.getArgument(0, AccountTokensDto.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeAccountRetrievalRequest(eventPayload, correlationid, true);
    }

    public void handleTokensCreated(Event e) { 
        var successful = e.getType().equals("TokensCreated");
        var correlationid = e.getArgument(0, CorrelationId.class);
        service.completeTokenCreationRequest(successful, correlationid);
    }

    public void handleCustomerReportSent(Event e) { 
        var report = e.getArgument(0, List.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeReportRequest(report, correlationid);
    }
}

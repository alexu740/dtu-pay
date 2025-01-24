package adapters;

import java.util.ArrayList;
import java.util.List;

import boilerplate.Event;
import boilerplate.MessageQueue;
import service.ManagerFacadeService;
import service.CorrelationId;
import dto.Report;
/**
 * @author: Alex Ungureanu (s225525)
 */
public class RabbitMqFacade {
    ManagerFacadeService service;

    public RabbitMqFacade(MessageQueue queue, ManagerFacadeService service) {
        queue.addHandler("ManagerReportSent", this::handleManagerReportSent);

        this.service = service;
    }

    public void handleManagerReportSent(Event e) { 
        var report = e.getArgument(0, List.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        service.completeReportRequest(report, correlationid);
    }
}

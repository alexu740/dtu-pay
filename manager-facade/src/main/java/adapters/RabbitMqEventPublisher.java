package adapters;

import adapters.EventPublisher;
import boilerplate.Event;
import boilerplate.MessageQueue;
import service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;
    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitManagerReportRequested(CorrelationId correlationId) {
        Event event = new Event("ManagerReportRequested", new Object[] { correlationId });
        queue.publish(event);
    }
}

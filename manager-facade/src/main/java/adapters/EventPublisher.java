package adapters;

import service.CorrelationId;

public interface EventPublisher {
    public void emitManagerReportRequested(CorrelationId correlationId);
}

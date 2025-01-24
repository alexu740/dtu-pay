package adapters;

import service.CorrelationId;
/**
 * @author: Alex Ungureanu (s225525)
 */
public interface EventPublisher {
    public void emitManagerReportRequested(CorrelationId correlationId);
}

package adapters;

import service.CorrelationId;

public interface EventPublisher {
    public void emitCreateUserEvent(String payload, CorrelationId correlationId);
    public void emitUnregisterUserEvent();
}

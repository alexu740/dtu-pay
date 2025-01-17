package adapters;

import dto.RegistrationDto;
import service.CorrelationId;

public interface EventPublisher {
    public void emitCreateUserEvent(RegistrationDto payload, CorrelationId correlationId);
    public void emitUnregisterUserEvent();
}

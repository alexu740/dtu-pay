package adapters;

import dto.Payment;
import impl.CorrelationId;

public interface EventPublisher {
    public void emitCreateUserEvent();
    public void emitUnregisterUserEvent();
}

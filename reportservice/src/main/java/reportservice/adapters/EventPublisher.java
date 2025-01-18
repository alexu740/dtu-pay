package reportservice.adapters;

import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

public interface EventPublisher {
    public void emitCreateUserEvent();
    public void emitUnregisterUserEvent();
}

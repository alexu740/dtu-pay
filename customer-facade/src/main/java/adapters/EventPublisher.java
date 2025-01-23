package adapters;

import dto.RegistrationDto;
import service.CorrelationId;

public interface EventPublisher {
    public void emitRetrieveUserEvent(String id, CorrelationId correlationId);
    public void emitCreateUserEvent(RegistrationDto payload, CorrelationId correlationId);
    public void emitUnregisterUserEvent(String customerId, CorrelationId correlationId);
    public void emitCreateTokensEvent(String customerId, String tokenNumber, CorrelationId correlationId);
    public void emitCustomerReportRequested(String customerId, CorrelationId correlationId);
}

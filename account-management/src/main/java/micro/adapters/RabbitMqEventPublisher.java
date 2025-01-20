package micro.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import micro.repositories.viewmodel.PaymentInstrumentViewModel;
import micro.repositories.viewmodel.TokenViewModel;
import micro.service.CorrelationId;

public class RabbitMqEventPublisher implements EventPublisher {
    private MessageQueue queue;

    public RabbitMqEventPublisher(MessageQueue queue) {
        this.queue = queue;
    }

    public void emitAccountTokensRequestedEvent(TokenViewModel vm, CorrelationId correlationId) {
        queue.publish(new Event("CustomerRetrieved", new Object[] { vm, correlationId }));
    }

    public void emitAccountTokensRequestRejectedEvent(CorrelationId correlationId) {
        queue.publish(new Event("CustomerRetrieved", new Object[] { correlationId }));
    }

    public void emitTokensCreatedEvent(CorrelationId correlationId) {
        queue.publish(new Event("TokensCreated", new Object[] { correlationId }));
    }

    public void emitTokensCreateFailedEvent(CorrelationId correlationId) {
        queue.publish(new Event("TokensCreateFailed", new Object[] { correlationId }));
    }

    public void emitCheckTokenPresent(String accountId, String token, boolean present, CorrelationId correlationId, String transactionId) {
        queue.publish(new Event("CustomerHasTokenChecked", new Object[] { accountId, token, present, correlationId, transactionId }));
    }

    public void emitPaymentInformationResolved(String transactionId, PaymentInstrumentViewModel vm, CorrelationId correlationId) {
        queue.publish(new Event("PaymentInformationResolved", new Object[] { transactionId, vm.getCustomerBankAccount(), vm.getMerchantBankAccount(), correlationId }));
    }
}

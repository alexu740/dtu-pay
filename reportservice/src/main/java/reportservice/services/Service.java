package reportservice.services;

import reportservice.lib.IRepository;
import reportservice.lib.IService;
import reportservice.adapters.EventPublisher;

import reportservice.commands.PaymentCommandHandler;
import reportservice.dto.Payment;
import reportservice.queries.PaymentQueryHandler;
import reportservice.repositories.Repository;
import reportservice.models.CustomerPaymentViewModel;
import reportservice.models.MerchantPaymentViewModel;

import java.util.List;

public class Service implements IService {
    private final PaymentCommandHandler commandHandler;
    private final PaymentQueryHandler queryHandler;
    private final EventPublisher publisher;

    public Service(Repository repo, EventPublisher publisher) {
        this.publisher = publisher;
        this.commandHandler = new PaymentCommandHandler(repo);
        this.queryHandler = new PaymentQueryHandler(repo);
    }

    @Override
    public void handlePaymentReceived(CorrelationId correlationId, Payment payment) {
        try {
            commandHandler.addPayment(payment);
            publisher.emitPaymentStorageSucceededEvent(correlationId);
        } catch (Exception e) {
            publisher.emitPaymentStorageFailedEvent(correlationId, e);
        }
    }

    public void handlePaymentReportRequested(CorrelationId correlationId) {
        try {
            List<Payment> payments = queryHandler.getAllPayments(); 
            publisher.emitPaymentReportSucceededEvent(correlationId, payments);
        } catch (Exception e) {
            publisher.emitPaymentReportFailedEvent(correlationId, e);
        }
    }

    public void handleMerchantReportRequested(CorrelationId correlationId, String merchantID) {
        try {
            List<MerchantPaymentViewModel> payments = queryHandler.getMerchantPayments(merchantID);
            publisher.emitMerchantReportSucceededEvent(correlationId, payments);
        } catch (Exception e) {
            publisher.emitMerchantReportFailedEvent(correlationId, e);
        }
    }

    public void handleCustomerReportRequested(CorrelationId correlationId, String customerID) {
        try {
            List<CustomerPaymentViewModel> payments = queryHandler.getCustomerPayments(customerID);
            publisher.emitCustomerReportSucceededEvent(correlationId, payments);
        } catch (Exception e) {
            publisher.emitCustomerReportFailedEvent(correlationId, e);
        }
    }
}

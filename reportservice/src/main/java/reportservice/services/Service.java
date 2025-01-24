package reportservice.services;

import reportservice.adapters.EventPublisher;
import reportservice.dto.Payment;
import reportservice.repositories.ReadModelRepository;
import reportservice.repositories.ReportRepository;
/**
 * @author: Nicolas Venizelou, s232523
 */
public class Service {
    private final EventPublisher publisher;
    private ReportRepository writeRepo;
    ReadModelRepository readRepo; 

    public Service(ReportRepository writeRepo, ReadModelRepository readRepo, EventPublisher publisher) {
        this.writeRepo = writeRepo;
        this.readRepo = readRepo;
        this.publisher = publisher;
    }

    public void handlePaymentReceived(Payment payment) {
        this.writeRepo.save(payment);
    }

    public void handlePaymentReportRequested(CorrelationId correlationId) {
        var managerReport = readRepo.getManagerReport();
        publisher.emitManagerReportGenerated(managerReport, correlationId);
    }

    public void handleMerchantReportRequested(CorrelationId correlationId, String merchantID) {
        var report = readRepo.getPaymentsByMerchantID(merchantID);
        publisher.emitMerchantReportGenerated(report, correlationId);
    }

    public void handleCustomerReportRequested(CorrelationId correlationId, String customerID) {
        var report = readRepo.getPaymentsByCustomerID(customerID);
        publisher.emitCustomerReportGenerated(report, correlationId);
    }
}
package reportservice.adapters;

import java.util.List;

import reportservice.repositories.valueobjects.CustomerReport;
import reportservice.repositories.valueobjects.ManagerReport;
import reportservice.repositories.valueobjects.MerchantReport;
import reportservice.services.CorrelationId;

public interface EventPublisher {
    public void emitManagerReportGenerated(List<ManagerReport> report, CorrelationId correlationId);
    public void emitMerchantReportGenerated(List<MerchantReport> report, CorrelationId correlationId);
    public void emitCustomerReportGenerated(List<CustomerReport> report, CorrelationId correlationId);
}

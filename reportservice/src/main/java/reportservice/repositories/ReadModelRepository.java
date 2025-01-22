package reportservice.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boilerplate.Event;
import boilerplate.MessageQueue;
import reportservice.dto.Payment;
import reportservice.repositories.valueobjects.CustomerReport;
import reportservice.repositories.valueobjects.ManagerReport;
import reportservice.repositories.valueobjects.MerchantReport;

public class ReadModelRepository {
    private MessageQueue queue;
    private final List<ManagerReport> managerReport = new ArrayList<ManagerReport>();

    private final Map<String, List<CustomerReport>> customerReport = new HashMap<>();
    private final Map<String, List<MerchantReport>> merchantReport = new HashMap<>();

    public ReadModelRepository(MessageQueue queue) {
        this.queue = queue;
        queue.addHandler("ReportGenerated", this::applyReport);
    }

    private void applyReport(Event e) {
        var payment = e.getArgument(0, Payment.class);

        updateManagerReport(payment);
        updateCustomerReports(payment);
        updateMerchantReports(payment);
    }

    private void updateManagerReport(Payment payment) {
        var report = new ManagerReport();
        report.setAmount(payment.getAmount());
        report.setCustomerId(payment.getCustomerId());
        report.setMerchantId(payment.getMerchantId());
        report.setTokenUsed(payment.getCustomerToken());
        managerReport.add(report);
    }

    private void updateCustomerReports(Payment payment) {
        var report = new CustomerReport();
        report.setAmount(payment.getAmount());
        report.setUsedToken(payment.getCustomerToken());
        report.setMerchantId(payment.getMerchantId());
        if(customerReport.get(payment.getCustomerId()) == null) {
            customerReport.put(payment.getCustomerId(), new ArrayList<CustomerReport>());
        }
        customerReport.get(payment.getCustomerId()).add(report);
    }

    private void updateMerchantReports(Payment payment) {
        var report = new MerchantReport();
        report.setAmount(payment.getAmount());
        report.setUsedToken(payment.getCustomerToken());
        if(merchantReport.get(payment.getMerchantId()) == null) {
            merchantReport.put(payment.getMerchantId(), new ArrayList<MerchantReport>());
        }
        merchantReport.get(payment.getMerchantId()).add(report);
    }

    public List<ManagerReport> getManagerReport() {
        return new ArrayList<ManagerReport>(managerReport);
    }

    // Query: Get payments by customer ID
    public List<CustomerReport> getPaymentsByCustomerID(String customerId) {
        var list = customerReport.getOrDefault(customerId, new ArrayList<CustomerReport>());
        return new ArrayList<CustomerReport>(list);
    }

    // Query: Get payments by merchant ID
    public List<MerchantReport> getPaymentsByMerchantID(String merchantId) {
        var list = merchantReport.getOrDefault(merchantId, new ArrayList<MerchantReport>());
        return new ArrayList<MerchantReport>(list);
    }
}

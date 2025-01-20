package reportservice.queries;

import java.util.ArrayList;
import java.util.List;

import reportservice.dto.Payment;
import reportservice.models.CustomerPaymentViewModel;
import reportservice.models.MerchantPaymentViewModel;
import reportservice.repositories.Repository;

public class PaymentQueryHandler {
    private Repository repository;

    public PaymentQueryHandler(Repository repository) {
        this.repository = repository;
    }
    public List<Payment> getAllPayments() {
        return repository.getAllPayments();
    }

    public List<CustomerPaymentViewModel> getCustomerPayments(String customerID) {
        List<Payment> payments = repository.getPaymentsByCustomerID(customerID);
        List<CustomerPaymentViewModel> viewModels = new ArrayList<>();
        
        for (Payment payment : payments) {
            CustomerPaymentViewModel viewModel = new CustomerPaymentViewModel();
            viewModel.setCustomerToken(payment.getCustomerToken());
            viewModel.setMerchantID(payment.getMerchantID());
            viewModel.setAmount(payment.getAmount());
            viewModels.add(viewModel);
        }
        
        return viewModels;
    }

    public List<MerchantPaymentViewModel> getMerchantPayments(String merchantID) {
        List<Payment> payments = repository.getPaymentsByMerchantID(merchantID);
        List<MerchantPaymentViewModel> viewModels = new ArrayList<>();
        
        for (Payment payment : payments) {
            MerchantPaymentViewModel viewModel = new MerchantPaymentViewModel();
            viewModel.setCustomerToken(payment.getCustomerToken());
            viewModel.setAmount(payment.getAmount());
            viewModels.add(viewModel);
        }
        
        return viewModels;
    }


}

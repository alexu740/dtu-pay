package reportservice.repositories;

import reportservice.dto.Payment;
import reportservice.lib.IRepository;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<Payment> payments = new ArrayList<>();

    // Command: Add a payment
    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    // Query: Get all payments
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments); // Return a copy to avoid external modification
    }

    // Query: Get payments by customer ID
    public List<Payment> getPaymentsByCustomerID(String customerID) {
        List<Payment> customerPayments = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getCustomerID().equals(customerID)) {
                customerPayments.add(payment);
            }
        }
        return customerPayments;
    }

    // Query: Get payments by merchant ID
    public List<Payment> getPaymentsByMerchantID(String merchantID) {
        List<Payment> merchantPayments = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getMerchantID().equals(merchantID)) {
                merchantPayments.add(payment);
            }
        }
        return merchantPayments;
    }
}

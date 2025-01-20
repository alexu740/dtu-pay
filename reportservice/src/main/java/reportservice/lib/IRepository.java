package reportservice.lib;

import reportservice.dto.*;

import java.util.List;

public interface IRepository {

    void addPayment(Payment payment);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByMerchantID(String merchantID);

    List<Payment> getPaymentsByCustomerID(String customerID);

}

package reportservice.lib;

import dtupay.dto.*;

import java.util.List;

public interface IRepository {

    void addTransaction(Payment payment);

    List<Payment> getTransactions();

    List<Payment> getMerchantTransactions(String id);

}

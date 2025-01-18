package reportservice.lib;

import reportservice.dto.*;

import java.util.List;

public interface IRepository {

    void addTransaction(Payment payment);

    List<Payment> getTransactions();

    List<Payment> getMerchantTransactions(String id);

}

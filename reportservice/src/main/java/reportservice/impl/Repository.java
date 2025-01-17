package reportservice.impl;

import reportservice.dto.Payment;
import reportservice.lib.IRepository;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private ArrayList<Payment> transactions = new ArrayList<Payment>();

    public void addTransaction(Payment payment) {
        transactions.add(payment);
    }

    public List<Payment> getTransactions() {
        return transactions;
    }

    public List<Payment> getMerchantTransactions(String id) {
        ArrayList<Payment> merchantTransactions = new ArrayList<Payment>();
        for (Payment payment : transactions) {
            if (payment.getMerchantId().equals(id)) {
                merchantTransactions.add(payment);
            }
        }
        return merchantTransactions;
    }
}

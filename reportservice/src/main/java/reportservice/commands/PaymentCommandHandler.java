package reportservice.commands;

import reportservice.dto.Payment;
import reportservice.repositories.Repository;

public class PaymentCommandHandler {
    private Repository repository;

    public PaymentCommandHandler(Repository repository) {
        this.repository = repository;
    }

    public void addPayment(Payment payment) {
        repository.addPayment(payment);
    }
}
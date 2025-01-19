package micro.service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import micro.adapters.EventPublisher;
import micro.aggregate.Payment;
import micro.commands.InitializePaymentCommand;
import micro.exception.BusinessValidationException;
import micro.repositories.PaymentReadModelRepository;
import micro.repositories.PaymentRepository;

public class PaymentManagementService {

	private PaymentRepository repository;
	private PaymentReadModelRepository readRepository;
	private EventPublisher publisher;

	public PaymentManagementService(PaymentRepository repository, PaymentReadModelRepository readRepository, EventPublisher publisher) {
		this.readRepository = readRepository;
		this.repository = repository;
		this.publisher = publisher;
	}
	
	/* Command Operations */
	public void handlePaymentCommand(InitializePaymentCommand command, CorrelationId correlationId) {
		Payment payment = Payment.create(command.getCustomerId(), command.getMerchantId(), command.getToken(), command.getAmount(), correlationId);
		this.repository.save(payment);
	}
}

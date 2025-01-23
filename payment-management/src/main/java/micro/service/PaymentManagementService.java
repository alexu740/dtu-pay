package micro.service;


import micro.adapters.EventPublisher;
import micro.adapters.ExternalBank;
import micro.aggregate.Payment;
import micro.commands.InitializePaymentCommand;
import micro.repositories.PaymentReadModelRepository;
import micro.repositories.PaymentRepository;

public class PaymentManagementService {

	private PaymentRepository repository;
	private PaymentReadModelRepository readRepository;
	private EventPublisher publisher;
	private ExternalBank bank;

	public PaymentManagementService(PaymentRepository repository, PaymentReadModelRepository readRepository, EventPublisher publisher, ExternalBank bank) {
		this.readRepository = readRepository;
		this.repository = repository;
		this.publisher = publisher;
		this.bank = bank;
	}
	
	/* Command Operations */
	public void handlePaymentInitializationCommand(InitializePaymentCommand command, CorrelationId correlationId) {
		Payment payment = Payment.create(command.getCustomerId(), command.getMerchantId(), command.getToken(), command.getAmount(), correlationId);
		this.repository.save(payment);
	}

	public void handleTokenValidated(String customerId, String validToken, CorrelationId correlationId, String transactionId) {
		var payment = this.repository.getById(transactionId);
		payment.updatePaymentTokenValidated();
		this.repository.save(payment);
		this.publisher.emitPaymentInformationResolutionRequested(transactionId, payment.getCustomerId(), payment.getMerchantId(), correlationId);
	}

	public void handlePaymentTokenValidationFailed(String transactionId, CorrelationId correlationId) {
		var payment = this.repository.getById(transactionId);
		payment.updateTokenInvalid(correlationId);
		this.repository.save(payment);
	}

	public void handlePaymentInformationResolved(String transactionId, String customerBank, String merchantBank, CorrelationId correlationId) {
		var payment = this.repository.getById(transactionId);
		payment.update(customerBank, merchantBank, correlationId);
		this.repository.save(payment);
	}
	
	public void handlePaymentTransaction(String transactionId, CorrelationId correlationId) {
		var payment = this.repository.getById(transactionId);
		var result = bank.pay(payment.getCustomerBankAccount(), payment.getMerchantBankAccount(), payment.getAmount(), payment.generatePaymentNote());
		
		payment.markAsCompleted(result, correlationId);
		this.repository.save(payment);
	}
}

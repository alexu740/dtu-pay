package reportservice.impl;

import reportservice.lib.IRepository;
import reportservice.lib.IService;

import reportservice.adapters.EventPublisher;

import reportservice.boilerplate.implementations.RabbitMqQueue;
import reportservice.boilerplate.MessageQueue;
import reportservice.boilerplate.Event;

import reportservice.dto.Payment;
import reportservice.impl.CorrelationId;

import java.util.List;

public class Service implements IService {
	private final IRepository repository;
	private final MessageQueue queue;
	private EventPublisher publisher;

	public Service(MessageQueue q, IRepository repo,EventPublisher publisher) {
		this.publisher = publisher;
		this.queue = q;
		this.repository = repo;
	}

	@Override
	public void handlePaymentReceived(Payment payment,CorrelationId correlationId) {
		Event ev;
		try {
			repository.addTransaction(payment);
			publisher.emitPaymentStorageSucceededEvent(correlationId);
			// ev = new Event("payment.storage.succeeded", new Object[] { correlationId });
		} catch (Exception e) {
			publisher.emitPaymentStorageFailedEvent(correlationId,e);
			//ev = new Event("payment.storage.failed", new Object[] { correlationId, e });
		}
		// Publish the event
		//queue.publish(ev);
	}

	public void handlePaymentsReportRequested(CorrelationId correlationId) {
		Event ev;
		try {
			List<Payment> transactions = repository.getTransactions();
			publisher.emitPaymentReportSucceededEvent(correlationId, transactions);
			//ev = new Event("payments.report.succeeded", new Object[] {  });
		} catch (Exception e) {
			publisher.emitPaymentReportFailedEvent(correlationId, e);
			//ev = new Event("payments.report.failed", new Object[] { correlationId, e });
		}
		//queue.publish(ev);
	}

	public void handleMerchantReportRequested(CorrelationId correlationId,String id) {
		Event ev;
		try {
			List<Payment> transactions = repository.getMerchantTransactions(id);
			publisher.emitMerchantReportSucceededEvent(correlationId, transactions);
			//ev = new Event("payments.report.succeeded", new Object[] { correlationId, transactions });
		} catch (Exception e) {
			publisher.emitMerchantReportFailedEvent(correlationId, e);
			//ev = new Event("payments.report.failed", new Object[] { correlationId, e });
		}
		//queue.publish(ev);
	}

	@Override
	public void handleCustomerReportRequested(Event ev) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'handleCustomerReportRequested'");
	}
}

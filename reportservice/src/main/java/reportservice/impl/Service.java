package reportservice.impl;

import reportservice.lib.IRepository;
import reportservice.lib.IService;

import reportservice.adapters.EventPublisher;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;

import dto.Payment;
import impl.CorrelationId;

import reportservice.dto.*;

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
	public void handlePaymentReceived(Payment payment,correlationId correlationId) {
		try {
			repository.addTransaction(payment);
			ev = new Event("payment.storage.succeeded", new Object[] { correlationId });
		} catch (Exception e) {
			ev = new Event("payment.storage.failed", new Object[] { correlationId, e });
		}
		// Publish the event
		queue.publish(ev);
	}

	public void handlePaymentsReportRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			List<Payment> transactions = repository.getTransactions();
			ev = new Event("payments.report.succeeded", new Object[] { correlationId, transactions });
		} catch (Exception e) {
			ev = new Event("payments.report.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	public void handleMerchantReportRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var id = ev.getArgument(1, String.class);
		try {
			List<Payment> transactions = repository.getMerchantTransactions(id);
			ev = new Event("payments.report.succeeded", new Object[] { correlationId, transactions });
		} catch (Exception e) {
			ev = new Event("payments.report.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	@Override
	public void handleCustomerReportRequested(Event ev) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'handleCustomerReportRequested'");
	}
}

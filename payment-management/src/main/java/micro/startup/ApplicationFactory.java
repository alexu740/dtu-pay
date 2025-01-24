package micro.startup;

import micro.adapters.EventPublisher;
import micro.adapters.ExternalBank;
import micro.adapters.FastMoneyBank;
import micro.adapters.RabbitMqEventPublisher;
import micro.adapters.RabbitMqFacade;
import micro.repositories.PaymentReadModelRepository;
import micro.repositories.PaymentRepository;
import micro.service.PaymentManagementService;

/**
 * @author: Alex Ungureanu (s225525)
 */

public class ApplicationFactory {
    public static RabbitMqFacade createApplication(String queueName) {
		var mq = new EnrichedRabbitMqQueue(queueName);

		PaymentRepository repo = new PaymentRepository(mq);
		PaymentReadModelRepository rm = new PaymentReadModelRepository(mq);
		EventPublisher pub = new RabbitMqEventPublisher(mq);
		ExternalBank bank = new FastMoneyBank();

		var service = new PaymentManagementService(repo, rm, pub, bank);

        return new RabbitMqFacade(mq, service);
    }
}

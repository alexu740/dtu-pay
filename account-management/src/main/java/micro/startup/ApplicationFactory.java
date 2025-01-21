package micro.startup;

import boilerplate.MessageQueue;
import micro.adapters.EventPublisher;
import micro.adapters.RabbitMqEventPublisher;
import micro.adapters.RabbitMqFacade;
import micro.repositories.AccountReadModelRepository;
import micro.repositories.AccountRepository;
import micro.service.AccountManagementService;

public class ApplicationFactory {
    public static RabbitMqFacade createApplication(String queueName) {
        MessageQueue mq = new EnrichedRabbitMqQueue(queueName);

        AccountRepository accountRepository = new AccountRepository(mq);
        AccountReadModelRepository readModelRepository = new AccountReadModelRepository(mq);

        EventPublisher eventPublisher = new RabbitMqEventPublisher(mq);

        AccountManagementService service = new AccountManagementService(accountRepository, readModelRepository, eventPublisher);

        return new RabbitMqFacade(mq, service);
    }
}

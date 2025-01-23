package tokenservice.startup;

import tokenservice.repositories.TokenRepository;
import tokenservice.service.TokenService;
import tokenservice.adapters.*;
import boilerplate.MessageQueue;
import boilerplate.implementations.RabbitMqQueue;

public class ApplicationFactory {
    public static RabbitMqFacade createApplication() {
        MessageQueue mq = new RabbitMqQueue("rabbitMq");
        TokenRepository repo = new TokenRepository();
        EventPublisher publisher = new RabbitMqEventPublisher(mq);
        var service = new TokenService(publisher, repo);
        var facade = new RabbitMqFacade(mq, service);
        
        return facade;
    }
}



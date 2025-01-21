package tokenservice.startup;

import tokenservice.lib.IRepository;
import tokenservice.service.TokenService;
import tokenservice.impl.Repository;
import tokenservice.adapters.*;
import boilerplate.MessageQueue;
import boilerplate.implementations.RabbitMqQueue;

public class ApplicationFactory {
    public static RabbitMqFacade createApplication() {
        MessageQueue mq = new RabbitMqQueue("rabbitMq");
        IRepository repo = new Repository();
        EventPublisher publisher = new RabbitMqEventPublisher(mq);
        var service = new TokenService(publisher);
        var facade = new RabbitMqFacade(mq, service);
        
        return facade;
    }
}



package tokenservice.lib;

import tokenservice.impl.TokenGenerator;
import tokenservice.service.TokenService;
import tokenservice.impl.Repository;
import tokenservice.adapters.*;
import boilerplate.MessageQueue;
import boilerplate.implementations.RabbitMqQueue;

public class Factory {
    static TokenService service = null;

    public synchronized TokenService getService() {
        if (service != null) {
            return service;
        }
        MessageQueue mq = new RabbitMqQueue("rabbitMq");
        IRepository repo = new Repository();
        TokenGenerator tokenGenerator = new TokenGenerator();
        EventPublisher publisher = new RabbitMqEventPublisher(mq);
        service = new TokenService(publisher);
        var facade = new RabbitMqFacade(mq, service);
        
        return service;
    }
}



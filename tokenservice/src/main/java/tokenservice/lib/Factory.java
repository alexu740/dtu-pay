package tokenservice.lib;

import tokenservice.impl.TokenGenerator;
import tokenservice.impl.Repository;
import tokenservice.impl.Service;
import tokenservice.adaptors.*;
import messaging.implementations.RabbitMqQueue;

public class Factory {


    static IService service = null;


    public synchronized IService getService() {
        if (service != null) {
            return service;
        }
        var mq = new RabbitMqQueue();
        IRepository repo = new Repository();
        TokenGenerator tokenGenerator = new TokenGenerator();
        EventPublisher publisher = new RabbitMqPublisher(mq);
        service = new Service(publisher, repo, tokenGenerator);
        var facade = new RabbitMqFacade(mq, service);
        
        return service;
    }
}



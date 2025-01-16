package tokenservice.lib;

import tokenservice.impl.TokenGenerator;
import tokenservice.impl.Repository;
import tokenservice.impl.Service;
import messaging.implementations.RabbitMqQueue;

public class Factory {


    static IService service = null;


    public synchronized IService getService() {
        if (service != null) {
            return service;
        }
        var mq = new RabbitMqQueue("dtupay");
        IRepository repo = new Repository();
        TokenGenerator tokenGenerator = new TokenGenerator();
        service = new Service(mq, repo, tokenGenerator);
        return service;
    }
}

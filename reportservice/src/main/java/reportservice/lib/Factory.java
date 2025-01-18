package reportservice.lib;

import reportservice.impl.Repository;
import reportservice.impl.Service;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;
public class Factory {


    static IService service = null;


    public synchronized IService getService() {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService
        if (service != null) {
            return service;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        System.out.println("STARTING the Account Management Service");
        Thread.sleep(10000);
        var mq = new RabbitMqQueue("rabbitMq");

        Thread.currentThread().join();

        IRepository repo = new Repository();

        service = new Service(mq, repo);
        // new Adapter(service, mq);
        return service;
    }
}

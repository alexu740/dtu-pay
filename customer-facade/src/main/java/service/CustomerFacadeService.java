package service;

import boilerplate.Event;

import boilerplate.MessageQueue;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import adapters.EventPublisher;
import dto.RegistrationDto;

public class CustomerFacadeService {
    private EventPublisher publisher;
    private Map<String, CompletableFuture<String>> correlations = new ConcurrentHashMap<>();

    public CustomerFacadeService(EventPublisher publisher) {
        this.publisher = publisher;
    }

    public String create(RegistrationDto request) {
        var correlationId = CorrelationId.randomId();
		correlations.put(correlationId.get(), new CompletableFuture<String>());
        publisher.emitCreateUserEvent(request, correlationId);
        return correlations.get(correlationId.get()).join();
    }

    public void completeRegistration(String eventPayload, CorrelationId correlationId, boolean isSuccessful) {
        System.out.println(correlationId.get());
        System.out.println(eventPayload);
        var promise = correlations.get(correlationId.get());
        if(promise != null) {
            promise.complete(eventPayload);
        }
    }
    
    public void remove() {
        //queue.publish(new Event("CustomerRegistrationRequested"))
    }
}

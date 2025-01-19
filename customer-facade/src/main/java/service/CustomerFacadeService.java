package service;

import boilerplate.Event;

import boilerplate.MessageQueue;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import adapters.EventPublisher;
import dto.AccountTokensDto;
import dto.RegistrationDto;

public class CustomerFacadeService {
    private EventPublisher publisher;
    private Map<String, CompletableFuture<String>> correlationsWithStringReturn = new ConcurrentHashMap<>();
    private Map<String, CompletableFuture<AccountTokensDto>> correlationsWithAccount = new ConcurrentHashMap<>();

    public CustomerFacadeService(EventPublisher publisher) {
        this.publisher = publisher;
    }

    public AccountTokensDto get(String id) {
        var correlationId = CorrelationId.randomId();
		correlationsWithAccount.put(correlationId.get(), new CompletableFuture<AccountTokensDto>());
        publisher.emitRetrieveUserEvent(id, correlationId);
        return correlationsWithAccount.get(correlationId.get()).join();
    }

    public String create(RegistrationDto request) {
        var correlationId = CorrelationId.randomId();
		correlationsWithStringReturn.put(correlationId.get(), new CompletableFuture<String>());
        publisher.emitCreateUserEvent(request, correlationId);
        return correlationsWithStringReturn.get(correlationId.get()).join();
    }
    
    public String createTokens(String customerId, String tokenNumber) {
        var correlationId = CorrelationId.randomId();
        correlationsWithStringReturn.put(correlationId.get(), new CompletableFuture<String>());
        publisher.emitCreateTokensEvent(customerId, tokenNumber, correlationId);
        return correlationsWithStringReturn.get(correlationId.get()).join();
    }

    public void completeRegistration(String eventPayload, CorrelationId correlationId, boolean isSuccessful) {
        System.out.println(correlationId.get());
        System.out.println(eventPayload);
        var promise = correlationsWithStringReturn.get(correlationId.get());
        if(promise != null) {
            promise.complete(eventPayload);
        }
    }

    public void completeAccountRetrievalRequest(AccountTokensDto eventPayload, CorrelationId correlationId, boolean isSuccessful) {
        var promise = correlationsWithAccount.get(correlationId.get());
        if(promise != null) {
            promise.complete(eventPayload);
        }
    }

    public void completeTokenCreationRequest(boolean successful, CorrelationId correlationId) {
        var promise = correlationsWithStringReturn.get(correlationId.get());
        if(promise != null) {
            promise.complete(successful ? "success" : "fail");
        }
    }
    
    public void remove() {
        //queue.publish(new Event("CustomerRegistrationRequested"))
    }
}

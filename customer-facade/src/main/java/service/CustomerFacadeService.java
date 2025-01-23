package service;

import boilerplate.Event;

import boilerplate.MessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import adapters.EventPublisher;
import dto.AccountTokensDto;
import dto.RegistrationDto;
import dto.Report;

public class CustomerFacadeService {
    private EventPublisher publisher;
    private Map<String, CompletableFuture<String>> correlationsWithStringReturn = new ConcurrentHashMap<>();
    private Map<String, CompletableFuture<AccountTokensDto>> correlationsWithAccount = new ConcurrentHashMap<>();
    private Map<String, CompletableFuture<List<Report>>> reportCorrelations = new ConcurrentHashMap<>();

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

    public String deregister(String id) {

        var correlationId = CorrelationId.randomId();
        correlationsWithStringReturn.put(correlationId.get(), new CompletableFuture<String>());
        publisher.emitUnregisterUserEvent(id, correlationId);

        return correlationsWithStringReturn.get(correlationId.get()).join();
    }

    public void completeDeregisteration(String eventPayload, CorrelationId correlationId, boolean isSuccessful) {
        System.out.println(correlationId.get());
        System.out.println(eventPayload);
        var promise = correlationsWithStringReturn.get(correlationId.get());
        if(promise != null) {
            promise.complete(eventPayload);
        }
    }

    public List<Report> getReport(String id) {
        var correlationId = CorrelationId.randomId();
		reportCorrelations.put(correlationId.get(), new CompletableFuture<List<Report>>());
        publisher.emitCustomerReportRequested(id, correlationId);
        return reportCorrelations.get(correlationId.get()).join();
    }

    public void completeReportRequest(List<Report> report, CorrelationId correlationId) {
        var promise = reportCorrelations.get(correlationId.get());
        if(promise != null) {
            promise.complete(report);
        }
    }

    public List<String> getAllCorrelations() {
        List<String> allKeys = new ArrayList<>();
        
        allKeys.addAll(correlationsWithStringReturn.keySet());
        allKeys.addAll(correlationsWithAccount.keySet());
        allKeys.addAll(reportCorrelations.keySet());

        return allKeys;
    }
}

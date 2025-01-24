package service;

import boilerplate.Event;

import boilerplate.MessageQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import adapters.EventPublisher;
import dto.PaymentDto;
import dto.RegistrationDto;
import dto.Report;
/**
 * @author: Lukas Åkefeldt, s242204
 */
public class MerchantFacadeService {
    private EventPublisher publisher;
    private Map<String, CompletableFuture<String>> correlations = new ConcurrentHashMap<>();
    private Map<String, CompletableFuture<List<Report>>> reportCorrelations = new ConcurrentHashMap<>();
    

    public MerchantFacadeService(EventPublisher publisher) {
        this.publisher = publisher;
    }

    public String create(RegistrationDto request) {
        var correlationId = CorrelationId.randomId();
		correlations.put(correlationId.get(),new CompletableFuture<String>());

        publisher.emitCreateUserEvent(request, correlationId);
        return correlations.get(correlationId.get()).join();
    }

    public void completeRegistration(String eventPayload, CorrelationId correlationId) {
        System.out.println(correlationId.get());
        System.out.println(eventPayload);
        var promise = correlations.get(correlationId.get());
        if(promise != null) {
            promise.complete(eventPayload);
        }
    }

    public String initialisePayment(PaymentDto dto) {
        var correlationId = CorrelationId.randomId();
		correlations.put(correlationId.get(),new CompletableFuture<String>());

        publisher.emitInitialisePayment(dto, correlationId);
        return correlations.get(correlationId.get()).join();
    }

    public void completePaymentTransaction(boolean success, CorrelationId correlationId) {
        var promise = correlations.get(correlationId.get());
        if(promise != null) {
            promise.complete(success ? "successful" : "failed");
        }
    }

    public String deregister(String id) {

        var correlationId = CorrelationId.randomId();
        correlations.put(correlationId.get(), new CompletableFuture<String>());
        publisher.emitUnregisterUserEvent(id, correlationId);

        return correlations.get(correlationId.get()).join();
    }

    public void completeDeregisteration(String eventPayload, CorrelationId correlationId, boolean isSuccessful) {
        System.out.println(correlationId.get());
        System.out.println(eventPayload);
        var promise = correlations.get(correlationId.get());
        if(promise != null) {
            promise.complete(eventPayload);
        }
    }

    public List<Report> getReport(String id) {
        var correlationId = CorrelationId.randomId();
		reportCorrelations.put(correlationId.get(), new CompletableFuture<List<Report>>());
        publisher.emitMerchantReportRequested(id, correlationId);
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
        
        allKeys.addAll(correlations.keySet());
        allKeys.addAll(reportCorrelations.keySet());

        return allKeys;
    }
}

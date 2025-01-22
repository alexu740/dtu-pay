package service;

import boilerplate.Event;

import boilerplate.MessageQueue;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import adapters.EventPublisher;
import dto.Report;

public class ManagerFacadeService {
    private EventPublisher publisher;
    private Map<String, CompletableFuture<List<Report>>> reportCorrelations = new ConcurrentHashMap<>();
    
    public ManagerFacadeService(EventPublisher publisher) {
        this.publisher = publisher;
    }

    public List<Report> getReport() {
        var correlationId = CorrelationId.randomId();
		reportCorrelations.put(correlationId.get(), new CompletableFuture<List<Report>>());
        publisher.emitManagerReportRequested(correlationId);
        return reportCorrelations.get(correlationId.get()).join();
    }

    public void completeReportRequest(List<Report> report, CorrelationId correlationId) {
        var promise = reportCorrelations.get(correlationId.get());
        if(promise != null) {
            promise.complete(report);
        }
    }
}

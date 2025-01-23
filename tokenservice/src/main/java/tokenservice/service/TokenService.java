package tokenservice.service;

import java.util.HashMap;
import java.util.Map;

import tokenservice.adapters.EventPublisher;
import tokenservice.repositories.TokenRepository;

public class TokenService {
	private EventPublisher publisher;
    private TokenRepository repo;

	public TokenService(EventPublisher p, TokenRepository repo) {
		this.publisher = p;
		this.repo = repo;
	}

	public void handlePaymentInitialised(String customerId, String token, CorrelationId correlationId, String transactionId) {
		var tokenStatus = this.repo.get(token);
		if(tokenStatus != null) {
			publisher.emitTokenValidationFailed(transactionId, correlationId);
			return;
		}
		this.repo.put(token, "validating");
		publisher.emitCustomerHasTokenCheckRequested(customerId, token, correlationId, transactionId);
	}

	public void handleCustomerHasTokenChecked(String customerId, String token, Boolean isPresent, CorrelationId correlationId, String transactionId) {
		if(repo.get(token) != null && repo.get(token).equals("validating")) {
			if(isPresent) {
				this.repo.remove(token);
				this.repo.put(token, "validated");
				publisher.emitTokenValidated(customerId, token, correlationId, transactionId);
				publisher.emitTokenUsed(customerId, token);
			}
			else {
				publisher.emitTokenValidationFailed(transactionId, correlationId);
				repo.remove(token);
			}
		} else {
			System.out.print("Token " + token + " is not known. Ignoring the request.");
		}
	}
}

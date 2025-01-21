package tokenservice.service;

import java.util.HashMap;
import java.util.Map;

import tokenservice.adapters.EventPublisher;

public class TokenService {
	private EventPublisher publisher;
	//private final TokenGenerator tokenGenerator;

    private Map<String, String> tokens;

	public TokenService(EventPublisher p) {
		this.publisher = p;
		tokens = new HashMap<>();
	}

	public void handlePaymentInitialised(String customerId, String token, CorrelationId correlationId, String transactionId) {
		var tokenStatus = this.tokens.get(token);
		if(tokenStatus != null) {
			publisher.emitTokenValidationFailed(transactionId, correlationId);
			return;
		}
		this.tokens.put(token, "validating");
		publisher.emitCustomerHasTokenCheckRequested(customerId, token, correlationId, transactionId);
	}

	public void handleCustomerHasTokenChecked(String customerId, String token, Boolean isPresent, CorrelationId correlationId, String transactionId) {
		if(tokens.get(token) != null && tokens.get(token).equals("validating")) {
			if(isPresent) {
				this.tokens.remove(token);
				this.tokens.put(token, "validated");
				publisher.emitTokenValidated(customerId, token, correlationId, transactionId);
				publisher.emitTokenUsed(customerId, token);
			}
			else {
				publisher.emitTokenValidationFailed(transactionId, correlationId);
				tokens.remove(token);
			}
		} else {
			System.out.print("Token " + token + " is not known. Ignoring the request.");
		}
	}

	public void handlePaymentInformationResolved() {
		//payment is pending, so mark token as used
		//this.tokens.remove(token);
		//this.tokens.put(token, "used");
	}
}

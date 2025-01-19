package tokenservice.service;

import boilerplate.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import tokenservice.adapters.EventPublisher;
import tokenservice.impl.TokenGenerator;

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
			//emit token invalid event
		}
		this.tokens.put(token, "validating");
		publisher.emitCustomerHasTokenCheckRequested(customerId, token, correlationId, transactionId);
	}

	public void handleCustomerHasTokenChecked(String customerId, String token, Boolean isPresent, CorrelationId correlationId, String transactionId) {
		if(isPresent && tokens.get(token).equals("validating")) {
			this.tokens.remove(token);
			this.tokens.put(token, "validated");
			publisher.emitTokenValidated(customerId, token, correlationId, transactionId);
		}
		//emit TokenValidated event
	}

	public void handlePaymentInformationResolved() {
		//payment is pending, so mark token as used
		//this.tokens.remove(token);
		//this.tokens.put(token, "used");
	}
/* 
	@Override
	public void handleTokensRequested(String id, CorrelationId correlationId) {

		Stack<String> tks = new Stack<String>();
		try {
			tks = tokenGenerator.generateTokens();
			System.out.println("tokens: " + tks);
			repository.addTokens(id, tks);
			publisher.emitTokenGenerated(correlationId);
		} catch (Exception e) {
			publisher.emitTokenGenerated(correlationId);
		}
	}

	public void handleTokenIdRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		String token = ev.getArgument(1, String.class);
		try {
			String id = repository.getIdByToken(token);
			ev = new Event("find.id.by.token.succeeded", new Object[] { correlationId, id });
		} catch (Exception e) {
			ev = new Event("find.id.by.token.failed", new Object[] { correlationId, e });
		}

	}

	public void handleTokenUsed(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var token = ev.getArgument(1, String.class);
		try {
			repository.useToken(token);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
*/
}

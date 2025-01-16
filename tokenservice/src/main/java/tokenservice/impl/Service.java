package tokenservice.impl;

import tokenservice.lib.IRepository;
import tokenservice.lib.IService;
import messaging.Event;
import java.util.Stack;
import tokenservice.adaptors.*;

public class Service implements IService {
	private final IRepository repository;
	private final EventPublisher publisher;
	private final TokenGenerator tokenGenerator;

	public Service(EventPublisher p, IRepository repo, TokenGenerator tokenGenerator) {
		this.publisher = p;
		this.repository = repo;
		this.tokenGenerator = tokenGenerator;

	}

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
}

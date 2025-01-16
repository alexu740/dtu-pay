package tokenservice.impl;

import dtupay.dto.*;
import tokenservice.lib.IRepository;
import tokenservice.lib.IService;
import messaging.Event;
import messaging.MessageQueue;
import java.util.Stack;

public class Service implements IService {
	private final IRepository repository;
	private final MessageQueue queue;
	private final TokenGenerator tokenGenerator;

	public Service(MessageQueue q, IRepository repo, TokenGenerator tokenGenerator) {
		this.queue = q;
		this.repository = repo;
		this.tokenGenerator = tokenGenerator;
		this.queue.addHandler("generate.tokens.requested", this::handleTokensRequested);
		this.queue.addHandler("find.id.by.token.requested", this::handleTokenIdRequested);
		this.queue.addHandler("token.is.used", this::handleTokenUsed);
	}

	@Override
	public void handleTokensRequested(Event ev) {
		var id = ev.getArgument(1, String.class);
		var correlationId = ev.getArgument(0, CorrelationId.class);

		Stack<String> tks = new Stack<String>();
		try {
			tks = tokenGenerator.generateTokens();
			System.out.println("tokens: " + tks);
			repository.addTokens(id, tks);
			ev = new Event("tokens.generate.succeeded", new Object[] { correlationId, tks });
		} catch (Exception e) {
			ev = new Event("tokens.generate.failed", new Object[] { correlationId, e });
		}

		// Publish the event
		queue.publish(ev);
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

		// Publish the event
		queue.publish(ev);
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

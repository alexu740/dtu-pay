package servicetest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import micro.adapters.EventPublisher;
import micro.adapters.RabbitMqEventPublisher;
import micro.adapters.RabbitMqFacade;
import micro.aggregate.Account;
import micro.aggregate.AccountId;
import micro.aggregate.CustomerAccount;
import micro.commands.AccountDeletionCommand;
import micro.commands.AccountTokenCreationCommand;
import micro.commands.CommandFactory;
import micro.events.AccountRegistered;
import micro.events.TokenAdded;
import micro.repositories.AccountReadModelRepository;
import micro.repositories.AccountRepository;
import micro.service.AccountManagementService;
import micro.service.CorrelationId;

public class TokenSteps {
    MessageQueue mockedQueue;
    MessageQueue queue;
    AccountRepository accountRepository;
    AccountReadModelRepository readModelRepository;
    EventPublisher eventPublisher;

    AccountManagementService service;
    AccountManagementService mockedService;

    RabbitMqFacade facade;

    CorrelationId correlationId;
    Event event; 

    @Before
    public void setUp() {
        mockedQueue = mock(MessageQueue.class);
        queue = new MessageQueueSync();

        accountRepository = mock(AccountRepository.class);
        readModelRepository = mock(AccountReadModelRepository.class);

        eventPublisher = new RabbitMqEventPublisher(mockedQueue);
        
        service = new AccountManagementService(accountRepository, readModelRepository, eventPublisher);
        mockedService = mock(AccountManagementService.class);

        facade = new RabbitMqFacade(queue, mockedService);

        correlationId = CorrelationId.randomId();
    }

    @Given("a token creation event is received")
    public void aTokenCreationEventIsReceived() {
        event = new Event("CustomerTokensRequested", new Object[] { "123", "1", correlationId });
        queue.publish(event);
    }

    @Then("a token creation command is sent to the service")
    public void aTokenCreationCommandIsSent() {
        verify(mockedService).handleCreateTokens(any(AccountTokenCreationCommand.class), any(CorrelationId.class));
    }

    @And("token creation fails for merchant users")
    public void tokensAreNotCreatedForMerchants() {
        when(accountRepository.getById("123")).thenReturn(Account.create("test", "test", "test", "test", false, correlationId));
        
        event = new Event("CustomerTokensRequested", new Object[] { "123", "1", correlationId });
        var command = CommandFactory.createAccountTokenCreationCommand(event);

        service.handleCreateTokens(command, correlationId);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokensCreateFailed")));
    }

    @Then("token creation fails for customer users with 2 tokens")
    public void tokensAreNotCreatedForUsersWithTooManyTokens() {
        var list = new ArrayList<>(Arrays.asList(
            new AccountRegistered(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), correlationId),
            new TokenAdded(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), "token1"),
            new TokenAdded(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), "token2")
        ));
        var c = CustomerAccount.createFromEvents(list.stream());
        
        when(accountRepository.getById("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")).thenReturn(c);
        
        event = new Event("CustomerTokensRequested", new Object[] { "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", "1", correlationId });
        var command = CommandFactory.createAccountTokenCreationCommand(event);

        service.handleCreateTokens(command, correlationId);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokensCreateFailed")));
    }

    @Then("token creation fails when adding less than 1 token")
    public void tokensAreNotCreatedForUser0() {
        var list = new ArrayList<>(Arrays.asList(
            new AccountRegistered(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), correlationId),
            new TokenAdded(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), "token1")
        ));
        var c = CustomerAccount.createFromEvents(list.stream());
        
        when(accountRepository.getById("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")).thenReturn(c);
        
        event = new Event("CustomerTokensRequested", new Object[] { "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", "0", correlationId });
        var command = CommandFactory.createAccountTokenCreationCommand(event);

        service.handleCreateTokens(command, correlationId);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokensCreateFailed")));
    }

    @Then("token creation fails when adding more than 5 tokens")
    public void tokensAreNotCreatedForUser6() {
        var list = new ArrayList<>(Arrays.asList(
            new AccountRegistered(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), correlationId),
            new TokenAdded(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), "token1")
        ));
        var c = CustomerAccount.createFromEvents(list.stream());
        
        when(accountRepository.getById("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")).thenReturn(c);
        
        event = new Event("CustomerTokensRequested", new Object[] { "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", "6", correlationId });
        var command = CommandFactory.createAccountTokenCreationCommand(event);

        service.handleCreateTokens(command, correlationId);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokensCreateFailed")));
    }

    @Then("token creation succeedes when adding between 1 and 5 tokens to a user with 0 or 1 existing tokens")
    public void tokensAreNotCreatedForUser5() {
        var list = new ArrayList<>(Arrays.asList(
            new AccountRegistered(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), correlationId),
            new TokenAdded(new AccountId(UUID.fromString("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")), "token1")
        ));
        var c = CustomerAccount.createFromEvents(list.stream());
        
        when(accountRepository.getById("a494ac5e-7a34-4a1d-ae9b-68c25d4e189d")).thenReturn(c);
        
        event = new Event("CustomerTokensRequested", new Object[] { "a494ac5e-7a34-4a1d-ae9b-68c25d4e189d", "2", correlationId });
        var command = CommandFactory.createAccountTokenCreationCommand(event);

        service.handleCreateTokens(command, correlationId);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals("TokensCreated")));
    }

}

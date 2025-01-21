package servicetest;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import boilerplate.Event;
import boilerplate.MessageQueue;
import boilerplate.implementations.MessageQueueSync;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import micro.adapters.EventPublisher;
import micro.adapters.RabbitMqEventPublisher;
import micro.adapters.RabbitMqFacade;
import micro.commands.AccountCreationCommand;
import micro.commands.AccountGetQuery;
import micro.commands.CommandFactory;
import micro.commands.QueryFactory;
import micro.dto.RegistrationDto;
import micro.events.AccountRegistered;
import micro.repositories.AccountReadModelRepository;
import micro.repositories.AccountRepository;
import micro.service.AccountManagementService;
import micro.service.CorrelationId;

public class AccountSteps {
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

        accountRepository = new AccountRepository(mockedQueue);
        readModelRepository = mock(AccountReadModelRepository.class);

        eventPublisher = new RabbitMqEventPublisher(mockedQueue);
        
        service = new AccountManagementService(accountRepository, readModelRepository, eventPublisher);
        mockedService = mock(AccountManagementService.class);

        facade = new RabbitMqFacade(queue, mockedService);

        correlationId = CorrelationId.randomId();
    }

    @Given("a {string} registration event is received")
    public void anEventIsReceived(String eventName) {
        var registrationRequest = new RegistrationDto();
        registrationRequest.firstName = "Test First Name";
        registrationRequest.lastName = "Test Last Name";
        registrationRequest.cpr = "010101-0101";
        registrationRequest.bankAccount = "bankAccount";

        event = new Event(eventName, new Object[] { registrationRequest, correlationId });
        queue.publish(event);
    }

    @Then("then an account creation command is sent")
    public void theAccountCreationCommandIsSent() {
        var isCustomer = event.getType().equals("CustomerRegistrationRequested");
        AccountCreationCommand command = CommandFactory.createAccountCreationCommand(event.getArgument(0, RegistrationDto.class), isCustomer);
        verify(mockedService).handleCreateAccount(any(AccountCreationCommand.class), any(CorrelationId.class));
        service.handleCreateAccount(command, correlationId);
    }

    @And("the {string} event is sent")
    public void theAccountCreationCommandIsSent(String eventName) {
        verify(mockedQueue).publish(argThat(event -> event.getType().equals(eventName)));
    }

    @And("the registered account is of type customer")
    public void theAccountIsOfTypeCustomer() {
        verify(mockedQueue).publish(argThat(event -> ((AccountRegistered)event).isCustomerAccountType()));
    }

    @And("the registered account is NOT of type customer")
    public void theAccountIsNotOfTypeCustomer() {
        verify(mockedQueue).publish(argThat(event -> !((AccountRegistered)event).isCustomerAccountType()));
    }



    @Given("a {string} query event is received")
    public void aQueryEventIsReceived(String eventName) {
        event = new Event(eventName, new Object[] { "accountId", correlationId });
        queue.publish(event);
    }

    @Then("a AccountGetQuery query is sent in case the requested account is of customer type and an event of type {string} is raised")
    public void theQueryIsSent(String eventName) {
        verify(mockedService).handleGetAccount(any(AccountGetQuery.class), any(CorrelationId.class));
        var query = QueryFactory.createAccountGetCommand(event, true);
        service.handleGetAccount(query, correlationId);
        verify(readModelRepository).getCustomerTokens(query);
        verify(mockedQueue).publish(argThat(event -> event.getType().equals(eventName)));
    }
}

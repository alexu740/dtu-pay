package micro.adapters;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;
import micro.commands.AccountCreationCommand;
import micro.commands.CommandFactory;
import micro.commands.QueryFactory;
import micro.dto.RegistrationDto;
import boilerplate.Event;
import micro.commands.AccountCreationCommand;
import micro.service.AccountManagementService;
import micro.service.CorrelationId;

public class RabbitMqFacade {
  AccountManagementService service;

  public RabbitMqFacade(MessageQueue queue, AccountManagementService service) {
    System.out.println("Starting facade");
    queue.addHandler("CustomerRegistrationRequested", this::handleCustomerRegistration);
    queue.addHandler("CustomerRetrievalRequested", this::handleGetCustomer);
    queue.addHandler("MerchantRegistrationRequested", this::handleMerchantRegistration);
    
    this.service = service;
  }

  public void handleCustomerRegistration(Event e) {
    var eventPayload = e.getArgument(0, RegistrationDto.class);
    var correlationId = e.getArgument(1, CorrelationId.class);

    AccountCreationCommand command = CommandFactory.createAccountCreationCommand(eventPayload, true);

    service.handleCreateAccount(command, correlationId);
  }

  public void handleGetCustomer(Event e) {
    var correlationId = e.getArgument(1, CorrelationId.class);
    service.handleGetAccount(QueryFactory.createAccountGetCommand(e, true), correlationId);
  }

  public void handleMerchantRegistration(Event e) {
    var eventPayload = e.getArgument(0, RegistrationDto.class);
    var correlationId = e.getArgument(1, CorrelationId.class);
    AccountCreationCommand command = new AccountCreationCommand(eventPayload, false);
    service.handleCreateAccount(command, correlationId);
  }
}

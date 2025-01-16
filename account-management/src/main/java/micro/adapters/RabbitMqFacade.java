package micro.adapters;

import messaging.implementations.RabbitMqQueue;
import messaging.MessageQueue;
import messaging.Event;
import micro.commands.AccountCreationCommand;
import micro.dto.AccountRegistrationRequest;
import messaging.Event;
import micro.commands.AccountCreationCommand;
import micro.dto.AccountRegistrationRequest;
import micro.service.AccountManagementService;

public class RabbitMqFacade {
  AccountManagementService service;

  public RabbitMqFacade(MessageQueue queue, AccountManagementService service) {
    queue.addHandler("AccountRegistrationRequested", this::handleCustomerRegistration);
    this.service = service;
  }

  private void handleCustomerRegistration(Event e) {
    var eventPayload = e.getArgument(0, AccountRegistrationRequest.class);
    AccountCreationCommand command = new AccountCreationCommand(eventPayload);
      
    service.handleCreateAccount(command);
  }
}

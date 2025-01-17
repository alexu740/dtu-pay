package micro.adapters;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;
import micro.commands.AccountCreationCommand;
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
    queue.addHandler("MerchantRegistrationRequested", this::handleMerchantRegistration);
    
    this.service = service;
  }

  public void handleCustomerRegistration(Event e) {
    System.out.println("CustomerRegistrationRequested triggered");
    var eventPayload = e.getArgument(0, RegistrationDto.class);
    System.out.println("CustomerRegistrationRequested arg" + eventPayload.bankAccount);
    var correlationId = e.getArgument(1, CorrelationId.class);
    System.out.println(correlationId.get());
    AccountCreationCommand command = new AccountCreationCommand(eventPayload, true);
    System.out.println(correlationId);
    service.handleCreateAccount(command, correlationId);
  }

  public void handleMerchantRegistration(Event e) {
    var eventPayload = e.getArgument(0, RegistrationDto.class);
    var correlationId = e.getArgument(1, CorrelationId.class);
    AccountCreationCommand command = new AccountCreationCommand(eventPayload, false);
    service.handleCreateAccount(command, correlationId);
  }
}

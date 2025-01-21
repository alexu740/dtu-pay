package micro.adapters;

import boilerplate.implementations.RabbitMqQueue;
import boilerplate.MessageQueue;
import boilerplate.Event;
import micro.commands.AccountCreationCommand;
import micro.commands.AccountTokenCreationCommand;
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
    queue.addHandler("CustomerDeregistrationRequested", this::handleCustomerDeregistration);
    queue.addHandler("MerchantRegistrationRequested", this::handleMerchantRegistration);
    queue.addHandler("MerchantDeregistrationRequested", this::handleMerchantDeregistration);

    queue.addHandler("CustomerTokensRequested", this::handleCustomerTokensRequested);
    queue.addHandler("CustomerHasTokenCheckRequested", this::handleCustomerHasTokenCheckRequested);
    queue.addHandler("PaymentInformationResolutionRequested", this::handlePaymentInformationResolutionRequested);
    queue.addHandler("TokenUsed", this::handleTokenUsed);
    
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

  public void handleCustomerDeregistration(Event e) {
    var customerId = e.getArgument(0, String.class);
    var correlationId = e.getArgument(1, CorrelationId.class);
    AccountCreationCommand command = new AccountDeletionCommand(customerId, true);
    service.handleDeleteAccount(command, correlationId);
  }

  public void handleMerchantRegistration(Event e) {
    var eventPayload = e.getArgument(0, RegistrationDto.class);
    var correlationId = e.getArgument(1, CorrelationId.class);
    AccountCreationCommand command = new AccountCreationCommand(eventPayload, false);
    service.handleCreateAccount(command, correlationId);
  }

  public void handleMerchantDeregistration(Event e) {
    var merchantId = e.getArgument(0, String.class);
    var correlationId = e.getArgument(1, CorrelationId.class);
    AccountCreationCommand command = new AccountDeletionCommand(merchantId, false);
    service.handleDeleteAccount(command, correlationId);
  }

  public void handleCustomerTokensRequested(Event e) {
    AccountTokenCreationCommand command = CommandFactory.createAccountTokenCreationCommand(e);
    var correlationId = e.getArgument(2, CorrelationId.class);
    service.handleCreateTokens(command, correlationId);
  }

  public void handleCustomerHasTokenCheckRequested(Event e) {
    var customerId = e.getArgument(0, String.class);
    var token = e.getArgument(1, String.class);
    var correlationId = e.getArgument(2, CorrelationId.class);
    var transactionId = e.getArgument(3, String.class);
    
    service.handleCheckTokenPresent(customerId, token, correlationId, transactionId);
  }

  public void handlePaymentInformationResolutionRequested(Event e) {
    var transactionId = e.getArgument(0, String.class);
    var customerId = e.getArgument(1, String.class);
    var merchantId = e.getArgument(2, String.class);
    var correlationId = e.getArgument(3, CorrelationId.class);

    service.handlePaymentInformationResolutionQuery(transactionId, customerId, merchantId, correlationId);
  }

  public void handleTokenUsed(Event e) {
    var customerId = e.getArgument(0, String.class);
    var token = e.getArgument(1, String.class);

    service.handleTokenUserCommand(customerId, token);
  }
}

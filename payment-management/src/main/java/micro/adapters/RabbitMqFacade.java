package micro.adapters;

import boilerplate.MessageQueue;
import micro.commands.CommandFactory;
import micro.dto.PaymentDto;
import boilerplate.Event;
import micro.commands.InitializePaymentCommand;
import micro.service.PaymentManagementService;
import micro.service.CorrelationId;

public class RabbitMqFacade {
  PaymentManagementService service;

  public RabbitMqFacade(MessageQueue queue, PaymentManagementService service) {
    System.out.println("Starting facade");
    queue.addHandler("PaymentRequested", this::handlePaymentRequested);
    queue.addHandler("TokenValidated", this::handleTokenValidated);
    
    
    this.service = service;
  }

  public void handlePaymentRequested(Event e) {
    var eventPayload = e.getArgument(0, PaymentDto.class);
    var correlationId = e.getArgument(1, CorrelationId.class);

    InitializePaymentCommand command = CommandFactory.createInitializePaymentCommand(eventPayload);

    service.handlePaymentCommand(command, correlationId);
  }

  public void handleTokenValidated(Event e) {
    var customerId = e.getArgument(0, String.class);
    var token = e.getArgument(1, String.class);
    var correlationId = e.getArgument(2, CorrelationId.class);
    var transactionId = e.getArgument(3, String.class);
    
    
    service.handleTokenValidated(customerId, token, correlationId, transactionId);
  }
}

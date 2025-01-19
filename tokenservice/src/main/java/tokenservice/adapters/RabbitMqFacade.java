package tokenservice.adapters;

import boilerplate.Event;
import boilerplate.MessageQueue;
import tokenservice.service.CorrelationId;
import tokenservice.service.TokenService;

public class RabbitMqFacade {
  TokenService service;

  public RabbitMqFacade(MessageQueue queue, TokenService service) {
    System.out.println("Starting facade");
    queue.addHandler("PaymentInitilised", this::handlePaymentInitilised);
    queue.addHandler("CustomerHasTokenChecked", this::handleCustomerHasTokenChecked);
    
    this.service = service;
  }

  public void handlePaymentInitilised(Event e) {
    var customerId = e.getArgument(0, String.class);
    var token = e.getArgument(1, String.class);
    var correlationId = e.getArgument(2, CorrelationId.class);
    var transactionId = e.getArgument(2, String.class);

    service.handlePaymentInitialised(customerId, token, correlationId, transactionId);
  }

  public void handleCustomerHasTokenChecked(Event e) {
    var customerId = e.getArgument(0, String.class);
    var token = e.getArgument(1, String.class);
    var present = e.getArgument(2, Boolean.class);
    var correlationId = e.getArgument(4, CorrelationId.class);
    var transactionId = e.getArgument(5, String.class);

    service.handleCustomerHasTokenChecked(customerId, token, present, correlationId, transactionId);
  }  
}

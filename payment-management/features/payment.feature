Feature: Payment feature

Scenario: Payment request received
    Given a payment is requested with the PaymentRequested event
    Then the application service executes a PaymentInitializationCommand
    And a PaymentInitialised event is emitted

Scenario: Token validated received
    Given a TokenValidated event is received
    Then the application service executes a handleTokenValidated command
    And an internal PaymentTokenValidated and outgoing PaymentInformationResolutionRequested events are emitted

Scenario: Token validation failed results in failed payment
    Given a TokenValidationFailed event is received
    Then the application service executes a handlePaymentTokenValidationFailed command
    And then a PaymentFailed event is emitted

Scenario: Bank information is resolved
    Given a PaymentInformationResolved event is received
    Then the application service executes a handlePaymentInformationResolved command
    And then the payment is updated with the bank information
    And a PaymentResolved event is emitted

Scenario: Successful payment
    Given a PaymentResolved event is received
    Then the application service executes a handlePaymentTransaction command
    And if the bank accepts the payment
    And the payment is processed
    Then a PaymentSucceeded event is emitted

Scenario: Failed payment from bank
    Given a PaymentResolved event is received
    Then the application service executes a handlePaymentTransaction command
    And if the bank rejects the payment
    And the payment is processed
    Then a PaymentFailed event is emitted
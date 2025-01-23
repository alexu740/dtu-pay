Feature: Token Feature

Scenario: Payment is initialised
    Given a PaymentInitialised event is received
    Then the application service executes a handlePaymentInitialised command
    And a CustomerHasTokenCheckRequested event is emitted

Scenario: Token is validated
    Given a CustomerHasTokenChecked event is received
    Then the application service executes a handleCustomerHasTokenChecked command
    And a TokenValidated event is emitted
    And a TokenUsed event is emitted

Scenario: Token is invalid
    Given a CustomerHasTokenChecked event is received
    Then the application service executes a handleCustomerHasTokenChecked command where the token is invalid
    And a TokenValidationFailed event is emitted
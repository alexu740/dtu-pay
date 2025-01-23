Feature: Token generation

Scenario: A generation event from RabbitMq triggers a TokenGeneration command
    Given a token creation event is received
    Then a token creation command is sent to the service

Scenario: Generation for merchant accounts
    And token creation fails for merchant users

Scenario: Generation for customer accounts with too many tokens
    Then token creation fails for customer users with 2 tokens

Scenario: Generation of 0 tokens
    Then token creation fails when adding less than 1 token

Scenario: Generation of 6 tokens
    Then token creation fails when adding more than 5 tokens

Scenario: Successful generation
    Thentoken creation succeedes when adding between 1 and 5 tokens to a user with 0 or 1 existing tokens
    
Scenario: Token present check fail
    Given a "CustomerHasTokenCheckRequested" lookup event is received
    Then a "handleCheckTokenPresent" query is sent to the service
    And if an account does not exist, a CustomerHasTokenChecked is emitted with value false

Scenario: Token present check succeeded
    Given a "CustomerHasTokenCheckRequested" lookup event is received
    Then a "handleCheckTokenPresent" query is sent to the service
    And if an account has token, a CustomerHasTokenChecked is emitted with value true

Scenario: Pay information resolved
    Given a PaymentInformationResolutionRequested event is received
    Then the handlePaymentInformationResolutionQuery is called on the service
    And the payment information is resolved

Scenario: Pay information not resolved
    Given a PaymentInformationResolutionRequested event is received
    Then the handlePaymentInformationResolutionQuery is called on the service
    And the payment information is not resolved

Scenario: A used token is removed from the customer
    Given a TokenUsed event is received
    Then an account update command is run
    And the used token is removed from the customer

Scenario: Removal of nonexisting token has no effect on token list
    Given a TokenUsed event is received
    Then an account update command is run
    And a token that is not present cannot be deleted
    And nothing happens when deleting a token on a merchant account
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
    
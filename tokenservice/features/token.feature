Feature: Token Feature

Scenario: Sucessfully initiate token check after payment is initiated
    When a "PaymentInitialised" event is received to initiate token validation
    Then a "CustomerHasTokenCheckRequested" event is sent to check if customer has token

Scenario: Fail token validation after payment is initiated with a token that was already used
    When a token is marked as validating 
    When a "PaymentInitialised" event is received to initiate token validation
    Then a "TokenValidationFailed" event is sent to fail the token validation

Scenario: Validate token after successful customer token check
    When a token is marked as validating 
    When a "CustomerHasTokenChecked" event is received with token present on customer account
    Then a "TokenValidated" is sent to validate token
    And a "TokenUsed" event is sent to mark token as used


Scenario: Fail token validation after failed customer token check
    When a token is marked as validating
    And a "CustomerHasTokenChecked" event is received with token not present on customer account
    Then a "TokenValidationFailed" event sent to indicate validation failure

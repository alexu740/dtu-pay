Feature: Feature

Scenario: Customer Registration
    Given an API call for registering a customer
    Then the CustomerRegistrationRequested event is emitted
    And when the corresponding CustomerRegistered is received
    Then the customer id is returned

Scenario: Customer Registration Failed
    Given an API call for registering a customer
    Then the CustomerRegistrationRequested event is emitted
    And when the corresponding AccountRegistrationFailed is received with payload "failed"
    Then the payload "failed" is returned

Scenario: Customer deregistration
    Given an API call to delete a customer
    Then the CustomerDeregistrationRequested event is emitted
    And when the correspoding AccountDeregistered event is received with payload "success"
    Then the payload "success" is returned

Scenario: Customer token lookup
    Given an API call to lookup a customer
    Then the CustomerRetrievalRequested event is emitted
    And when the correspoding CustomerRetrieved event is received
    Then the customer tokens are returned

Scenario: Customer token request
    Given an API call to generate tokens for customer
    Then the CustomerTokensRequested event is emitted
    And when the correspoding "TokensCreated" event is received
    Then a "success" status is returned

Scenario: Customer token request failed
    Given an API call to generate tokens for customer
    Then the CustomerTokensRequested event is emitted
    And when the correspoding "TokensCreateFailed" event is received
    Then a "fail" status is returned

Scenario: Customer report
    Given an API call to get the customer report
    Then the CustomerReportRequested event is emitted
    And when the correspoding CustomerReportSent event is received
    Then a report is returned
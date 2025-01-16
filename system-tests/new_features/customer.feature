Feature: Customer feature
Scenario: Customer Registration
    Given a customer with name "Susan"
    When the customer is registered
    Then the user id is returned

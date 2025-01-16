Feature: Token
Scenario: Successful Token generation
    Given a customer with name "Susan", last name "Baldwin", and CPR "030154-4400"  
    And the customer is registered with Simple DTU Pay using their bank account
    And the customer has one active tokens
    When the customer requests to generate 5 token
    Then the token is generated sucessfully

Scenario: Failed Token generation
    Given a customer with name "Susan", last name "Baldwin", and CPR "030154-4400"  
    And the customer is registered with Simple DTU Pay using their bank account
    And the customer has 2 active tokens
    When the customer requests to generate 1 tokens
    Then the token generation fails

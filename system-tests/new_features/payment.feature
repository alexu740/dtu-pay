Feature: Payment feature

Scenario: Successful Payment
    Given a customer with name "Susan", last name "Baldwin", and CPR "030154-4400"  
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with DTU Pay using their bank account
    And the customer has a valid token
    And a merchant with name "Daniel", last name "Oliver", and CPR "131161-3046"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with DTU Pay using their bank account
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful
    And the token is removed
    And the balance of the customer at the bank is 990 kr
    And the balance of the merchant at the bank is 1010 kr

Scenario: Failed Payment
    Given a customer with name "Caren", last name "Baldwin", and CPR "030215-6345"
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with DTU Pay using their bank account
    And the customer has a valid token
    And a merchant with name "Danielo", last name "Oliver", and CPR "131161-5235"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with DTU Pay using their bank account
    When the merchant initiates a payment for 1001 kr by the customer
    Then the payment is failed
    And the token is removed

Scenario: Failed Payment due to token
    Given a customer with name "Mary", last name "Baldwin", and CPR "041134-8567"  
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with DTU Pay using their bank account
    And the customer has a invalid token
    And a merchant with name "John", last name "Oliver", and CPR "230576-3865"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with DTU Pay using their bank account
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is failed

Scenario: Customer is not known
    Given a merchant with name "Danieli", who is registered with DTU Pay
    When the merchant initiates a payment for 10 kr using customer id "non-existent-id"
    Then the payment is failed

Scenario: Merchant is not known
    Given a customer with name "Line", who is registered with DTU Pay
    When a payment for 10 kr is initiated by a corrupted merchant with id "non-existent-id"
    Then the payment is failed
    And the token is removed
Feature: Payment
Scenario: Successful Payment
    Given a customer with name "Susan", last name "Baldwin", and CPR "030154-4400"  
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Daniel", last name "Oliver", and CPR "131161-3046"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with Simple DTU Pay using their bank account
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful
    And the balance of the customer at the bank is 990 kr
    And the balance of the merchant at the bank is 1010 kr

Scenario: Failed Payment
    Given a customer with name "Susan", last name "Baldwin", and CPR "030154-4400"  
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with Simple DTU Pay using their bank account
    And a merchant with name "Daniel", last name "Oliver", and CPR "131161-3046"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with Simple DTU Pay using their bank account
    When the merchant initiates a payment for 1001 kr by the customer
    Then the payment is failed


Scenario: List of payments
    Given a customer with name "Susan", who is registered with Simple DTU Pay
    And a merchant with name "Daniel", who is registered with Simple DTU Pay
    Given a successful payment of 10 kr from the customer to the merchant
    When the manager asks for a list of payments
    Then the list contains a payments where customer "Susan" paid 10 kr to merchant "Daniel"

Scenario: Customer is not known
    Given a merchant with name "Daniel", who is registered with Simple DTU Pay
    When the merchant initiates a payment for 10 kr using customer id "non-existent-id"
    Then [cust]the payment is not successful
    And [cust]an error message is returned saying "customer with id \"non-existent-id\" is unknow

Scenario: Merchant is not known
    Given a customer with name "Susan", who is registered with Simple DTU Pay
    When the customer initiates a payment for 10 kr using merchant id "non-existent-id"
    Then the payment is not successful
    And an error message is returned saying "customer with id \"non-existent-id\" is unknow
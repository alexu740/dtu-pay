Feature: Report generation

Scenario: All reports are available after payment
    Given a customer with name "Susan", last name "Baldwin", and CPR "030154-9678"  
    And the customer is registered with the bank with an initial balance of 1000 kr
    And the customer is registered with DTU Pay using their bank account
    And the customer has a valid token
    And a merchant with name "Daniel", last name "Oliver", and CPR "131161-8272"
    And the merchant is registered with the bank with an initial balance of 1000 kr
    And the merchant is registered with DTU Pay using their bank account
    When the merchant initiates a payment for 10 kr by the customer
    Then the payment is successful
    And the payment is in the customer report, containing the used token, the amount of 10 kr and the merchant reference
    And the payment is in the merchant report, containing the used token and the amount of 10 kr
    And the payment is in the manager report, containing the used token and the amount of 10 kr, as well as the customer and merchant references
Feature: Merchant feature
Scenario: Merchant Registration
    Given a merchant with name "Susan"
    When the merchant is registered
    Then the merchant id is returned

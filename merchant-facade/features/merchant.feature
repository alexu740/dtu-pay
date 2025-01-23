Feature: Feature

Scenario: Merchant Registration
    Given an API call for registering a merchant
    Then the MerchantRegistrationRequested event is emitted
    And when the corresponding MerchantRegistered is received
    Then the merchant id is returned

Scenario: Merchant deregistration
    Given an API call to delete a merchant
    Then the MerchantDeregistrationRequested event is emitted
    And when the correspoding AccountDeregistered event is received with payload "success"
    Then the payload "success" is returned

Scenario: Merchant report
    Given an API call to get the merchant report
    Then the MerchantReportRequested event is emitted
    And when the correspoding MerchantReportSent event is received
    Then a report is returned

Scenario: Payment request successful
    Given an API call initiate payment
    Then the PaymentRequested event is emitted
    And when the correspoding PaymentSucceeded event is received
    Then the payment is successful

Scenario: Payment request successful
    Given an API call initiate payment
    Then the PaymentRequested event is emitted
    And when the correspoding PaymentFailed event is received
    Then the payment is failed
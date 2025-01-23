Feature: Report Feature

Scenario: Add payment report when PaymentSucceeded is received
    When a "PaymentSucceeded" event is received to indicate a successful payment
    Then a "ReportGenerated" event is sent to show that the report was generated

Scenario: Generate Merchant report when MerchantReportRequested is received
    Given that a successful payment occured in the past
    When a "MerchantReportRequested" event is received from a merchant requesting a report
    Then a "MerchantReportSent" event is sent to show the token and amount of that past payment


Scenario: Generate Customer report when CustomerReportRequested is received
    Given that a successful payment occured in the past
    When a "CustomerReportRequested" event is received from a customer requesting a report
    Then a "CustomerReportSent" event is sent to show the token, merchantId and amount of that past payment

Scenario: Generate Manager report when ManagerReportRequested is received
    Given that a successful payment occured in the past
    When a "ManagerReportRequested" event is received from a manager requesting a report
    Then a "ManagerReportSent" event is sent to show the customerId, token, merchantId and amount of that past payment


Feature: Feature

Scenario: Generation of reports
    Given a PaymentSucceeded event is received
    Then the application service executes a handlePaymentReceived command
    And the payment is saved

Scenario: Customer reports
    Given a CustomerReportRequested event is received
    Then the application service executes a handleCustomerReportRequested query
    And the CustomerReportSent event is emitted

Scenario: Merchant reports
    Given a MerchantReportRequested event is received
    Then the application service executes a handleMerchantReportRequested query
    And the MerchantReportSent event is emitted

Scenario: Manager reports
    Given a ManagerReportRequested event is received
    Then the application service executes a handleManagerReportRequested query
    And the ManagerReportSent event is emitted
Feature: Account Feature

Scenario: Customer registration Scenario
    Given a "CustomerRegistrationRequested" registration event is received
    Then then an account creation command is sent
    And the "AccountRegistered" event is sent
    And the registered account is of type customer

Scenario: Merchant registration Scenario
    Given a "MerchantRegistrationRequested" registration event is received
    Then then an account creation command is sent
    And the "AccountRegistered" event is sent
    And the registered account is NOT of type customer

Scenario: Customer Retrieval
    Given a "CustomerRetrievalRequested" query event is received
    Then a AccountGetQuery query is sent in case the requested account is of customer type and an event of type "CustomerRetrieved" is raised

    
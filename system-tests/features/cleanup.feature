Feature: Cleanup
Scenario: Cleanup successful
    Given a bank account is created
    Then the account is cleaned up correctly upon deletion

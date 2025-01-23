Feature: Manager Feature

Scenario: Manager report
    Given an API call to get the manager report
    Then the ManagerReportRequested event is emitted
    And when the correspoding ManagerReportSent event is received
    Then a report is returned
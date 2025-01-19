Feature: Token feature

Scenrio: Creating new tokens for new user
    Given a registered customer with name "Susan", cpr "010201-0001" and 0 tokens
    When requesting 5 new tokens for "Susan"
    Then the customer "Susan" has 5 tokens

Scenrio: Creating new tokens for user with 1 valid token
    Given a registered customer with name "Daniel", cpr "010201-0002" and 1 tokens
    When requesting 5 new tokens for "Daniel"
    Then the customer "Daniel" has 6 tokens

Scenrio: Creating too many tokens
    Given a registered customer with name "Lina", cpr "010201-0003" and 0 tokens
    When requesting 6 new tokens for "Lina"
    Then the request for "Lina" is denied

Scenrio: Creating tokens when tokens are already available
    Given a registered customer with name "Bob", cpr "010201-0004" and 2 tokens
    When requesting 6 new tokens for "Bob"
    Then the request is for "Bob" denied

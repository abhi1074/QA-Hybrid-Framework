@api
Feature: Booking API

  @smoke
  Scenario: Create a booking
    When a new booking is created via the API
    Then the API responds with status 200
    And the response contains a booking id

  @regression
  Scenario: Created booking can be retrieved
    Given a new booking is created via the API
    When the booking is retrieved by id
    Then the API responds with status 200
    And the retrieved booking matches the one created

  @regression
  Scenario: Booking can be updated with a valid token
    Given a new booking is created via the API
    And a valid auth token
    When the booking first name is updated to "Updated"
    Then the API responds with status 200
    And the response first name is "Updated"

  @regression
  Scenario: Deleted booking is no longer available
    Given a new booking is created via the API
    And a valid auth token
    When the booking is deleted
    And the booking is retrieved by id
    Then the API responds with status 404

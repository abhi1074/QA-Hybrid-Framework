@ui
Feature: Login

  @smoke
  Scenario: Valid user can log in
    Given the user is on the SauceDemo login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the products page is displayed

  @regression
  Scenario Outline: Login is rejected for invalid credentials
    Given the user is on the SauceDemo login page
    When the user logs in with username "<username>" and password "<password>"
    Then the login error message contains "<error>"

    Examples:
      | username        | password     | error                 |
      | standard_user   | wrong_pass   | do not match any user |
      | locked_out_user | secret_sauce | locked out            |
      |                 | secret_sauce | Username is required  |

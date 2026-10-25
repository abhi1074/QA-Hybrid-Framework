@ui
Feature: Checkout

  @smoke @regression
  Scenario: Customer completes an order end to end
    Given the user is on the SauceDemo login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    And the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    Then the cart contains "Sauce Labs Backpack"
    When the user checks out with first name "Test", last name "User" and zip "302001"
    Then the order confirmation says "Thank you for your order!"

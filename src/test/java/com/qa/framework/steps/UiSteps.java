package com.qa.framework.steps;

import com.qa.framework.core.DriverFactory;
import com.qa.framework.pages.CartPage;
import com.qa.framework.pages.CheckoutPage;
import com.qa.framework.pages.InventoryPage;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.Product;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class UiSteps {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @Given("the user is on the SauceDemo login page")
    public void openLoginPage() {
        loginPage = new LoginPage(DriverFactory.getDriver()).open();
    }

    @When("the user logs in with username {string} and password {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
        inventoryPage = new InventoryPage(DriverFactory.getDriver());
    }

    @Then("the products page is displayed")
    public void productsPageDisplayed() {
        Assert.assertTrue(inventoryPage.isLoaded(), "Products page not shown");
    }

    @Then("the login error message contains {string}")
    public void loginErrorContains(String expected) {
        Assert.assertTrue(loginPage.getErrorMessage().contains(expected),
                "Unexpected error: " + loginPage.getErrorMessage());
    }

    @When("the user adds {string} to the cart")
    public void addToCart(String productName) {
        inventoryPage.addProduct(Product.fromName(productName));
    }

    @When("the user opens the cart")
    public void openCart() {
        cartPage = inventoryPage.openCart();
    }

    @Then("the cart contains {string}")
    public void cartContains(String productName) {
        Assert.assertTrue(cartPage.getItemNames().contains(productName),
                "Cart items were: " + cartPage.getItemNames());
    }

    @When("the user checks out with first name {string}, last name {string} and zip {string}")
    public void checkout(String first, String last, String zip) {
        checkoutPage = cartPage.checkout().enterDetails(first, last, zip).finish();
    }

    @Then("the order confirmation says {string}")
    public void confirmationSays(String expected) {
        Assert.assertEquals(checkoutPage.getConfirmation(), expected);
    }
}

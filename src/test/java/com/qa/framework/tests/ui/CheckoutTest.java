package com.qa.framework.tests.ui;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.core.BaseUITest;
import com.qa.framework.core.DriverFactory;
import com.qa.framework.pages.CartPage;
import com.qa.framework.pages.CheckoutPage;
import com.qa.framework.pages.InventoryPage;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.Product;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("UI")
@Feature("Checkout")
public class CheckoutTest extends BaseUITest {

    private InventoryPage loginAsStandardUser() {
        LoginPage login = new LoginPage(DriverFactory.getDriver()).open();
        login.login(ConfigReader.get("ui.username"), ConfigReader.get("ui.password"));
        return new InventoryPage(DriverFactory.getDriver());
    }

    @Test(groups = "regression")
    @Description("Cart badge reflects the number of items added")
    public void cartBadgeUpdatesWhenItemsAreAdded() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addProduct(Product.BACKPACK).addProduct(Product.BIKE_LIGHT);
        Assert.assertEquals(inventory.getCartCount(), 2);
    }

    @Test(groups = {"smoke", "regression"})
    @Description("End-to-end: login, add items, checkout, see confirmation")
    public void customerCanCompleteAnOrder() {
        InventoryPage inventory = loginAsStandardUser();
        inventory.addProduct(Product.BACKPACK).addProduct(Product.BIKE_LIGHT);

        CartPage cart = inventory.openCart();
        Assert.assertEquals(cart.getItemNames(),
                List.of(Product.BACKPACK.displayName(), Product.BIKE_LIGHT.displayName()));

        CheckoutPage checkout = cart.checkout().enterDetails("Test", "User", "302001").finish();
        Assert.assertEquals(checkout.getConfirmation(), "Thank you for your order!");
    }
}

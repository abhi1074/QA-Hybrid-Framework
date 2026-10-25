package com.qa.framework.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {

    private static final By TITLE = By.className("title");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By CART_BADGE = By.className("shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Products".equals(text(TITLE));
    }

    @Step("Add {product} to cart")
    public InventoryPage addProduct(Product product) {
        click(By.cssSelector("[data-test='add-to-cart-" + product.slug() + "']"));
        // wait for the button to flip to "Remove" so the cart count is up to date
        visible(By.cssSelector("[data-test='remove-" + product.slug() + "']"));
        return this;
    }

    public int getCartCount() {
        var badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    @Step("Open cart")
    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }
}

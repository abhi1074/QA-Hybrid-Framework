package com.qa.framework.tests.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qa.framework.config.ConfigReader;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Same flows as the Selenium suite, written with Playwright's auto-waiting and web-first assertions.
 * No explicit waits needed: compare with the pages/ package to see the difference.
 */
@Epic("UI")
@Feature("Playwright")
public class PlaywrightCheckoutTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(ConfigReader.getBoolean("headless")));
        page = browser.newPage();
        page.navigate(ConfigReader.get("ui.base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && page != null) {
            Allure.addAttachment("Failure - " + result.getName(), "image/png",
                    new ByteArrayInputStream(page.screenshot()), "png");
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    private void login() {
        page.locator("#user-name").fill(ConfigReader.get("ui.username"));
        page.locator("#password").fill(ConfigReader.get("ui.password"));
        page.locator("#login-button").click();
    }

    @Test(groups = {"smoke", "regression", "playwright"})
    @Description("Login and add an item to the cart")
    public void userCanAddItemToCart() {
        login();
        assertThat(page.locator(".title")).hasText("Products");

        page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
        assertThat(page.locator(".shopping_cart_badge")).hasText("1");
    }

    @Test(groups = {"regression", "playwright"})
    @Description("Complete an order end to end")
    public void userCanCompleteCheckout() {
        login();
        page.locator("[data-test='add-to-cart-sauce-labs-backpack']").click();
        page.locator(".shopping_cart_link").click();
        assertThat(page.locator(".inventory_item_name")).hasText("Sauce Labs Backpack");

        page.locator("#checkout").click();
        page.locator("#first-name").fill("Test");
        page.locator("#last-name").fill("User");
        page.locator("#postal-code").fill("302001");
        page.locator("#continue").click();
        page.locator("#finish").click();

        assertThat(page.locator(".complete-header")).hasText("Thank you for your order!");
    }
}

package com.qa.framework.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By FINISH = By.id("finish");
    private static final By CONFIRMATION = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter shipping details")
    public CheckoutPage enterDetails(String first, String last, String zip) {
        type(FIRST_NAME, first);
        type(LAST_NAME, last);
        type(POSTAL_CODE, zip);
        click(CONTINUE);
        return this;
    }

    @Step("Finish order")
    public CheckoutPage finish() {
        click(FINISH);
        return this;
    }

    public String getConfirmation() {
        return text(CONFIRMATION);
    }
}

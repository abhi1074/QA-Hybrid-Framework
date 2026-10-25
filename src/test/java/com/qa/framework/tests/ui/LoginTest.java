package com.qa.framework.tests.ui;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.core.BaseUITest;
import com.qa.framework.core.DriverFactory;
import com.qa.framework.pages.InventoryPage;
import com.qa.framework.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("UI")
@Feature("Login")
public class LoginTest extends BaseUITest {

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][]{
                {"standard_user", "wrong_pass", "do not match any user"},
                {"locked_out_user", "secret_sauce", "locked out"},
                {"", "secret_sauce", "Username is required"}
        };
    }

    @Test(groups = {"smoke", "regression"})
    @Description("A valid user lands on the products page")
    public void validUserCanLogin() {
        LoginPage login = new LoginPage(DriverFactory.getDriver()).open();
        login.login(ConfigReader.get("ui.username"), ConfigReader.get("ui.password"));
        Assert.assertTrue(new InventoryPage(DriverFactory.getDriver()).isLoaded(), "Products page not shown");
    }

    @Test(groups = "regression", dataProvider = "invalidLogins")
    @Description("Invalid or locked users see the right error message")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        LoginPage login = new LoginPage(DriverFactory.getDriver()).open();
        login.login(username, password);
        Assert.assertTrue(login.getErrorMessage().contains(expectedError),
                "Unexpected error: " + login.getErrorMessage());
    }
}

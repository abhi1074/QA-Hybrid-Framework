package com.qa.framework.core;

import com.qa.framework.utils.ScreenshotUtil;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUITest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && DriverFactory.hasDriver()) {
            ScreenshotUtil.attachToAllure(DriverFactory.getDriver(), "Failure - " + result.getName());
        }
        DriverFactory.quitDriver();
    }
}

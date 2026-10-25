package com.qa.framework.steps;

import com.qa.framework.core.DriverFactory;
import com.qa.framework.utils.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before("@ui")
    public void startBrowser() {
        DriverFactory.initDriver();
    }

    @After("@ui")
    public void stopBrowser(Scenario scenario) {
        if (scenario.isFailed() && DriverFactory.hasDriver()) {
            byte[] png = ScreenshotUtil.bytes(DriverFactory.getDriver());
            if (png.length > 0) {
                scenario.attach(png, "image/png", "Failure screenshot");
            }
        }
        DriverFactory.quitDriver();
    }
}

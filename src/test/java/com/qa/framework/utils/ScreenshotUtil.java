package com.qa.framework.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static byte[] bytes(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static void attachToAllure(WebDriver driver, String name) {
        byte[] png = bytes(driver);
        if (png.length > 0) {
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), "png");
        }
    }
}

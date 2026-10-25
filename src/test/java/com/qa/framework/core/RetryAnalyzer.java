package com.qa.framework.core;

import com.qa.framework.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** Re-runs a failed test up to retry.count times to separate flaky tests from real defects. */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int attempt = 0;
    private final int maxRetries = ConfigReader.getInt("retry.count");

    @Override
    public boolean retry(ITestResult result) {
        if (attempt < maxRetries) {
            attempt++;
            return true;
        }
        return false;
    }
}

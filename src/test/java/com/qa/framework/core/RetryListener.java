package com.qa.framework.core;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/** Attaches RetryAnalyzer to every @Test automatically (Cucumber scenarios are skipped). */
public class RetryListener implements IAnnotationTransformer {

    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod != null && AbstractTestNGCucumberTests.class.isAssignableFrom(testMethod.getDeclaringClass())) {
            return;
        }
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}

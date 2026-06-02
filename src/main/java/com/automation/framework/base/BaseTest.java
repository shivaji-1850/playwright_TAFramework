package com.automation.framework.base;

import com.automation.framework.abstractions.*;
import com.automation.framework.factory.DriverFactory;
import com.automation.framework.logging.FrameworkLogger;
import com.automation.framework.reporting.ReportManager;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * ALL test classes in the consumer project extend this.
 * No Playwright/Selenium import should ever appear in a test class.
 */
public abstract class BaseTest {

    protected static final Logger log = FrameworkLogger.getLogger(BaseTest.class);
    protected IDriverManager driver;
    protected IWebActions actions;
    protected IWaits waits;
    protected IAssertions assertions;

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ReportManager.initReport();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        log.info("▶ Starting test: {}", method.getName());
        ReportManager.createTest(method.getName());
        driver     = DriverFactory.getDriver();
        actions    = driver.getWebActions();
        waits      = driver.getWaits();
        assertions = driver.getAssertions();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("✘ Test FAILED: {}", result.getName());
            String screenshotPath = "target/screenshots/" + result.getName() + ".png";
            driver.takeScreenshot(screenshotPath);
            ReportManager.failWithScreenshot(result.getName(), screenshotPath);
        } else {
            log.info("✔ Test PASSED: {}", result.getName());
            ReportManager.pass(result.getName());
        }
        DriverFactory.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ReportManager.flushReport();
    }
}

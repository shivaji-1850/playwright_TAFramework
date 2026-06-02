package com.automation.framework.abstractions;

/**
 * Framework-agnostic driver manager interface.
 * Implement this for Playwright, Selenium, Appium, etc.
 * Tests ONLY interact with this interface — never with concrete classes.
 */
public interface IDriverManager {
    void initDriver(String browserType, boolean headless);
    void navigateTo(String url);
    void closeBrowser();
    IWebActions getWebActions();
    IWaits getWaits();
    IAssertions getAssertions();
    Object getDriver();  // Returns Page (Playwright) or WebDriver (Selenium)
    String getCurrentUrl();
    String getTitle();
    void takeScreenshot(String filePath);
    void clearCookies();
    void addCookies(String name, String value, String domain);
}

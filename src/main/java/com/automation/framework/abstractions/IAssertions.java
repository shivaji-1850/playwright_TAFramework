package com.automation.framework.abstractions;

public interface IAssertions {
    void assertElementVisible(String selector);
    void assertElementHidden(String selector);
    void assertElementEnabled(String selector);
    void assertElementDisabled(String selector);
    void assertElementContainsText(String selector, String expectedText);
    void assertElementHasText(String selector, String expectedText);
    void assertPageTitle(String expectedTitle);
    void assertCurrentUrl(String expectedUrl);
    void assertElementCount(String selector, int expectedCount);
    void assertTrue(boolean condition, String message);
    void assertFalse(boolean condition, String message);
    void assertEquals(Object actual, Object expected, String message);
}

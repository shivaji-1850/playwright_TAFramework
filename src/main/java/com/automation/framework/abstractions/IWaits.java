package com.automation.framework.abstractions;

public interface IWaits {
    void waitForElementVisible(String selector, int timeoutMs);
    void waitForElementHidden(String selector, int timeoutMs);
    void waitForElementEnabled(String selector, int timeoutMs);
    void waitForElementClickable(String selector, int timeoutMs);
    void waitForUrlContains(String urlPart, int timeoutMs);
    void waitForNavigationComplete(int timeoutMs);
    void waitForLoadState(String state, int timeoutMs);
    void implicitWait(int milliseconds);
}

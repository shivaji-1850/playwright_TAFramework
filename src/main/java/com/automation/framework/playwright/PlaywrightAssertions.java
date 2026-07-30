package com.automation.framework.playwright;

import com.automation.framework.abstractions.IAssertions;
import com.automation.framework.logging.FrameworkLogger;
import com.microsoft.playwright.Locator;
import org.slf4j.Logger;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightAssertions implements IAssertions {
    private static final Logger log = FrameworkLogger.getLogger(PlaywrightAssertions.class);
    private final PlaywrightDriverManager driverManager;

    public PlaywrightAssertions(PlaywrightDriverManager playwrightDriverManager) {
        this.driverManager = playwrightDriverManager;
        log.debug("PlaywrightAssertions initialized");
    }

    @Override
    public void assertElementVisible(String selector) {
        log.info("Asserting element is visible: selector='{}'", selector);
        try {
            Locator element = driverManager.getPage().locator(selector);
            assertThat(element).isVisible();
            log.info("✓ Element is visible: '{}'", selector);
        } catch (AssertionError e) {
            log.error("✗ Element is NOT visible: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertElementHidden(String selector) {
        log.info("Asserting element is hidden: selector='{}'", selector);
        try {
            Locator element = driverManager.getPage().locator(selector);
            assertThat(element).isHidden();
            log.info("✓ Element is hidden: '{}'", selector);
        } catch (AssertionError e) {
            log.error("✗ Element is NOT hidden: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertElementEnabled(String selector) {
        log.info("Asserting element is enabled: selector='{}'", selector);
        try {
            Locator element = driverManager.getPage().locator(selector);
            assertThat(element).isEnabled();
            log.info("✓ Element is enabled: '{}'", selector);
        } catch (AssertionError e) {
            log.error("✗ Element is NOT enabled: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertElementDisabled(String selector) {
        log.info("Asserting element is disabled: selector='{}'", selector);
        try {
            Locator element = driverManager.getPage().locator(selector);
            assertThat(element).isDisabled();
            log.info("✓ Element is disabled: '{}'", selector);
        } catch (AssertionError e) {
            log.error("✗ Element is NOT disabled: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertElementContainsText(String selector, String expectedText) {
        log.info("Asserting element contains text: selector='{}', expectedText='{}'", selector, expectedText);
        try {
            Locator element = driverManager.getPage().locator(selector);
            assertThat(element).containsText(expectedText);
            log.info("✓ Element contains expected text: '{}' in '{}'", expectedText, selector);
        } catch (AssertionError e) {
            log.error("✗ Element does NOT contain text '{}': selector='{}', error={}",
                    expectedText, selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertElementHasText(String selector, String expectedText) {
        log.info("Asserting element has exact text: selector='{}', expectedText='{}'", selector, expectedText);
        try {
            Locator element = driverManager.getPage().locator(selector);
            assertThat(element).hasText(expectedText);
            log.info("✓ Element has exact text: '{}' in '{}'", expectedText, selector);
        } catch (AssertionError e) {
            log.error("✗ Element does NOT have exact text '{}': selector='{}', error={}",
                    expectedText, selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertPageTitle(String expectedTitle) {
        log.info("Asserting page title: expectedTitle='{}'", expectedTitle);
        try {
            String actualTitle = driverManager.getPage().title();
            if (!actualTitle.equals(expectedTitle)) {
                throw new AssertionError("Expected title '" + expectedTitle + "' but got '" + actualTitle + "'");
            }
            log.info("✓ Page title matches: '{}'", expectedTitle);
        } catch (AssertionError e) {
            log.error("✗ Page title does NOT match: expected='{}', error={}", expectedTitle, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertCurrentUrl(String expectedUrl) {
        log.info("Asserting current URL: expectedUrl='{}'", expectedUrl);
        try {
            String actualUrl = driverManager.getPage().url();
            if (!actualUrl.contains(expectedUrl)) {
                throw new AssertionError("Expected URL to contain '" + expectedUrl + "' but got '" + actualUrl + "'");
            }
            log.info("✓ Current URL matches: '{}'", expectedUrl);
        } catch (AssertionError e) {
            log.error("✗ Current URL does NOT match: expected='{}', error={}", expectedUrl, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertElementCount(String selector, int expectedCount) {
        log.info("Asserting element count: selector='{}', expectedCount={}", selector, expectedCount);
        try {
            int actualCount = driverManager.getPage().locator(selector).count();
            if (actualCount != expectedCount) {
                throw new AssertionError("Expected " + expectedCount + " elements but found " + actualCount);
            }
            log.info("✓ Element count matches: {} element(s) found for '{}'", actualCount, selector);
        } catch (AssertionError e) {
            log.error("✗ Element count does NOT match: selector='{}', expected={}, error={}",
                    selector, expectedCount, e.getMessage());
            throw e;
        }
    }

    @Override
    public void assertTrue(boolean condition, String message) {
        log.info("Asserting condition is TRUE: message='{}'", message);
        try {
            if (!condition) {
                throw new AssertionError(message);
            }
            log.info("✓ Condition is TRUE: {}", message);
        } catch (AssertionError e) {
            log.error("✗ Condition is NOT TRUE: {}", message);
            throw e;
        }
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        log.info("Asserting condition is FALSE: message='{}'", message);
        try {
            if (condition) {
                throw new AssertionError(message);
            }
            log.info("✓ Condition is FALSE: {}", message);
        } catch (AssertionError e) {
            log.error("✗ Condition is NOT FALSE: {}", message);
            throw e;
        }
    }

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        log.info("Asserting equality: expected='{}', actual='{}', message='{}'", expected, actual, message);
        try {
            if (!expected.equals(actual)) {
                throw new AssertionError("Expected '" + expected + "' but got '" + actual + "'. " + message);
            }
            log.info("✓ Values are equal: expected='{}', actual='{}'", expected, actual);
        } catch (AssertionError e) {
            log.error("✗ Values are NOT equal: expected='{}', actual='{}', message='{}'",
                    expected, actual, message);
            throw e;
        }
    }
}

package com.automation.framework.playwright;

import com.automation.framework.abstractions.IWaits;
import com.automation.framework.logging.FrameworkLogger;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;

import java.util.concurrent.TimeoutException;

public class PlaywrightWaits implements IWaits {
    private static final Logger log = FrameworkLogger.getLogger(PlaywrightWaits.class);
    private final PlaywrightDriverManager driverManager;
    private final int defaultTimeout = 30000; // 30 seconds

    public PlaywrightWaits(PlaywrightDriverManager playwrightDriverManager) {
        this.driverManager = playwrightDriverManager;
        log.debug("PlaywrightWaits initialized");
    }

    @Override
    public void waitForElementVisible(String selector, int timeoutMs) {
        log.info("Waiting for element to be visible: selector='{}', timeout={}ms", selector, timeoutMs);
        try {
            driverManager.getPage().locator(selector).waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
            log.info("Element became visible: '{}'", selector);
        } catch (Exception e) {
            log.error("Failed waiting for element visibility: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void waitForElementHidden(String selector, int timeoutMs) {
        log.info("Waiting for element to be hidden: selector='{}', timeout={}ms", selector, timeoutMs);
        try {
            Page page = driverManager.getPage();
            page.locator(selector).waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(timeoutMs));
            log.info("Element became hidden: '{}'", selector);
        } catch (Exception e) {
            log.error("Failed waiting for element to be hidden: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void waitForElementEnabled(String selector, int timeoutMs) {
        log.info("Waiting for element to be enabled: selector='{}', timeout={}ms", selector, timeoutMs);
        try {
            Page page = driverManager.getPage();
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < timeoutMs) {
                if (page.locator(selector).isEnabled()) {
                    log.info("Element is now enabled: '{}'", selector);
                    return;
                }
                Thread.sleep(500);
            }
            log.error("Element did not become enabled within timeout: '{}'", selector);
            throw new TimeoutException("Element '" + selector + "' not enabled within " + timeoutMs + "ms");
        } catch (Exception e) {
            log.error("Failed waiting for element to be enabled: selector='{}', error={}", selector, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void waitForElementClickable(String selector, int timeoutMs) {
        log.info("Waiting for element to be clickable: selector='{}', timeout={}ms", selector, timeoutMs);
        try {
            waitForElementVisible(selector, timeoutMs);
            waitForElementEnabled(selector, timeoutMs);
            log.info("Element is now clickable: '{}'", selector);
        } catch (Exception e) {
            log.error("Failed waiting for element to be clickable: selector='{}', error={}", selector, e.getMessage());
            throw e;
        }
    }

    @Override
    public void waitForUrlContains(String urlPart, int timeoutMs) {
        log.info("Waiting for URL to contain: '{}', timeout={}ms", urlPart, timeoutMs);
        try {
            driverManager.getPage().waitForURL("**/*" + urlPart + "*",
                    new Page.WaitForURLOptions().setTimeout(timeoutMs));
            log.info("URL now contains: '{}'", urlPart);
        } catch (Exception e) {
            log.error("Failed waiting for URL to contain '{}': error={}", urlPart, e.getMessage());
            throw e;
        }
    }

    @Override
    public void waitForNavigationComplete(int timeoutMs) {
        log.info("Waiting for navigation to complete: timeout={}ms", timeoutMs);
        try {
            driverManager.getPage().waitForLoadState(LoadState.NETWORKIDLE,
                    new Page.WaitForLoadStateOptions().setTimeout(timeoutMs));
            log.info("Navigation completed successfully");
        } catch (Exception e) {
            log.error("Failed waiting for navigation: error={}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void waitForLoadState(String state, int timeoutMs) {
        log.info("Waiting for page load state: state='{}', timeout={}ms", state, timeoutMs);
        try {
            driverManager.getPage().waitForLoadState(resolveLoadState(state),
                    new Page.WaitForLoadStateOptions().setTimeout(timeoutMs));
            log.info("Page reached load state: '{}'", state);
        } catch (Exception e) {
            log.error("Failed waiting for load state '{}': error={}", state, e.getMessage());
            throw e;
        }
    }

    private LoadState resolveLoadState(String state) {
        if (state == null) {
            throw new IllegalArgumentException("Load state cannot be null.");
        }
        switch (state.trim().toLowerCase()) {
            case "load":
                return LoadState.LOAD;
            case "domcontentloaded":
                return LoadState.DOMCONTENTLOADED;
            case "networkidle":
                return LoadState.NETWORKIDLE;
            default:
                throw new IllegalArgumentException("Unsupported load state: " + state);
        }
    }

    @Override
    public void implicitWait(int milliseconds) {
        log.info("Setting implicit wait: {}ms", milliseconds);
        try {
            Thread.sleep(milliseconds);
            log.debug("Implicit wait completed");
        } catch (InterruptedException e) {
            log.warn("Implicit wait interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

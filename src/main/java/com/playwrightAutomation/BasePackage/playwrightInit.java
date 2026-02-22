package com.playwrightAutomation.BasePackage;

import com.microsoft.playwright.*;

public class playwrightInit {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public playwrightInit(String browserType, boolean headless) {
        launchBrowser(browserType, headless);
        createPage();
    }
    // Constructor
    public playwrightInit() {
        this.playwright = null;
        this.browser = null;
        this.context = null;
        this.page = null;
    }

    // Initialize Playwright
    public void initPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
    }

    // Launch browser ("chromium", "firefox", "webkit")
    public void launchBrowser(String browserType, boolean headless) {
        initPlaywright();
        if (browser != null) {
            browser.close();
        }
        switch (browserType.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }
        createContext();
    }

    // Create a new browser context
    public void createContext() {
        if (browser == null) {
            throw new IllegalStateException("Browser not initialized. Call launchBrowser() first.");
        }
        if (context != null) {
            context.close();
        }
        context = browser.newContext();
    }

    // Get the current browser context
    public BrowserContext getContext() {
        if (context == null) {
            throw new IllegalStateException("Context not initialized. Call createContext() first.");
        }
        return context;
    }

    // Close the current browser context
    public void closeContext() {
        if (context != null) {
            context.close();
            context = null;
        }
    }

    // Create a new page in the context
    public void createPage() {
        if (context == null) {
            throw new IllegalStateException("Context not initialized. Call createContext() first.");
        }
        page = context.newPage();
    }

    // Get the current browser
    public Browser getBrowser() {
        if (browser == null) {
            throw new IllegalStateException("Browser not initialized.");
        }
        return browser;
    }

    // Get the current page
    public Page getPage() {
        if (page == null) {
            throw new IllegalStateException("Page not initialized. Call createPage() first.");
        }
        return page;
    }

    // Close all resources
    public void close() {
        if (page != null) {
            page.close();
            page = null;
        }
        closeContext();
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}

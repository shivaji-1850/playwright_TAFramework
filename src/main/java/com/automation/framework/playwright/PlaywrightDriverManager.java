package com.automation.framework.playwright;

import com.automation.framework.abstractions.*;
import com.automation.framework.config.ConfigManager;
import com.automation.framework.logging.FrameworkLogger;
import com.microsoft.playwright.*;
import org.slf4j.Logger;

import java.nio.file.Paths;
import java.util.Base64;

public class PlaywrightDriverManager implements IDriverManager {

    private static final Logger log = FrameworkLogger.getLogger(PlaywrightDriverManager.class);

    // ThreadLocal ensures each test thread has its own browser instance
    private static final ThreadLocal<Playwright>      TL_PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser>         TL_BROWSER    = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext>  TL_CONTEXT    = new ThreadLocal<>();
    private static final ThreadLocal<Page>            TL_PAGE       = new ThreadLocal<>();

    private final PlaywrightWebActions webActions;
    private final PlaywrightWaits waits;
    private final PlaywrightAssertions assertions;

    public PlaywrightDriverManager() {
        this.webActions  = new PlaywrightWebActions(this);
        this.waits       = new PlaywrightWaits(this);
        this.assertions  = new PlaywrightAssertions(this);
    }

    @Override
    public void initDriver(String browserType, boolean headless) {
        log.info("Initializing Playwright driver: browser={}, headless={}", browserType, headless);
        Playwright playwright = Playwright.create();
        TL_PLAYWRIGHT.set(playwright);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(ConfigManager.getInt("slowmo.ms", 0));

        Browser browser;
        switch (browserType.toLowerCase()) {
            case "chromium": browser = playwright.chromium().launch(options); break;
            case "firefox":  browser = playwright.firefox().launch(options);  break;
            case "webkit":   browser = playwright.webkit().launch(options);   break;
            case "chrome":
                browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(headless)
                                .setChannel("chrome"));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }
        TL_BROWSER.set(browser);

        // Context with viewport, locale, timezone etc from config
        Browser.NewContextOptions ctxOptions = new Browser.NewContextOptions()
                .setViewportSize(
                        ConfigManager.getInt("viewport.width", 1920),
                        ConfigManager.getInt("viewport.height", 1080))
                .setLocale(ConfigManager.get("locale", "en-US"))
                .setIgnoreHTTPSErrors(ConfigManager.getBool("ignore.https.errors", false));

        // Optional: record video
        if (ConfigManager.getBool("record.video", false)) {
            ctxOptions.setRecordVideoDir(Paths.get("target/videos"));
        }

        BrowserContext context = browser.newContext(ctxOptions);
        TL_CONTEXT.set(context);

        Page page = context.newPage();
        TL_PAGE.set(page);

        log.info("Browser initialized successfully: {}", browserType);
    }

    @Override
    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        getPage().navigate(url);
    }

    @Override
    public void closeBrowser() {
        log.info("Closing browser resources");
        if (TL_PAGE.get()       != null) TL_PAGE.get().close();
        if (TL_CONTEXT.get()    != null) TL_CONTEXT.get().close();
        if (TL_BROWSER.get()    != null) TL_BROWSER.get().close();
        if (TL_PLAYWRIGHT.get() != null) TL_PLAYWRIGHT.get().close();
        TL_PAGE.remove();
        TL_CONTEXT.remove();
        TL_BROWSER.remove();
        TL_PLAYWRIGHT.remove();
    }

    @Override
    public void takeScreenshot(String filePath) {
        log.info("Taking screenshot: {}", filePath);
        getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)).setFullPage(true));
    }

    @Override
    public void clearCookies() {
        getContext().clearCookies();
    }

    @Override
    public void addCookies(String name, String value, String domain) {
        getContext().addCookies(java.util.List.of(
                new com.microsoft.playwright.options.Cookie(name, value).setDomain(domain)));
    }

    @Override
    public String getCurrentUrl() { return getPage().url(); }

    @Override
    public String getTitle() { return getPage().title(); }

    @Override
    public IWebActions getWebActions()   { return webActions; }

    @Override
    public IWaits getWaits()             { return waits; }

    @Override
    public IAssertions getAssertions()   { return assertions; }

    @Override
    public Object getDriver()            { return getPage(); }

    // Package-visible getters for sibling classes
    public Page getPage() {
        Page page = TL_PAGE.get();
        if (page == null) throw new IllegalStateException("Page not initialized. Call initDriver() first.");
        return page;
    }

    public BrowserContext getContext() {
        BrowserContext ctx = TL_CONTEXT.get();
        if (ctx == null) throw new IllegalStateException("Context not initialized.");
        return ctx;
    }
}

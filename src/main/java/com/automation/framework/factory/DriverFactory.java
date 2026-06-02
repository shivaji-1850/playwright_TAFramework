package com.automation.framework.factory;

import com.automation.framework.abstractions.IDriverManager;
import com.automation.framework.config.ConfigManager;
import com.automation.framework.playwright.PlaywrightDriverManager;

/**
 * The SWAP POINT.
 * Change config "framework=selenium" and add SeleniumDriverManager
 * — test code doesn't change at all.
 */
public class DriverFactory {

    private static final ThreadLocal<IDriverManager> TL_DRIVER = new ThreadLocal<>();

    public static IDriverManager getDriver() {
        if (TL_DRIVER.get() == null) {
            String framework = ConfigManager.get("framework", "playwright");
            IDriverManager driver;
            switch (framework.toLowerCase()) {
                case "playwright":
                    driver = new PlaywrightDriverManager();
                    break;
                // case "selenium":
                //     driver = new SeleniumDriverManager();
                //     break;
                default:
                    throw new IllegalArgumentException("Unsupported framework: " + framework);
            }
            String browser  = ConfigManager.get("browser", "chromium");
            boolean headless = ConfigManager.getBool("headless", true);
            driver.initDriver(browser, headless);
            TL_DRIVER.set(driver);
        }
        return TL_DRIVER.get();
    }

    public static void quitDriver() {
        if (TL_DRIVER.get() != null) {
            TL_DRIVER.get().closeBrowser();
            TL_DRIVER.remove();
        }
    }
}

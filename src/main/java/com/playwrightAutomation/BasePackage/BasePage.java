/*
package com.playwrightAutomation.BasePackage;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import com.playwrightAutomation.BaseUtils.Waits;
import com.playwrightAutomation.BaseUtils.WebInteractions;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class BasePage {
    public static Playwright playwright;
    public static Browser browser;
    public static BrowserContext context;
    public static Page page;
    public static Properties prop = null;
    public static final String USER_DIR = "user.dir";
    public static final String BASE_URL = "https://eitest.garmin.com";
    public static boolean isLoggedInUser = false;
    protected static Waits waits=new Waits();
    protected static WebInteractions webInteractions =new WebInteractions();
    protected static List<Cookie> cookies;

    public static void initProperties(String filePath) {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if (prop == null) {
            throw new IllegalStateException("Properties not initialized. Call initProperties() first.");
        }
        return prop.getProperty(key);
    }

    public static void driverSetup() {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--disable-features=ForceEnableSignIn","--auth-server-whitelist=\"_\"","--start-maximized"));
        browser = playwright.chromium().launch(options);
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        context.clearCookies();
        page = context.newPage();
        waits.setPage(page);
        WebInteractions.setPage(page,waits);
    }

    public static void tearDown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    public static void openBaseURL() {
        if (page == null){
            page = context.newPage();
        }
        page.navigate(BASE_URL);
    }

    public static void closeBrowserWindow() {
        if (page != null) page.close();
    }

    public static void clearCookies() {
        if (context != null) context.clearCookies();
    }

    public static List<Cookie> getLoggedInCookies() {
        return context.cookies();
    }

    public static void refreshPage() {
        page.reload();
        page.waitForLoadState();
    }

    public static void setLoggedinContext() {
        try {
            context = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("login-session.json")).setViewportSize(null));
            page = context.newPage();
            waits.setPage(page);
            WebInteractions.setPage(page, waits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openURL(String url) {
        page.navigate(BASE_URL + url);
    }



    public static void loginViaMicrosoft() {
        page.navigate("https://login.microsoftonline.com");
        String userName = System.getProperty("username");
        String password = System.getProperty("password");
        String submitButton = "//input[@type='submit']";
        page.fill("//input[@type='email']", userName);
        page.click(submitButton);
        page.waitForSelector("//input[@type='password']", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED));
        page.click("//input[@type='password']");
        page.fill("//input[@type='password']", password);
        page.click(submitButton);
        page.click(submitButton);
        page.waitForLoadState(LoadState.LOAD);
        page.waitForSelector("//button[text()='Continue']");
        page.navigate(BASE_URL);
        page.waitForLoadState(LoadState.LOAD);
        page.click(submitButton);
        isLoggedInUser = true;
    }


    public static byte[] getScreenshotToReport(String screenshotName) {
        String screenshotPath = "target/playwrightScreenshots/" + screenshotName.replaceAll("\\s+", "_") + ".png";
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)).setFullPage(true));
        return screenshot;
    }
}
*/

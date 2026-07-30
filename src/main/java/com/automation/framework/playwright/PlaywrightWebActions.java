package com.automation.framework.playwright;

import com.automation.framework.abstractions.IWebActions;
import com.automation.framework.logging.FrameworkLogger;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import org.slf4j.Logger;
import java.nio.file.Paths;
import java.util.List;

public class PlaywrightWebActions implements IWebActions {

    private static final Logger log = FrameworkLogger.getLogger(PlaywrightWebActions.class);
    private final PlaywrightDriverManager driverManager;
    // Holds current frame context; null = main frame
    private FrameLocator currentFrame = null;

    public PlaywrightWebActions(PlaywrightDriverManager driverManager) {
        this.driverManager = driverManager;
    }

    private Page page() {
        Page selectedPage = TL_CURRENT_PAGE.get();
        if (selectedPage != null && !selectedPage.isClosed()) {
            return selectedPage;
        }
        TL_CURRENT_PAGE.remove();
        return driverManager.getPage();
    }

    private Locator locate(String selector) {
        return currentFrame != null
                ? currentFrame.locator(selector)
                : page().locator(selector);
    }

    // ── Navigation ──────────────────────────────────────────────────────────
    @Override public void navigateTo(String url)  { log.info("Navigate → {}", url); page().navigate(url); }
    @Override public void goBack()                { page().goBack(); }
    @Override public void goForward()             { page().goForward(); }
    @Override public void refresh()               { page().reload(); }

    // ── Clicks ──────────────────────────────────────────────────────────────
    @Override public void click(String locator)        { log.info("Click: {}", locator); locate(locator).click(); }
    @Override public void doubleClick(String locator)  { locate(locator).dblclick(); }
    @Override public void rightClick(String locator)   { locate(locator).click(new Locator.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT)); }
    @Override public void hover(String locator)        { locate(locator).hover(); }

    // ── Typing ──────────────────────────────────────────────────────────────
    @Override public void type(String locator, String text)          { log.info("Type '{}' → {}", text, locator); locate(locator).type(text); }
    @Override public void clearAndType(String locator, String text)  { locate(locator).clear(); locate(locator).fill(text); }
    @Override public void pressKey(String locator, String key)       { locate(locator).press(key); }

    // ── Form Elements ────────────────────────────────────────────────────────
    @Override public void selectByText(String l, String text)    { locate(l).selectOption(new SelectOption().setLabel(text)); }
    @Override public void selectByValue(String l, String value)  { locate(l).selectOption(new SelectOption().setValue(value)); }
    @Override public void selectByIndex(String l, int index)     { locate(l).selectOption(new SelectOption().setIndex(index)); }
    @Override public void checkCheckbox(String l)                { locate(l).check(); }
    @Override public void uncheckCheckbox(String l)              { locate(l).uncheck(); }
    @Override public boolean isChecked(String l)                 { return locate(l).isChecked(); }

    // ── Reading Values ───────────────────────────────────────────────────────
    @Override public String getText(String l)                        { return locate(l).innerText(); }
    @Override public String getAttribute(String l, String attr)      { return locate(l).getAttribute(attr); }
    @Override public String getInputValue(String l)                  { return locate(l).inputValue(); }
    @Override public List<String> getAllTexts(String l)              { return locate(l).allInnerTexts(); }

    // ── Visibility ───────────────────────────────────────────────────────────
    @Override public boolean isVisible(String l)  { return locate(l).isVisible(); }
    @Override public boolean isEnabled(String l)  { return locate(l).isEnabled(); }
    @Override public boolean isHidden(String l)   { return locate(l).isHidden(); }
    @Override public int getElementCount(String l){ return locate(l).count(); }

    // ── Scroll ───────────────────────────────────────────────────────────────
    @Override public void scrollToElement(String l) { locate(l).scrollIntoViewIfNeeded(); }
    @Override public void scrollToTop()    { page().evaluate("window.scrollTo(0, 0)"); }
    @Override public void scrollToBottom() { page().evaluate("window.scrollTo(0, document.body.scrollHeight)"); }

    // ── File Upload ──────────────────────────────────────────────────────────
    @Override public void uploadFile(String l, String filePath) {
        locate(l).setInputFiles(Paths.get(filePath));
    }

    // ── Drag & Drop ──────────────────────────────────────────────────────────
    @Override public void dragAndDrop(String src, String tgt) {
        locate(src).dragTo(locate(tgt));
    }

    // ── JavaScript ───────────────────────────────────────────────────────────
    @Override public Object executeScript(String script, String locator) {
        return page().evaluate(script, locate(locator).elementHandle());
    }

    // ── Frames ───────────────────────────────────────────────────────────────
    @Override public void switchToFrame(String frameLocator) {
        log.info("Switching to frame: {}", frameLocator);
        currentFrame = page().frameLocator(frameLocator);
    }

    @Override public void switchToMainFrame() {
        log.info("Switching back to main frame");
        currentFrame = null;
    }

    // ── Shadow DOM ───────────────────────────────────────────────────────────
    @Override public Object shadowRoot(String hostLocator) {
        return page().locator(hostLocator).elementHandle().getProperty("shadowRoot");
    }

    @Override public void clickInShadowDom(String host, String shadowSelector) {
        // Playwright pierces shadow DOM natively with >> css:light or locator chaining
        log.info("Click in ShadowDOM host={} selector={}", host, shadowSelector);
        page().locator(host + " >> " + shadowSelector).click();
    }

    @Override public String getTextFromShadowDom(String host, String shadowSelector) {
        return page().locator(host + " >> " + shadowSelector).innerText();
    }

    // ── Dialogs / Alerts ─────────────────────────────────────────────────────
    // Playwright handles dialogs via event listeners on the Page
    @Override public void acceptAlert() {
        page().onDialog(dialog -> {
            log.info("Accepting dialog: {}", dialog.message());
            dialog.accept();
        });
    }

    @Override public void dismissAlert() {
        page().onDialog(dialog -> {
            log.info("Dismissing dialog: {}", dialog.message());
            dialog.dismiss();
        });
    }

    @Override public void typeInAlert(String text) {
        page().onDialog(dialog -> dialog.accept(text));
    }

    @Override public String getAlertText() {
        final String[] msg = {""};
        page().onDialog(dialog -> { msg[0] = dialog.message(); dialog.accept(); });
        return msg[0];
    }

    // ── New Tab / Window ─────────────────────────────────────────────────────
    @Override public void switchToNewTab() {
        BrowserContext context = driverManager.getContext();
        List<Page> pages = context.pages();
        if (pages.isEmpty()) {
            throw new IllegalStateException("No tabs available to switch.");
        }
        // Switch to last opened tab
        Page newPage = pages.get(pages.size() - 1);
        TL_CURRENT_PAGE.set(newPage);
    }

    @Override public void closeCurrentTab() {
        Page currentPage = page();
        currentPage.close();
        TL_CURRENT_PAGE.remove();

        List<Page> pages = driverManager.getContext().pages();
        if (!pages.isEmpty()) {
            TL_CURRENT_PAGE.set(pages.get(pages.size() - 1));
        }
    }

    @Override public void switchToTabByIndex(int index) {
        List<Page> pages = driverManager.getContext().pages();
        if (index < 0 || index >= pages.size()) {
            throw new IllegalArgumentException("Tab index out of range: " + index);
        }
        TL_CURRENT_PAGE.set(pages.get(index));
    }

    // ThreadLocal to allow tab switching
    private static final ThreadLocal<Page> TL_CURRENT_PAGE = new ThreadLocal<>();
}

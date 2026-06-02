package com.automation.framework.abstractions;

import java.util.List;

/**
 * All UI interaction methods abstracted.
 * Same method signatures work for both Playwright and Selenium.
 */
public interface IWebActions {

    // ── Navigation ──────────────────────────────────────────────
    void navigateTo(String url);
    void goBack();
    void goForward();
    void refresh();

    // ── Basic Interactions ────────────────────────────────────────
    void click(String locator);
    void doubleClick(String locator);
    void rightClick(String locator);
    void type(String locator, String text);
    void clearAndType(String locator, String text);
    void pressKey(String locator, String key);
    void hover(String locator);

    // ── Form Elements ─────────────────────────────────────────────
    void selectByText(String locator, String text);
    void selectByValue(String locator, String value);
    void selectByIndex(String locator, int index);
    void checkCheckbox(String locator);
    void uncheckCheckbox(String locator);
    boolean isChecked(String locator);

    // ── Reading Values ────────────────────────────────────────────
    String getText(String locator);
    String getAttribute(String locator, String attribute);
    String getInputValue(String locator);
    List<String> getAllTexts(String locator);

    // ── Visibility ────────────────────────────────────────────────
    boolean isVisible(String locator);
    boolean isEnabled(String locator);
    boolean isHidden(String locator);
    int getElementCount(String locator);

    // ── Scroll ────────────────────────────────────────────────────
    void scrollToElement(String locator);
    void scrollToTop();
    void scrollToBottom();

    // ── File Upload ───────────────────────────────────────────────
    void uploadFile(String locator, String filePath);

    // ── Drag & Drop ───────────────────────────────────────────────
    void dragAndDrop(String sourceLocator, String targetLocator);

    // ── JavaScript ────────────────────────────────────────────────
    Object executeScript(String script, String locator);

    // ── Frames ────────────────────────────────────────────────────
    void switchToFrame(String frameLocator);
    void switchToMainFrame();

    // ── Shadow DOM ───────────────────────────────────────────────
    Object shadowRoot(String hostLocator);
    void clickInShadowDom(String hostLocator, String shadowLocator);
    String getTextFromShadowDom(String hostLocator, String shadowLocator);

    // ── Dialogs/Alerts ────────────────────────────────────────────
    void acceptAlert();
    void dismissAlert();
    void typeInAlert(String text);
    String getAlertText();

    // ── New Tab/Window ───────────────────────────────────────────
    void switchToNewTab();
    void closeCurrentTab();
    void switchToTabByIndex(int index);
}

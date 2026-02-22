package com.playwrightAutomation.BaseUtils;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.KeyboardModifier;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebInteractions {
    private Page page;
    private Waits waits;

    public void setPage(Page page, Waits waits) {
        this.page = page;
        this.waits = waits;
    }

    public void clickOn(String selector) {
        waits.waitForElement(selector);
        find(selector).click();
        waits.waitForPageLoad();
    }

    public void setValue(String selector, String value) {
        find(selector).fill(value);
    }

    public String getText(String selector) {
        return find(selector).textContent();
    }

    public String getText(Locator locator) {
        return locator.textContent();
    }

    public String getText(String selector, int nthIndex) {
        return findNth(selector,nthIndex).textContent();
    }

    public boolean isElementDisplayed(String selector) {
        return page.isVisible(selector);
    }

    public void selectByVisibleText(String selector, String text) {
        find(selector).selectOption(new SelectOption().setLabel(text));
    }

    public void selectByValue(String selector, String value) {
        find(selector).selectOption( value);
    }

    public void selectByIndex(String selector, int index) {
        waits.waitForElement(selector);
        List<String> values = page.locator(selector + " option").all().stream().map(e -> e.getAttribute("value")).collect(Collectors.toList());
        if (index >= 0 && index < values.size()) {
            page.selectOption(selector, values.get(index));
        }
    }

    public String getSelectedOptionText(String selector) {
        waits.waitForElement(selector);
        Locator selected = page.locator(selector + " option:checked");
        return selected.textContent();
    }

    public String getSelectedOptionValue(String selector) {
        waits.waitForElement(selector);
        Locator selected = page.locator(selector + " option:checked");
        return selected.getAttribute("value");
    }

    public boolean isElementEnabled(String selector) {
        return find(selector).isEnabled();
    }

    public boolean isElementSelected(String selector) {
        return find(selector).isChecked();
    }

    public boolean isElementNotDisplayed(String selector) {
        return !page.isVisible(selector);
    }

    public void clearField(String selector) {
        find(selector).fill( "");
    }

    public void submitForm(String selector) {
        waits.waitForElement(selector);
        page.locator(selector).evaluate("el => el.form.submit()");
    }

    public String getAttribute(String selector, String attributeName) {
        return find(selector).getAttribute(attributeName);
    }

    public void switchToFrame(String selector) {
        Frame targetFrame = null;
        for (Frame f : page.frames()) {
            if (f.name().equals(selector) || f.url().contains(selector)) {
                targetFrame = f;
                break;
            }
        }
        if (targetFrame != null) {
            this.page = targetFrame.page();
        }
    }

    public void switchToDefaultContent() {
        // Playwright always operates in the main frame unless switched
        this.page = page.context().pages().get(0);
    }

    public void switchToWindow(int windowIndex) {
        List<Page> pages = page.context().pages();
        if (windowIndex >= 0 && windowIndex < pages.size()) {
            this.page = pages.get(windowIndex);
        }
    }

    public void acceptAlert() {
        page.onceDialog(dialog -> dialog.accept());
    }

    public void performMouseRightClickOnElement(String selector) {
        find(selector).click(new Locator.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
    }

    public void typeStringWithKeyPress(String text) {
        page.keyboard().type(text);
    }

    public void typeStringWithKeyPress(String selector, String text) {
        find(selector).click();
        page.keyboard().type(text);
    }

    public void pressDownArrow() {
        page.keyboard().press("ArrowDown");
    }

    public void pressEnter() {
        page.keyboard().press("Enter");
    }

    public void openLinkInNewWindow(String linkSelector) {
        page.click(linkSelector, new Page.ClickOptions().setModifiers(Arrays.asList(KeyboardModifier.CONTROL)));
    }

    public void openLinkInNewTab(String linkSelector) {
        page.click(linkSelector, new Page.ClickOptions().setModifiers(Arrays.asList(KeyboardModifier.CONTROL)));
    }

    public String getCurrentPageURL() {
        return page.url();
    }

    public void verifyNewTabLoadedWithURL(String url) {
        List<Page> pages = page.context().pages();
        Page newTab = pages.get(pages.size() - 1);
        String actualUrl = newTab.url();
        if (!(url.contains("http://") || url.contains("https://"))) {
            url = "BASE_URL" + url;
        }
        if (actualUrl == null || !actualUrl.contains(url)) {
            throw new AssertionError("New tab URL does not contain expected URL: " + url);
        }
    }

    public void hoverOver(String selector) {
        find(selector).hover();
    }

    public void scrollIntoView(String selector) {
        find(selector).scrollIntoViewIfNeeded();
    }

    public String getTitle() {
        waits.waitForPageLoad();
        return page.title();
    }

    public List<String> getAllWebElements(String selector) {
        return page.locator(selector).allTextContents();
    }

    public void openLinkInNewIncognitoTab(String linkSelector) {
        page.click(linkSelector, new Page.ClickOptions().setModifiers(Arrays.asList(KeyboardModifier.CONTROL)));
    }

    public void clickOnCopyLinkAdress(String s) {
        // Clipboard and OS-level actions are not directly supported in Playwright Java
        // You may need to use Java's Toolkit for clipboard, but Playwright cannot interact with browser context menu
        // The following methods are omitted or would need a custom implementation
    }

    public void validateClipboardData(String s) {
        //no clipboard support in playwright java
    }

    public void takeScreenshot(String filePath) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
    }

    public String getAttributeValue(String selector, String attributeName) {
        return page.getAttribute(selector, attributeName).trim();
    }

    public String getAttributeValue(Locator locator, String attributeName) {
        return locator.getAttribute(attributeName).trim();
    }

    public String getAttributeValue(String selector,int index, String attributeName) {
        return  findNth(selector,index).getAttribute(attributeName).trim();
    }

    public void hoverAndClick(String listPageSearchBox) {
        waits.waitForElement(listPageSearchBox);
        page.hover(listPageSearchBox);
        page.click(listPageSearchBox);
    }

    public String getInnerText(String locator){
        waits.waitForElement(locator);
        return page.locator(locator).first().innerText();
    }

    public String getInnerText(Locator locator){
        return locator.innerText();
    }

    public void clickUsingJavaScript(String locator){
        waits.waitForElement(locator);
        page.locator(locator).evaluate("element => element.click()");
    }

    public void rightClickUsingJavaScript(String locator){
        waits.waitForElement(locator);
        page.locator(locator).evaluate("element => element.dispatchEvent(new MouseEvent('contextmenu', {bubbles: true, cancelable: true, view: window}))");
    }

    public List<String> getAllInnerTexts(String locator){
        waits.waitForElement(locator);
        return page.locator(locator).allInnerTexts();
    }

    // Clicks on the element
    public void doubleClick(String selector) {
        waits.waitForElement(selector);
        find(selector).dblclick();
    }

    // Right-clicks on the element
    public void rightClick(String selector) {
        waits.waitForElement(selector);
        find(selector).click(new Locator.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
    }

    // Focuses the element
    public void focus(String selector) {
        waits.waitForElement(selector);
        find(selector).focus();
    }

    // Blurs the element
    public void blur(String selector) {
        waits.waitForElement(selector);
        find(selector).evaluate("el => el.blur()");
    }

    // Checks a checkbox or radio button
    public void check(String selector) {
        waits.waitForElement(selector);
        find(selector).check();
    }

    // Unchecks a checkbox
    public void uncheck(String selector) {
        waits.waitForElement(selector);
        find(selector).uncheck();
    }

    // Drags an element to a target selector
    public void dragAndDrop(String sourceSelector, String targetSelector) {
        waits.waitForElement(sourceSelector);
        waits.waitForElement(targetSelector);
        find(sourceSelector).dragTo(find(targetSelector));
    }

    // Uploads a file to an input[type="file"]
    public void uploadFile(String selector, String filePath) {
        waits.waitForElement(selector);
        find(selector).setInputFiles(Paths.get(filePath));
    }

    // Clears file input
    public void clearFileInput(String selector) {
        waits.waitForElement(selector);
        find(selector).setInputFiles(new java.nio.file.Path[]{});
    }

    // Presses a key on the element
    public void pressKey(String selector, String key) {
        waits.waitForElement(selector);
        find(selector).press(key);
    }

    // Types text into the element
    public void type(String selector, String text) {
        waits.waitForElement(selector);
        find(selector).type(text);
    }

    // Evaluates JavaScript in the context of the element
    public Object evaluate(String selector, String script) {
        waits.waitForElement(selector);
        return find(selector).evaluate(script);
    }

    // Waits for a selector to be attached to the DOM
    public void waitForSelector(String selector) {
        page.waitForSelector(selector);
    }

    // Waits for a selector to be detached from the DOM
    public void waitForSelectorDetached(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    // Waits for a function to return true
    public void waitForFunction(String script) {
        page.waitForFunction(script);
    }

    // Accepts the next dialog
    public void acceptNextDialog() {
        page.onceDialog(dialog -> dialog.accept());
    }

    // Dismisses the next dialog
    public void dismissNextDialog() {
        page.onceDialog(dialog -> dialog.dismiss());
    }

    // Gets the value of an input element
    public String getValue(String selector) {
        waits.waitForElement(selector);
        return find(selector).inputValue();
    }

    // Sets the value of an input element
    public void setInputValue(String selector, String value) {
        waits.waitForElement(selector);
        find(selector).fill(value);
    }

    // Scrolls to the element
    public void scrollTo(String selector) {
        waits.waitForElement(selector);
        find(selector).scrollIntoViewIfNeeded();
    }

    // Gets the bounding box of an element
    public com.microsoft.playwright.options.BoundingBox getBoundingBox(String selector) {
        waits.waitForElement(selector);
        return find(selector).boundingBox();
    }

    // Takes a full page screenshot
    public void takeFullPageScreenshot(String filePath) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)).setFullPage(true));
    }

    // Copies text to clipboard (requires browser support)
    public void copyToClipboard(String text) {
        page.evaluate("navigator.clipboard.writeText(arguments[0])", text);
    }

    // Pastes text from clipboard (requires browser support)
    public String pasteFromClipboard() {
        return (String) page.evaluate("return navigator.clipboard.readText()");
    }

    public Locator find(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(selector).first();
    }

    public Locator findAll(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(selector);
    }

    public Locator findNth(String selector, int index) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(selector).nth(index);
    }

    // --- Frame Handling ---

    /**
     * Switch to a frame by its name or id attribute.
     */
    public void switchToFrameByName(String name) {
        Frame frame = page.frame(name);
        if (frame == null) throw new IllegalArgumentException("No frame found with name: " + name);
        this.page = frame.page();
    }

    /**
     * Switch to a frame by its index (order on the page).
     */
    public void switchToFrameByIndex(int index) {
        List<Frame> frames = page.frames();
        if (index < 0 || index >= frames.size()) throw new IllegalArgumentException("Frame index out of bounds");
        this.page = frames.get(index).page();
    }

    /**
     * Interact with an element inside a frame by selector using frameLocator.
     * Example usage: interactWithElementInFrameBySelector("iframe#myframe", "#button")
     */
    public Locator interactWithElementInFrameBySelector(String frameSelector, String elementSelector) {
        return page.frameLocator(frameSelector).locator(elementSelector);
    }

    /**
     * Switch back to the main frame.
     */
    public void switchToMainFrame() {
        this.page = page.context().pages().get(0);
    }

    /**
     * Find an element inside a frame by frame name and selector.
     */
    public Locator findInFrame(String frameName, String selector) {
        Frame frame = page.frame(frameName);
        if (frame == null) throw new IllegalArgumentException("No frame found with name: " + frameName);
        return frame.locator(selector);
    }

    // --- Dialog/Alert Handling ---

    /**
     * Accepts the next prompt dialog with the given text.
     */
    public void acceptNextPrompt(String text) {
        page.onceDialog(dialog -> dialog.accept(text));
    }

    /**
     * Gets the message text of the next dialog (alert, confirm, or prompt).
     */
    public String getNextDialogMessage() {
        final String[] message = new String[1];
        page.onceDialog(dialog -> message[0] = dialog.message());
        return message[0];
    }

    // --- Shadow DOM Handling ---

    /**
     * Finds a shadow root element and returns a locator for an element inside it.
     */
    public Locator findInShadowRoot(String hostSelector, String shadowSelector) {
        Locator host = page.locator(hostSelector);
        return host.locator(":shadow=" + shadowSelector);
    }

    /**
     * Clicks an element inside a shadow root.
     */
    public void clickInShadowRoot(String hostSelector, String shadowSelector) {
        Locator shadowElement = findInShadowRoot(hostSelector, shadowSelector);
        shadowElement.click();
    }

    /**
     * Types text into an element inside a shadow root.
     */
    public void typeInShadowRoot(String hostSelector, String shadowSelector, String text) {
        Locator shadowElement = findInShadowRoot(hostSelector, shadowSelector);
        shadowElement.fill(text);
    }
}

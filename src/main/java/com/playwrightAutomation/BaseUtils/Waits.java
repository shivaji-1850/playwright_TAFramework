package com.playwrightAutomation.BaseUtils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Waits {
    private Page page;

    public Waits() {
        // Default constructor
    }
    public Waits(Page page) {
        setPage(page);
    }

    public void setPage(Page page) {
        this.page = page;
    }

    // Wait for the page to be fully loaded
    public void waitForPageLoad() {
        page.waitForLoadState();
    }

    // Wait for the page title to match exactly
    public void waitForTitleToAppear(String title) {
        page.waitForCondition(() -> page.title().equals(title));
    }

    // Wait for an element to be visible using a selector string
    public void waitForElement(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    // Wait for at least one element matching selector to be present in DOM
    public void waitFor(String selector) {
        page.waitForSelector(selector);
    }

    // Wait for a Locator to be visible
    public void waitFor(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    // Wait for a Locator to be detached (not attached to DOM)
    public void waitForElementNotToBeStale(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
    }

    public void waitForElementNotToBeStale(String Locator) {
        page.locator(Locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
    }

    // Wait for URL to contain a substring
    public void waitForURL(String urlPart) {
        page.waitForURL("**" + urlPart + "**");
    }

    // Wait for an element to be enabled and visible (clickable)
    public void waitForElementToBeClickable(String selector) {
        Locator locator = page.locator(selector);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
        // Playwright does not have a direct 'clickable' state, but visible+enabled is sufficient
    }

    // Wait for the number of open pages (tabs) to be a certain number
    // Playwright does not manage tabs in the same way, so this is omitted or can be handled externally

    // Wait for all text to appear on the page
    public void waitForAllTextToAppear(String text) {
        page.waitForSelector("xpath=//*[contains(text(),'" + text + "')]");
    }
}

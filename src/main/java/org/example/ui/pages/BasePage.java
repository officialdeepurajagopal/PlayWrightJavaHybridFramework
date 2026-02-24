package org.example.ui.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

public class BasePage {
    protected static Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void navigateTo(String url){
        page.navigate(url);
    }

    public static ElementHandle getElement(String locator) {
        return page.querySelector(locator);
    }

    public static void clickElement(String locator) {
        getElement(locator).click();
    }

    public static void typeText(String locator, String text) {
        getElement(locator).fill(text);
    }

    public static boolean validateElementVisible(String locator) {
        return getElement(locator).isVisible();
    }

}


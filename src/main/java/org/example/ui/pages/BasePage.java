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

    public static void clickElement(ElementHandle locator) {
        locator.click();
    }

    public static void typeText(ElementHandle locator, String text) {
        locator.fill(text);
    }

    public static boolean validateElementVisible(ElementHandle locator) {
        return locator.isVisible();
    }

}


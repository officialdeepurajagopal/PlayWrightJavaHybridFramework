package org.example.ui.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    static Page page;

    public HomePage(Page page) {
        super(page);
        this.page = page;
    }

    private static ElementHandle signOutButton() {
        return page.querySelector("//button[contains(text(), 'Sign Out')]");
    }

    public void logout() {
        clickElement(signOutButton());
    }
}

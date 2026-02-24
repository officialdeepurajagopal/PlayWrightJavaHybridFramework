package org.example.ui.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage{

    public HomePage(Page page) {
        super(page);
    }

    public void logout(){
        clickElement("//button[contains(text(), 'Sign Out')]");
    }
}

package org.example.ui.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    static Page page;

    public LoginPage(Page page) {
        super(page);
        this.page = page;
    }

    public ElementHandle inputUserName(String locator){
        return getElement("#email");
    }

    public ElementHandle inputPassword(String locator){
        return getElement("#password");
    }

    public void enterCredentials(String username, String password){
        typeText("#userEmail", username);
        typeText("#userPassword", password);
    }

    public void clickLoginButton(){
        clickElement("#login");
        log.info("Clicked on login button");
    }

    public boolean isLoginSuccessful(){
        return validateElementVisible("//button[contains(text(), 'Sign Out')]");
    }

}

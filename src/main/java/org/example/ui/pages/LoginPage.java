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

    private static ElementHandle inputUserName(){
        return page.querySelector("#userEmail");
    }

    public static ElementHandle inputPassword(){
        return page.querySelector("#userPassword");
    }

    public ElementHandle loginButton(){
        return page.querySelector("#login");
    }

    public ElementHandle registerHereButton(){
        return page.querySelector("//a[contains(text(),'Register here')]");
    }

    public ElementHandle signOutButton(){
        return page.querySelector("//button[contains(text(), 'Sign Out')]");
    }

    public void enterCredentials(String username, String password){
        typeText(inputUserName(), username);
        typeText(inputPassword(), password);
    }

    public void clickLoginButton(){
        clickElement(loginButton());
        log.info("Clicked on login button");
    }

    public void clickRegisterLink(){
        clickElement(registerHereButton());
        log.info("Clicked on register link");
    }

    public boolean isLoginSuccessful(){
        return validateElementVisible(signOutButton());
    }

}

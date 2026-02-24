package org.example.ui.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.example.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class RegistrationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(RegistrationPage.class);
    static Page page;

    public RegistrationPage(Page page) {
        super(page);
        this.page = page;
    }

    public void enterRegistrationDetails(HashMap<String, String> registrationData){
        typeText("#firstName", registrationData.get("firstName"));
        typeText("#lastName", registrationData.get("lastname"));
        typeText("#userEmail", registrationData.get("email"));
        typeText("#userMobile", registrationData.get("phone"));
        typeText("#userPassword", registrationData.get("password"));
        typeText("#confirmPassword", registrationData.get("password"));
        log.info("Entered registration details");
    }

    public void clickAgeCheckbox(){
        page.click("//input[@type='checkbox']");
        log.info("Clicked on age consent checkbox");
    }

    public void clickRegisterButton(){
        page.click("//input[@type='submit']");
        log.info("Clicked on register button");
    }

    public boolean validateRegistrationSuccess(){
        return validateElementVisible("//h1[contains(text(), 'Account Created Successfully')]");
    }

}

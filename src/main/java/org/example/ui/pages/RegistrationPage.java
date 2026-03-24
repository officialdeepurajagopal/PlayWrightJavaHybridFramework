package org.example.ui.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
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

    private static ElementHandle inputFirstName() {
        return page.querySelector("#firstName");
    }

    private static ElementHandle inputLastName() {
        return page.querySelector("#lastName");
    }

    private static ElementHandle inputEmail() {
        return page.querySelector("#userEmail");
    }

    private static ElementHandle inputMobile() {
        return page.querySelector("#userMobile");
    }

    private static ElementHandle inputPassword() {
        return page.querySelector("#userPassword");
    }

    private static ElementHandle inputConfirmPassword() {
        return page.querySelector("#confirmPassword");
    }

    private static ElementHandle ageCheckbox() {
        return page.querySelector("//input[@type='checkbox']");
    }

    private static ElementHandle registerButton() {
        return page.querySelector("//input[@type='submit']");
    }

    private static ElementHandle registrationSuccessMessage() {
        return page.querySelector("//h1[contains(text(), 'Account Created Successfully')]");
    }

    public void enterRegistrationDetails(HashMap<String, String> registrationData) {
        typeText(inputFirstName(), registrationData.get("firstName"));
        typeText(inputLastName(), registrationData.get("lastname"));
        typeText(inputEmail(), registrationData.get("email"));
        typeText(inputMobile(), registrationData.get("phone"));
        typeText(inputPassword(), registrationData.get("password"));
        typeText(inputConfirmPassword(), registrationData.get("password"));
        log.info("Entered registration details");
    }

    public void clickAgeCheckbox() {
        clickElement(ageCheckbox());
        log.info("Clicked on age consent checkbox");
    }

    public void clickRegisterButton() {
        clickElement(registerButton());
        log.info("Clicked on register button");
    }

    public boolean validateRegistrationSuccess() {
        return validateElementVisible(registrationSuccessMessage());
    }

}

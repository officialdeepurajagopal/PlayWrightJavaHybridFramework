package org.example.tests.UITests;

import com.microsoft.playwright.*;
import org.example.tests.Common.BaseTest;
import org.example.ui.pages.HomePage;
import org.example.ui.pages.LoginPage;
import org.example.ui.pages.RegistrationPage;
import org.example.utils.Utils;
import org.testng.annotations.*;

public class UITest extends BaseTest {
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private HomePage homePage;

    @Test(groups = {"uitest"})
    public void loginTest() {
        loginPage = new LoginPage(page);
        homePage = new HomePage(page);
        loginPage.navigateTo(urlToLaunch);
        loginPage.enterCredentials("officialdeepurajagopal@gmail.com", "Alliswell@2026");
        loginPage.clickLoginButton();
        assert loginPage.isLoginSuccessful() : "Login was not successful";
        homePage.logout();
    }

    @Test(groups = {"uitest"})
    public void testRegistration() {
        loginPage = new LoginPage(page);
        loginPage.navigateTo(urlToLaunch);
        loginPage.clickRegisterLink();
        registrationPage = new RegistrationPage(page);
        registrationPage.enterRegistrationDetails(Utils.generateRegistrationData());
        registrationPage.clickAgeCheckbox();
        registrationPage.clickRegisterButton();
        assert registrationPage.validateRegistrationSuccess() : "Registration was not successful";

    }




}


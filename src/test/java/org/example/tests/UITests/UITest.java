package org.example.tests.UITests;

import com.microsoft.playwright.*;
import org.example.tests.Common.BaseTest;
import org.example.ui.pages.BasePage;
import org.example.ui.pages.LoginPage;
import org.testng.annotations.*;

public class UITest extends BaseTest {
    private LoginPage loginPage;

    @Test(groups = {"uitest"})
    public void loginTest() {
        loginPage = new LoginPage(page);
        loginPage.navigateTo(urlToLaunch);
        loginPage.enterCredentials("officialdeepurajagopal@gmail.com", "Alliswell@2026");
        loginPage.clickLoginButton();
        assert loginPage.isLoginSuccessful() : "Login was not successful";
    }


}


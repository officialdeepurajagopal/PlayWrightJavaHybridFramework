package org.example.tests;

import com.microsoft.playwright.*;
import org.example.ui.BasePage;
import org.testng.annotations.*;

public class UITest extends BaseTest {
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private BasePage basePage;

    @BeforeClass
    public void setUpUI() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        basePage = new BasePage(page);
    }

    @Test
    public void sampleUITest() {
        page.navigate("https://example.com");
        // Add assertions and UI actions
    }

    @AfterClass
    public void tearDownUI() {
        browser.close();
        playwright.close();
    }
}


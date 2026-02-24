package org.example.tests.Common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.example.ApiService.ApiClient;
import org.example.utils.ConfigLoader;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Arrays;

public class BaseTest {

    public Playwright playwright;
    public Browser browser;
    public Page page;
    public ApiClient client;
    public String urlToLaunch;
    public ObjectMapper objectMapper = new ObjectMapper();

    @BeforeSuite(alwaysRun = true)
    public void setup() {
        String env = System.getProperty("env", "dev");
        ConfigLoader.load(env);
    }

    @BeforeClass(alwaysRun = true)
    public void setUpTest() {
        // Determine test type by inspecting @Test group annotations on the actual subclass
        boolean isApiTest = false;
        boolean isUiTest = false;

        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Test testAnnotation = method.getAnnotation(Test.class);
            if (testAnnotation != null && testAnnotation.groups() != null) {
                String[] groups = testAnnotation.groups();
                if (Arrays.asList(groups).contains("apitest")) {
                    isApiTest = true;
                    break;
                } else if (Arrays.asList(groups).contains("uitest")) {
                    isUiTest = true;
                    break;
                }
            }
        }

        // Set up based on test type
        if (isApiTest) {
            String apiBaseUrl = ConfigLoader.get("apiBaseUrl");
            System.out.println("Loaded apiBaseUrl: " + apiBaseUrl);
            client = new ApiClient(apiBaseUrl);
        } else if (isUiTest) {
            playwright = Playwright.create();
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setSlowMo(1000);  // Slow down by 1 second to see actions
            browser = playwright.webkit().launch(options);
            urlToLaunch = ConfigLoader.get("appUrl");
            System.out.println("Loaded urlToLaunch: " + urlToLaunch);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void openNewPage() {
        if (browser != null) {
            page = browser.newPage();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void closePage() {
        if (page != null) {
            page.close();
            page = null;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownUI() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}

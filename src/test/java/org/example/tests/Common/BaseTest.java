package org.example.tests.Common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.example.ApiService.ApiClient;
import org.example.utils.ConfigLoader;
import org.testng.ITestContext;
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
    public void setUpTest(ITestContext context) {
        // Check groups from test methods in the class to determine test type
        boolean isApiTest = false;
        boolean isUiTest = false;

        // First check if groups are specified in the context (from -Dgroups parameter)
        String[] includedGroups = context.getIncludedGroups();

        if (includedGroups != null && includedGroups.length > 0) {
            for (String group : includedGroups) {
                if ("apitest".equals(group)) {
                    isApiTest = true;
                    break;
                } else if ("uitest".equals(group)) {
                    isUiTest = true;
                    break;
                }
            }
        }

        // If no groups from context, check the test methods in the class for group annotations
        if (!isApiTest && !isUiTest) {
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
            page = browser.newPage();
            urlToLaunch = ConfigLoader.get("appUrl");
            System.out.println("Loaded urlToLaunch: " + urlToLaunch);
        }
    }


    @AfterClass(alwaysRun = true)
    public void tearDownUI() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}


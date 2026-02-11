package org.example.tests;

import org.example.utils.ConfigLoader;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setup() {
        String env = System.getProperty("env", "dev");
        ConfigLoader.load(env);
    }
}


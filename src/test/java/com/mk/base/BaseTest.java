package com.mk.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.mk.pages.DashboardPage;
import com.mk.pages.LoginPage;

public class BaseTest {
    public WebDriver driver;
    protected static Properties properties;
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;

    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(BaseTest.class);


    public void loadProperty() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            properties.load(fis);
            logger.info("Config properties loaded successfully.");
        } catch (IOException e) {
            logger.error("Failed to load config properties.", e);
            throw new RuntimeException("Could not load configuration file.");
        }
    }
}

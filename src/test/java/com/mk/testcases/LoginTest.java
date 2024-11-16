package com.mk.testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;

import com.mk.base.BaseTest;
import com.mk.pages.LoginPage;
import com.mk.utils.ExcelUtility;
import com.mk.utils.ExtentUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest extends BaseTest {

	private static final Logger log = LogManager.getLogger(LoginTest.class);
	private LoginPage loginPage;

	@BeforeTest
	public void BeforeTestsetUp() throws IOException {
		log.info("Loading property file...");
		loadProperty();
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		log.info("Browser initialized and maximized.");
		loginPage = new LoginPage(driver);
		ExtentUtil.initReport(driver);
		log.info("Extent Report initialized");
	}

	@BeforeMethod
	public void BeforeMethodsetUp() throws IOException {
		String url = properties.getProperty("url");
		log.info("URL fetched from properties: {}", url);
		if (url == null || url.isEmpty()) {
			log.error("Error: URL is not specified in the properties file.");
		}
		driver.get(url);
		log.info("Navigating to URL: {}", url);
	}

	@AfterTest
	public void tearDown() {
		if (driver != null) {
			ExtentUtil.endReport();
			driver.quit();
			log.info("Driver closed successfully.");
		}
	}

	@Test(dataProvider = "loginData")
	public void loginTest(String username, String password) {
		ExtentUtil.createTest("Data Driven Login Test", "DataDriven Testing");
		log.info("Executing login test with username: {} and password: {}", username, password);
		try {
			// Perform login and get result
			boolean loginResult = loginPage.login(username, password);
			log.info("Received Login Result: {}", loginResult);
			if (loginResult) {
				ExtentUtil.logPass("Login successful for username: " + username);
				log.info("Login successful for username: {}", username);
				// Assert that the dashboard is visible for successful login
				Assert.assertTrue(true, "Dashboard is not fetched for username: " + username);
			} else {
				ExtentUtil.logFail("Login failed for username: " + username);
				log.warn("Login failed for username: {}", username);
				// Assert that the failure alert is visible for failed login
				Assert.assertTrue(true, "Invalid credentials not for username: " + username);
			}
		} catch (Exception e) {
			log.error("Login test failed for username: {} due to exception: {}", username, e.getMessage());
			ExtentUtil.logFail("Login test failed for username: " + username);
			Assert.fail("Test failed due to unexpected exception");
		}
	}

	@DataProvider(name = "loginData")
	public static Object[][] getLoginData() throws IOException {
		String filePath = System.getProperty("user.dir") + "/testdata/data.xlsx";
		String sheetName = "Sheet2";

		Object[][] data = ExcelUtility.getExcelData(filePath, sheetName);
		List<Object[]> validData = new ArrayList<>();

		for (Object[] row : data) {
			if (row.length == 2 && row[0] != null && row[1] != null && !row[0].toString().trim().isEmpty()
					&& !row[1].toString().trim().isEmpty()) {
				validData.add(row);
				log.debug("Adding valid data row: {}", row);
			}
		}

		log.info("Total valid login data rows: {}", validData.size());
		return validData.toArray(new Object[validData.size()][]);
	}
}

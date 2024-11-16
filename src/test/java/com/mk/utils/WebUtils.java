package com.mk.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebUtils { // explicit wait reading from webUtils

	private WebDriver driver;
	private WebDriverWait wait;

	// Constructor to initialize WebDriver and set timeout
	public WebUtils(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	// Wait for element visibility
	public WebElement waitForVisibility(By quickLaunchLocator)  {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(quickLaunchLocator));
	}

	// Wait for element clickability
	public  WebElement waitForClickability(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	// Wait for visibility and send keys to the element
	public void waitForVisibilityAndSendKeys(By locator, String text) throws InterruptedException {
		WebElement element = waitForVisibility(locator);
		element.clear(); // Clear existing text
		element.sendKeys(text); // Send the specified text
	}

	// Wait for clickability and click on the element
	public  WebElement waitForClickabilityAndClick(By locator) {
		WebElement element = waitForClickability(locator);
		element.click(); // Click the element
		return element;
	}

	// Scroll to the element using JavaScript Executor with exception handling
	public  void scrollToElementJS(By locator) {

		// Wait until the element is visible
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		String jsScript = "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});"; 
		// Scroll to the center of the viewport
		((JavascriptExecutor) driver).executeScript(jsScript, element);

	}

	// Click on the element using JavaScript Executor
	public void clickJS(By locator) {
		WebElement element = driver.findElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element); // Execute script to click the element
	}

}

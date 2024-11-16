package com.mk.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentUtil {
    private static ExtentReports extent;
    private static ExtentTest test;
    private static WebDriver driver;
    private static String reportPath;

    public static void initReport(WebDriver webDriver) throws IOException {
    	driver = webDriver;
    	if (extent == null) {
             extent = new ExtentReports();
         }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        reportPath = System.getProperty("user.dir") + "/Reports/ExtentReport_" + timeStamp + ".html";
    
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", "Chrome");
    }

    public static void createTest(String testName, String testtype) {
        test = extent.createTest(testName).assignAuthor("Megha").assignCategory(testtype).assignDevice("Chrome");
    }

    public static void logPass(String message) {
        String base64Screenshot = getBase64Screenshot();
        Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build();
        if (test != null) {
        	 test.pass(message, media);  
        }
    }

    public static void logFail(String message) {
        String base64Screenshot = getBase64Screenshot();
        Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build();
        if (test != null) {
        test.fail(message, media);
        }else {
            System.out.println("Test is not initialized.");
        }
    }

    public static void logInfo(String message) {
        String base64Screenshot = getBase64Screenshot();
        Media media = MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build();
        if (test != null) {
        test.info(message, media);
        } else {
            System.out.println("Test is not initialized.");
        }
    }
    
    public static void logError(String message) {
    	test.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
    }

    public static String getBase64Screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }
    

    public static void endReport() {
        if (extent != null) {
            extent.flush();
            openReportInBrowser();
        }
    }

    // Method to open the report automatically after test execution
    private static void openReportInBrowser() {
        try {
            File reportFile = new File(reportPath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(reportFile.toURI());
            } else {
                System.out.println("Desktop is not supported. Please manually open the report: " + reportPath);
            }
        } catch (IOException e) {
            System.out.println("Error opening the report in the browser: " + e.getMessage());
        }
    }
}

package api.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentListenerClass implements ITestListener {

    private ExtentSparkReporter htmlReporter;//user interface/look & feel the report
    private ExtentReports reports;//common information
    private ExtentTest test;//entries for test

    // Configure Extent Report
    public void configureReport() {
     
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportName = "petStoreAutomationTestReport-" + timestamp + ".html";
        String reportPath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator + reportName;
        
        htmlReporter = new ExtentSparkReporter(reportPath);
        reports = new ExtentReports();
        reports.attachReporter(htmlReporter);

        reports.setSystemInfo("Machine", "TestPC1");
        reports.setSystemInfo("OS", "Windows 11");
        
        reports.setSystemInfo("User Name", "Sagar");

        htmlReporter.config().setDocumentTitle("Extent Listener Report");
        htmlReporter.config().setReportName("Automation Test Report");
        htmlReporter.config().setTheme(Theme.DARK);
    }

    public void onStart(ITestContext context) {
        configureReport();
        System.out.println("Extent Report Initialized...");
    }

    public void onFinish(ITestContext context) {
        System.out.println("Flushing Extent Report...");
        if (reports != null) {
            reports.flush();
        }
    }

    public void onTestStart(ITestResult result) {
        System.out.println("Starting Test: " + result.getName());
        test = reports.createTest(result.getName());
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
        test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - PASSED", ExtentColor.GREEN));
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());
        test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - FAILED", ExtentColor.RED));
        test.fail(result.getThrowable());

        // Capture screenshot if test fails
        String screenshotPath = System.getProperty("user.dir") + File.separator + "ScreenShots" + File.separator + result.getName() + ".png";
        
        try {
            WebDriver driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");
            if (driver != null) {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File destFile = new File(screenshotPath);
                FileUtils.copyFile(screenshot, destFile);

                // Attach Screenshot to Extent Report
                test.fail("Screenshot:"+ test.addScreenCaptureFromPath(screenshotPath));
            } else {
                System.out.println("WebDriver instance is null, cannot capture screenshot.");
            }
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
        test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - SKIPPED", ExtentColor.YELLOW));
        test.skip(result.getThrowable());
    }
}

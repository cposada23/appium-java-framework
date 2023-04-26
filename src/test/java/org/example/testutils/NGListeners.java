package org.example.testutils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import lombok.SneakyThrows;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class NGListeners extends AppiumUtils implements ITestListener {

    private ExtentTest test;

    private ExtentReports extentReports = ExtendReporterNG.getReporterObject();
    private AppiumDriver driver;
    @Override
    public void onTestStart(ITestResult result) {
        test = extentReports.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(result.getThrowable());
        try {
            driver = (AppiumDriver) result
                    .getTestClass()
                    .getRealClass()
                    .getField("driver")
                    .get(result.getInstance());
            test.addScreenCaptureFromPath(
                    getScreenshot(result.getMethod().getMethodName(), driver),
                    result.getMethod().getMethodName()
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
    }
}

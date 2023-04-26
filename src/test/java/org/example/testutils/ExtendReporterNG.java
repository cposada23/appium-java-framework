package org.example.testutils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
public class ExtendReporterNG {

    private static ExtentReports extentReports;
    public static ExtentReports getReporterObject() {
        String path = System.getProperty("user.dir") + "/reports/index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("Mobile Automation Results");
        reporter.config().setDocumentTitle("Test Result");

        extentReports = new ExtentReports();
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Tester", "Camilo");

        return extentReports;
    }

}

package com.automation.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportManager {

    private static final Logger log = LoggerFactory.getLogger(ReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TL_TEST = new ThreadLocal<>();

    public static void initReport() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportPath = "target/reports/ExtentReport_" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("Test Execution Report");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        log.info("Extent report initialized: {}", reportPath);
    }

    public static void createTest(String testName) {
        TL_TEST.set(extent.createTest(testName));
    }

    public static void pass(String message)  { TL_TEST.get().pass(message); }
    public static void fail(String message)  { TL_TEST.get().fail(message); }
    public static void info(String message)  { TL_TEST.get().info(message); }

    public static void failWithScreenshot(String message, String screenshotPath) {
        try {
            TL_TEST.get().fail(message,
                    com.aventstack.extentreports.MediaEntityBuilder
                            .createScreenCaptureFromPath(screenshotPath).build());
        } catch (Exception e) {
            log.error("Failed to attach screenshot to report", e);
        }
    }

    public static void flushReport() {
        if (extent != null) extent.flush();
    }
}

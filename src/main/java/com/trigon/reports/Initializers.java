package com.trigon.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.trigon.bean.ExtentPojo;
import com.trigon.bean.PropertiesPojo;
import com.trigon.bean.testenv.TestEnv;
import com.trigon.database.Database;
import com.trigon.exceptions.TrigonAsserts;
import com.trigon.utils.CommonUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedWriter;
import java.util.*;

public class Initializers {
    public static PropertiesPojo propertiesPojo = new PropertiesPojo();
    protected static ThreadLocal<List<String>> failAnalysisThread = new ThreadLocal<>();
    protected static ThreadLocal<List<String>> classFailAnalysisThread = new ThreadLocal<>();
    protected static ThreadLocal<List<String>> moduleFailAnalysisThread = new ThreadLocal<>();
    protected static ThreadLocal<TestEnv> envThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<List<LinkedHashMap<String, Object>>> dataTableCollectionApi = new ThreadLocal<>();
    protected static ThreadLocal<LinkedHashMap<String, Object>> dataTableMapApi = new ThreadLocal<>();
    protected TrigonAsserts sAssert = new TrigonAsserts();
    protected static String executionType = "local";
    protected static String pipelineExecution = "false";

    public static boolean customAPIReportStartFlag = false;
    public static BufferedWriter customAPIReport;
    public static List<String> customAPIHeaderData = new ArrayList<>();
    public static int customAPIReportHeaderSize = 2;

    protected static Hashtable<String, String> customReportStatusMap = new Hashtable();
    protected static Hashtable<Collection<?>, Collection<?>> customReportStatusMap1 = new Hashtable();

    public static List<String> mobileApps;
    public static List<String> webApps;

    public static ExtentReports extent = null;
    public static ThreadLocal<ExtentTest> extentTestNode = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentClassNode = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentMethodNode = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentScenarioNode = new ThreadLocal<>();
    public static List<String> apiCoverage = new ArrayList<>();
    public static int totalEndpoints = 0;
    public static String executedGitBranch = "NA";
    public static ExtentPojo extentPojo = null;
    public ExtentTest extentTest = null;

    //variables used for saving failure functionality for retried tests
    public static int sizeOfLogs=0;
    public static int indexTest=0;
    public static int indexClass=0;
    public static int indexMethod=0;
    public static String detailsOfLogs;

    protected static ThreadLocal<AndroidDriver<AndroidElement>> androidDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<IOSDriver<IOSElement>> iosDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<RemoteWebDriver> webDriverThreadLocal = new ThreadLocal<>();
    protected static CommonUtils commonUtils = new CommonUtils();
    protected static String getSuiteNameWithTime;
    protected static String platformType;
    protected static String appType;
    protected static String suiteParallel;
    protected static int totalTestModules;
    protected static String email_recipients = null;
    protected static String error_email_recipients = null;
    protected static String failure_email_recipients = null;
    protected static boolean failStatus = false;
    protected static boolean exceptionStatus = false;

    public static RemoteWebDriver browser() {
        return webDriverThreadLocal.get();
    }

    public static AndroidDriver<AndroidElement> android() {
        return androidDriverThreadLocal.get();
    }

    public static IOSDriver<IOSElement> ios() {
        return iosDriverThreadLocal.get();
    }

    public static TestEnv tEnv() {
        return envThreadLocal.get();
    }

    public static CommonUtils cUtils() {
        return commonUtils;
    }

    public static Database db = new Database();
}

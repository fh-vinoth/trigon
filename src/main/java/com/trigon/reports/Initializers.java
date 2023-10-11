package com.trigon.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.trigon.bean.ExtentPojo;
import com.trigon.bean.PropertiesPojo;
import com.trigon.bean.testenv.TestEnv;
import com.trigon.database.ADatabase;
import com.trigon.database.Database;
import com.trigon.exceptions.TrigonAsserts;
import com.trigon.tribot.GenerateSuiteForModules;
import com.trigon.utils.CommonUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
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

    protected static String grid_execution_local = "false";
    protected static String gps_location = "false";
    protected static String networkProfile = "Reset";
    protected static String customNetwork = "false";
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
    public static List<String> initFailedLogs;

    protected static ThreadLocal<AndroidDriver> androidDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<IOSDriver> iosDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<RemoteWebDriver> webDriverThreadLocal = new ThreadLocal<>();
    protected static CommonUtils commonUtils = new CommonUtils();
    protected static String getSuiteNameWithTime;
    protected static String getSuiteExecutionDate;
    protected static String reportPath;
    protected static String platformType;
    protected static String appType;
    protected static String suiteParallel;
    protected static int totalTestModules;
    protected static String email_recipients = null;
    protected static String error_email_recipients = null;
    protected static String failure_email_recipients = null;
    protected static boolean failStatus = false;
    protected static boolean exceptionStatus = false;
    protected static int reportModuleRun =0;
    protected static boolean execute = false;

    protected static ThreadLocal<List<String>> testCaseIDThread = new ThreadLocal<>();
    public static ThreadLocal<ArrayList> passedTCs = new ThreadLocal<>();
    public static ThreadLocal<HashMap<String,String>> failedTCs = new ThreadLocal<>();
    public static ThreadLocal<ArrayList> skippedTCs = new ThreadLocal<>();
    public static ThreadLocal<HashMap<String, Object>> resultTCs = new ThreadLocal<>();
    public static LinkedHashMap resultTCCollectionMap = new LinkedHashMap();

    public static String suiteRunId;
    public static RemoteWebDriver browser() {
        return webDriverThreadLocal.get();
    }

    public static AndroidDriver android() {
        return androidDriverThreadLocal.get();
    }

    public static IOSDriver ios() {
        return iosDriverThreadLocal.get();
    }

    public static TestEnv tEnv() {
        return envThreadLocal.get();
    }

    public static CommonUtils cUtils() {
        return commonUtils;
    }

    public static Database db = new Database();
    public static ADatabase adb = new ADatabase();
    public static GenerateSuiteForModules gs = new GenerateSuiteForModules();

    protected static int executionCount = 0;

}

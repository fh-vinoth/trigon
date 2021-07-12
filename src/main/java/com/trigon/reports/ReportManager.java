package com.trigon.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigon.bean.ExtentPojo;
import com.trigon.bean.PropertiesPojo;
import com.trigon.bean.TestMethodReporter;
import com.trigon.bean.testenv.TestEnv;
import com.trigon.database.Database;
import com.trigon.exceptions.TrigonAsserts;
import com.trigon.utils.CommonUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static com.trigon.testbase.TestInitialization.trigonPaths;
import static java.awt.Toolkit.getDefaultToolkit;
import static javax.imageio.ImageIO.write;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.FILE;


public class ReportManager {

    private static final Logger logger = LogManager.getLogger(ReportManager.class);
    public static PropertiesPojo propertiesPojo = new PropertiesPojo();
    protected static ThreadLocal<TestMethodReporter> testThreadMethodReporter = new ThreadLocal<>();
    protected static ThreadLocal<List<String>> failAnalysisThread = new ThreadLocal<>();
    protected static ThreadLocal<List<String>> classFailAnalysisThread = new ThreadLocal<>();
    protected static ThreadLocal<TestEnv> envThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<List<LinkedHashMap<String, Object>>> dataTableCollectionApi = new ThreadLocal<>();
    protected static ThreadLocal<LinkedHashMap<String, Object>> dataTableMapApi = new ThreadLocal<>();
    protected TrigonAsserts sAssert = new TrigonAsserts();
    protected static String executionType = "local";

    public static List<String> mobileApps = Arrays.asList("mobile","myt","d2s","fhapp","caapp","mypos","apos","fusionapp","digitalboard");
    public static List<String> webApps = Arrays.asList("web","caweb","fhweb","fhnative","fheatappy");

    public static ExtentReports extent = null;
    public static ThreadLocal<ExtentTest> extentTestNode = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentClassNode = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentMethodNode = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentScenarioNode = new ThreadLocal<>();
    public static ExtentPojo extentPojo = null;
    public ExtentTest extentTest = null;


    protected static ThreadLocal<AndroidDriver<AndroidElement>> androidDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<IOSDriver<IOSElement>> iosDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<RemoteWebDriver> webDriverThreadLocal = new ThreadLocal<>();
    protected static CommonUtils commonUtils = new CommonUtils();
    protected static String getSuiteNameWithTime;
    protected static String platformType;
    protected static String appType;
    protected static String suiteParallel;
    protected static int totalTestModules;
    protected static String email_recipients =null;
    protected static String error_email_recipients =null;
    protected static String failure_email_recipients =null;
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

    public static Database db;

    public void author_ScenarioName(String author, String scenario) {
        try{
            testThreadMethodReporter.get().setTestAuthor(author);
            testThreadMethodReporter.get().setTestScenarioName(scenario);
            extentMethodNode.get().assignAuthor(author);

        }catch (Exception e){
            captureException(e);
        }
    }
    public void author_ScenarioName(String author, String scenario, String verificationPoint) {
        try{
            testThreadMethodReporter.get().setTestAuthor(author);
            testThreadMethodReporter.get().setTestScenarioName(scenario);
            testThreadMethodReporter.get().setTestVerificationPoint(verificationPoint);
            extentMethodNode.get().assignAuthor(author);
        }catch (Exception e){
            captureException(e);
        }
    }
    public void author_ScenarioName(String author, LinkedHashMap tData) {
        try{
            testThreadMethodReporter.get().setTestAuthor(author);
            testThreadMethodReporter.get().setTestScenarioName(tData.get("Scenario").toString());
            extentMethodNode.get().assignAuthor(author);
        }catch (Exception e){
            captureException(e);
        }
    }

    public void testStatus(String status, String message) {
        try{
            testThreadMethodReporter.get().setTestStatus(status);
            if (status.equalsIgnoreCase("FAIL")) {
                failAnalysisThread.get().add(message);
            } else {

            }
        }catch (Exception e){
            captureException(e);
        }
    }

    public void testTearDown() {
        if (failAnalysisThread.get().size() > 0) {
            Assert.fail("Test Failed !! Look for above failures/exceptions and fix it !! ");
        }
    }
    
    
    public Map<String, Object> createJiraTicket(String bugSummary, String description) {
        String URI = "https://touch2success.atlassian.net/";
        Map<String, Object> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Basic ZnVzaW9uYm90QHRvdWNoMnN1Y2Nlc3MuY29tOnQ2TmVYem9QanBiTGczVko0V1ZPNDExMQ==");

        String projectId = "AUTOMATION";
        if (tEnv().getTestType().equalsIgnoreCase("API")) {
            projectId = propertiesPojo.getJira_api_proj();
        }
        if (tEnv().getTestType().equalsIgnoreCase("MYT")) {
            projectId = propertiesPojo.getJira_myt_proj();
        }
        if (tEnv().getTestType().equalsIgnoreCase("D2S")) {
            projectId = propertiesPojo.getJira_d2s_proj();
        }
        if (tEnv().getTestType().equalsIgnoreCase("FHApp")) {
            projectId = propertiesPojo.getJira_fhapp_proj();
        }
        if (tEnv().getTestType().equalsIgnoreCase("CA")) {
            projectId = propertiesPojo.getJira_ca_proj();
        }
        if (tEnv().getTestType().equalsIgnoreCase("FHWeb")) {
            projectId = propertiesPojo.getJira_fhweb_proj();
        }
        if (tEnv().getTestType().equalsIgnoreCase("FHNative")) {
            projectId = propertiesPojo.getJira_fhnative_proj();
        }

        String body = "{\n" +
                "    \"fields\": {\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"key\": \"" + projectId + "\"\n" +
                "       },\n" +
                "       \"summary\": \"" + bugSummary + "\",\n" +
                "       \"description\": \"" + description + "\",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \"Task\"\n" +
                "       }\n" +
                "   }\n" +
                "}";
        Map<String, Object> rep = new HashMap<>();
        try {
            RestAssured.baseURI = URI;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification requestSpecification = RestAssured.given().request().headers(headers).body(body);
            Response response = requestSpecification.post("rest/api/2/issue/").then().extract().response();
            rep = JsonFlattener.flattenAsMap(response.asString());
            logger.error("Jira Ticket Created , TicketID : " + rep.get("self").toString());
            failAnalysisThread.get().add("JIRA ID: +" + rep.get("self").toString() + "");
        } catch (Exception e) {
            logger.error("Failed to create Jira Ticket");
            captureException(e);
        }
        return rep;
    }

/*    public void logReport(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        if (!elementReportCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
            try {
                if (extentTestThreadLocal.get() != null) {
                    if (status.equalsIgnoreCase("PASS")) {
                        extentTestThreadLocal.get().pass(message);
                        logger.info(message);
                        testThreadMethodReporter.get().setTestStatus("PASSED");
                        //classWriter.get().name("Step_"+System.nanoTime()).value("PASS:"+message);
//                    testAction.add("count");
//                    individualTestStatus.add("PASSED");
                    } else if (status.equalsIgnoreCase("FAIL")) {
                        extentTestThreadLocal.get().fail(badge2 + message + badge5);
                        //testStatus1.add(message);
                        logger.error("****FAILED MESSAGE******" + message);
                        testThreadMethodReporter.get().setTestStatus("FAILED");
                        // classWriter.get().name("Step_"+System.nanoTime()).value("FAIL:"+message);
                        failAnalysisThread.get().add(message);
                        Assert.fail(message);
//                    testStatus1.add("<br/>");
//                    testAction.add("count");
                    } else if (status.equalsIgnoreCase("INFO")) {
                        extentTestThreadLocal.get().info(message);
                        logger.info(message);
                        //classWriter.get().name("Step_"+System.nanoTime()).value("INFO:"+message);
                    } else if (status.equalsIgnoreCase("SKIP")) {
                        extentTestThreadLocal.get().skip(message);
                        logger.warn(message);
                        testThreadMethodReporter.get().setTestStatus("SKIPPED");
                        // classWriter.get().name("Step_"+System.nanoTime()).value("SKIP:"+message);
                    }  else if (status.equalsIgnoreCase("WARN")) {
                        extentTestThreadLocal.get().warning(message);
                        logger.warn(message);
                        // classWriter.get().name("Step_"+System.nanoTime()).value("SKIP:"+message);
                    }else {
                        logger.error("Report Initialization is Failed");
                    }
                } else {
                    Assert.fail("Error!! Report Initialization failed!! please Set setScenarioName in Test");
                }
            } catch (Exception ie) {
                icaptureException(e);
            }

        }
    }*/

    public void logReport(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        if (!elementReportCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
            try {
                if (status.equalsIgnoreCase("PASS")) {
                    logger.info(message);
                    if(extentScenarioNode.get()!=null){
                        extentScenarioNode.get().pass(message);
                    }else if (extentMethodNode.get() != null) {
                        extentMethodNode.get().pass(message);
                    }
                    if (testThreadMethodReporter.get() != null) {
                        testThreadMethodReporter.get().setTestStatus("PASSED");
                    }
                } else if (status.equalsIgnoreCase("FAIL")) {
                    logger.error(message);
                    if(extentScenarioNode.get()!=null){
                        extentScenarioNode.get().fail(message);
                    }else if (extentMethodNode.get() != null) {
                        extentMethodNode.get().fail(message);
                    }
                    if (testThreadMethodReporter.get() != null) {
                        testThreadMethodReporter.get().setTestStatus("FAILED");
                    }
                    if (failAnalysisThread.get() != null) {
                        failAnalysisThread.get().add(message);
                    } else {
                        classFailAnalysisThread.get().add(message);
                    }
                } else if (status.equalsIgnoreCase("INFO")) {
                    logger.info(message);
                    if(extentScenarioNode.get()!=null){
                        extentScenarioNode.get().info(message);
                    }else if (extentMethodNode.get() != null) {
                        extentMethodNode.get().info(message);
                    }
                } else if (status.equalsIgnoreCase("SKIP")) {
                    logger.warn(message);
                    if(extentScenarioNode.get()!=null){
                        extentScenarioNode.get().skip(message);
                    }else if (extentMethodNode.get() != null) {
                        extentMethodNode.get().skip(message);
                    }
                    if (testThreadMethodReporter.get() != null) {
                        testThreadMethodReporter.get().setTestStatus("SKIPPED");
                    } else {
                    }

                } else if (status.equalsIgnoreCase("WARN")) {
                    logger.warn(message);
                    if(extentScenarioNode.get()!=null){
                        extentScenarioNode.get().warning(message);
                    }else if (extentMethodNode.get() != null) {
                        extentMethodNode.get().warning(message);
                    }
                } else {
                    logger.error("Report Initialization is Failed");
                }
            } catch (Exception e) {
                captureException(e);
            }

        }
    }

    protected void logJSON(String status, String message) {
        try {
            if ("api".equalsIgnoreCase(tEnv().getTestType())) {

                if (status.equalsIgnoreCase("PASS")) {
                    testThreadMethodReporter.get().setTestStatus("PASSED");
                    //classWriter.get().name("Step_"+System.nanoTime()).value("PASS:"+message);
                } else if (status.equalsIgnoreCase("FAIL")) {
                    testThreadMethodReporter.get().setTestStatus("FAILED");
                    // classWriter.get().name("Step_"+System.nanoTime()).value("FAIL:"+message);
                } else if (status.equalsIgnoreCase("INFO")) {
                    //classWriter.get().name("Step_"+System.nanoTime()).value("INFO:"+message);
                } else {
                    hardFail("Error!! Report Initialization failed!! please Set setScenarioName in Test");
                }
            }

        } catch (Exception e) {
            captureException(e);
        }
    }
/*    protected void logJSON(String status, String message) {

        Markup type = MarkupHelper.createCodeBlock(message, CodeLanguage.JSON);
//        if (message.startsWith("{") || message.startsWith("[")) {
//            type = MarkupHelper.createCodeBlock(message, CodeLanguage.JSON);
//        }
        try {
            if ("api".equalsIgnoreCase(tEnv().getTestType())) {
                if (extentTestThreadLocal.get() != null) {
                    if (status.equalsIgnoreCase("PASS")) {
                        extentTestThreadLocal.get().pass("<details><summary><font color=\"green\"><b>Click to open Response JSON</b></font></summary> " + type.getMarkup() + "</details>");
                        testThreadMethodReporter.get().setTestStatus("PASSED");
                        //classWriter.get().name("Step_"+System.nanoTime()).value("PASS:"+message);
                    } else if (status.equalsIgnoreCase("FAIL")) {
                        extentTestThreadLocal.get().fail("<details><summary><font color=\"green\"><b>Click to open Response JSON</b></font></summary>" + type.getMarkup() + "</details>");
                        testThreadMethodReporter.get().setTestStatus("FAILED");
                        // classWriter.get().name("Step_"+System.nanoTime()).value("FAIL:"+message);
                    } else if (status.equalsIgnoreCase("INFO")) {
                        extentTestThreadLocal.get().info("<details><summary><font color=\"green\"><b>Click to open Response JSON</b></font></summary> " + type.getMarkup() + "</details>");
                        //classWriter.get().name("Step_"+System.nanoTime()).value("INFO:"+message);
                    } else {
                        Assert.fail("Error!! Report Initialization failed!! please Set setScenarioName in Test");
                    }
                }
            }

            if (("web".equalsIgnoreCase(tEnv().getTestType())) || ("mobile".equalsIgnoreCase(tEnv().getTestType()))) {

//            apiLogStatus.add(status);
//            apiLogForWeb.add("<details><summary><font color=\"green\"><b>Click to open Response JSON</b></font></summary>" + type.getMarkup() + "</details>");

            }
        } catch (Exception e) {
            captureException(e);
        }
    }*/


    public void logApiReport(String status, String message) {
        if ("api".equalsIgnoreCase(tEnv().getTestType())) {
            logReport(status, message);
        }
    }


/*
    public void logReportWithScreenShot(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {

        if (!elementReportCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
            if (screenshot_on_failure.equalsIgnoreCase("true")) {
                if (status.equalsIgnoreCase("FAIL")) {
                    String Screenshot = screenshotDriver();
                    extentTestThreadLocal.get().fail(badge2 + message + badge5, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
//                        testStatus1.add(message);
//                        testStatus1.add("<br/>");
//                        testAction.add("count");
                    logger.error("****FAILED MESSAGE******" + message);
//                        individualTestStatus.add("FAILED");
                } else {
                    logReport(status, message);
                }

            } else if (screenshot_on_failure.equalsIgnoreCase("false")) {

                String Screenshot = screenshotDriver();

                if ((extentTestThreadLocal.get() != null) && (Screenshot != null)) {
                    if (status.equalsIgnoreCase("PASS")) {
                        extentTestThreadLocal.get().pass(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
                        //                            testAction.add("count");
//                            individualTestStatus.add("PASSED");
                    } else if (status.equalsIgnoreCase("FAIL")) {
                        extentTestThreadLocal.get().fail(badge2 + message + badge5, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
                        //                            testStatus1.add(message);
//                            testStatus1.add("<br/>");
//                            testAction.add("count");
//                            logger.error("****FAILED MESSAGE******" + message);
//                            individualTestStatus.add("FAILED");


                    } else if (status.equalsIgnoreCase("INFO")) {
                        extentTestThreadLocal.get().info(message).addScreenCaptureFromPath(Screenshot);
                    } else if (status.equalsIgnoreCase("SKIP")) {
                        extentTestThreadLocal.get().skip(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
                    } else {
                        Assert.fail("Error!! Report Initialization failed!! please Set setScenarioName in Test");
                    }
                }
            }
        }

    }
*/



    public void logReportWithScreenShot(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        try {
            if (!elementReportCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                String Screenshot = screenshotDriver();
                if ((Screenshot != null)) {
                    logReport(status, message);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }


    private String screenshotDriver() {
        File screenshot = null;
        String path = null;
        try {
            if (browser() != null) {
                screenshot = ((TakesScreenshot) browser())
                        .getScreenshotAs(FILE);
            } else if (android() != null) {
                screenshot = ((TakesScreenshot) android())
                        .getScreenshotAs(FILE);
            } else if (ios() != null) {
                screenshot = ((TakesScreenshot) ios())
                        .getScreenshotAs(FILE);
            }
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            String methodScreeenShotPath = trigonPaths.getScreenShotsPath()+ tEnv().getCurrentTestMethodName();
            tEnv().setScreenshotPath(new File(methodScreeenShotPath));

            path = methodScreeenShotPath + "/" + testThreadMethodReporter.get().getTestMethodName() + "" + "_" + "" + stackTrace[5].getMethodName() + "" + "_" + "[" + cUtils().getDateTimeStamp() + "]" + ".png";
            copyFile(screenshot, new File((path)));
        } catch (IOException e) {
            captureException(e);
        }

        return path;
    }

    private void fullScreenshot() {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            String path = trigonPaths.getScreenShotsPath() + "/" + testThreadMethodReporter.get().getTestMethodName() + "" + "_" + "" + stackTrace[5].getMethodName() + "" + "_" + "[" + cUtils().getDateTimeStamp() + "]" + ".png";
            Robot robot = null;
            robot = new Robot();
            Rectangle captureSize = new Rectangle(
                    getDefaultToolkit().getScreenSize());
            BufferedImage capture = robot.createScreenCapture(captureSize);
            write(capture, "png", new File(path));
        } catch (Exception e) {
            captureException(e);
        }
    }
    
    public void logStepAction(String message) {
       // dataToJSONStep("INFO",message);
        logReport("INFO",message);
    }


    public void logScenario(String ScenarioName) {
        logger.info("Scenario : " + ScenarioName);
        extentScenarioNode.set(extentMethodNode.get().createNode("<font color=\"#d0b2e6\">" + ScenarioName + "</font>"));
    }

    protected void customAssertEqualsApi(String actual, String expected, String testType) {
        try {
            if ("api".equalsIgnoreCase(testType)) {
                customAssertEquals(actual, expected);
            }
            if (("web".equalsIgnoreCase(testType)) || ("mobile".equalsIgnoreCase(testType))) {

                if (expected.equals(actual)) {

                } else {
                    sAssert.assertEquals(actual, expected);
               }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertEquals(String actual, String expected) {
        try {
            logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
            if (expected.equals(actual)) {
                logReport("PASS", "Actual Text:" + actual  + "Expected Exact Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReport("FAIL", "Actual Text:" + actual  + "Expected Exact Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertNotEquals(String actual, String expected) {
        try {
            logger.info("Verifying NOT Equals Actual : " + actual + " with Expected : " + expected + "");
            if (!(expected.equals(actual))) {
                logReport("PASS", "Actual Text:" + actual  + " Expected NOT EQUALS Text:" + expected);
            } else {
                logReport("FAIL", "Actual Text:" + actual  + " Expected NOT EQUALS Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialEquals(String actual, String expected) {
        logger.info("Verifying Partial Equals Actual : " + actual + " with Expected : " + expected + "");
        try {
            if (actual.contains(expected)) {
                logReport("PASS", "Actual Text:" + actual  + "Expected Partial Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReport("FAIL", "Actual Text:" + actual  + "Expected Partial Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertEquals(Collection<?> actual, Collection<?> expected) {
        logger.info("Collection Verifying  Actual : " + actual + " with Expected : " + expected + "");
        try {
            int actualValueSize = actual.size();
            int expectedValueSize = expected.size();

            List<Object> expectedlist = new ArrayList<>();
            for (Object expValue : expected) {
                expectedlist.add(expValue);
            }
            List<Object> actuallist = new ArrayList<>();
            for (Object actValue : actual) {
                actuallist.add(actValue);
            }
            if (expectedValueSize == actualValueSize) {
                if ((expectedlist.equals(actuallist))) {
                    logReport("PASS", "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "Expected Exact Text:" + expected  + "ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "Expected Exact Text:" + expected  + "ExpectedSize :" + expectedValueSize  + "Additional values in Actual List" + actuallist  + "Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched "  + "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "Expected Exact Text:" + expected  + "ExpectedSize :" + expectedValueSize  + "Additional values in Actual List" + actuallist  + "Additional values in Expected List" + expectedlist);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialEquals(Collection<?> actual, Collection<?> expected) {
        logger.info("Collection Verifying  Actual : " + actual + " with Expected : " + expected + "");
        try {
            int actualValueSize = actual.size();
            int expectedValueSize = expected.size();
            List<Object> expectedlist = new ArrayList<>();
            for (Object expValue : expected) {
                expectedlist.add(expValue);
            }
            List<Object> actuallist = new ArrayList<>();
            for (Object actValue : actual) {
                actuallist.add(actValue);
            }
            if (expectedValueSize == actualValueSize) {
                if ((expectedlist.contains(actuallist))) {
                    logReport("PASS", "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "Expected Partial Text:" + expected  + "ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "Expected Partial Text:" + expected  + "ExpectedSize :" + expectedValueSize  + "Additional values in Actual List" + actuallist  + "Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched "  + "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "  Expected Partial Text:" + expected  + "ExpectedSize :" + expectedValueSize  + "Additional values in Actual List" + actuallist  + "Additional values in Expected List" + expectedlist);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertNotEquals(Collection<?> actual, Collection<?> expected) {
        logger.info("Collection Asserting NOT Equals...");
        try {
            int actualValueSize = actual.size();
            int expectedValueSize = expected.size();
            List<Object> expectedlist = new ArrayList<>();
            for (Object expValue : expected) {
                expectedlist.add(expValue);
            }
            List<Object> actuallist = new ArrayList<>();
            for (Object actValue : actual) {
                actuallist.add(actValue);
            }
            if (expectedValueSize == actualValueSize) {
                if (!(expectedlist.equals(actuallist))) {
                    logReport("PASS", "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "  Expected NOT EQUALS Text:" + expected  + " ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "  Expected NOT EQUALS Text:" + expected  + " ExpectedSize :" + expectedValueSize  + " Additional values in Actual List" + actuallist  + " Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched "  + "Actual Text:" + actual  + "ActualSize :" + actualValueSize  + "  Expected NOT EQUALS Text:" + expected  + " ExpectedSize :" + expectedValueSize  + " Additional values in Actual List" + actuallist  + " Additional values in Expected List" + expectedlist);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertEqualsWithScreenShot(String actual, String expected) {
        logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
        try {
            if (expected.equals(actual)) {
                logReportWithScreenShot("PASS", "Actual Text:" + actual  + "Expected Exact Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReportWithScreenShot("FAIL", "Actual Text:" + actual  + "Expected Exact Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialWithScreenShot(String actual, String expected) {
        try {
            logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
            if (actual.contains(expected)) {
                logReportWithScreenShot("PASS", "Actual Text:" + actual  + "Expected Partial Text:" + expected);
            } else {
                logReportWithScreenShot("FAIL", "Actual Text:" + actual  + "Expected Partial Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void fail(String message) {
        logReport("FAIL", message);
        Thread.dumpStack();
        sAssert.fail(message);
    }

    protected boolean elementReportCheck(String... wait_logReport_isPresent_Up_Down_XpathValues) {
        Boolean reportStatus = false;
        try {
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 0) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("noreport"))) {
                    reportStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 1) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("noreport"))) {
                    reportStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 2) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("noreport"))) {
                    reportStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 3) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("noreport"))) {
                    reportStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 4) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("noreport"))) {
                    reportStatus = true;
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return reportStatus;
    }

    protected boolean elementIsPresentCheck(String... wait_logReport_isPresent_Up_Down_XpathValues) {
        Boolean isPresentStatus = false;
        try {
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 0) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("isPresent"))) {
                    isPresentStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 1) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("isPresent"))) {
                    isPresentStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 2) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("isPresent"))) {
                    isPresentStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 3) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("isPresent"))) {
                    isPresentStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 4) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("isPresent"))) {
                    isPresentStatus = true;
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return isPresentStatus;
    }

    protected boolean scrollUpCheck(String... wait_logReport_isPresent_Up_Down_XpathValues) {
        Boolean upStatus = false;
        try {
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 0) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("Up"))) {
                    upStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 1) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("Up"))) {
                    upStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 2) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("Up"))) {
                    upStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 3) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("Up"))) {
                    upStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 4) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("Up"))) {
                    upStatus = true;
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return upStatus;
    }

    protected boolean scrollDownCheck(String... wait_logReport_isPresent_Up_Down_XpathValues) {
        Boolean downStatus = false;
        try {
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 0) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("Down"))) {
                    downStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 1) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("Down"))) {
                    downStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 2) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("Down"))) {
                    downStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 3) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("Down"))) {
                    downStatus = true;
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 4) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("Down"))) {
                    downStatus = true;
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return downStatus;
    }

    protected int elementWaitCheck(String... wait_logReport_isPresent_Up_Down_XpathValues) {
        int returnWait = -1;
        try {
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 0) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[0].startsWith("wait_"))) {
                    String[] customWait = wait_logReport_isPresent_Up_Down_XpathValues[0].split("_");
                    returnWait = Integer.parseInt(customWait[1]);
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 1) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[1].startsWith("wait_"))) {
                    String[] customWait = wait_logReport_isPresent_Up_Down_XpathValues[1].split("_");
                    returnWait = Integer.parseInt(customWait[1]);
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 2) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[2].startsWith("wait_"))) {
                    String[] customWait = wait_logReport_isPresent_Up_Down_XpathValues[2].split("_");
                    returnWait = Integer.parseInt(customWait[1]);
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 3) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[3].startsWith("wait_"))) {
                    String[] customWait = wait_logReport_isPresent_Up_Down_XpathValues[3].split("_");
                    returnWait = Integer.parseInt(customWait[1]);
                }
            }
            if (wait_logReport_isPresent_Up_Down_XpathValues.length > 4) {
                if ((wait_logReport_isPresent_Up_Down_XpathValues[4].startsWith("wait_"))) {
                    String[] customWait = wait_logReport_isPresent_Up_Down_XpathValues[4].split("_");
                    returnWait = Integer.parseInt(customWait[1]);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return returnWait;
    }

    protected boolean hardFail(String message, String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        Boolean isPresentStatus = false;
        try {
            if (!elementIsPresentCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                logger.error(message + locatorString);
                logReportWithScreenShot("FAIL", message + locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                Thread.dumpStack();
                Assert.fail(message + locatorString);
            } else {
                isPresentStatus = true;
            }
        } catch (Exception e) {
            captureException(e);
        }
        return isPresentStatus;
    }

    protected void hardFail(String message) {
        logReport("FAIL", message);
        Thread.dumpStack();
        Assert.fail(message);
    }

    protected void hardFail(Exception e) {
        logReport("FAIL", e.getMessage());
        e.printStackTrace();
        Assert.fail(e.getMessage());
    }

    protected void hardFail() {
        logReport("FAIL", "Test Exception Occured");
        Thread.dumpStack();
        Assert.fail("Test Exception Occured");
    }

    protected void captureException(Exception e) {
        try {
            logger.error(ExceptionUtils.getStackTrace(e));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public String mapKeyFinder(LinkedHashMap<String, Object> map, String KeyName) {
        String returnValue = "";
        try {
            Object KeyName1 = map.get(KeyName);
            if (KeyName1 != null) {
                returnValue = String.valueOf(KeyName1);
            } else {
                Assert.fail(" " + KeyName + " Key is Not Found in Map: Check your map values or Excel header keys !!!");
                logReport("FAIL", " " + KeyName + " Key is Not Found in Map: Check your map values or Excel header keys !!!");
            }
        } catch (Exception e) {
            captureException(e);
        }
        return returnValue;
    }

    public void hardWait(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            captureException(e);
        }
        if (delay > 0) {
            logger.info("Proceeding After waiting for Hardwait: " + delay + " milli Seconds");
        }
    }

    protected void dataToJSON(String name, String value) {
        try {
            if (value.equals("FAILED")) {
                logger.error(name + " : " + value);
                logReport("FAIL",name +" : "+value);
            } else {

                if(tEnv().getTestType().equalsIgnoreCase("API")){
                    logger.info(name + " : " + value);
                    logReport("INFO",name +" : "+value);
                }
            }
            if(dataTableMapApi.get()!=null){
                dataTableMapApi.get().put(name, value);
            }

        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void dataToJSON(String name, Map<String, Object> value) {
        try {
            Gson pGson = new GsonBuilder().setPrettyPrinting().create();
            String data = pGson.toJson(value);
           // logger.info("\n" + name + ": \n" + data);
            logReport("INFO",name +" : "+data);
            dataTableMapApi.get().put(name, value);
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void dataToJSONStep(String stepStatus, String stepDetails) {
        try {
            LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("status", stepStatus);
            dataMap.put("timeStamp", cUtils().getCurrentTimeinMilliSecondsWithColon());
            dataMap.put("stepDetails", stepDetails);
            Gson pGson = new GsonBuilder().setPrettyPrinting().create();
            String data = pGson.toJson(dataMap);
            //logger.info("\n" + "step" + ": \n" + data);
            dataTableMapApi.get().put("step", data);
        } catch (Exception e) {
            captureException(e);
        }
    }

    public void step(String stepDetails) {
        dataToJSON("NewStep", stepDetails);
    }

}

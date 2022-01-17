package com.trigon.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.github.wnameless.json.flattener.JsonFlattener;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.trigon.testbase.TestInitialization.trigonPaths;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.FILE;


public class ReportManager extends CustomReport {

    private static final Logger logger = LogManager.getLogger(ReportManager.class);

    public void author_ScenarioName(String author, String scenario) {
        try {
            captureScenarioAndAuthor(author, scenario);
        } catch (Exception e) {
            captureException(e);
        }
    }

    public void author_ScenarioName(String author, LinkedHashMap tData) {
        try {
            captureScenarioAndAuthor(author, tData.get("Scenario").toString());
        } catch (Exception e) {
            captureException(e);
        }
    }

    public void testTearDown() {
        if (failAnalysisThread.get().size() > 0) {
            Assert.fail("Test Failed !! Look for above failures/exceptions and fix it !! ");
        }
    }

    public void logReport(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        if (!elementReportCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
            try {
                if (status.equalsIgnoreCase("PASS")) {
                    logPass(message, false);
                } else if (status.equalsIgnoreCase("FAIL")) {
                    logFail(message, false);
                } else if (status.equalsIgnoreCase("INFO")) {
                    logInfo(message, false);
                } else if (status.equalsIgnoreCase("SKIP")) {
                    logSkip(message, false);
                } else if (status.equalsIgnoreCase("WARN")) {
                    logWarn(message, false);
                } else {
                    logInfo(message, false);
                }
            } catch (Exception e) {
                captureException(e);
            }
        }
    }

    public void logMultipleJSON(String status, String message, String responseJSON) {
        String apiName = "API : " + getAPIMethodName();
        Markup m1 = MarkupHelper.createCodeBlock(message, CodeLanguage.JSON);
        Markup m2 = MarkupHelper.createCodeBlock(responseJSON, CodeLanguage.JSON);
        String m = "<details><summary><font color=\"green\"><b>" + apiName + "</b></font></summary> <table><tr><td>" + m1.getMarkup() + " </td><td>" + m2.getMarkup() + "</td></tr></table></details>";
        try {
            if (status.equalsIgnoreCase("PASS")) {
                if (extentScenarioNode.get() != null) {
                    extentScenarioNode.get().pass(m);
                } else if (extentMethodNode.get() != null) {
                    extentMethodNode.get().pass(m);
                }
                if (tEnv().getTestType().equalsIgnoreCase("api")) {
                    logger.info(message);
                    logger.info(apiName + " is PASSED");
                } else {
                    logger.info(apiName + " is PASSED");
                }

            } else if (status.equalsIgnoreCase("FAIL")) {
                logger.error(message);
                if (extentScenarioNode.get() != null) {
                    extentScenarioNode.get().fail(m);
                } else if (extentMethodNode.get() != null) {
                    extentMethodNode.get().fail(m);
                }
                if (failAnalysisThread.get() != null) {
                    failAnalysisThread.get().add(message);
                }
                logger.error(apiName + " is FAILED !! Check your API Parameters ");
            }
        } catch (Exception e) {
            captureException(e);
        }

    }

    public void logApiReport(String status, String message) {
        try {
            if (status.equalsIgnoreCase("PASS")) {
                logPass(message, false);
            } else if (status.equalsIgnoreCase("FAIL")) {
                String apiName = "Failed API : " + getAPIMethodName();
                String failMessage = "<div style=\"color: #e50909;font-weight:bold;\"> " + apiName + " due to " + message + "</div>";
                logFail(failMessage, false);
            } else if (status.equalsIgnoreCase("INFO")) {
                logInfo(message, false);
            } else if (status.equalsIgnoreCase("SKIP")) {
                logSkip(message, false);
            } else if (status.equalsIgnoreCase("WARN")) {
                logWarn(message, false);
            } else {
                logInfo(message, false);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void logReportWithScreenShot(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        if (!elementReportCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
            try {
                if (status.equalsIgnoreCase("PASS")) {
                    logPass(message, true);
                } else if (status.equalsIgnoreCase("FAIL")) {
                    logFail(message, true);
                } else if (status.equalsIgnoreCase("INFO")) {
                    logInfo(message, true);
                } else if (status.equalsIgnoreCase("SKIP")) {
                    logSkip(message, true);
                } else if (status.equalsIgnoreCase("WARN")) {
                    logWarn(message, true);
                } else {
                    logInfo(message, true);
                }
            } catch (Exception e) {
                captureException(e);
            }
        }
    }

    public void logStepAction(String message) {
        if(extentScenarioNode.get()!=null){
            extentScenarioNode.get().info("<font style=\"color: #366792;font-weight:bold;\"> STEP : </font>"+message);
        }else{
            extentMethodNode.get().info("<font style=\"color: #366792;font-weight:bold;\"> STEP : </font>"+message);
        }
    }

    public void logScenario(String ScenarioName) {
        logger.info("Scenario : " + ScenarioName);
        if(extent!=null){
            if(extentScenarioNode!=null){
                extentScenarioNode.set(extentMethodNode.get().createNode("<font color=\"#d0b2e6\">" + ScenarioName + "</font>"));
            }
        }
    }

    protected void customAssertEquals(String actual, String expected) {
        try {
            logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
            if (expected.equals(actual)) {
                logReport("PASS", "Actual Text:" + actual + "Expected Exact Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReport("FAIL", "Actual Text:" + actual + "Expected Exact Text:" + expected);

            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertNotEquals(String actual, String expected) {
        try {
            logger.info("Verifying NOT Equals Actual : " + actual + " with Expected : " + expected + "");
            if (!(expected.equals(actual))) {
                logReport("PASS", "Actual Text:" + actual + " Expected NOT EQUALS Text:" + expected);
            } else {
                logReport("FAIL", "Actual Text:" + actual + " Expected NOT EQUALS Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialEquals(String actual, String expected) {
        logger.info("Verifying Partial Equals Actual : " + actual + " with Expected : " + expected + "");
        try {
            if (actual.contains(expected)) {
                logReport("PASS", "Actual Text:" + actual + "Expected Partial Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReport("FAIL", "Actual Text:" + actual + "Expected Partial Text:" + expected);
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
                    logReport("PASS", "Actual Text:" + actual + "ActualSize :" + actualValueSize + "Expected Exact Text:" + expected + "ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text:" + actual + "ActualSize :" + actualValueSize + "Expected Exact Text:" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched " + "Actual Text:" + actual + "ActualSize :" + actualValueSize + "Expected Exact Text:" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
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
                    logReport("PASS", "Actual Text:" + actual + "ActualSize :" + actualValueSize + "Expected Partial Text:" + expected + "ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text:" + actual + "ActualSize :" + actualValueSize + "Expected Partial Text:" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched " + "Actual Text:" + actual + "ActualSize :" + actualValueSize + "  Expected Partial Text:" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
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
                    logReport("PASS", "Actual Text:" + actual + "ActualSize :" + actualValueSize + "  Expected NOT EQUALS Text:" + expected + " ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text:" + actual + "ActualSize :" + actualValueSize + "  Expected NOT EQUALS Text:" + expected + " ExpectedSize :" + expectedValueSize + " Additional values in Actual List" + actuallist + " Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched " + "Actual Text:" + actual + "ActualSize :" + actualValueSize + "  Expected NOT EQUALS Text:" + expected + " ExpectedSize :" + expectedValueSize + " Additional values in Actual List" + actuallist + " Additional values in Expected List" + expectedlist);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertEqualsWithScreenShot(String actual, String expected) {
        logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
        try {
            if (expected.equals(actual)) {
                logReportWithScreenShot("PASS", "Actual Text:" + actual + "Expected Exact Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReportWithScreenShot("FAIL", "Actual Text:" + actual + "Expected Exact Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialWithScreenShot(String actual, String expected) {
        try {
            logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
            if (actual.contains(expected)) {
                logReportWithScreenShot("PASS", "Actual Text:" + actual + "Expected Partial Text:" + expected);
            } else {
                logReportWithScreenShot("FAIL", "Actual Text:" + actual + "Expected Partial Text:" + expected);
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
        logReport("FAIL", "Test Exception Occurred");
        Thread.dumpStack();
        Assert.fail("Test Exception Occured");
    }

    protected void captureException(Exception e) {
        try {
            logger.error(ExceptionUtils.getStackTrace(e));
            exceptionStatus = true;
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
            if (dataTableMapApi.get() != null) {
                dataTableMapApi.get().put(name, value);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void dataToJSON(String name, Map<String, Object> value) {
        try {
            if (dataTableMapApi.get() != null) {
                dataTableMapApi.get().put(name, value);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected Map<String, Object> createJiraTicket(String bugSummary, String description) {
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

    private void captureScenarioAndAuthor(String author, String scenario) {
        if(extentMethodNode.get()!=null){
            extentMethodNode.get().assignAuthor(author);
            extentMethodNode.get().getModel().setDescription(scenario);
            extentMethodNode.get().info("<font style=\"color: #3cbb2d;font-weight:bold;\"> SCENARIO : </font>"+scenario);
        }
        if(extentScenarioNode.get()!=null){
            extentScenarioNode.get().assignAuthor(author);
            extentScenarioNode.get().getModel().setDescription(scenario);
            extentScenarioNode.get().info("<font style=\"color: #3cbb2d;font-weight:bold;\"> SCENARIO : </font>"+scenario);
        }
    }

    private String screenshotDriver() {
        File screenshot = null;
        String path = null;
        String newPath = null;
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
            String methodScreeenShotPath = trigonPaths.getScreenShotsPath() + tEnv().getCurrentTestMethodName();
            tEnv().setScreenshotPath(new File(methodScreeenShotPath));

            String dateTime = cUtils().getDateTimeStamp();

            path = methodScreeenShotPath + "/" + "" + stackTrace[7].getMethodName() + "" + "_" + "[" + dateTime + "]" + ".png";
            copyFile(screenshot, new File((path)));
            newPath = "ScreenShots" + "/" + tEnv().getCurrentTestMethodName() + "" + "/" + "" + stackTrace[7].getMethodName() + "" + "_" + "[" + dateTime + "]" + ".png";

        } catch (IOException e) {
            captureException(e);
        }

        return newPath;
    }

    private String getAPIMethodName() {
        String apiName;
        int getIndex = 0;
        for (int i = 0; i < 15; i++) {
            if (Thread.currentThread().getStackTrace()[i].getMethodName().contains("validate")) {
                getIndex = i;
                getIndex++;
                if (Thread.currentThread().getStackTrace()[getIndex].getMethodName().contains("validate")) {
                    getIndex++;
                }
                break;
            }
        }
        apiName = Thread.currentThread().getStackTrace()[getIndex].getMethodName();
        return apiName;
    }

    private void logWarn(String message, boolean screenshotMode) {
        logger.warn(message);
        if (extentScenarioNode.get() != null) {
            screenshotInfo(extentScenarioNode.get(), message, screenshotMode);
        } else if (extentMethodNode.get() != null) {
            screenshotInfo(extentMethodNode.get(), message, screenshotMode);
        }
    }

    private void screenshotWarn(ExtentTest node, String message, boolean screenshotMode) {
        if (screenshotMode) {
            String Screenshot = screenshotDriver();
            node.warning(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
        } else {
            node.warning(message);
        }
    }

    private void logSkip(String message, boolean screenshotMode) {
        logger.warn(message);
        if (extentScenarioNode.get() != null) {
            screenshotSkip(extentScenarioNode.get(), message, screenshotMode);
        } else if (extentMethodNode.get() != null) {
            screenshotSkip(extentMethodNode.get(), message, screenshotMode);
        }
    }

    private void screenshotSkip(ExtentTest node, String message, boolean screenshotMode) {
        if (screenshotMode) {
            String Screenshot = screenshotDriver();
            node.skip(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
        } else {
            node.skip(message);
        }
    }

    private void logInfo(String message, boolean screenshotMode) {
        logger.info(message);
        if (extentScenarioNode.get() != null) {
            screenshotInfo(extentScenarioNode.get(), message, screenshotMode);
        } else if (extentMethodNode.get() != null) {
            screenshotInfo(extentMethodNode.get(), message, screenshotMode);
        }
    }

    private void screenshotInfo(ExtentTest node, String message, boolean screenshotMode) {
        if (screenshotMode) {
            String Screenshot = screenshotDriver();
            node.info(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
        } else {
            node.info(message);
        }
    }

    private void logFail(String message, boolean screenshotMode) {
        logger.error(message);
        Thread.dumpStack();
        if (extentScenarioNode.get() != null) {
            screenshotFail(extentScenarioNode.get(), message, screenshotMode);
        } else if (extentMethodNode.get() != null) {
            screenshotFail(extentMethodNode.get(), message, screenshotMode);
        }
        if (failAnalysisThread.get() != null) {
            failAnalysisThread.get().add(message);
        }
    }

    private void screenshotFail(ExtentTest node, String message, boolean screenshotMode) {
        if (screenshotMode) {
            String Screenshot = screenshotDriver();
            node.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
        } else {
            node.fail(message);
        }
    }

    private void logPass(String message, boolean screenshotMode) {
        logger.info(message);
        if (extentScenarioNode.get() != null) {
            screenshotPass(extentScenarioNode.get(), message, screenshotMode);
        } else if (extentMethodNode.get() != null) {
            screenshotPass(extentMethodNode.get(), message, screenshotMode);
        }
    }

    private void screenshotPass(ExtentTest node, String message, boolean screenshotMode) {
        if (screenshotMode) {
            String Screenshot = screenshotDriver();
            node.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(Screenshot).build());
        } else {
            node.pass(message);
        }
    }
}

package com.trigon.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.trigon.testrail.TestRailManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.io.*;
import java.util.*;

import static com.trigon.testbase.TestInitialization.trigonPaths;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.openqa.selenium.OutputType.FILE;


public class ReportManager extends CustomReport {

    private static final Logger logger = LogManager.getLogger(ReportManager.class);
    static JsonWriter writer;
    List<Map<String, Object>> resultList = new ArrayList<>();
    Map<String, Object> resultMap = new HashMap<>();


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
        Assert.fail("Test Failed !! Look for above failures/exceptions and fix it !! ");
    }

    public void testTearDown(ArrayList<String> allTestCaseIDs) {

        if (failedTCs.get().size()>0 && !failedTCs.get().containsKey(testCaseIDThread.get().get(0).split(",")[0])) {
            for (String tcID : testCaseIDThread.get().get(0).split(",")) {
                updateHashMapWithTCDetails(tcID, "PASS", tEnv().getCurrentTestClassName(), tEnv().getCurrentTestMethodName());
            }
        } else if(failAnalysisThread.get().size()==0) {
            for (String tcID : testCaseIDThread.get().get(0).split(",")) {
            updateHashMapWithTCDetails(tcID, "PASS", tEnv().getCurrentTestClassName(), tEnv().getCurrentTestMethodName());
        }}
        else
        {   allTestCaseIDs.removeAll(passedTCs.get());
            allTestCaseIDs.removeAll(failedTCs.get().keySet());
            for (String testCaseID : allTestCaseIDs) {
                updateHashMapWithTCDetails(testCaseID, "NOT EXECUTED", tEnv().getCurrentTestClassName(), tEnv().getCurrentTestMethodName());
            }
        }
        resultTCs.get().put("Passed", passedTCs.get());
        resultTCs.get().put("Failed", failedTCs.get());
        resultTCs.get().put("Skipped", skippedTCs.get());
        resultTCCollectionMap.put(tEnv().getCurrentTestClassName() + "_" + tEnv().getCurrentTestMethodName(), new HashMap(resultTCs.get()));
        testCaseIDThread.remove();

        if(failAnalysisThread.get().size() > 0)
        Assert.fail("Test Failed !! Look for above failures/exceptions and fix it !! ");
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

    public void getBSVideoSession(){
        logReport("INFO", "<b>BS Video:</b> <a href=\""+bsVideo().get("public_url").toString()+"\" target=\"_blank\"> View Recorded Video </a>");
    }

    public JSONObject bsVideo(){
        Object response = null;
        if(ios()!=null) {
            response  = ios().executeScript("browserstack_executor: {\"action\": \"getSessionDetails\"}");
        }
        if(android()!=null) {
            response  = android().executeScript("browserstack_executor: {\"action\": \"getSessionDetails\"}");
        }
        if(browser()!=null) {
            response  = browser().executeScript("browserstack_executor: {\"action\": \"getSessionDetails\"}");
        }

        JSONObject bsResponse = new JSONObject(response.toString());
        return bsResponse;
    }

    public void logMultipleJSON(String status, LinkedHashMap message, Object responseJSON, String curl, LinkedHashMap responseValidation) {
        String apiName = "API : "+getAPIMethodName();
        Gson pGson1 = new GsonBuilder().create();
        Gson pGson = new GsonBuilder().setPrettyPrinting().create();
        String m = apiCard(status,apiName,pGson1.toJson(message),String.valueOf(responseJSON),curl,pGson1.toJson(responseValidation));
        try {
            if (status.equalsIgnoreCase("PASS")) {

                if((tEnv().getJenkins_execution().equalsIgnoreCase("true") || tEnv().getPipeline_execution().equalsIgnoreCase("true")) && tEnv().getTestType().equalsIgnoreCase("api")){
                     m = apiName + " is PASSED";
                }
                if (extentScenarioNode.get() != null) {
                    extentScenarioNode.get().pass(m);

                } else if (extentMethodNode.get() != null) {
                    extentMethodNode.get().pass(m);
                }
                if (tEnv().getTestType().equalsIgnoreCase("api")) {
                    logger.info(pGson.toJson(message));
                    logger.info(pGson.toJson(responseValidation));
                    logger.info(apiName + " is PASSED");
                } else {
                    logger.info(apiName + " is PASSED");
                }

            } else if (status.equalsIgnoreCase("FAIL")) {
                logger.info(pGson.toJson(message));
                logger.info(pGson.toJson(responseValidation));
                if (extentScenarioNode.get() != null) {
                    extentScenarioNode.get().fail(m);
                } else if (extentMethodNode.get() != null) {
                    extentMethodNode.get().fail(m);
                }
                if (failAnalysisThread.get() != null) {
                    failAnalysisThread.get().add(pGson.toJson(message));
                }
                logger.error(apiName + " is FAILED !! Check your API Parameters ");
                logger.info("*******************************************************************************");
                logger.info("Failed curl : \n"+curl);
                logger.info("*******************************************************************************");
            }
        } catch (Exception e) {
            captureException(e);
        }

    }

    private String navTabs() {

        int random = commonUtils.getRandomNumber(100, 100000);

        String a = "<div class=\"col-md-12 col-sm-12\">\n" +
                "            <!-- Nav tabs -->\n" +
                "            <ul class=\"nav nav-tabs\" role=\"tablist\">\n" +
                "                <li class=\"nav-item\">\n" +
                "                    <a class=\"nav-link active\" data-toggle=\"tab\" href=\"#request_" + random + "\">Request</a>\n" +
                "                </li>\n" +
                "                <li class=\"nav-item\">\n" +
                "                    <a class=\"nav-link\" data-toggle=\"tab\" href=\"#response_" + random + "\">Response</a>\n" +
                "                </li>\n" +
                "                <li class=\"nav-item\">\n" +
                "                    <a class=\"nav-link\" data-toggle=\"tab\" href=\"#curl_" + random + "\">Curl</a>\n" +
                "                </li>\n" +
                "            </ul>\n" +
                "\n" +
                "            <!-- Tab panes -->\n" +
                "            <div class=\"tab-content\">\n" +
                "                <div id=\"request_" + random + "\" class=\"container tab-pane active\"><br>\n" +
                "                    <div class=\"bd-clipboard\"><button type=\"button\" class=\"btn-clipboard\" data-clipboard-target=\"#responseCopy_1-" + random + "-1\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Copy\" data-original-title=\"Copy to clipboard\">Copy</button></div>\n" +
                "                    <div><pre><div class='json-tree' id='responseCopy_1-" + random + "-1'></div>\n" +
                "                                                            <script>function jsonTreeCreate1() {\n" +
                "                                                                document.getElementById('responseCopy_1-" + random + "-1').innerHTML = JSONTree.create({\n" +
                "                                                                    \"URI\": \"https://api-preprod.t2sonline.com\",\n" +
                "                                                                    \"queryParams\": {\n" +
                "                                                                        \"api_token\": \"J6WDf00hQKGhfYhQkbRCjwraBS11JYuIDx\"\n" +
                "                                                                    },\n" +
                "                                                                    \"formParams\": {\n" +
                "                                                                        \"tag\": \"foodhub-db\",\n" +
                "                                                                        \"key\": \"country\"\n" +
                "                                                                    },\n" +
                "                                                                    \"httpMethod\": \"POST\",\n" +
                "                                                                    \"endPoint\": \"clear/cache\",\n" +
                "                                                                    \"responseTime\": \"1.641\",\n" +
                "                                                                    \"statusCode\": {\n" +
                "                                                                        \"ACT\": \"200\",\n" +
                "                                                                        \"EXP\": \"200\"\n" +
                "                                                                    },\n" +
                "                                                                    \"expectedResponse\": {\n" +
                "                                                                        \"outcome\": \"success\"\n" +
                "                                                                    },\n" +
                "                                                                    \"actualResponse\": {\n" +
                "                                                                        \"outcome\": \"success\"\n" +
                "                                                                    },\n" +
                "                                                                    \"apiTestStatus\": \"PASSED\"\n" +
                "                                                                });\n" +
                "                                                            }\n" +
                "\n" +
                "                                                            jsonTreeCreate1();</script>\n" +
                "                                                    </pre></div>\n" +
                "                </div>\n" +
                "                <div id=\"response_" + random + "\" class=\"container tab-pane fade\"><br>\n" +
                "                    <div class=\"bd-clipboard\"><button type=\"button\" class=\"btn-clipboard\" data-clipboard-target=\"#responseCopy_1-" + random + "-2\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Copy\" data-original-title=\"Copy to clipboard\">Copy</button></div>\n" +
                "                    <div><pre><div class='json-tree' id='responseCopy_1-" + random + "-2'></div>\n" +
                "                                                            <script>function jsonTreeCreate1() {\n" +
                "                                                                document.getElementById('responseCopy_1-" + random + "-2').innerHTML = JSONTree.create({\n" +
                "                                                                    \"Outcome\": \"Success\",\n" +
                "\n" +
                "                                                                });\n" +
                "                                                            }\n" +
                "                                                            jsonTreeCreate1();</script>\n" +
                "                                                    </pre></div>\n" +
                "                </div>\n" +
                "                <div id=\"curl_" + random + "\" class=\"container tab-pane fade\"><br>\n" +
                "                    <div class=\"bd-clipboard\"><button type=\"button\" class=\"btn-clipboard\" data-clipboard-target=\"#responseCopy_1-" + random + "-3\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Copy\" data-original-title=\"Copy to clipboard\">Copy</button></div>\n" +
                "                    <div><pre><div id='responseCopy_1-" + random + "-3'>curl --location --request POST 'https://api-cloud.browserstack.com/app-automate/upload' \\\n" +
                "--header 'Authorization: Basic dG91Y2hzdWNjZXNzMTpVakJSTHNzOUFUYVRDZWFId3RkYw==' \\\n" +
                "--header 'Cookie: tracking_id=56ce83d7-b830-44ce-82a4-0a9f31be2d17' \\\n" +
                "--form 'data=\"{\\\"custom_id\\\": \\\"MYT_Android\\\"}\"' \\\n" +
                "--form 'file=@\"/path/to/file\"'</div></pre></div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>";

        return a;
    }

    private String apiCard(String status, String apiName, String request, String response, String curl, String responseValidation) {
        int random = commonUtils.getRandomNumber(100, 100000);
        String jsonRequestId = "json-request-" + random + "";
        String jsonResponseValidationId = "json-response-validation-" + random + "";
        String jsonResponseId = "json-response-" + random + "";
        String curlId = "json-curl-" + random + "";
        String bColor = "#efebeb";
        if (status.equalsIgnoreCase("FAIL")) {
            bColor = "#e47373";
        }

        String apiFormat = "<div class=\"accordion\" role=\"tablist\"><div class=\"card\" style=\"background-color: " + bColor + "\">\n" +
                "               <div class=\"card-header\">\n" +
                "                   <div class=\"card-title\">\n" +
                "                       <a class=\"node\" ><span class=\"apiSpan\">" + apiName + "</span></a>\n" +
                "                   </div>\n" +
                "               </div>\n" +
                "               <div class=\"collapse\">\n" +
                "                   <div class=\"card-body\">\n" +
                "                       <p>\n" +
                "                           <button class=\"btn\" type=\"button\" data-toggle=\"collapse\" data-target=\"#collapse_request_" + random + "\" aria-expanded=\"false\" >\n" +
                "                               Request\n" +
                "                           </button>\n" +
                "                           <button class=\"btn \" type=\"button\" data-toggle=\"collapse\" data-target=\"#collapse_response_" + random + "\" aria-expanded=\"false\" >\n" +
                "                               Response\n" +
                "                           </button>\n" +
                "                           <button class=\"btn \" type=\"button\" data-toggle=\"collapse\" data-target=\"#collapse_response-validation_" + random + "\" aria-expanded=\"false\" >\n" +
                "                               ResponseValidation\n" +
                "                           </button>\n" +
                "                           <button class=\"btn \" type=\"button\" data-toggle=\"collapse\" data-target=\"#collapse_curl_" + random + "\" aria-expanded=\"false\" >\n" +
                "                               Curl\n" +
                "                           </button>\n" +
                "                       </p>\n" +
                "                       <div class=\"collapse\" id=\"collapse_request_" + random + "\">\n" +
                "                           <div class=\"card card-body\">\n" +
                "                               <div class=\"bd-clipboard\">\n" +
                "                                   <button type=\"button\"\n" +
                "                                           onclick=\"copy('" + jsonRequestId + "')\"\n" +
                "                                           class=\"btn-clipboard\">\n" +
                "                                       Copy\n" +
                "                                   </button>\n" +
                "                               </div>\n" +
                "                               <div>\n" +
                "                                   <pre class=\"preCode\"><code  id=\"" + jsonRequestId + "\"></code></pre>\n" +
                "                               </div>\n" +
                "                               <script>\n" +
                "                                   document.getElementById('" + jsonRequestId + "').innerHTML = JSON.stringify(JSON.parse('" + request + "'), undefined, 4);\n" +
                "                               </script>\n" +
                "                           </div>\n" +
                "                       </div>\n" +
                "                       <div class=\"collapse\" id=\"collapse_response_" + random + "\">\n" +
                "                           <div class=\"card card-body\">\n" +
                "                               <div class=\"bd-clipboard\">\n" +
                "                                   <button type=\"button\"\n" +
                "                                           onclick=\"copy('" + jsonResponseId + "')\"\n" +
                "                                           class=\"btn-clipboard\">\n" +
                "                                       Copy\n" +
                "                                   </button>\n" +
                "                               </div>\n" +
                "                               <div>\n" +
                "                                   <pre class=\"preCode\"><code  id=\"" + jsonResponseId + "\"></code></pre>\n" +
                "                               </div>\n" +
                "                               <script>\n" +
                "                                   document.getElementById('" + jsonResponseId + "').innerHTML = JSON.stringify(JSON.parse('" + response + "'), undefined, 4);\n" +
                "                               </script>\n" +
                "                           </div>\n" +
                "                       </div>\n" +
                "                       <div class=\"collapse\" id=\"collapse_response-validation_" + random + "\">\n" +
                "                           <div class=\"card card-body\">\n" +
                "                               <div class=\"bd-clipboard\">\n" +
                "                                   <button type=\"button\"\n" +
                "                                           onclick=\"copy('" + jsonResponseValidationId + "')\"\n" +
                "                                           class=\"btn-clipboard\">\n" +
                "                                       Copy\n" +
                "                                   </button>\n" +
                "                               </div>\n" +
                "                               <div>\n" +
                "                                   <pre class=\"preCode\"><code  id=\"" + jsonResponseValidationId + "\"></code></pre>\n" +
                "                               </div>\n" +
                "                               <script>\n" +
                "                                   document.getElementById('" + jsonResponseValidationId + "').innerHTML = JSON.stringify(JSON.parse('" + responseValidation + "'), undefined, 4);\n" +
                "                               </script>\n" +
                "                           </div>\n" +
                "                       </div>\n" +
                "                       <div class=\"collapse\" id=\"collapse_curl_" + random + "\">\n" +
                "                           <div class=\"card card-body\">\n" +
                "                               <div class=\"bd-clipboard\">\n" +
                "                                   <button type=\"button\"\n" +
                "                                           onclick=\"copy('" + curlId + "')\"\n" +
                "                                           class=\"btn-clipboard\">\n" +
                "                                       Copy\n" +
                "                                   </button>\n" +
                "                               </div>\n" +
                "                               <div>\n" +
                "                                   <pre class=\"preCode\"><code  id=\"" + curlId + "\">" + curl + "</code></pre>\n" +
                "                               </div>\n" +
                "                           </div>\n" +
                "                       </div>\n" +
                "                   </div>\n" +
                "               </div>\n" +
                "           </div>\n" +
                "       </div>";

        return apiFormat;
    }

    public void logDBData(String query, String dbResponse) {
        logger.info("Query : " + query);
        if (dbResponse == null) {
            dbResponse = "No Data found or returned null";
        }
        String m = "<details><summary><font color=\"green\"><b>DBQuery</b></font></summary> " + MarkupHelper.createCodeBlock(query, dbResponse).getMarkup() + "</details>";
        extentMethodNode.get().info(m);
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

        if (extentScenarioNode.get() != null) {
            extentScenarioNode.get().info("<span class=\"stepSpan\"> STEP : </span>" + message);
        } else {
            extentMethodNode.get().info("<span class=\"stepSpan\"> STEP : </span>" + message);
        }
    }

    public void logStepAction(String message, String testCaseIDs) {

        if (testCaseIDThread.get().size() == 0) {
            testCaseIDThread.get().add(testCaseIDs);
        } else if (failedTCs.get().size() > 0 && failedTCs.get().containsKey(testCaseIDThread.get().get(0).split(",")[0])) {
            testCaseIDThread.get().clear();
            testCaseIDThread.get().add(testCaseIDs);
        } else {
            for (String tcID : testCaseIDThread.get().get(0).split(",")) {
                updateHashMapWithTCDetails(tcID, "PASS", tEnv().getCurrentTestClassName(), tEnv().getCurrentTestMethodName());
            }
            testCaseIDThread.get().clear();            //removing previous TC
            testCaseIDThread.get().add(testCaseIDs);     //Adding next TC
        }
        if (extentScenarioNode.get() != null) {
            extentScenarioNode.get().info("<span class=\"stepSpan\"> STEP : </span>" + message + " - " + testCaseIDs);
        } else {
            extentMethodNode.get().info("<span class=\"stepSpan\"> STEP : </span>" + message + " - " + testCaseIDs);
        }
    }

    public void updateHashMapWithTCDetails(String tcId, String status, String className, String methodName) {
        if (status.equalsIgnoreCase("PASS")) {
            passedTCs.get().add(tcId);
        } else if (status.equalsIgnoreCase("FAIL")) {
            String failure = extent.getReport().getTestList().stream().filter(modules -> tEnv().getContext().getCurrentXmlTest().getName().replaceAll("-", "_").replaceAll(" ", "_").trim().equalsIgnoreCase(modules.getName().substring(0, modules.getName().indexOf('<')))).findAny().get().getChildren().stream().filter(classes ->
                    className.equalsIgnoreCase(classes.getName())).findAny().get().getChildren().stream().filter(methods -> methodName.equalsIgnoreCase(methods.getName())).findAny().get().getLogs().stream().filter(logs -> logs.getStatus().toString().equalsIgnoreCase("Fail")).findAny().get().getDetails();
            failedTCs.get().put(tcId, failure);
        } else {
            skippedTCs.get().add(tcId);
        }
    }


//    public void logStepAction(String ScenarioName) {
//        logger.info("Scenario : " + ScenarioName);
//        if(extent!=null){
//            if(extentScenarioNode!=null){
//                extentScenarioNode.set(extentMethodNode.get().createNode("<font color=\"#d0b2e6\">" + ScenarioName + "</font>"));
//            }
//        }
//    }

    protected void customAssertEquals(String actual, String expected) {
        try {
            logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
            if (expected.equals(actual)) {
                logReport("PASS", "Actual Text :" + actual + "<br> Expected Exact Text :" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReport("FAIL", "Actual Text :" + actual + "<br> Expected Exact Text :" + expected);

            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertNotEquals(String actual, String expected) {
        try {
            logger.info("Verifying NOT Equals Actual : " + actual + " with Expected : " + expected + "");
            if (!(expected.equals(actual))) {
                logReport("PASS", "Actual Text :" + actual + " <br> Expected NOT EQUALS Text:" + expected);
            } else {
                logReport("FAIL", "Actual Text :" + actual + " <br> Expected NOT EQUALS Text:" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialEquals(String actual, String expected) {
        logger.info("Verifying Partial Equals Actual : " + actual + " with Expected : " + expected + "");
        try {
            if (actual.contains(expected)) {
                logReport("PASS", "Actual Text :" + actual + "<br> Expected Partial Text:" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReport("FAIL", "Actual Text :" + actual + "<br> Expected Partial Text:" + expected);
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
                    logReport("PASS", "Actual Text :" + actual + "ActualSize :" + actualValueSize + "<br> Expected Exact Text :" + expected + "ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text :" + actual + "ActualSize :" + actualValueSize + "<br> Expected Exact Text :" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched " + "Actual Text :" + actual + "ActualSize :" + actualValueSize + "<br> Expected Exact Text :" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
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
                    logReport("PASS", "Actual Text :" + actual + "ActualSize :" + actualValueSize + "<br> Expected Partial Text:" + expected + "ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text :" + actual + "ActualSize :" + actualValueSize + "<br> Expected Partial Text:" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched " + "Actual Text :" + actual + "ActualSize :" + actualValueSize + "  <br> Expected Partial Text:" + expected + "ExpectedSize :" + expectedValueSize + "Additional values in Actual List" + actuallist + "Additional values in Expected List" + expectedlist);
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
                    logReport("PASS", "Actual Text :" + actual + "ActualSize :" + actualValueSize + "  <br> Expected NOT EQUALS Text:" + expected + " ExpectedSize :" + expectedValueSize);
                } else {
                    actuallist.removeAll(expectedlist);
                    expectedlist.removeAll(actuallist);
                    logReport("FAIL", "Actual Text :" + actual + "ActualSize :" + actualValueSize + "  <br> Expected NOT EQUALS Text:" + expected + " ExpectedSize :" + expectedValueSize + " Additional values in Actual List" + actuallist + " Additional values in Expected List" + expectedlist);
                }
            } else {
                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                logReport("FAIL", "List Size mismatched " + "Actual Text :" + actual + "ActualSize :" + actualValueSize + "  <br> Expected NOT EQUALS Text:" + expected + " ExpectedSize :" + expectedValueSize + " Additional values in Actual List" + actuallist + " Additional values in Expected List" + expectedlist);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertEqualsWithScreenShot(String actual, String expected) {
        logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
        try {
            if (expected.equals(actual)) {
                logReportWithScreenShot("PASS", "Actual Text :" + actual + "<br> Expected Exact Text :" + expected);
            } else {
                sAssert.assertEquals(actual, expected);
                logReportWithScreenShot("FAIL", "Actual Text :" + actual + "<br> Expected Exact Text :" + expected);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void customAssertPartialWithScreenShot(String actual, String expected) {
        try {
            logger.info("Verifying  Actual : " + actual + " with Expected : " + expected + "");
            if (actual.contains(expected)) {
                logReportWithScreenShot("PASS", "Actual Text :" + actual + "<br> Expected Partial Text:" + expected);
            } else {
                logReportWithScreenShot("FAIL", "Actual Text :" + actual + "<br> Expected Partial Text:" + expected);
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

    protected void hardFail(String message, Exception e) {
        logReport("FAIL", message);
        e.printStackTrace();
        Assert.fail(message + e.getMessage());
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
            extentMethodNode.get().info("<span class=\"scenarioSpan\"> SCENARIO : </span>"+scenario);
        }
        if(extentScenarioNode.get()!=null){
            extentScenarioNode.get().assignAuthor(author);
            extentScenarioNode.get().getModel().setDescription(scenario);
            extentScenarioNode.get().info("<span class=\"scenarioSpan\"> SCENARIO : </span>"+scenario);
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
        String replacedMessage = message;
        logger.info(replacedMessage.replace("<br>",""));
        if((tEnv().getJenkins_execution().equalsIgnoreCase("true") || tEnv().getPipeline_execution().equalsIgnoreCase("true")) && tEnv().getTestType().equalsIgnoreCase("api")){

        }else{
            if (extentScenarioNode.get() != null) {
                screenshotInfo(extentScenarioNode.get(), message, screenshotMode);
            } else if (extentMethodNode.get() != null) {
                screenshotInfo(extentMethodNode.get(), message, screenshotMode);
            }
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
        String replacedMessage = message;
        logger.error(replacedMessage.replace("<br>",""));
        Thread.dumpStack();
        if (extentScenarioNode.get() != null) {
            screenshotFail(extentScenarioNode.get(), message, screenshotMode);
        } else if (extentMethodNode.get() != null) {
            screenshotFail(extentMethodNode.get(), message, screenshotMode);
        }
        if (failAnalysisThread.get() != null) {
            failAnalysisThread.get().add(message);
        }
        for (String tcID : testCaseIDThread.get().get(0).split(",")) {
            updateHashMapWithTCDetails(tcID, "FAIL", tEnv().getCurrentTestClassName(), tEnv().getCurrentTestMethodName());
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
        String replacedMessage = message;
        logger.info(replacedMessage.replace("<br>",""));
        if((tEnv().getJenkins_execution().equalsIgnoreCase("true") || tEnv().getPipeline_execution().equalsIgnoreCase("true")) && tEnv().getTestType().equalsIgnoreCase("api")){

        }else{
            if (extentScenarioNode.get() != null) {
                screenshotPass(extentScenarioNode.get(), message, screenshotMode);
            } else if (extentMethodNode.get() != null) {
                screenshotPass(extentMethodNode.get(), message, screenshotMode);
            }
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

    public  void tearDownGenerateTCStatusJson(){
        Gson gson = new Gson();
        String val = gson.toJson(resultTCCollectionMap,LinkedHashMap.class);
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter(trigonPaths.getTestResultsPath()+"/TestStatus.json")));
            writer.jsonValue(val);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void uploadSingleTestcaseStatus(String testRunId, String path) {
        TestRailManager t = new TestRailManager();
        Gson gson = new Gson();
        try {
            JsonElement ele = JsonParser.parseReader(new FileReader(path));
            JsonObject result = gson.fromJson(ele, JsonObject.class);
            result.getAsJsonObject().entrySet().forEach(class_methodName -> {
                class_methodName.getValue().getAsJsonObject().get("Passed").getAsJsonArray().forEach(passedCase -> {
                    try {
                        System.out.println(String.valueOf(passedCase.getAsNumber()).substring(1));
                        t.addTestResultForTestCase(testRunId, String.valueOf(passedCase.getAsNumber()).substring(1), TestRailManager.TEST_CASE_PASSED_STATUS,"Executed Test got passed after test execution");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                class_methodName.getValue().getAsJsonObject().get("Failed").getAsJsonObject().entrySet().forEach(failedCase -> {
                    try {
                        t.addTestResultForTestCase(testRunId, failedCase.getKey().substring(1), TestRailManager.TEST_CASE_FAILED_STATUS,failedCase.getValue().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                class_methodName.getValue().getAsJsonObject().get("Skipped").getAsJsonArray().forEach(skippedCase -> {
                    try {
                        t.addTestResultForTestCase(testRunId, String.valueOf(skippedCase.getAsNumber()).substring(1), TestRailManager.TEST_CASE_SKIPPED_STATUS,"Test got skipped due to some error occured in previous tests");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTestRunId(String product) {
        final String[] runId = {""};
        Gson gson = new Gson();
        try {
            JsonElement ele = JsonParser.parseReader(new FileReader("src/test/resources/Configuration/TestRunIds.json"));
            JsonObject result = gson.fromJson(ele, JsonObject.class);
            result.entrySet().stream()
                    .filter(MainProduct -> MainProduct.getKey().equalsIgnoreCase(product.split("_")[0]))
                    .forEach(subProduct -> {
                        subProduct.getValue().getAsJsonObject().entrySet().stream().filter(getSubProduct -> getSubProduct.getKey().equalsIgnoreCase(product.split("_")[1]))
                                .forEach(subProductRunId -> {
                                    runId[0] = String.valueOf(subProductRunId.getValue());
                                });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return runId[0];
    }

    public void getJsonToResultUpload(String path) {
        Gson gson = new Gson();
        try {
            JsonElement ele = JsonParser.parseReader(new FileReader(path));
            JsonObject result = gson.fromJson(ele, JsonObject.class);
            result.getAsJsonObject().entrySet().forEach(class_methodName -> {
                class_methodName.getValue().getAsJsonObject().get("Passed").getAsJsonArray().forEach(passedCase -> {
                    try {
                        String testCaseId = String.valueOf(passedCase.getAsNumber()).substring(1);
                        addTestCase(testCaseId,"1","Executed Test got passed after test execution");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                class_methodName.getValue().getAsJsonObject().get("Failed").getAsJsonObject().entrySet().forEach(failedCase -> {
                    try {
                        String testCaseId = failedCase.getKey().substring(1);
                        addTestCase(testCaseId,"4",failedCase.getValue().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                class_methodName.getValue().getAsJsonObject().get("Skipped").getAsJsonArray().forEach(skippedCase -> {
                    try {
                        String testCaseId =  String.valueOf(skippedCase.getAsNumber()).substring(1);
                        addTestCase(testCaseId,"5","Test got skipped due to some error occured in previous tests");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTestCase(String testCaseId, String statusId, String comment){
        resultMap = new HashMap<>();
        resultMap.put("case_id",testCaseId);
        resultMap.put("status_id", statusId);
        resultMap.put("comment", comment);
        resultList.add(resultMap);
    }

    public void uploadBulkTestResultToTestRail(String testRunId, String path){
        TestRailManager trm = new TestRailManager();
        getJsonToResultUpload(path);
        try {
            trm.addTestResultForTestCases(resultList, testRunId);
        }catch ( Exception e){
            e.printStackTrace();
        }

    }

}

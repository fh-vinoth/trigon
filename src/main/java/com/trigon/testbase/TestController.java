package com.trigon.testbase;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.remoteenv.RemoteEnvPojo;
import com.trigon.email.SendEmail;
import com.trigon.reports.EmailReport;
import com.trigon.testrail.BaseMethods;
import com.trigon.testrail.Runs;
import com.trigon.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.List;
import java.util.stream.Collectors;


public class TestController extends TestInitialization {
    private static final Logger logger = LogManager.getLogger(TestController.class);
    private static RemoteEnvPojo tre;

    @BeforeSuite(alwaysRun = true)
    protected void suiteInitialization(ITestContext iTestContext, XmlTest xmlTest) {
        try {
            logger.info("Test Execution Started for Suite : " + iTestContext.getSuite().getName());
            Gson pGson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement element1 = JsonParser.parseReader(new FileReader("tenv/remote-env.json"));
            tre = pGson.fromJson(element1, RemoteEnvPojo.class);
            executionType = tre.getExecution_type();
            pipelineExecution = tre.getPipeline_execution();
            propertiesPojo = setProperties();
            reportsInitialization(iTestContext.getSuite().getName());
            logInitialization(iTestContext.getSuite().getName());
            testSuiteCollectionBeforeSuite(iTestContext);
            testRailInit();
            if (executionType == null) {
                executionType = "local";
            }
            if (extent != null) {
                extent.setSystemInfo("testExecutionType", executionType);
            }

        } catch (FileNotFoundException fe) {
            captureException(fe);
        } catch (Exception e) {
            captureException(e);
        }
    }


    protected void moduleInitilalization(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                                         @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,
                                         @Optional String store, @Optional String host, @Optional String locale,
                                         @Optional String region, @Optional String country, @Optional String currency,
                                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName) {
        try {
            if (platformType != null) {
                logger.info("Test Execution Started for Module : " + xmlTest.getName());
                setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName);
//                addDataToHeader("URI: "+tEnv().getApiURI()+"","Host : "+tEnv().getApiHost()+"");
//                addHeaderToCustomReport("HTTPMethod","Endpoint","responseEmptyKeys","responseNullKeys","responseHtmlTagKeys","responseHtmlTagKeysAndValues");

                if (context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative")) {
                    remoteBrowserInit(context, xmlTest);
                }
                moduleFailAnalysisThread.set(new ArrayList<>());
                testModuleCollection(xmlTest.getName());
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void classInitialization(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                                       @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                                       @Optional String browserVersion, @Optional String device, @Optional String os_version,
                                       @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,
                                       @Optional String store, @Optional String host, @Optional String locale,
                                       @Optional String region, @Optional String country, @Optional String currency,
                                       @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName) {
        try {
            logger.info("Test Execution Started for Class  : " + getClass().getSimpleName());
            classFailAnalysisThread.set(new ArrayList<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName);
            if (context.getSuite().getName().contains("adhoc")) {
                remoteBrowserInit(context, xmlTest);
            }
            createExtentClassName(xmlTest);
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void setUp(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,
                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                         @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,
                         @Optional String store, @Optional String host, @Optional String locale,
                         @Optional String region, @Optional String country, @Optional String currency,
                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName) {
        logger.info("Test Execution Started for Method : " + method.getName());
        try {
            dataTableCollectionApi.set(new ArrayList<>());
            dataTableMapApi.set(new LinkedHashMap<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName);

            if (context.getSuite().getName().contains("adhoc") || context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative")) {

            } else {
                remoteBrowserInit(context, xmlTest);
            }
            if (context.getSuite().getName().contains("adhoc_parallel")) {
                remoteBrowserInit(context, xmlTest);
            }
            remoteMobileInit(context, xmlTest);
            if (!context.getSuite().getName().toLowerCase().startsWith("api") && (executionType.equalsIgnoreCase("remote"))) {
                logger.info("########################################################################################################");
                logger.info("BS Interactive Session -> " + bsVideo().get("public_url").toString());
                logger.info("########################################################################################################");
            }
            setMobileLocator();
            setWebLocator();
            failAnalysisThread.set(new ArrayList<>());
            tEnv().setContext(context);
            tEnv().setCurrentTestMethodName(method.getName());
            createExtentMethod(context, xmlTest, method);

        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void processTearDown(Method method, XmlTest xmlTest, ITestContext context, ITestResult result) {
        try {
            if (result.wasRetried()) {
                if (result.getStatus() == 3) {
                    if (extentMethodNode.get() != null) {
                        initFailedLogs = getInitialFailureMessages(xmlTest.getName().replaceAll("-", "_").replaceAll(" ", "_").trim(),
                                tEnv().getCurrentTestClassName(),
                                method.getName());
                        extent.removeTest(extentMethodNode.get());
                    }
                } else {
                    failStatusCheck(method);
                }
            } else {
                failStatusCheck(method);
            }

            if (result.getStatus() == 2 && initFailedLogs != null) {
                List<Log> currentLogs = extent.getReport().getTestList().stream().filter(modules -> xmlTest.getName().replaceAll("-", "_").replaceAll(" ", "_").trim().equalsIgnoreCase(modules.getName().substring(0, modules.getName().indexOf('<')))).findAny().get().getChildren().stream().filter(classes ->
                        tEnv().getCurrentTestClassName().equalsIgnoreCase(classes.getName())).findAny().get().getChildren().stream().filter(methods -> method.getName().equalsIgnoreCase(methods.getName())).findAny().get().getLogs().stream().filter(logs -> initFailedLogs.contains(logs.getDetails())).collect(Collectors.toList());

                for (Log currentLog : currentLogs) {
                    if (!currentLog.getStatus().toString().equalsIgnoreCase("Fail")) {
                        logReport("INFO", "<b>RETRY FAILURE</b> " + currentLog.getDetails());  //Initial Execution Failure Reporting
                    }
                    initFailedLogs.remove(currentLog.getDetails());
                }
                if (!initFailedLogs.isEmpty()) {
                    for (String logDetail : initFailedLogs)
                        logReport("INFO", "<b>RETRY FAILURE</b> " + logDetail);   //Initial Execution Failure Reporting
                }
            }
            if (context.getSuite().getName().contains("adhoc") || context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative")) {

            } else {
                closeBrowserClassLevel();
            }
            if (context.getSuite().getName().contains("adhoc_parallel")) {
                closeBrowserClassLevel();
            }
            closeMobileClassLevel();
            if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
                BaseMethods b = new BaseMethods();
                b.setTestCaseFinalStatus(runId, 1, "TEST PASSED", method.getName());
            }
            extentFlush();
            failAnalysisThread.remove();
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterClass(alwaysRun = true)
    protected void finalValidation(ITestContext context, XmlTest xmlTest) {
        try {
            dataTableCollectionApi.remove();
            logger.info("Test Execution Finished for Class  : " + getClass().getSimpleName());
            if (context.getSuite().getName().contains("adhoc")) {
                closeBrowserClassLevel();
            }
            if (classFailAnalysisThread.get().size() > 0) {
                if (moduleFailAnalysisThread.get() != null) {
                    moduleFailAnalysisThread.get().add("FAIL");
                }
            } else {
                if (extentClassNode.get() != null) {
                    extentClassNode.get().getModel().setStatus(Status.PASS);
                }

            }
            classFailAnalysisThread.remove();
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterTest(alwaysRun = true)
    protected void methodClosure(ITestContext context, XmlTest xmlTest) {
        try {
            logger.info("Test Execution Finished for Module : " + xmlTest.getName());
            if (context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative")) {
                closeBrowserClassLevel();
            }
            if (extentTestNode.get() != null) {
                if (moduleFailAnalysisThread.get().size() > 0) {

                } else {
                    if (extentTestNode.get() != null) {
                        extentTestNode.get().getModel().setStatus(Status.PASS);
                    }

                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterSuite(alwaysRun = true)
    protected void suiteClosure(ITestContext iTestContext, XmlTest xmlTest) {
        try {
            logger.info("Test Execution Finished for Suite : " + iTestContext.getSuite().getName());
            logger.info("Generating HTML Reports from the path :" + trigonPaths.getTestResultsPath());
            if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
                Runs r = new Runs();
                try {
                    r.closeRunById(runId);
                } catch (Exception e) {
                    captureException(e);
                }
            }
            tearDownCustomReport(iTestContext);

        } catch (Exception e) {
            captureException(e);
        } finally {
            if (!iTestContext.getSuite().getName().contains("adhoc")) {
                getGitBranch();
                if (apiCoverage.size() > 0) {
                    if (extent != null) {
                        extent.setSystemInfo("API Endpoints Covered", String.valueOf(totalEndpoints));
                        extent.flush();
                    }
                    getAPICoverage(apiCoverage);
                }
                EmailReport.createEmailReport(trigonPaths.getTestResultsPath(), extent, iTestContext.getSuite().getName(), platformType, executionType, pipelineExecution);

                if (executionType.equalsIgnoreCase("remote")) {
                    if (System.getProperty("user.name").equalsIgnoreCase("root") || System.getProperty("user.name").equalsIgnoreCase("ec2-user")) {
                        emailTrigger();
                    }
                }
            }

        }

    }

    private void emailTrigger() {
        SendEmail sendEmail = new SendEmail();
        boolean emailTriggerStatus = false;

        if (failure_email_recipients != null) {
            if (failStatus && !emailTriggerStatus) {
                sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(), failure_email_recipients, "true", "false");
                emailTriggerStatus = true;
            }
        }
        if (error_email_recipients != null) {
            if (exceptionStatus && !emailTriggerStatus) {
                sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(), error_email_recipients, "true", "false");
                emailTriggerStatus = true;
            }
        }
        if (email_recipients != null) {
            if (!failStatus && !emailTriggerStatus) {
                sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(), email_recipients, "true", "false");
            }
        }
    }

    private void failStatusCheck(Method method) {
        if (failAnalysisThread.get().size() > 0) {
            if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
                BaseMethods b = new BaseMethods();
                b.setTestCaseFinalStatus(runId, 4, failAnalysisThread.get().toString(), method.getName());
            }
            if (propertiesPojo.getEnable_jira().equalsIgnoreCase("true")) {
                createJiraTicket(method.getName() + " Test failed !! validate Issue ", failAnalysisThread.get().toString());
            }
            failStatus = true;
            classFailAnalysisThread.get().add("fail");
        } else {
            if (tEnv().getScreenshotPath() != null) {
                CommonUtils.fileOrFolderDelete(tEnv().getScreenshotPath());
            }
        }
    }

    private void getGitBranch() {
        try {
//            Process process = Runtime.getRuntime().exec( "git branch --show-current" );
//            Process process = Runtime.getRuntime().exec( "git rev-parse --abbrev-ref HEAD" );
            Process process = Runtime.getRuntime().exec("git rev-parse --abbrev-ref HEAD");
            process.waitFor();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            executedGitBranch = reader.readLine();
            if (executedGitBranch == null) {
                executedGitBranch = "git-branch";
            }

        } catch (Exception e) {

        }
    }

    public List<String> getInitialFailureMessages(String testName, String className, String methodName) {
        List<String> failedSteps = new ArrayList<>();
        extent.getReport().getTestList().stream().filter(modules -> testName.equalsIgnoreCase(modules.getName().substring(0, modules.getName().indexOf('<')))).findAny().get().getChildren().stream().filter(classes ->
                className.equalsIgnoreCase(classes.getName())).findAny().get().getChildren().stream().filter(methods -> methodName.equalsIgnoreCase(methods.getName())).findAny().get().getLogs().stream().filter(logs -> logs.getStatus().toString().equalsIgnoreCase("Fail")).collect(Collectors.toList()).forEach(failLog -> failedSteps.add(failLog.getDetails()));
        return failedSteps;
    }

}

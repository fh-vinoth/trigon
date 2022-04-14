package com.trigon.testbase;

import com.aventstack.extentreports.Status;
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
                                         @Optional String URI,@Optional String envType,@Optional String appSycURI,@Optional String appSycAuth, @Optional String version, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,
                                         @Optional String store, @Optional String host, @Optional String locale,
                                         @Optional String region, @Optional String country, @Optional String currency,
                                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName) {
        try {
            if (platformType != null) {
                logger.info("Test Execution Started for Module : " + xmlTest.getName());
                setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType,appSycURI,appSycAuth,version, token, accessToken, isJWT, endpointPrefix, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName);
//                addDataToHeader("URI: "+tEnv().getApiURI()+"","Host : "+tEnv().getApiHost()+"");
//                addHeaderToCustomReport("HTTPMethod","Endpoint","responseEmptyKeys","responseNullKeys","responseHtmlTagKeys","responseHtmlTagKeysAndValues");
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
                                       @Optional String URI, @Optional String envType,@Optional String appSycURI,@Optional String appSycAuth,@Optional String version, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,
                                       @Optional String store, @Optional String host, @Optional String locale,
                                       @Optional String region, @Optional String country, @Optional String currency,
                                       @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName) {
        try {
            logger.info("Test Execution Started for Class  : " + getClass().getSimpleName());
            classFailAnalysisThread.set(new ArrayList<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI,envType,appSycURI,appSycAuth, version, token, accessToken, isJWT, endpointPrefix, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName);
            if(context.getSuite().getName().contains("adhoc")){
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
                         @Optional String URI,@Optional String envType,@Optional String appSycURI,@Optional String appSycAuth, @Optional String version, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,
                         @Optional String store, @Optional String host, @Optional String locale,
                         @Optional String region, @Optional String country, @Optional String currency,
                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName) {
        logger.info("Test Execution Started for Method : " + method.getName());
        try {
            dataTableCollectionApi.set(new ArrayList<>());
            dataTableMapApi.set(new LinkedHashMap<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI,envType,appSycURI,appSycAuth,version, token, accessToken, isJWT, endpointPrefix, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName);

            if (!context.getSuite().getName().contains("adhoc")) {
                remoteBrowserInit(context, xmlTest);
            }
            if (context.getSuite().getName().contains("adhoc_parallel")) {
                remoteBrowserInit(context, xmlTest);
            }
            remoteMobileInit(context, xmlTest);
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
                        int sizeOfTestList = extent.getReport().getTestList().size();
                        A:     for (indexTest = 0; indexTest < sizeOfTestList; indexTest++) {
                            if (extent.getReport().getTestList().get(indexTest).getName().substring(0, extent.getReport().getTestList().get(indexTest).getName().indexOf('<')).equalsIgnoreCase(xmlTest.getName().replaceAll("-", "_").replaceAll(" ", "_").trim())) {
                                int sizeOfClasses = extent.getReport().getTestList().get(indexTest).getChildren().size();
                                for (indexClass = 0; indexClass < sizeOfClasses; indexClass++) {
                                    if (extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getName().equalsIgnoreCase(tEnv().getCurrentTestClassName())) {
                                        int sizeOfMethods = extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getChildren().size();
                                        for (indexMethod = 0; indexMethod < sizeOfMethods; indexMethod++) {
                                            if (extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getChildren().get(indexMethod).getName().equalsIgnoreCase(method.getName())) {
                                                sizeOfLogs = extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getChildren().get(indexMethod).getLogs().size() - 1;
                                                while (sizeOfLogs >= 0) {
                                                    if (extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getChildren().get(indexMethod).getLogs().get(sizeOfLogs).getStatus().toString().equalsIgnoreCase("Fail")) {
                                                        detailsOfLogs = extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getChildren().get(indexMethod).getLogs().get(sizeOfLogs).getDetails();
                                                        break A;
                                                    } else {
                                                        --sizeOfLogs;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        extent.removeTest(extentMethodNode.get());
                    }
                } else {
                    failStatusCheck(method);
                }
            } else {
                failStatusCheck(method);
            }
            if(result.getStatus()==2 && sizeOfLogs>0) {
                if(!extent.getReport().getTestList().get(indexTest).getChildren().get(indexClass).getChildren().get(indexMethod).getLogs().get(sizeOfLogs).getStatus().toString().equalsIgnoreCase("Fail")) {
                    logReport("Fail",detailsOfLogs);
                    sizeOfLogs=0;
                }
            }
            if (!context.getSuite().getName().contains("adhoc")) {
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
            if(context.getSuite().getName().contains("adhoc")){
                closeBrowserClassLevel();
            }
            if (classFailAnalysisThread.get().size() > 0) {
                if(moduleFailAnalysisThread.get()!=null){
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
    protected void methodClosure(XmlTest xmlTest) {
        try {
            logger.info("Test Execution Finished for Module : " + xmlTest.getName());
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

}

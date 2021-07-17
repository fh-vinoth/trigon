package com.trigon.testbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.TestMethodReporter;
import com.trigon.bean.remoteenv.RemoteEnvPojo;
import com.trigon.email.SendEmail;
import com.trigon.reports.EmailReport;
import com.trigon.reports.ReportGenerator;
import com.trigon.testrail.BaseMethods;
import com.trigon.testrail.Runs;
import com.trigon.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;
import org.testng.internal.annotations.IBeforeTest;
import org.testng.xml.XmlTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
            propertiesPojo = setProperties();
            suiteStartTime = System.currentTimeMillis();
            reportsInitialization(iTestContext.getSuite().getName());
            logInitialization();
            testSuiteCollectionBeforeSuite(iTestContext);
            testRailInit();
            if (executionType == null) {
                executionType = "local";
            }
        } catch (FileNotFoundException fe){
            captureException(fe);
        }catch (Exception e) {
            captureException(e);
        }
    }


    protected void moduleInitilalization(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath,@Optional String excelFilePath,
                                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String url, @Optional String browser,
                                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                                         @Optional String URI, @Optional String version, @Optional String token,
                                         @Optional String store, @Optional String host, @Optional String locale,
                                         @Optional String region, @Optional String country, @Optional String currency,
                                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId ,@Optional String test_region,@Optional String browserstack_execution_local) {
        try {
            if (platformType != null) {
                logger.info("Test Execution Started for Module : " + xmlTest.getName());
                setTestEnvironment(testEnvPath,excelFilePath,jsonFilePath,jsonDirectory,applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region,browserstack_execution_local,getClass().getSimpleName());
                testModuleCollection(xmlTest.getName());
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void classInitialization(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                                       @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String url,@Optional String browser,
                                       @Optional String browserVersion, @Optional String device, @Optional String os_version,
                                       @Optional String URI, @Optional String version, @Optional String token,
                                       @Optional String store, @Optional String host, @Optional String locale,
                                       @Optional String region, @Optional String country, @Optional String currency,
                                       @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region,@Optional String browserstack_execution_local) {
        try {
            logger.info("Test Execution Started for Class  : " + getClass().getSimpleName());
            classFailAnalysisThread.set(new ArrayList<>());
            setTestEnvironment(testEnvPath,excelFilePath,jsonFilePath,jsonDirectory,applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region,browserstack_execution_local,getClass().getSimpleName());
            createExtentClassName(xmlTest);
            remoteBrowserInit(context, xmlTest);
            remoteMobileInit(context, xmlTest);
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void setUp(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,
                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                         @Optional String URI, @Optional String version, @Optional String token,
                         @Optional String store, @Optional String host, @Optional String locale,
                         @Optional String region, @Optional String country, @Optional String currency,
                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local) {
        logger.info("Test Execution Started for Method : " + method.getName());
        try {
            dataTableCollectionApi.set(new ArrayList<>());
            dataTableMapApi.set(new LinkedHashMap<>());
            testThreadMethodReporter.set(new TestMethodReporter());
            testThreadMethodReporter.get().setContext(context);
            setTestEnvironment(testEnvPath,excelFilePath,jsonFilePath,jsonDirectory,applicationType, url,browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region,browserstack_execution_local,getClass().getSimpleName());
            setMobileLocator();
            setWebLocator();
            failAnalysisThread.set(new ArrayList<>());
            tEnv().setCurrentTestMethodName(method.getName());
            createExtentMethod(context,xmlTest,method);

        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void processTearDown(Method method, XmlTest xmlTest) {
        try {
            if (failAnalysisThread.get().size() > 0) {
                if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
                    BaseMethods b = new BaseMethods();
                    b.setTestCaseFinalStatus(runId, 4, failAnalysisThread.get().toString(), method.getName());
                }
                if (propertiesPojo.getEnable_jira().equalsIgnoreCase("true")) {
                    createJiraTicket(method.getName() + " Test failed !! validate Issue ", failAnalysisThread.get().toString());
                }
                failStatus = true;
            }else{
                if(tEnv().getScreenshotPath()!=null){
                    CommonUtils.fileOrFolderDelete(tEnv().getScreenshotPath());
                }
            }

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
    protected void finalValidation(XmlTest xmlTest) {
        try {
            dataTableCollectionApi.remove();
            logger.info("Test Execution Finished for Class  : " + getClass().getSimpleName());
            closeBrowserClassLevel();
            closeMobileClassLevel();
            classFailAnalysisThread.remove();
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterTest(alwaysRun = true)
    protected void methodClosure(XmlTest xmlTest) {
        try {
            logger.info("Test Execution Finished for Module : " + xmlTest.getName());
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

        } catch (Exception e) {
            captureException(e);
        } finally {

            EmailReport.createEmailReport(trigonPaths.getTestResultsPath(),extent);

            if(executionType.equalsIgnoreCase("remote"))
            {
                if(System.getProperty("user.name").equalsIgnoreCase("root"))
                {
                    SendEmail sendEmail = new SendEmail();
                    if(email_recipients !=null){
                        if(!failStatus){
                            sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(), email_recipients,"true","false","false");
                        }
                    }
                    if(failure_email_recipients !=null){
                        if(failStatus){
                            sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(), failure_email_recipients,"true","false","false");
                        }
                    }
                    if(error_email_recipients !=null){
                        if(exceptionStatus){
                            sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(), error_email_recipients,"true","false","false");
                        }
                    }
                }
            }
        }

    }

}

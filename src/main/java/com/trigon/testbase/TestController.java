package com.trigon.testbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.trigon.bean.TestMethodReporter;
import com.trigon.bean.TestModuleReporter;
import com.trigon.bean.remoteenv.RemoteEnvPojo;
import com.trigon.database.SSHSQLConnection;
import com.trigon.email.TriggerEmailImpl;
import com.trigon.reports.ReportGenerator;
import com.trigon.testrail.BaseMethods;
import com.trigon.testrail.Runs;
import com.trigon.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class TestController extends TestInitialization {
    private static final Logger logger = LogManager.getLogger(TestController.class);
    private static RemoteEnvPojo tre;


    @BeforeSuite(alwaysRun = true)
    protected void suiteInitialization(ITestContext iTestContext, XmlTest xmlTest) {
        try {

            File file = new File("errors");
            if (!file.exists()) {
                file.mkdir();
            }
            errorWriter.set(new JsonWriter(new BufferedWriter(new FileWriter("errors/errors_" + System.getProperty("user.name") + "_" + System.getProperty("os.name"), true))));
            errorWriter.get().setLenient(true);
            errorWriter.get().setIndent(" ");
            errorWriter.get().beginArray();

            try{
                Gson pGson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement element1 = JsonParser.parseReader(new FileReader("tenv/remote-env.json"));
                tre = pGson.fromJson(element1, RemoteEnvPojo.class);
                executionType = tre.getExecution_type();
            }catch (FileNotFoundException fe){
                captureException(fe);
            }

            logger.info("Test Execution Started for Suite : " + iTestContext.getSuite().getName());
            propertiesPojo = setProperties();
            suiteStartTime = System.currentTimeMillis();
            reportsInitialization(iTestContext.getSuite().getName());
            logInitialization();
            testSuiteCollectionBeforeSuite(iTestContext);
            testRailInit();
            if (executionType == null) {
                executionType = "local";
            }

        } catch (Exception e) {
            captureException(e);
        }
    }


    protected void moduleInitilalization(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath,@Optional String excelFilePath,
                                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String browser,
                                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                                         @Optional String URI, @Optional String version, @Optional String token,
                                         @Optional String store, @Optional String host, @Optional String locale,
                                         @Optional String region, @Optional String country, @Optional String currency,
                                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId) {
        try {
            if (platformType != null) {
                dBThreadLocal.set(new SSHSQLConnection());
                moduleReporter = new TestModuleReporter();
                moduleReporter.setTestModuleStartTime(System.currentTimeMillis());
                logger.info("Test Execution Started for Module : " + xmlTest.getName());
                setTestEnvironment(testEnvPath,excelFilePath,jsonFilePath,jsonDirectory,applicationType, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId);
                testModuleCollection(Thread.currentThread().getId(), xmlTest.getName(), testEnvPath);
            }

        } catch (Exception e) {
            captureException(e);
            hardFail("Check TestEnv JSON File and Rerun Test");
        }
    }

    protected void classInitialization(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                                       @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String browser,
                                       @Optional String browserVersion, @Optional String device, @Optional String os_version,
                                       @Optional String URI, @Optional String version, @Optional String token,
                                       @Optional String store, @Optional String host, @Optional String locale,
                                       @Optional String region, @Optional String country, @Optional String currency,
                                       @Optional String timezone, @Optional String phoneNumber, @Optional String emailId) {
        try {
            dBThreadLocal.set(new SSHSQLConnection());
            errorWriter.set(new JsonWriter(new BufferedWriter(new FileWriter("errors/errors_" + System.getProperty("user.name") + "_" + System.getProperty("os.name"), true))));
            errorWriter.get().setLenient(true);
            errorWriter.get().setIndent(" ");
            errorWriter.get().beginArray();
            classFailAnalysisThread.set(new ArrayList<>());
            logger.info("Test Execution Started for Class  : " + getClass().getSimpleName());
            setTestEnvironment(testEnvPath,excelFilePath,jsonFilePath,jsonDirectory,applicationType, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId);
            classCollection(getClass().getName(), xmlTest.getName(), testEnvPath);
            //createClassNodes(context, xmlTest);
            remoteBrowserInit(context, xmlTest);
            remoteMobileInit(context, xmlTest);

        } catch (Exception e) {
            captureException(e);
        }
    }


    protected void setUp(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath,@Optional String excelFilePath,
                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String browser,
                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                         @Optional String URI, @Optional String version, @Optional String token,
                         @Optional String store, @Optional String host, @Optional String locale,
                         @Optional String region, @Optional String country, @Optional String currency,
                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId) {
        logger.info("Test Execution Started for Method : " + method.getName());
        try {
            dBThreadLocal.set(new SSHSQLConnection());
            dataTableCollectionApi.set(new ArrayList<>());
            dataTableMapApi.set(new LinkedHashMap<>());
            setTestEnvironment(testEnvPath,excelFilePath,jsonFilePath,jsonDirectory,applicationType, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId);
            setMobileLocator();
            setWebLocator();
            failAnalysisThread.set(new ArrayList<>());
            testThreadMethodReporter.set(new TestMethodReporter());
            testThreadMethodReporter.get().setTestMethodStartTime(System.currentTimeMillis());
            classWriter.get().beginObject();
            classWriter.get().name("threadId").value(Thread.currentThread().getId());
            classWriter.get().name("testMethodName").value(method.getName());
            testThreadMethodReporter.get().setTestMethodName(method.getName());
            tEnv().setCurrentTestMethodName(method.getName());
            classWriter.get().name("testMethodStartTime").value(cUtils().getCurrentTimeStamp());
            if (tEnv().getTestType().equalsIgnoreCase("api")) {
                classWriter.get().name("apiTableData").beginArray();
            } else {
                classWriter.get().name("UITableData").beginArray();
            }
            classWriter.get().flush();

            if(tEnv().getTestType().equalsIgnoreCase("api")){
                String cls = getClass().getPackage().getName()+".testdata";
                String clsReplaced = cls.replaceAll("\\.","/");
                String finalPath = "src/test/java/"+clsReplaced;
                logger.info("testdata path is identified as "+finalPath);
                apiInputData = getApiInputs(finalPath);
            }

//            if (context.getIncludedGroups().length > 0) {
//                for (String abc : context.getIncludedGroups()) {
//                    parentTest.get().assignCategory(abc);
//                }
//            }

        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterMethod(alwaysRun = true)
    protected void processTearDown(Method method, XmlTest xmlTest) {
        try {
            classWriter.get().endArray().flush();
            if (failAnalysisThread.get().size() > 0) {
                testThreadMethodReporter.get().setTestStatus("FAILED");
                if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
                    BaseMethods b = new BaseMethods();
                    b.setTestCaseFinalStatus(runId, 4, testThreadMethodReporter.get().getTestAnalysis().toString(), method.getName(), classWriter);
                }
                if (propertiesPojo.getEnable_jira().equalsIgnoreCase("true")) {
                    createJiraTicket(method.getName() + " Test failed !! validate Issue ", testThreadMethodReporter.get().getTestAnalysis().toString());
                }
            }else{
                if(tEnv().getScreenshotPath()!=null){
                    CommonUtils.fileOrFolderDelete(tEnv().getScreenshotPath());
                }
            }

            if (testThreadMethodReporter.get() != null) {
                if (testThreadMethodReporter.get().getTestStatus().equalsIgnoreCase("PASSED")) {
                    if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
                        BaseMethods b = new BaseMethods();
                        b.setTestCaseFinalStatus(runId, 1, "TEST PASSED", method.getName(), classWriter);
                    }
                }
            }
            testThreadMethodReporter.get().setTestAnalysis(failAnalysisThread.get());
            classWriter.get().name("testMethodAuthorName").value(testThreadMethodReporter.get().getTestAuthor());
            classWriter.get().name("testMethodScenario").value(testThreadMethodReporter.get().getTestScenarioName());
            classWriter.get().name("testMethodStatus").value(testThreadMethodReporter.get().getTestStatus());
            if (testThreadMethodReporter.get().getTestAnalysis().size() > 0) {
                classWriter.get().name("testMethodAnalysis").beginArray();
                testThreadMethodReporter.get().getTestAnalysis().forEach(item -> {
                    try {
                        classWriter.get().value(item);
                    } catch (IOException e) {
                        captureException(e);
                    }
                });
                classWriter.get().endArray();
            } else {
                if (classFailAnalysisThread.get().size() > 0) {
                    classWriter.get().name("testMethodAnalysis").beginArray();
                    classFailAnalysisThread.get().forEach(fail -> {
                        try {
                            classWriter.get().value(fail);
                        } catch (IOException e) {
                            captureException(e);
                        }
                    });
                    classWriter.get().endArray();
                }

            }
            classWriter.get().name("testMethodEndTime").value(cUtils().getCurrentTimeStamp());
            classWriter.get().name("testMethodDuration").value(cUtils().getRunDuration(testThreadMethodReporter.get().getTestMethodStartTime(), System.currentTimeMillis()));
            classWriter.get().endObject().flush();
            logger.info("Test Execution Finished for Method : " + method.getName());
            //flushAll();
            failAnalysisThread.remove();
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterClass(alwaysRun = true)
    protected void finalValidation(XmlTest xmlTest) {
        try {
            dataTableCollectionApi.remove();
            classWriter.get().endArray().endObject().flush();
            classWriter.get().close();
            logger.info("Test Execution Finished for Class  : " + getClass().getSimpleName());
            //flushAll();
            closeBrowserClassLevel();
            closeMobileClassLevel();
            classWriter.remove();
            errorWriter.get().endArray().flush();
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterTest(alwaysRun = true)
    protected void methodClosure(XmlTest xmlTest) {
        try {
            logger.info("Test Execution Finished for Module : " + xmlTest.getName());
            testModuleWriter.get().name("testModuleEndTime").value(cUtils().getCurrentTimeStamp());
            testModuleWriter.get().name("testModuleDuration").value(cUtils().getRunDuration(moduleReporter.getTestModuleStartTime(), System.currentTimeMillis()));
            testModuleWriter.get().endObject();
            testModuleWriter.get().flush();
            testModuleWriter.get().close();
            testModuleWriter.remove();
        } catch (Exception e) {
            captureException(e);
        }
    }

    @AfterSuite(alwaysRun = true)
    protected void suiteClosure(ITestContext iTestContext, XmlTest xmlTest) {
        try {
            testSuiteCollectionAfterSuite();
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
            testSuiteWriter.remove();
            try{
                errorWriter.get().endArray().flush();
            }catch (IllegalStateException ie){
            }

        } catch (Exception e) {
            captureException(e);
        } finally {
            ReportGenerator rg = new ReportGenerator();
            rg.generateHTMLReports(trigonPaths.getTestResultsPath());
//            SendEmail sendEmail = new SendEmail();
//            sendEmail.SendOfflineEmail(trigonPaths.getTestResultsPath(),"automation@touch2success.com","true","false","false");
            logger.info("Successfully Generated HTML Reports Refer to the path:" + trigonPaths.getTestResultsPath());

            TriggerEmailImpl TriggerEmailImpl = new  TriggerEmailImpl();
            TriggerEmailImpl.uploadErrorsToAWS();
        }

    }

}

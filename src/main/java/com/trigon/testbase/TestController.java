package com.trigon.testbase;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.remoteenv.RemoteEnvPojo;
import com.trigon.email.SendEmail;
import com.trigon.exceptions.ThrowableTypeAdapter;
import com.trigon.reports.EmailReport;
import com.trigon.testrail.BaseMethods;
import com.trigon.testrail.Runs;
import com.trigon.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import org.testng.xml.XmlTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


public class TestController extends TestInitialization {
    private static final Logger logger = LogManager.getLogger(TestController.class);
    private static RemoteEnvPojo tre;

    @BeforeSuite(alwaysRun = true)
    protected void suiteInitialization(ITestContext iTestContext, XmlTest xmlTest) {
        try {
            logger.info("Test Execution Started for Suite : " + iTestContext.getSuite().getName());
            Gson pGson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
            JsonElement element1 = JsonParser.parseReader(new FileReader("tenv/remote-env.json"));
            tre = pGson.fromJson(element1, RemoteEnvPojo.class);
            executionType = tre.getExecution_type();
            grid_execution_local=tre.getGrid_execution_local();
            gps_location=tre.getGps_location();
            networkProfile=tre.getNetworkProfile();
            customNetwork=tre.getCustomNetwork();
            customNetwork=tre.getCustomNetwork();
            initialSelfHeal=tre.getInitialSelfHeal();
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
                                         @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix, @Optional String authorization, @Optional String franchiseId, @Optional String dbType, @Optional String serviceType,
                                         @Optional String store, @Optional String host, @Optional String locale,
                                         @Optional String region, @Optional String country, @Optional String currency,
                                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps, @Optional String unblockToken, @Optional String networkProfile, @Optional String customNetwork, @Optional String initialSelfHeal, @Optional String healingMatchScore) {
        try {
            if (platformType != null) {
                logger.info("Test Execution Started for Module : " + xmlTest.getName());
                setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType,appSycURI,appSycAuth,version,partnerURI, token, accessToken, isJWT,franchiseId,dbType,serviceType, endpointPrefix,authorization, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName, grid_Hub_IP,gps_location,moduleNames,email_recipients,error_email_recipients,failure_email_recipients,browserstack_midSessionInstallApps,unblockToken,networkProfile,customNetwork,initialSelfHeal,healingMatchScore);
//                addDataToHeader("URI: "+tEnv().getApiURI()+"","Host : "+tEnv().getApiHost()+"");
//                addHeaderToCustomReport("HTTPMethod","Endpoint","responseEmptyKeys","responseNullKeys","responseHtmlTagKeys","responseHtmlTagKeysAndValues");

                if (context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp") ) {
                    remoteBrowserInit(context, xmlTest);
                    remoteMobileInit(context, xmlTest);
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
                                       @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix, @Optional String authorization, @Optional String franchiseId, @Optional String dbType, @Optional String serviceType,
                                       @Optional String store, @Optional String host, @Optional String locale,
                                       @Optional String region, @Optional String country, @Optional String currency,
                                       @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps, @Optional String unblockToken, @Optional String networkProfile, @Optional String customNetwork, @Optional String initialSelfHeal, @Optional String healingMatchScore) {
        try {
            logger.info("Test Execution Started for Class  : " + getClass().getSimpleName());
            classFailAnalysisThread.set(new ArrayList<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI,envType,appSycURI,appSycAuth, version,partnerURI, token, accessToken, isJWT,franchiseId,dbType,serviceType, endpointPrefix,authorization ,store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName, grid_Hub_IP, gps_location,moduleNames,email_recipients,error_email_recipients,failure_email_recipients,browserstack_midSessionInstallApps,unblockToken,networkProfile,customNetwork,initialSelfHeal,healingMatchScore);
            if(context.getSuite().getName().contains("adhoc")){
                remoteBrowserInit(context, xmlTest);
                remoteMobileInit(context, xmlTest);
            }

            createExtentClassName(xmlTest);

        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void setUp(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,
                         @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                         @Optional String browserVersion, @Optional String device, @Optional String os_version,
                         @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix, @Optional String authorization, @Optional String franchiseId, @Optional String dbType, @Optional String serviceType,
                         @Optional String store, @Optional String host, @Optional String locale,
                         @Optional String region, @Optional String country, @Optional String currency,
                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps, @Optional String unblockToken, @Optional String networkProfile, @Optional String customNetwork, @Optional String initialSelfHeal, @Optional String healingMatchScore) {
        logger.info("Test Execution Started for Method : " + method.getName());
        try {
            dataTableCollectionApi.set(new ArrayList<>());
            dataTableMapApi.set(new LinkedHashMap<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI,envType,appSycURI,appSycAuth,version,partnerURI, token, accessToken, isJWT, endpointPrefix,authorization,franchiseId,dbType,serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName,grid_Hub_IP,gps_location,moduleNames,email_recipients,error_email_recipients,failure_email_recipients,browserstack_midSessionInstallApps,unblockToken,networkProfile,customNetwork,initialSelfHeal,healingMatchScore);

            if (context.getSuite().getName().contains("adhoc") || context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp") ) {

            } else {
                remoteBrowserInit(context, xmlTest);
                remoteMobileInit(context, xmlTest);
            }
            if (context.getSuite().getName().contains("adhoc_parallel")) {
                remoteBrowserInit(context, xmlTest);
                remoteMobileInit(context, xmlTest);
            }

            if (!context.getSuite().getName().toLowerCase().startsWith("api") && (executionType.equalsIgnoreCase("remote"))) {
                logger.info("########################################################################################################");
                logger.info("BS Interactive Session -> " + bsVideo().get("public_url").toString());
                logger.info("########################################################################################################");
            }
            setMobileLocator();
            setWebLocator();
            failAnalysisThread.set(new ArrayList<>());
            testCaseIDThread.set(new ArrayList<>());
            passedTCs.set(new ArrayList<>());
            failedTCs.set(new HashMap<>());
            skippedTCs.set(new ArrayList<>());
            resultTCs.set(new HashMap<>());

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
            String status = "NA";
            if (result.wasRetried()) {
                if (result.getStatus() == 3) {
                    if (extentMethodNode.get() != null) {
                        initFailedLogs = getInitialFailureMessages(xmlTest.getName().replaceAll("-", "_").replaceAll(" ", "_").trim(),
                                tEnv().getCurrentTestClassName(),
                                method.getName());
                        extent.removeTest(extentMethodNode.get());
                    }
                } else {
                    status = failStatusCheck(method);
                }
            } else {
                status = failStatusCheck(method);
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
            if(result.getStatus()==-1){
                extentMethodNode.get().getModel().setStatus(Status.SKIP);
            }
            if (context.getSuite().getName().contains("adhoc") || context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp")) {

            } else {
                closeBrowserClassLevel();
                closeMobileClassLevel();
            }
            if (context.getSuite().getName().contains("adhoc_parallel")) {
                closeBrowserClassLevel();
                closeMobileClassLevel();
            }

            if (executionType.equalsIgnoreCase("remote")  && tEnv().getApiEnvType().equalsIgnoreCase("SIT")) {
//                adb.insertData(method, xmlTest, context, result,status);
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
    protected void finalValidation(ITestContext context, XmlTest xmlTest) {
        try {
            dataTableCollectionApi.remove();
            logger.info("Test Execution Finished for Class  : " + getClass().getSimpleName());
            if (context.getSuite().getName().contains("adhoc")) {
                closeBrowserClassLevel();
                closeMobileClassLevel();
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
            if (context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp") ) {
                closeBrowserClassLevel();
                closeMobileClassLevel();
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
//            tearDownGenerateTCStatusJson();
            tearDownCustomReport(iTestContext);

        } catch (Exception e) {
            captureException(e);
        } finally {
            if (!iTestContext.getSuite().getName().contains("adhoc")) {
                getGitBranch();
                if (apiCoverage.size() > 0) {
                    if (extent != null) {
//                        getEndpointCount();
//                        getApiCallCount();
                        extent.setSystemInfo("API Endpoints Covered", String.valueOf(totalEndpoints));
                        extent.flush();
                    }
                    getAPICoverage(apiCoverage);
                }
                EmailReport.createEmailReport(trigonPaths.getTestResultsPath(), extent, iTestContext.getSuite().getName(), platformType, executionType, pipelineExecution);

                if (executionType.equalsIgnoreCase("remote")) {
                    execute = true;

                    if (getSuiteNameWithTime.toLowerCase().contains("module") && reportModuleRun < 1) {
                        reportModuleRun = reportModuleRun + 1;
                        execute = true;
                    } else if (getSuiteNameWithTime.toLowerCase().contains("module") && reportModuleRun >= 1) {
                        execute = false;
                    }

                    if (execute) {
                        if (System.getProperty("user.name").equalsIgnoreCase("root") || System.getProperty("user.name").equalsIgnoreCase("ec2-user")) {
                            emailTrigger();
                        }
                    }
                }
            }

        }

    }

//    private void getApiCallCount() {
//        for (int i = 0; i < apiCallCoverage.size(); i++) {
//            if (apiCallCoverage.get(i).toString().equalsIgnoreCase("POST")) {
//                postRequest++;
//            }
//            if (apiCallCoverage.get(i).toString().equalsIgnoreCase("Get")) {
//                getRequest++;
//            }
//            if (apiCallCoverage.get(i).toString().equalsIgnoreCase("put")) {
//                putRequest++;
//            }
//            if (apiCallCoverage.get(i).toString().equalsIgnoreCase("delete")) {
//                deleteRequest++;
//            }
//            if (apiCallCoverage.get(i).toString().equalsIgnoreCase("patch")) {
//                patchRequest++;
//            }
//        }

//    }

//    private void getEndpointCount() {
//        Set endpoint = new LinkedHashSet();
//        List actualEndpoints = apiCoverage;
//        List http = apiCallCoverage;
//        String addedendpoint = "";
//        Map<String, Integer> endpointCount = new HashMap<>();
//        actualEndpoints.stream().forEach(c -> {
//            String ep = c.toString();
//            ep = ep.replaceAll("[0-9]", "");
//            if (endpointCount.containsKey(ep)) {
//                endpointCount.put(ep, endpointCount.get(ep) + 1);
//            } else {
//                endpointCount.put(ep, 1);
//            }
//        });
//        for (int i = 0; i < actualEndpoints.size(); i++) {
//            String endPoint = actualEndpoints.get(i).toString();
//            String[] split = new String[0];
//            if (endPoint.contains("/")) {
//                if (endPoint.startsWith("/")) {
//                    char charOfSlash = endPoint.charAt(0);
//                    endPoint = endPoint.replaceFirst(String.valueOf(charOfSlash), "");
//                }
//                split = endPoint.split("/");
//                for (int j = 0; j < split.length; j++) {
//                    String splitt = split[j];
//                    char firstChar = splitt.charAt(0);
//                    int asciivalue = firstChar;
//                    if (asciivalue >= 97 && asciivalue <= 122) {
//                        String endendpoint = split[j];
//                        addedendpoint = addedendpoint + endendpoint + "/";
//                    } else {
//                        addedendpoint = addedendpoint + "12345/";
//                    }
//                    if (j == split.length - 1) {
//                        if (addedendpoint.contains("?")) {
//                            String[] splitQueryParam = addedendpoint.split("\\?");
//                            addedendpoint = splitQueryParam[0];
//                        }
//                        endpoint.add(addedendpoint + "[" + http.get(i) + "]");
//                        addedendpoint = "";
//                    }
//                }
//            } else {
//                endpoint.add(endPoint + "/" + "[" + http.get(i) + "]");
//            }
//        }
//        totalEndpoints = endpoint.size();
//
//        System.out.println("API Endpoints Covered :" + totalEndpoints);
//    }

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

    private String failStatusCheck(Method method) {
        String status = "NA";
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
            status = "FAIL";
        } else {
            status = "PASS";
            if (tEnv().getScreenshotPath() != null) {
                CommonUtils.fileOrFolderDelete(tEnv().getScreenshotPath());
            }
        }
        return status;
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

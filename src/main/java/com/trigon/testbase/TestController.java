package com.trigon.testbase;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;
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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import org.testng.xml.XmlTest;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
            grid_execution_local = tre.getGrid_execution_local();
            gps_location = tre.getGps_location();
            networkProfile = tre.getNetworkProfile();
            customNetwork = tre.getCustomNetwork();
            customNetwork = tre.getCustomNetwork();
            initialSelfHeal = tre.getInitialSelfHeal();
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
            try {
                fw = new ThreadLocal<>();
            } catch (Exception e) {
                captureException(e);
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
                                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps, @Optional String unblockToken, @Optional String networkProfile, @Optional String customNetwork, @Optional String initialSelfHeal, @Optional String healingMatchScore, @Optional String device_location) {
        try {
            if (platformType != null) {
                logger.info("Test Execution Started for Module : " + xmlTest.getName());
                setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, franchiseId, dbType, serviceType, endpointPrefix, authorization, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName, grid_Hub_IP, gps_location, moduleNames, email_recipients, error_email_recipients, failure_email_recipients, browserstack_midSessionInstallApps, unblockToken, networkProfile, customNetwork, initialSelfHeal, healingMatchScore, device_location);
//                addDataToHeader("URI: "+tEnv().getApiURI()+"","Host : "+tEnv().getApiHost()+"");
//                addHeaderToCustomReport("HTTPMethod","Endpoint","responseEmptyKeys","responseNullKeys","responseHtmlTagKeys","responseHtmlTagKeysAndValues");

                if (context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp")) {
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
                                       @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps, @Optional String unblockToken, @Optional String networkProfile, @Optional String customNetwork, @Optional String initialSelfHeal, @Optional String healingMatchScore, @Optional String device_location) {
        try {
            logger.info("Test Execution Started for Class  : " + getClass().getSimpleName());
            classFailAnalysisThread.set(new ArrayList<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, franchiseId, dbType, serviceType, endpointPrefix, authorization, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName, grid_Hub_IP, gps_location, moduleNames, email_recipients, error_email_recipients, failure_email_recipients, browserstack_midSessionInstallApps, unblockToken, networkProfile, customNetwork, initialSelfHeal, healingMatchScore, device_location);
            if (context.getSuite().getName().contains("adhoc")) {
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
                         @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps, @Optional String unblockToken, @Optional String networkProfile, @Optional String customNetwork, @Optional String initialSelfHeal, @Optional String healingMatchScore, @Optional String device_location) {
        logger.info("Test Execution Started for Method : " + method.getName());
        try {
            dataTableCollectionApi.set(new ArrayList<>());
            dataTableMapApi.set(new LinkedHashMap<>());
            setTestEnvironment(testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, authorization, franchiseId, dbType, serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, getClass().getSimpleName(), bs_app_path, productName, grid_Hub_IP, gps_location, moduleNames, email_recipients, error_email_recipients, failure_email_recipients, browserstack_midSessionInstallApps, unblockToken, networkProfile, customNetwork, initialSelfHeal, healingMatchScore, device_location);

            if (context.getSuite().getName().contains("adhoc") || context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp")) {

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
                logger.info("BS Interactive buildId -> " + bsVideo().get("build_hashed_id").toString());
                buildId = bsVideo().get("build_hashed_id").toString();
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
            methodData.put(method.getName(), methodData.getOrDefault(method.getName(), 0) + 1);
            if (methodData.get(method.getName()) == 1) {
                createExtentMethod(context, xmlTest, method.getName());
                methodIdentifier.set(2);
            } else {
                createExtentMethod(context, xmlTest, method.getName() + " [" + methodIdentifier.get() + "]");
                methodIdentifier.getAndIncrement();
            }

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
            if (result.getStatus() == -1) {
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

            if (executionType.equalsIgnoreCase("remote") && tEnv().getApiEnvType().equalsIgnoreCase("SIT")) {
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
            if (context.getSuite().getName().contains("msweb") || context.getSuite().getName().toLowerCase().startsWith("fhnative") || context.getSuite().getName().contains("msapp")) {
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
                if (extent != null) {
                    extent.setSystemInfo("API Endpoints Covered", String.valueOf(totalEndpoints));
                    extent.flush();
                }
                try {
                    parseExtent();
                    getAPICoverage(apiCoverage.get());
                } catch (Exception e) {
                    captureException(e);
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

    /********************ProcessStarted*************************/
    String testName = null;
    Map<String, Integer> classEndpoints = new LinkedHashMap<>();
    List<String> testNgTest = new LinkedList<>();
    List<String> testNgtest = new LinkedList<>();
    Map<String, Integer> endpointData = new LinkedHashMap<>();
    List<String> suiteTests = new LinkedList<>();
    Map<String, List<String>> TestNgData = new LinkedHashMap<>();
    List<com.aventstack.extentreports.model.Test> classes = new ArrayList<>();
    /*Map<List<String>, Map<String, List<String>>> var = new LinkedHashMap<>();*/
    List<com.aventstack.extentreports.model.Test> methods = new ArrayList<>();

    StringBuilder html = new StringBuilder();
    List<String> clss = new LinkedList<>();
    List<String> respTime = new LinkedList<>();
    List<String> apiTestStatus = new LinkedList<>();
    List<String> apicard = new LinkedList<>();

    private void parseExtent() {
        try {
            extent.getReport().getTestList().stream().forEach(test -> {
                test.getChildren().stream().forEach(classToLoad -> {
                    classToLoad.getChildren().stream().forEach(method -> {
                        method.getLogs().stream().forEach(log -> {
                            if (!log.getStatus().getName().toLowerCase().contains("info") && !log.getDetails().contains("Actual Text") && !log.getDetails().contains("Failed API")) {
                                if (log.getDetails().contains("<pre class=\"preCode\"><code  id=\"string-curl")) {
                                    String curlCommand = extractCurlCommand(log.getDetails());
                                    apicard.add(curlCommand);
                                    fetchFromHtml(log.getDetails(), method.getName(), curlCommand);
                                }

                            }
                        });
                    });
                });
            });
            getEndpointCount();
            apiCallsTotal = apiHttp.size();
            String piechart = piechart(apiHttp);
            List<Test> tests = extent.getReport().getTestList();

            AtomicInteger io = new AtomicInteger(0);
            StringBuilder writer = new StringBuilder();
            writer.append("<!DOCTYPE html>\n" +
                    "<html>");//headder
            writer.append(piechart);//chart

            writer.append("<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>AutomationReport</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div align=\"center\">\n" +
                    "    <h1 style=\"font-family: Arial, sans-serif; font-size: 20px; \">Endpoints Data</h1>\n" +
                    "    <table style=\"width:96%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 3px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: LightGray\">\n" +
                    "        <thead>\n" +
                    "        <th>Test names</th>\n" +
                    "        <th>Class name</th>\n" +
                    "        <th>Methed name</th>\n" +
                    "        <th>Enpoints</th>\n" +
                    "        <th>Endpoint Status</th>\n" +
                    "        <th>Curl</th>" +
                    "        <th>Time Taken</th>" +
                    /*"        <th>Status</th>" +*/
                    "");//ColumnNames
            tests.stream().forEach(test -> {
                io.set(0);
                tempEndpointVariable.set(0);
                apiCoverage.get().clear();
                endpointData.clear();
                testNgtest.clear();
                testNgTest.clear();
                classes.clear();
                respTime.clear();
                methods.clear();
                clss.clear();
                TestNgData.clear();
                apiTestStatus.clear();
                apicard.clear();
                apiHttp.clear();
                testName = test.getName().split("<div")[0];
                suiteTests.add(testName);
                //System.out.println("Test Name: " + testName);
                /************************Tests*********************************/
                classes = test.getChildren();
                classes.stream().forEach(classToLoad -> {
                    tempEndpointVariable = new AtomicInteger();
                    //System.out.println("Class Name: " + classToLoad.getName());
                    clss.add(classToLoad.getName());

                    /********************Class*************************/
                    methods = classToLoad.getChildren();
                    //System.out.println(methods);

                    methods.stream().forEach(method -> {
                        testNgtest.add(method.getName());
                        testNgTest.add(method.getName());
                        List<Log> logs = method.getLogs();

                        /******************Method***************************/
                        logs.stream().forEach(log -> {
                            if (!log.getStatus().getName().toLowerCase().contains("info") && !log.getDetails().contains("Actual Text") && !log.getDetails().contains("Failed API")) {
                                if (log.getDetails().contains("<pre class=\"preCode\"><code  id=\"string-curl")) {
                                    String curlCommand = extractCurlCommand(log.getDetails());
                                    //System.out.println("curlCommand" + curlCommand);
                                    apicard.add(curlCommand);
                                    fetchFromHtml(log.getDetails(), method.getName(), curlCommand);
                                }

                            }
                        });
                        endpointValueAssign(classToLoad.getName(), tempEndpointVariable.get());
                    });
                    TestNgData.put(classToLoad.getName(), testNgtest);
                    testNgtest = new LinkedList<>();
                });
                // parseExtentData();
                writeHtmlLog(io, writer);
            });
            writer.delete(writer.length() - 5, writer.length() - 1);
            writer.append("                <div id=\"copyMessage\">Copied to clipboard</div>\n");
            writer.append("<style>\n" +
                    "    table {\n" +
                    "    font-family: arial, sans-serif;\n" +
                    "    border-collapse: collapse;\n" +
                    "    width: 75%;\n" +
                    "}\n" +
                    "\n" +
                    "td, th {\n" +
                    "    border: 1px solid #0a0a0a;\n" +
                    "    text-align: centre;\n" +
                    "    padding: 5px;\n" +
                    "}\n" +
                    "</style>");
            writer.append("            </thead>\n" +
                    "        </table>\n" +
                    " <p class=\"blank-line\"></p>" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>");
            writeHtmlToFile(trigonPaths.getTestResultsPath() + "/updatedLogs.html", writer.toString());
            /*pieChart(apiHttp);*/

        } catch (Exception e) {
            captureException(e);
        }
    }

    private void endpointValueAssign(String key, int value) {
        try {
            classEndpoints.put(key, value);
        } catch (Exception e) {

        }
    }

    private void parseExtentData() {
        try {
            //System.out.println("test loaded " + testName);
            //System.out.println("endpoint loaded " + apiCoverage.size());
            //System.out.println("totalclasses loaded: " + clss.size());
            //System.out.println("methods loaded: " + testNgTest.size());
        } catch (Exception e) {
            captureException(e);
        }
    }

    private void writeHtmlLog(AtomicInteger io, StringBuilder writer) {
        try {

            /*-*************************Main ForEach Started ***********************-*/
            int span = 0;
            writer.append(" <tr>\n" +
                    "                    <td rowspan=\"" + apiCoverage.get().size() + "\">" + testName + "</td>\n");//testName
            for (int j = 0; j < clss.size(); j++) {
                if (classEndpoints.containsKey(clss.get(j))) {
                    span = classEndpoints.get(clss.get(j));
                }
                writer.append(" <td rowspan=\"" + span + "\">" + clss.get(j) + "</td>\n");//ClassName
                for (int k = 0; k < TestNgData.get(clss.get(j)).size(); k++) {
                    if (endpointData.containsKey(testNgTest.get(k))) {
                        //  span = endpointData.get(testNgTest.get(k));
                        span = endpointData.get(TestNgData.get(clss.get(j)).get(k));
                        //System.out.println(endpointData.get(testNgTest.get(k)));
                        //System.out.println(endpointData.get(TestNgData.get(clss.get(j)).get(k)));
                        writer.append("<td rowspan=\"").append(span).append("\">").append(TestNgData.get(clss.get(j)).get(k)).append("</td>\n");//testMethodNAme
                        for (int l = 0; l < endpointData.get(TestNgData.get(clss.get(j)).get(k)); l++) {
                            //System.out.println("current length :" + endpointData.get(TestNgData.get(clss.get(j)).get(k)));
                            writer.append("<td>").append(apiCoverage.get().get(io.get())).append(" [").append(apiHttp.get(io.get())).append("]</td>\n");//endpointName

                            if (apiTestStatus.get(io.get()).equalsIgnoreCase("passed")) {
                                writer.append("<td class=\"passed\" >").append(apiTestStatus.get(io.get())).append("</td>\n");//apiTestStatus\\Endpoint status
                                writer.append(copyClipBoard("passed", apicard.get(io.get())));
                            } else if (apiTestStatus.get(io.get()).equalsIgnoreCase("failed")) {
                                writer.append("<td class=\"failed\" >").append(apiTestStatus.get(io.get())).append("</td>\n");//apiTestStatus\\Endpoint status
                                writer.append(copyClipBoard("failed", apicard.get(io.get())));
                            } else {
                                writer.append("<td>").append(apiTestStatus.get(io.get())).append("</td>\n");//apiTestStatus
                                writer.append(copyClipBoard("failed", apicard.get(io.get())));
                            }
                            if (Double.parseDouble(respTime.get(io.get())) > 2) {
                                writer.append("<td>" + respTime.get(io.get()) + " [&#9888;]</td>\n");//responseTime
                                //writer.append("<td> <!-- Display warning symbol if the value is greater than 1 -->\n" + " &#9888; </td>\n");//responseTime
                            } else if (Double.parseDouble(respTime.get(io.get())) < 2) {
                                writer.append("<td>" + respTime.get(io.get()) + " [&#10004;]</td>\n");//responseTime
                                // writer.append("<td><!-- Display symbols based on the value -->\n" + "&#10004;</td>\n");//responseTime
                            }

                            if (apiCoverage.get().size() > 1) {
                                writer.append("</tr>");
                                writer.append("<tr>");
                            }
                            /* endpointData(writer, span);*/
                            io.getAndIncrement();
                       /*     if (l == endpointData.get(TestNgData.get(clss.get(j)).get(k)) - 1) {
                                io.set(l + 1);
                                  io.set(io.get()+1);
                            }*/
                        }
                    }
                }
            }

        } catch (Exception e) {
            writer.delete(writer.length() - 5, writer.length() - 1);
//            writer.deleteCharAt(writer.length() - 1);
            writer.append("                <div id=\"copyMessage\">Copied to clipboard</div>\n");
            writer.append("<style>\n" +
                    "    table {\n" +
                    "    font-family: arial, sans-serif;\n" +
                    "    border-collapse: collapse;\n" +
                    "    width: 75%;\n" +
                    "}\n" +
                    "\n" +
                    "td, th {\n" +
                    "    border: 1px solid #dddddd;\n" +
                    "    text-align: centre;\n" +
                    "    padding: 8px;\n" +
                    "}\n" +
                    "</style>");
            writer.append("<script>\n" +
                    "    // Get the select element\n" +
                    "    const filterSelect = document.getElementById(\"filterSelect\");\n" +
                    "\n" +
                    "    // Get all the table rows except the header row\n" +
                    "    const rows = document.querySelectorAll(\"table tr:not(:first-child)\");\n" +
                    "\n" +
                    "    // Add an event listener to the select element\n" +
                    "    filterSelect.addEventListener(\"change\", () => {\n" +
                    "        const selectedValue = filterSelect.value;\n" +
                    "\n" +
                    "        // Loop through all rows and hide/show them based on the selected test name\n" +
                    "        rows.forEach(row => {\n" +
                    "            const cells = row.querySelectorAll(\"td\");\n" +
                    "            let shouldShowRow = false;\n" +
                    "\n" +
                    "            if (selectedValue === \"all\") {\n" +
                    "                shouldShowRow = true;\n" +
                    "            } else {\n" +
                    "                const testNameCell = cells[0];\n" +
                    "                if (testNameCell && testNameCell.textContent === selectedValue) {\n" +
                    "                    shouldShowRow = true;\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            // Show/hide the row\n" +
                    "            row.style.display = shouldShowRow ? \"\" : \"none\";\n" +
                    "        });\n" +
                    "    });\n" +
                    "</script>");
            writer.append("            </thead>\n" +
                    "        </table>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>");
            writeHtmlToFile(trigonPaths.getTestResultsPath() + "/updatedLogs.html", writer.toString());
            captureException(e);
        }
    }

    private String copyClipBoard(String passOrFail, String template) {
        String templateNew = null;
        String templateV3 = null;
        int curlId = cUtils().getRandomNumber(100, 140000) + cUtils().getRandomNumber(12121212, 99999999);
        try {
        /*    template = " <td>\n" +
                    "                <div class=\"dropdown\">\n" +
                    "<button class=\"btn " + passOrFail + "\">curl</button>" +

                    "                    <div class=\"dropdown-content\" id=\"dropdownContent" + cUtils().getRandomNumber(100, 100000) + "\">\n" +
                    "                        <button class=\"copy-button\" onclick=\"copyToClipboard('" + template + "')\">Copy</button>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </td>";*/
           /* templateNew = " <td>\n" +
                    "                <div class=\"dropdown\">\n" +
                    "                    <button class=\"btn " + passOrFail + "\" onclick=\"copyCurlFromId('json-curl-" + curlId + "')\">curl</button>\n" +
                    "                </div>\n" +
                    "                <div style=\"display: none;\">\n" +
                    "                    <pre><code id=\"json-curl-" + curlId + "\">" + template + "</code></pre>\n" +
                    "                </div>\n" +
                    "            </td>";*/
            templateNew = "<td>\n" +
                    "<div class=\"dropdown\">\n"
                    + " <button class=\"btn " + passOrFail + "\" onclick=\"toggleText(" + curlId + ")\">Curl</button>\n" +
                    " <button id=\"copyButton" + curlId + "\" class=\"hidden\" onclick=\"copyText(" + curlId + ")\">copy</button>\n"
                    + "<div id=\"hiddenText" + curlId + "\" class=\"hidden\" contentEditable=\"true\">" + template + "\n" +
                    "</div>\n" +
                    "  <button id=\"closeButton" + curlId + "\" class=\"hidden\" onclick=\"closeText(" + curlId + ")\">close</button>\n"
                    + "                </div>\n" + "     " +
                    "       </td>";
            templateV3 = " <td>" +
                    "                <button class=\"btn passed\" onclick=\"toggleText(" + curlId + ")\">Curl</button>\n" +
                    "                <div class=\"overlay\" id=\"overlay" + curlId + "\">\n" +
                    "                    <div class=\"popup\" id=\"popup" + curlId + "\">\n" +
                    /* "                        <h2>Response view</h2>\n" +*/
                    "                        <p id=\"popupContent" + curlId + "\">" + template + "</p>\n" +
                    "                        <button onclick=\"closePopup(" + curlId + ")\">Close</button>\n" +
                    "                        <button onclick=\"copyContent(" + curlId + ")\">Copy</button>\n" +
                    "                        <div class=\"success-message\" id=\"successMessage" + curlId + "\"></div>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </td>";
        } catch (Exception e) {
            captureException(e);
        }
        return templateV3;
    }
    // ...

    // Defined a method to write HTML content to a file
    private static void writeHtmlToFile(String fileName, String htmlContent) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(htmlContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchFromHtml(String htmlFile, String methodName, String curlCommand) {
        requestAndEndpoint(curlCommand);
        endpointStatus(htmlFile);
        endpointData.put(methodName, endpointData.getOrDefault(methodName, 0) + 1);
    }

    private void endpointStatus(String htmlFile) {
        try {
            String delimeter = "document.getElementById\\('";
            String jsonId = htmlFile.split(delimeter)[1].split("\'")[0].replace("-", "_").split("_")[2];
            jsonId = "json_response_validation_" + jsonId;

            String content = new String(Files.readAllBytes(Paths.get(trigonPaths.getSupportFilePath() + "/respValidations.js")));
            String responseTime = content.split(jsonId)[1].split("var json")[0].split("responseTime")[1].split("apiTestStatus")[0].replaceAll("[,\": ]", "").trim();
            String apiTeststatus = content.split(jsonId)[1].split("var json")[0].split("responseTime")[1].split("apiTestStatus")[1].replaceAll("[,\": };`]", "").trim();
            respTime.add(responseTime);
            apiTestStatus.add(apiTeststatus);
        } catch (Exception e) {
            captureException(e);
        }
    }

    private void requestAndEndpoint(String htmlFile) {
        try {
            /*apiCall*/
            String delimeter = "request";
            String httpCall = htmlFile.split(delimeter)[1].split("\'")[0].replace("\\", "").trim();
            apiHttp.add(httpCall);
        } catch (Exception e) {
            captureException(e);
        }
        /*endpoint*/
        try {
            String delimeter = "https://";
            String jsonId = null;
            if (htmlFile.split(delimeter)[1].contains("?")) {
                jsonId = htmlFile.split(delimeter)[1].split("\\?")[0];
            } else if (htmlFile.split(delimeter)[1].contains("--header")) {
                jsonId = htmlFile.split(delimeter)[1].split("--header")[0];
            } else if (htmlFile.split(delimeter)[1].contains("--form")) {
                jsonId = htmlFile.split(delimeter)[1].split("--form")[0];
            }

            String endpoint = null;
            if (jsonId.contains(".com")) {
                endpoint = jsonId.split("m/")[1];
            } else if (jsonId.contains(".dev")) {
                endpoint = jsonId.split("v/")[1];
            } else if (jsonId.contains(".net")) {
                endpoint = jsonId.split("t/")[1];
            } else if (jsonId.contains("foodhub.co.uk")) {
                endpoint = jsonId.split("foodhub\\.co\\.uk/")[1];
            } else if (jsonId.contains(".co")) {
                endpoint = jsonId.split("o/")[1];
            }
            logger.info(endpoint.replaceAll("[`\\\\']", ""));
            String type = "";
            if (endpoint.replaceAll("[`\\\\']", "").contains("graphql")) {
                logger.info("identified graphql endpoint , proceeding to fetch it's type ");
                type = "( " + graphql(htmlFile) + " )";
            }
            logger.info("graphqlType" + type);
            apiCoverage.get().add(endpoint.replaceAll("[`\\\\']", "") + type);
            tempEndpointVariable.getAndIncrement();
        } catch (Exception e) {
            captureException(e);
        }
    }

    private String graphql(String curl) {
        String type = "unDefined";
        try {
            if (curl.toLowerCase().contains("mutation")) {
               /* logger.info(curl.split("utation")[1]);
                logger.info(curl.split("utation")[1].split("\\(")[0]);
                logger.info(curl.split("utation")[1].split("\\(")[0].replaceAll("[{0-9 ]", ""));
                logger.info(curl.split("utation")[1].split("\\(")[0].replaceAll("[{0-9 ]", "").replace("\\\\n", "").replace("\\",""));
           */
                type = curl.split("utation")[1].split("\\(")[0].replaceAll("[{0-9 ]", "").replace("\\\\n", "").replace("\\", "");
            }
            if (curl.toLowerCase().contains("myquery")) {
                type = curl.split("MyQuery")[1].split("\\(")[0].replaceAll("[{0-9 ]", "").replace("\\\\n", "");
            }
            if (curl.toLowerCase().contains("query getorder")) {
                type = "getOrder";
            }
        } catch (Exception e) {
            logger.warn(curl);

            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return type;
    }

    public String extractCurlCommand(String html) {
        String curl = "undefined";
        try {
            String delimeter = "document.getElementById\\('";
            String jsonId = html.split(delimeter)[1].split("\'")[0].replace("-", "_").split("_")[2];
            jsonId = "string_curl_" + jsonId + "=";
            String content = new String(Files.readAllBytes(Paths.get(trigonPaths.getSupportFilePath() + "/curl.js")));
            String varJson = content.split(jsonId)[1].split("var string_curl")[0].replace(";", "");
            curl = varJson.substring(1, varJson.length() - 2);
        } catch (Exception e) {
            captureException(e);
        }

        return curl;  // Return null if no curl command is found
    }

    public static String extractHttpCommand(String html) {
        String curlCommand = null;
        String delimiter = "\"httpMethod\":\"";
        int index = html.indexOf(delimiter);
        //System.out.println(delimiter.length());
        if (index != -1) {
            // Find the start and end positions of the curl command
            int start = html.indexOf("P", index + delimiter.length()) - 9;
            int end = html.indexOf("<", start);

            if (start != -1 && end != -1) {
                // Extract the curl command and replace single quotes with escaped single quotes
                curlCommand = html.substring(start, end).trim().replace("'", "\\\'");
            }
        }
        if (curlCommand == null) {
            //System.out.println(delimiter.length());
            //System.out.println(curlCommand);
            //System.out.println(html);

        }
        return curlCommand;  // Return null if no curl command is found
    }

    public static String piechart(List HTTPCALLS) {
        int post = (int) HTTPCALLS.stream().filter(httpCall -> String.valueOf(httpCall).equalsIgnoreCase("POST")).count();
        int put = (int) HTTPCALLS.stream().filter(httpCall -> String.valueOf(httpCall).equalsIgnoreCase("PUT")).count();
        int get = (int) HTTPCALLS.stream().filter(httpCall -> String.valueOf(httpCall).equalsIgnoreCase("GET")).count();
        int delete = (int) HTTPCALLS.stream().filter(httpCall -> String.valueOf(httpCall).equalsIgnoreCase("DELETE")).count();
        String lables = "['POST', 'PUT', 'GET','DELETE']";
        String data = "[" + post + ", " + put + ", " + get + ", " + delete + "]";

        // Generate the JavaScript code with dynamic data
        String dynamicChartData = "['APICalls_type', 'count']," + "['post', " + post + "]," + "['put', " + put + "]," + "['get', " + get + "]," + "['delete', " + delete + "]";

        // Insert the dynamicChartData into your HTML template
        String html = "  <head>\n" + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" + "    <script type=\"text/javascript\">\n" + "      google.charts.load('current', {'packages':['corechart']});\n" + "      google.charts.setOnLoadCallback(drawChart);\n" + "\n" + "      function drawChart() {\n" + "        var data = google.visualization.arrayToDataTable([" + dynamicChartData + "]);\n" + "        var options = {\n" + "          title: 'Apis Used On : " + siteName + "'\n" + "        };\n" + "        var chart = new google.visualization.PieChart(document.getElementById('piechart'));\n" + "        chart.draw(data, options);\n" + "      }\n" + "    </script>\n" + "  </head>\n" + "  <body>\n" + "    <div id=\"piechart\" style=\"width: 900px; height: 500px;\"></div>\n" + "  </body>\n";
        String htmlPopupTemplate = "<head>\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Muli&display=swap\" rel=\"stylesheet\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\"\n" +
                "          href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "        background-color: #e0dbdb;\n" +
                "        }        .b-skills {\n" +
                "        border-top: 1px solid #f9f9f9;\n" +
                "        padding-top: 30px;\n" +
                "        text-align: center;\n" +
                "        max-width: 600px;\n" +
                "        margin: 0 auto;\n" +
                "        }\n" +
                "        .blank-line {\n" +
                "        margin-bottom: 90px; /* Adjust the value to control the space */\n" +
                "        }        .b-skills h2 {\n" +
                "        margin-bottom: 30px;\n" +
                "        font-weight: 250;\n" +
                "        text-transform: uppercase;\n" +
                "        font-family: Arial, sans-serif;\n" +
                "        }\n" +
                "        .passed {\n" +
                "        color: green;\n" +
                "        font-weight: bold;\n" +
                "        }\n" +
                "        .failed {\n" +
                "        color: Tomato;\n" +
                "        font-weight: bold;\n" +
                "        }        .skill-item {\n" +
                "        position: relative;\n" +
                "        max-width: 250px;\n" +
                "        width: 100%;\n" +
                "        margin: 0 auto;\n" +
                "        margin-bottom: 30px;\n" +
                "        color: #555;\n" +
                "        }\n" +
                "        .chart-container {\n" +
                "        position: relative;\n" +
                "        width: 100%;\n" +
                "        height: 0;\n" +
                "        padding-top: 100%;\n" +
                "        margin-bottom: 15px;\n" +
                "        }\n" +
                "        .skill-item .chart, .skill-item .chart canvas {\n" +
                "        position: absolute;\n" +
                "        top: 0;\n" +
                "        left: 0;\n" +
                "        width: 100% !important;\n" +
                "        height: 100% !important;\n" +
                "        }\n" +
                "        .skill-item .chart:before {\n" +
                "        content: \"\";\n" +
                "        width: 0;\n" +
                "        height: 100%;\n" +
                "        }\n" +
                "        .skill-item .chart:before, .skill-item .percent {\n" +
                "        display: inline-block;\n" +
                "        vertical-align: middle;\n" +
                "        }\n" +
                "        .skill-item .percent {\n" +
                "        position: relative;\n" +
                "        line-height: 1;\n" +
                "        font-size: 30px;\n" +
                "        font-weight: 500;\n" +
                "        z-index: 2;\n" +
                "        }\n" +
                "        .skill-item .percent:after {\n" +
                "        content: attr(data-after);\n" +
                "        font-size: 15px;\n" +
                "        }\n" +
                /* ---------------*/
                "/* Styles for the overlay background */\n" +
                "        .overlay {\n" +
                "          display: none;\n" +
                "          position: fixed;\n" +
                "          top: 0;\n" +
                "          left: 0;\n" +
                "          width: 100%;\n" +
                "          height: 100%;\n" +
                "          background-color: rgba(0, 0, 0, 0.7);\n" +
                "          align-items: center;\n" +
                "          justify-content: center;\n" +
                "        }\n" +
                "\n" +
                "        /* Styles for the popup */\n" +
                "        .popup {\n" +
                "          background-color:#e0dbdb;\n" +
                "          padding: 20px;\n" +
                "          border-radius: 26px;\n" +
                "          box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);\n" +
                "          text-align: center;\n" +
                "          display: block;\n" +
                "          width: 70%;\n" +
                "          border: 3px solid green;\n" +
                "          padding: 10px;\n" +
                "          word-wrap: break-word;\n" +
                "          height:200px;\n" +
                "          overflow:auto;\n" +
                "        }\n" +
                "\n" +
                "        /* Style for the buttons in the popup */\n" +
                "        .popup button {\n" +
                "          margin: 5px;\n" +
                "          padding: 10px;\n" +
                "        }\n" +
                "\n" +
                "        /* Style for the success message */\n" +
                "        .success-message {\n" +
                "          color: red;\n" +
                "          font-weight: bold;\n" +
                "          margin-top: 10px;\n" +
                "        }" +
                /*------------------------------------------------*/
//                "        td {\n" +
//                "        position: relative; /* Make td a positioning context */\n" +
//                "        }\n" +
                "        #copyMessage {\n" +
                "        display: none;\n" +
                "        background-color: #5c75a1;\n" +
                "        color: white;\n" +
                "        padding: 10px;\n" +
                "        position: fixed;\n" +
                "        bottom: 20px;\n" +
                "        right: 20px;\n" +
                "        border-radius: 4px;\n" +
                "        }        p { font-weight: 9000; }\n" +
                "        .hidden {\n" +
                "        display: none;\n" +
                "        }\n" +
                "      [id^=\"hiddenText\"] {\n" +
                "    animation: fadeIn 2.0s;\n" +
                "}        @keyframes fadeIn {\n" +
                "        from {\n" +
                "        opacity: 0;\n" +
                "        }\n" +
                "        to {\n" +
                "        opacity: 1;\n" +
                "        }\n" +
                "        }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<section id=\"s-team\" class=\"section\">\n" +
                "    <div class=\"b-skills\">\n" +
                "        <div class=\"container\">\n" +
                "            <h2>Detailed Logs</h2>\n" +
                "            <div class=\"row\">\n" +
                "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" +
                "                    <div class=\"skill-item center-block\">\n" +
                "                        <div class=\"chart-container\">\n" +
                "                            <div class=\"chart\" data-percent=\"6\" data-bar-color=\"#f55677\">\n" +
                "                                <span class=\"percent\" data-after=\"\"></span>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <p>POST</p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" +
                "                    <div class=\"skill-item center-block\">\n" +
                "                        <div class=\"chart-container\">\n" +
                "                            <div class=\"chart\" data-percent=\"0\" data-bar-color=\"MediumSeaGreen\">\n" +
                "                                <span class=\"percent\" data-after=\"\"></span>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <p>PUT</p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" +
                "                    <div class=\"skill-item center-block\">\n" +
                "                        <div class=\"chart-container\">\n" +
                "                            <div class=\"chart\" data-percent=\"0\" data-bar-color=\"#3f8ceb\">\n" +
                "                                <span class=\"percent\" data-after=\"\"></span>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <p>GET</p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" +
                "                    <div class=\"skill-item center-block\">\n" +
                "                        <div class=\"chart-container\">\n" +
                "                            <div class=\"chart\" data-percent=\"5\" data-bar-color=\"#FFCE56\">\n" +
                "                                <span class=\"percent\" data-after=\"\"></span>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <p>DELETE</p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</section>\n" +
                "<script src=\"SupportFiles/plugins/jquery-2.2.4.min.js\"></script>\n" +
                "<script src=\"SupportFiles/plugins/jquery.appear.min.js\"></script>\n" +
                "<script src=\"SupportFiles/plugins/jquery.easypiechart.min.js\"></script>\n" +
               /* "<script>\n" +
                "    function copyCurlFromId(id) {\n" +
                "           const curlCommand = document.getElementById(id).innerText;\n" +
                "           copyToClipboard(curlCommand);\n" +
                "       }\n" +
                "    function copyToClipboard(content) {\n" +
                "        navigator.clipboard.writeText(content);\n" +
                "        const copyMessage = document.getElementById('copyMessage');\n" +
                "        copyMessage.style.display = 'block';\n" +
                "        setTimeout(() => copyMessage.style.display = 'none', 2000);\n" +
                "    }    'use strict';\n" +
                "        var $window = $(window);\n" +
                "        function run() {\n" +
                "            var fName = arguments[0], aArgs = Array.prototype.slice.call(arguments, 1);\n" +
                "            try {\n" +
                "                fName.apply(window, aArgs);\n" +
                "            } catch(err) {\n" +
                "            }\n" +
                "        }\n" +
                "        function _chart() {\n" +
                "            $('.b-skills').appear(function() {\n" +
                "                setTimeout(function() {\n" +
                "                    $('.chart').easyPieChart({\n" +
                "                        easing: 'easeOutElastic',\n" +
                "                        delay: 3000,\n" +
                "                        barColor: '#369670',\n" +
                "                        trackColor: '#fff',\n" +
                "                        scaleColor: false,\n" +
                "                        lineWidth: 21,\n" +
                "                        trackWidth: 11,\n" +
                "                        size: 300,\n" +
                "                        lineCap: 'round',\n" +
                "           animate: {\n" +
                "                            duration: 1000,  // Set the animation duration to 4 seconds\n" +
                "                            enabled: true\n" +
                "                        },                    onStep: function(from, to, percent) {\n" +
                "                            this.el.children[0].innerHTML = Math.round(percent);\n" +
                "                        }\n" +
                "                    });\n" +
                "                }, 150);\n" +
                "            });\n" +
                "        }\n" +
                "        $(document).ready(function() {\n" +
                "            run(_chart);\n" +
                "        });\n" +
                "    function toggleText(id) {\n" +
                "    var hiddenText = document.getElementById(\"hiddenText\" + id);\n" +
                "    var copyButton = document.getElementById(\"copyButton\" + id);\n" +
                "    var closeButton = document.getElementById(\"closeButton\" + id);\n" +
                "    if (hiddenText.style.display === \"none\" || hiddenText.style.display === \"\") {\n" +
                "        hiddenText.style.display = \"block\";\n" +
                "        copyButton.style.display = \"block\";\n" +
                "        closeButton.style.display = \"block\";\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    function copyText(id) {\n" +
                "      var hiddenText = document.getElementById(\"hiddenText\"+id);\n" +
                "      var textToCopy = hiddenText.textContent;\n" +
                "\n" +
                "      var textarea = document.createElement(\"textarea\");\n" +
                "      textarea.value = textToCopy;\n" +
                "      document.body.appendChild(textarea);\n" +
                "      textarea.select();\n" +
                "      document.execCommand(\"copy\");\n" +
                "      document.body.removeChild(textarea);\n" +
                "       const copyMessage = document.getElementById('copyMessage');\n" +
                "        copyMessage.style.display = 'block';\n" +
                "        setTimeout(() => copyMessage.style.display = 'none', 2000);\n" +
                "    }\n" +
                "\n" +
                "    function closeText(id) {\n" +
                "      var hiddenText = document.getElementById(\"hiddenText\"+id);\n" +
                "      var copyButton = document.getElementById(\"copyButton\"+id);\n" +
                "      var closeButton = document.getElementById(\"closeButton\"+id);\n" +
                "      hiddenText.style.display = \"none\";\n" +
                "      copyButton.style.display = \"none\";\n" +
                "      closeButton.style.display = \"none\";\n" +
                "    }\n" +
                "\n" +
                "</script>\n" +*/
                "<script>\n" +
                "  'use strict';\n" +
                "        var $window = $(window);\n" +
                "        function run() {\n" +
                "            var fName = arguments[0], aArgs = Array.prototype.slice.call(arguments, 1);\n" +
                "            try {\n" +
                "                fName.apply(window, aArgs);\n" +
                "            } catch(err) {\n" +
                "            }\n" +
                "        }\n" +
                "        function _chart() {\n" +
                "            $('.b-skills').appear(function() {\n" +
                "                setTimeout(function() {\n" +
                "                    $('.chart').easyPieChart({\n" +
                "                        easing: 'easeOutElastic',\n" +
                "                        delay: 3000,\n" +
                "                        barColor: '#369670',\n" +
                "                        trackColor: '#fff',\n" +
                "                        scaleColor: false,\n" +
                "                        lineWidth: 21,\n" +
                "                        trackWidth: 11,\n" +
                "                        size: 300,\n" +
                "                        lineCap: 'round',\n" +
                "           animate: {\n" +
                "                            duration: 1000,  // Set the animation duration to 4 seconds\n" +
                "                            enabled: true\n" +
                "                        },                    onStep: function(from, to, percent) {\n" +
                "                            this.el.children[0].innerHTML = Math.round(percent);\n" +
                "                        }\n" +
                "                    });\n" +
                "                }, 150);\n" +
                "            });\n" +
                "        }\n" +
                "        $(document).ready(function() {\n" +
                "            run(_chart);\n" +
                "        });" +
                "        function toggleText(id) {\n" +
                "            var overlay = document.getElementById(\"overlay\"+id);\n" +
                "            var popup = document.getElementById(\"popup\"+id);\n" +
                "            overlay.style.display = overlay.style.display === 'flex' ? 'none' : 'flex';\n" +
                "            popup.style.display = popup.style.display === 'block' ? 'none' : 'block';\n" +
                "        }\n" +
                "        function closePopup(id) {\n" +
                "            var overlay = document.getElementById(\"overlay\"+id);\n" +
                "            var popup = document.getElementById(\"popup\"+id);\n" +
                "            overlay.style.display = 'none';\n" +
                "            popup.style.display = 'none';\n" +
                "        }\n" +
                "        function copyContent(id) {\n" +
                "            var content = document.getElementById(\"popupContent\"+id).innerText;\n" +
                "            navigator.clipboard.writeText(content)\n" +
                "                .then(function() {\n" +
                "                    var successMessage = document.getElementById(\"successMessage\"+id);\n" +
                "                    successMessage.innerText = 'Successfully copied!';\n" +
                "                    successMessage.style.display = 'block';\n" +
                "                    setTimeout(function() {\n" +
                "                        successMessage.innerText = '';\n" +
                "                        successMessage.style.display = 'none';\n" +
                "                    }, 2000);\n" +
                "                })\n" +
                "                .catch(function(err) {\n" +
                "                    console.error('Unable to copy content', err);\n" +
                "                });\n" +
                "        }\n" +
                "</script>" +
                "</body>";
        String htmlTemplate = "<head>\n"
                + "    <link href=\"https://fonts.googleapis.com/css?family=Muli&display=swap\" rel=\"stylesheet\">\n"
                + "    <link rel=\"stylesheet\" type=\"text/css\"\n"
                + "          href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\">\n"
                + "<style>\n"
                + "        body {\n"
                + "        background-color: #e0dbdb;\n"
                + "        }        .b-skills {\n"
                + "        border-top: 1px solid #f9f9f9;\n"
                + "        padding-top: 30px;\n"
                + "        text-align: center;\n"
                + "        max-width: 600px;\n"
                + "        margin: 0 auto;\n"
                + "        }\n" + "        [id^=\"copyButton\"],\n"
                + "        [id^=\"closeButton\"] {\n"
                + "float: right;"
                + "        background-color: #5c75a1;\n"
                + "        color: white;\n"
                + "        padding: 2px 4px;\n"
                + "        border: none;\n"
                + "        border-radius: 5px;\n" + "        cursor: pointer;\n" + "        transition: background-color 0.3s;\n" + "        }\n" + "        .blank-line {\n" + "        margin-bottom: 90px; /* Adjust the value to control the space */\n" + "        }        .b-skills h2 {\n" + "        margin-bottom: 30px;\n" + "        font-weight: 250;\n" + "        text-transform: uppercase;\n" + "        font-family: Arial, sans-serif;\n" + "        }\n" + "        .passed {\n" + "        color: green;\n" + "        font-weight: bold;\n" + "        }\n" + "        .failed {\n" + "        color: Tomato;\n" + "        font-weight: bold;\n" + "        }        .skill-item {\n" + "        position: relative;\n" + "        max-width: 250px;\n" + "        width: 100%;\n" + "        margin: 0 auto;\n" + "        margin-bottom: 30px;\n" + "        color: #555;\n" + "        }\n" + "        .chart-container {\n" + "        position: relative;\n" + "        width: 100%;\n" + "        height: 0;\n" + "        padding-top: 100%;\n" + "        margin-bottom: 15px;\n" + "        }\n" + "        .skill-item .chart, .skill-item .chart canvas {\n" + "        position: absolute;\n" + "        top: 0;\n" + "        left: 0;\n" + "        width: 100% !important;\n" + "        height: 100% !important;\n" + "        }\n" + "        .skill-item .chart:before {\n" + "        content: \"\";\n" + "        width: 0;\n" + "        height: 100%;\n" + "        }\n" + "        .skill-item .chart:before, .skill-item .percent {\n" + "        display: inline-block;\n" + "        vertical-align: middle;\n" + "        }\n" + "        .skill-item .percent {\n" + "        position: relative;\n" + "        line-height: 1;\n" + "        font-size: 30px;\n" + "        font-weight: 500;\n" + "        z-index: 2;\n" + "        }\n" + "        .skill-item .percent:after {\n" + "        content: attr(data-after);\n" + "        font-size: 15px;\n" + "        }\n" + "        .dropdown {\n" + "        position: relative;\n" + "        display: inline-block;\n" + "        }\n" + "        .dropdown-content {\n" + "        display: none;\n" + "        position: absolute;\n" + "        background-color: #ffffff;\n" + "        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);\n" + "        padding: 0px;\n" + "        min-width: 0px;\n" + "        border-radius: 4px;\n" + "        z-index: 1;\n" + "        }\n" + "        .dropdown:hover .dropdown-content {\n" + "        display: block;\n" + "        }\n" + "        td {\n" + "        position: relative; /* Make td a positioning context */\n" + "        }\n" + "        .copy-button {\n" + "        color: green;\n" + "        padding: 5px 10px; /* Adjust padding as needed */\n" + "        border: none;\n" + "        cursor: pointer;\n" + "        position: absolute;\n" + "        top: 90%; /* Position vertically in the middle */\n" + "        right: -20px; /* Adjust the distance from the right side */\n" + "        transform: translateY(-50%); /* Adjust for vertical centering */\n" + "        }        .copy-button:hover {\n" + "        background-color: #005F6B;\n" + "        }\n" + "        #copyMessage {\n" + "        display: none;\n" + "        background-color: #5c75a1;\n" + "        color: white;\n" + "        padding: 10px;\n" + "        position: fixed;\n" + "        bottom: 20px;\n" + "        right: 20px;\n" + "        border-radius: 4px;\n" + "        }        p { font-weight: 9000; }\n" + "        .hidden {\n" + "        display: none;\n" + "        }\n" + "      [id^=\"hiddenText\"] {\n" + "    animation: fadeIn 2.0s;\n" + "}" + "        @keyframes fadeIn {\n" + "        from {\n" + "        opacity: 0;\n" + "        }\n" + "        to {\n" + "        opacity: 1;\n" + "        }\n" + "        }\n" + "    </style>" + "</head>\n" + "<body>\n" + "<section id=\"s-team\" class=\"section\">\n" + "    <div class=\"b-skills\">\n" + "        <div class=\"container\">\n" + "            <h2>Detailed Logs</h2>\n" + "            <div class=\"row\">\n" + "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" + "                    <div class=\"skill-item center-block\">\n" + "                        <div class=\"chart-container\">\n" + "                            <div class=\"chart\" data-percent=\"" + post + "\" data-bar-color=\"#f55677\">\n" + "                                <span class=\"percent\" data-after=\"\"></span>\n" + "                            </div>\n" + "                        </div>\n" + "                        <p>POST</p>\n" + "                    </div>\n" + "                </div>\n" + "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" + "                    <div class=\"skill-item center-block\">\n" + "                        <div class=\"chart-container\">\n" + "                            <div class=\"chart\" data-percent=\"" + put + "\" data-bar-color=\"MediumSeaGreen\">\n" + "                                <span class=\"percent\" data-after=\"\"></span>\n" + "                            </div>\n" + "                        </div>\n" + "                        <p>PUT</p>\n" + "                    </div>\n" + "                </div>\n" + "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" + "                    <div class=\"skill-item center-block\">\n" + "                        <div class=\"chart-container\">\n" + "                            <div class=\"chart\" data-percent=\"" + get + "\" data-bar-color=\"#3f8ceb\">\n" + "                                <span class=\"percent\" data-after=\"\"></span>\n" + "                            </div>\n" + "                        </div>\n" + "                        <p>GET</p>\n" + "                    </div>\n" + "                </div>\n" + "                <div class=\"col-xs-12 col-sm-6 col-md-3\">\n" + "                    <div class=\"skill-item center-block\">\n" + "                        <div class=\"chart-container\">\n" + "                            <div class=\"chart\" data-percent=\"" + delete + "\" data-bar-color=\"#FFCE56\">\n" + "                                <span class=\"percent\" data-after=\"\"></span>\n" + "                            </div>\n" + "                        </div>\n" + "                        <p>DELETE</p>\n" + "                    </div>\n" + "                </div>\n" + "            </div>\n" + "        </div>\n" + "    </div>\n" + "</section>\n" + "<script src=\"SupportFiles/plugins/jquery-2.2.4.min.js\"></script>\n" + "<script src=\"SupportFiles/plugins/jquery.appear.min.js\"></script>\n" + "<script src=\"SupportFiles/plugins/jquery.easypiechart.min.js\"></script>\n" + "<script>\n" + "    function copyCurlFromId(id) {\n" + "           const curlCommand = document.getElementById(id).innerText;\n" + "           copyToClipboard(curlCommand);\n" + "       }\n" + "    function copyToClipboard(content) {\n" + "        navigator.clipboard.writeText(content);\n" + "        const copyMessage = document.getElementById('copyMessage');\n" + "        copyMessage.style.display = 'block';\n" + "        setTimeout(() => copyMessage.style.display = 'none', 2000);\n" + "    }    'use strict';\n" + "        var $window = $(window);\n" + "        function run() {\n" + "            var fName = arguments[0], aArgs = Array.prototype.slice.call(arguments, 1);\n" + "            try {\n" + "                fName.apply(window, aArgs);\n" + "            } catch(err) {\n" + "            }\n" + "        }\n" + "        function _chart() {\n" + "            $('.b-skills').appear(function() {\n" + "                setTimeout(function() {\n" + "                    $('.chart').easyPieChart({\n" + "                        easing: 'easeOutElastic',\n" + "                        delay: 3000,\n" + "                        barColor: '#369670',\n" + "                        trackColor: '#fff',\n" + "                        scaleColor: false,\n" + "                        lineWidth: 21,\n" + "                        trackWidth: 11,\n" + "                        size: 300,\n" + "                        lineCap: 'round',\n" + "           animate: {\n" + "                            duration: 1000,  // Set the animation duration to 4 seconds\n" + "                            enabled: true\n" + "                        },                    onStep: function(from, to, percent) {\n" + "                            this.el.children[0].innerHTML = Math.round(percent);\n" + "                        }\n" + "                    });\n" + "                }, 150);\n" + "            });\n" + "        }\n" + "        $(document).ready(function() {\n" + "            run(_chart);\n" + "        });\n" +
                "    function toggleText(id) {\n"
                + "    var hiddenText = document.getElementById(\"hiddenText\" + id);\n"
                + "    var copyButton = document.getElementById(\"copyButton\" + id);\n"
                + "    var closeButton = document.getElementById(\"closeButton\" + id);\n"
                + "    if (hiddenText.style.display === \"none\" || hiddenText.style.display === \"\") {\n"
                + "        hiddenText.style.display = \"block\";\n"
                + "        copyButton.style.display = \"block\";\n"
                + "        closeButton.style.display = \"block\";\n"
                + "      }\n" + "    }\n"
                + "\n"
                + "    function copyText(id) {\n"
                + "      var hiddenText = document.getElementById(\"hiddenText\"+id);\n"
                + "      var textToCopy = hiddenText.textContent;\n"
                + "\n"
                + "      var textarea = document.createElement(\"textarea\");\n"
                + "      textarea.value = textToCopy;\n"
                + "      document.body.appendChild(textarea);\n"
                + "      textarea.select();\n"
                + "      document.execCommand(\"copy\");\n"
                + "      document.body.removeChild(textarea);\n"
                + "       const copyMessage = document.getElementById('copyMessage');\n"
                + "        copyMessage.style.display = 'block';\n"
                + "        setTimeout(() => copyMessage.style.display = 'none', 2000);\n"
                + "    }\n" + "\n" + "    function closeText(id) {\n"
                + "      var hiddenText = document.getElementById(\"hiddenText\"+id);\n"
                + "      var copyButton = document.getElementById(\"copyButton\"+id);\n"
                + "      var closeButton = document.getElementById(\"closeButton\"+id);\n"
                + "      hiddenText.style.display = \"none\";\n"
                + "      copyButton.style.display = \"none\";\n"
                + "      closeButton.style.display = \"none\";\n"
                + "    }\n"
                + "</script>"
                + "</body>";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(trigonPaths.getTestResultsPath() + "/ApiChart.html"));
            writer.write(html);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return htmlPopupTemplate;
    }

    public void pieChart(List HTTPCALLS) {

        int post = (int) HTTPCALLS.stream().filter(httpCall -> httpCall.equals("POST")).count();
        int put = (int) HTTPCALLS.stream().filter(httpCall -> httpCall.equals("PUT")).count();
        int get = (int) HTTPCALLS.stream().filter(httpCall -> httpCall.equals("GET")).count();
        int delete = (int) HTTPCALLS.stream().filter(httpCall -> httpCall.equals("DELETE")).count();

        String html = "<html>\n" + "<head>\n" + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + "    <script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js\"></script>\n" + "    <script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.bundle.min.js\"></script>\n" + "    <script type=\"text/javascript\">\n" + "    window.onload=function(){/*from ww  w  .j  a v  a  2s .co  m*/\n" + "$(function(){\n" + "  var postCall = " + post / 2 + ",\n" + "   putCall = " + put / 2 + ",\n" + "   getCall = " + get / 2 + ",\n" + "   deleteCall = " + delete / 2 + ",\n" + "   chart = $('#chart')[0];\n" + "   var charObj = new Chart(chart, {\n" + "   data: {\n" + "       datasets: [{\n" + "      data: [\n" + "          postCall,\n" + "          putCall,\n" + "          getCall,\n" + "         deleteCall\n" + "      ],\n" + "      backgroundColor: [\n" + "         \"#FF6283\",\n" + "         \"#4BC0C0\",\n" + "         \"#FFCC54\",\n" + "         \"#36A2EB\"\n" + "      ],\n" + "      label: 'Browser' // for legend\n" + "   }],\n" + "      labels: [\n" + "         \"post\",\n" + "         \"put\",\n" + "         \"get\",\n" + "         \"delete\"\n" + "      ]\n" + "   },\n" + "     type: 'polarArea'\n" + "   });\n" + "});\n" + "    }\n" + "\n" + "      </script>\n" + "</head>\n" + "<body>\n" + "<canvas id=\"chart\" width=\"100px\" height=\"30px\"></canvas>\n" + "</body>\n" + "</html>";
        // Write the HTML code to a file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(trigonPaths.getTestResultsPath() + "/ApiData.html"));
            writer.write(html);
            writer.flush();
            //System.out.println("HTML file generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /********************proccessDone*************************/
    private void getEndpointCount() {
        Set<String> endpoint = new LinkedHashSet<>();
        List actualEndpoints = apiCoverage.get();
        String addedendpoint = "";
        Map<String, Integer> endpointCount = new HashMap<>();
        actualEndpoints.stream().forEach(c -> {
            String ep = c.toString();
            ep = ep.replaceAll("[0-9]", "");
            if (endpointCount.containsKey(ep)) {
                endpointCount.put(ep, endpointCount.get(ep) + 1);
            } else {
                endpointCount.put(ep, 1);
            }
        });
        for (int i = 0; i < actualEndpoints.size(); i++) {
            String endPoint = actualEndpoints.get(i).toString();
            String[] split = new String[0];
            if (endPoint.contains("/")) {
                if (endPoint.startsWith("/")) {
                    char charOfSlash = endPoint.charAt(0);
                    endPoint = endPoint.replaceFirst(String.valueOf(charOfSlash), "");
                }
                split = endPoint.split("/");
                for (int j = 0; j < split.length; j++) {
                    String splitt = split[j];
                    char firstChar = splitt.charAt(0);
                    int asciivalue = firstChar;
                    if (asciivalue >= 97 && asciivalue <= 122) {
                        String endendpoint = split[j];
                        addedendpoint = addedendpoint + endendpoint + "/";
                    } else {
                        addedendpoint = addedendpoint + "12345/";
                    }
                    if (j == split.length - 1) {
                        if (addedendpoint.contains("?")) {
                            String[] splitQueryParam = addedendpoint.split("\\?");
                            addedendpoint = splitQueryParam[0];
                        }
                        endpoint.add(addedendpoint);
                        addedendpoint = "";
                    }
                }
            } else {
                endpoint.add(endPoint);
            }
        }
        apiCallsUnique = endpoint.size();

        //System.out.println("API Endpoints Covered :" + totalEndpoints);
    }
}

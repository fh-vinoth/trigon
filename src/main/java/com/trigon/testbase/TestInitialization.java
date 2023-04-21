package com.trigon.testbase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.trigon.bean.ExtentPojo;
import com.trigon.bean.PropertiesPojo;
import com.trigon.bean.TrigonPaths;
import com.trigon.bean.remoteenv.RemoteEnvPojo;
import com.trigon.bean.testenv.TestEnv;
import com.trigon.bean.testenv.TestEnvPojo;
import com.trigon.mobile.AppiumManager;
import com.trigon.properties.ConfigReader;
import com.trigon.security.AES;
import com.trigon.testrail.APIException;
import com.trigon.testrail.Runs;
import com.trigon.web.Browsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TestInitialization extends Browsers {

    private static final Logger logger = LogManager.getLogger(TestInitialization.class);
    public static TrigonPaths trigonPaths;

    protected static String runId = null;
    protected static long suiteStartTime;


    public void reportsInitialization(String suiteName) {
        trigonPaths = new TrigonPaths();
        String suiteNameReplaced = suiteName.replaceAll("-", "_").replaceAll(" ", "_").trim();
        String[] tType = suiteNameReplaced.split("_");
        mobileApps = Arrays.asList(propertiesPojo.getMobile_Apps().split(","));
        webApps = Arrays.asList(propertiesPojo.getWeb_Apps().split(","));
        if (tType.length > 0) {
            if (tType[0].toLowerCase().equalsIgnoreCase("API") ||
                    mobileApps.contains(tType[0].toLowerCase()) || webApps.contains(tType[0].toLowerCase())) {
                platformType = tType[0];
            } else {
                Assert.fail("Modify Your SuiteName as per standard structure : Example: [API]" + webApps + mobileApps + "");
                System.exit(0);
            }
            if (mobileApps.contains(platformType.toLowerCase())) {
                if (tType[1].equalsIgnoreCase("Android") || tType[1].equalsIgnoreCase("AndroidBrowser") || tType[1].equalsIgnoreCase("IOS") || tType[1].equalsIgnoreCase("IOSBrowser")) {
                    appType = tType[1].toLowerCase();
                } else {
                    Assert.fail("Modify Your SuiteName as per standard structure : Example: MOBILE_ANDROID/MYT_ANDROID/D2S_ANDROID/FHAPP_IOS/CA_ANDROID/MYPOS_ANDROID/APOS_ANDROID_FUSIONApp_ANDROID");
                    System.exit(0);
                }
            }

        } else {
            Assert.fail("Modify Your SuiteName as per standard structure : Example: [API]" + webApps + mobileApps + "");
            System.exit(0);
        }
        String suiteNameWithTime = suiteNameReplaced + "_" + cUtils().getCurrentTimeStamp();
        getSuiteNameWithTime = suiteNameWithTime;
        getSuiteExecutionDate = cUtils().getCurrentDate();
        String datePath = cUtils().createFolder("src/test/resources", "TestResults", getSuiteExecutionDate);
        String testResultsPath = cUtils().createFolder(datePath, suiteNameWithTime, "");
        trigonPaths.setTestResultsPath(testResultsPath);
        String supportFilePath = cUtils().createFolder(testResultsPath, "SupportFiles", "");
        trigonPaths.setSupportFilePath(supportFilePath);
        trigonPaths.setSupportFileHTMLPath(cUtils().createFolder(supportFilePath, "HTML", ""));

        if (!suiteName.contains("adhoc")) {
            trigonPaths.setLogsPath(cUtils().createFolder(testResultsPath, "RunTimeLogs", ""));
            if (!platformType.equalsIgnoreCase("API")) {
                trigonPaths.setScreenShotsPath(cUtils().createFolder(testResultsPath, "ScreenShots", ""));
            }
            trigonPaths.setSupportSubSuiteFilePath(cUtils().createFolder(supportFilePath, "TestResultJSON", ""));

//            File file2 = new File("reports-path.json");
//            if (file2.exists()) {
//                file2.delete();
//            }
//            try {
//                JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter("reports-path.json", false)));
//                writer.beginObject().name("path").value(testResultsPath);
//                writer.name("testType").value(platformType);
//                writer.name("platformType").value(appType).endObject().flush();
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            initializeExtentReport(testResultsPath, suiteNameWithTime);
        }


    }

    protected void getAPICoverage(List<String> apiCoverage) {
        try {
            JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(trigonPaths.getSupportFilePath() + "/TestResultJSON/apiCoverage.json", false)));
            TreeSet<String> listOfEndpoints = new TreeSet<>(apiCoverage);
            totalEndpoints = listOfEndpoints.size();
            Map<String, Long> getEndpointCount = apiCoverage.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            writer.beginArray();
            getEndpointCount.forEach((k, v) -> {
                try {
                    writer.value(k);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.endArray().flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeExtentReport(String testResultsPath, String suiteNameWithTime) {
        extent = new ExtentReports();
        ExtentSparkReporter sparkFail = new ExtentSparkReporter(testResultsPath + "/" + suiteNameWithTime + "_Failed.html")
                .filter()
                .statusFilter()
                .as(new Status[]{Status.FAIL})
                .apply();

        ExtentSparkReporter sparkAll = new ExtentSparkReporter(testResultsPath + "/" + suiteNameWithTime + ".html");
        JsonFormatter json = new JsonFormatter(trigonPaths.getSupportSubSuiteFilePath() + "/" + suiteNameWithTime + ".json");
        sparkFail.config().setReportName(suiteNameWithTime);
        sparkFail.config().setTimelineEnabled(false);
        sparkAll.config().setReportName(suiteNameWithTime);
        sparkAll.config().setTimelineEnabled(false);

        sparkAll.config().setCss(" .card {\n" +
                "        border-radius: 13px;\n" +
                "    }\n" +
                "\n" +
                "    .bd-clipboard {\n" +
                "        position: relative;\n" +
                "    / / display: none;\n" +
                "        float: right;\n" +
                "    }\n" +
                "\n" +
                "    .btn-clipboard {\n" +
                "        position: absolute;\n" +
                "        top: 0.65rem;\n" +
                "        right: 0.65rem;\n" +
                "        z-index: 10;\n" +
                "        display: block;\n" +
                "        padding: 0.25rem 0.5rem;\n" +
                "        font-size: 65%;\n" +
                "        color: #007bff;\n" +
                "        background-color: #fff;\n" +
                "        border: 1px solid;\n" +
                "        border-radius: 0.25rem;\n" +
                "        width: max-content;\n" +
                "    }\n" +
                "\n" +
                "    .btn-clipboard:hover, .btn-clipboard:focus {\n" +
                "        color: #FFFFFF;\n" +
                "        background-color: #007bff;\n" +
                "    }\n" +
                "    .apiSpan{\n" +
                "        color: #6b6b70;\n" +
                "        font-weight: bold;\n" +
                "    }\n" +
                "    .stepSpan{\n" +
                "        color: #366792;\n" +
                "        font-weight: bold;\n" +
                "    }\n" +
                "    .scenarioSpan{\n" +
                "        color: #a98572;\n" +
                "        font-weight: bold;\n" +
                "        font-size: 15px;\n" +
                "    }\n" +
                "    .btn{\n" +
                "        background-color: #c5c5d3;\n" +
                "        border-radius: 10px;\n" +
                "        color: #6b6b70;\n" +
                "    }\n" +
                "    .btn:hover, .btn:focus {\n" +
                "        color: #FFFFFF;\n" +
                "        background-color: #007bff;\n" +
                "    }\n" +
                "    .preCode{\n" +
                "        max-width: 90%;\n" +
                "        background-color: #ffffff;\n" +
                "        border: none;\n" +
                "    }");
        sparkAll.config().setJs("$('.test-item').click(function() {\n" +
                "    $('.test-content').scrollTop(0);\n" +
                "});</script>\n" +
                "<script>\n" +
                "    function copy(id) {\n" +
                "        navigator.clipboard\n" +
                "            .writeText(document.getElementById(id).innerText);\n" +
                "    }\n" +
                "</script>\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js\"></script>");
        //htmlReporter.config().enableOfflineMode(true);
        //spark.config().setTheme(Theme.DARK);
        extent.attachReporter(json, sparkFail, sparkAll);
        extent.setSystemInfo("frameworkVersion", getFrameworkVersion());
        extent.setSystemInfo("suite-name", suiteNameWithTime);
        suiteRunId = cUtils().getRunId();
        extent.setSystemInfo("run-id", suiteRunId);

    }

    public void createExtentClassName(XmlTest xmlTest) {
        if (extent != null) {
            if (xmlTest.getParallel().toString().equals("classes")) {
                if (extentPojo == null) {
                    createExtentTest(xmlTest.getName(), "");
                }
                extentClassNode.set(extentPojo.getExtentTest().createNode(getClass().getSimpleName()));
            } else if (xmlTest.getParallel().toString().equals("tests")) {
                if (extentTestNode.get() == null) {
                    createExtentTest(xmlTest.getName(), "");
                }
                extentClassNode.set(extentTestNode.get().createNode(getClass().getSimpleName()));
            } else {
                if (extentPojo == null) {
                    createExtentTest(xmlTest.getName(), "");
                }
                extentClassNode.set(extentPojo.getExtentTest().createNode(getClass().getSimpleName()));
            }
        }
    }

    public void createExtentMethod(ITestContext context, XmlTest xmlTest, Method method) {
        if (extent != null) {
            if (extentClassNode.get() == null) {
                createExtentClassName(xmlTest);
            }
            if (extentClassNode.get() != null) {
                extentMethodNode.set(extentClassNode.get().createNode(method.getName()));
            }
            if (tEnv().getContext().getIncludedGroups().length > 0) {
                for (String cat : context.getIncludedGroups()) {
                    extentClassNode.get().assignCategory(cat);
                }
            }
        }
    }

    public void extentFlush() {
        if (extent != null) {
            try {
                extent.flush();
            } catch (ConcurrentModificationException c) {
                try {
                    Thread.sleep(100);
                    extent.flush();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void testRailInit() {
        if (propertiesPojo.getEnable_testrail().equalsIgnoreCase("true")) {
            try {
                if (runId == null) {
                    logger.info("------------------------------------");
                    logger.info("Test Rail is Enabled ");

                    String projectId = null;
                    String testType = platformType;
                    if (testType.equalsIgnoreCase("API")) {
                        projectId = propertiesPojo.getTestrail_api_projectid();
                    }
                    if (testType.equalsIgnoreCase("MYT")) {
                        projectId = propertiesPojo.getTestrail_myt_projectid();
                    }
                    if (testType.equalsIgnoreCase("D2S")) {
                        projectId = propertiesPojo.getTestrail_d2s_projectid();
                    }
                    if (testType.equalsIgnoreCase("FHApp")) {
                        projectId = propertiesPojo.getTestrail_fhapp_projectid();
                    }
                    if (testType.equalsIgnoreCase("CAApp")) {
                        projectId = propertiesPojo.getTestrail_ca_projectid();
                    }
                    if (testType.equalsIgnoreCase("FHWeb")) {
                        projectId = propertiesPojo.getTestrail_fhweb_projectid();
                    }
                    if (testType.equalsIgnoreCase("FHNative")) {
                        projectId = propertiesPojo.getTestrail_fhnative_projectid();
                    }

                    if (projectId == null) {
                        Assert.fail("Check your Testrail Project ID in properties file (or) Provide Suite name as per standards (or) set enable_testrail to false");
                    }

                    logger.info("Testrail Project ID : " + projectId);
                    Runs r = new Runs();
                    try {
                        runId = r.createRunAndGetRunId(projectId, getSuiteNameWithTime);
                        extent.setSystemInfo("testRailProjectID", projectId);
                    } catch (IOException e) {
                        captureException(e);
                    } catch (APIException e) {
                        captureException(e);
                    } catch (NullPointerException e) {
                        captureException(e);
                    }
                    logger.info("Testrail Run ID : " + runId);
                    logger.info("------------------------------------");

                }
            } catch (Exception e) {
                captureException(e);
            }
        }
    }

    public synchronized PropertiesPojo setProperties() {
        try {
            ConfigReader testConfig = new ConfigReader("src/test/resources/Configuration/testconfig.properties");
            propertiesPojo.setEnable_testrail(testConfig.getProperty("enable_testrail"));
            propertiesPojo.setTestrail_username(testConfig.getProperty("testrail_username"));
            propertiesPojo.setTestrail_password(testConfig.getProperty("testrail_password"));
            propertiesPojo.setEnable_jira(testConfig.getProperty("enable_jira"));

            propertiesPojo.setTestrail_api_projectid(testConfig.getProperty("testRail_API_ProjectID"));
            propertiesPojo.setTestrail_myt_projectid(testConfig.getProperty("testRail_MYT_ProjectID"));
            propertiesPojo.setTestrail_d2s_projectid(testConfig.getProperty("testRail_D2S_ProjectID"));
            propertiesPojo.setTestrail_fhapp_projectid(testConfig.getProperty("testRail_FHAPP_ProjectID"));
            propertiesPojo.setTestrail_ca_projectid(testConfig.getProperty("testRail_CA_ProjectID"));
            propertiesPojo.setTestrail_fhweb_projectid(testConfig.getProperty("testRail_FHWeb_ProjectID"));
            propertiesPojo.setTestrail_fhnative_projectid(testConfig.getProperty("testRail_FHNative_ProjectID"));
            propertiesPojo.setJira_api_proj(testConfig.getProperty("jira_API_ProjectName"));
            propertiesPojo.setJira_myt_proj(testConfig.getProperty("jira_MYT_ProjectName"));
            propertiesPojo.setJira_d2s_proj(testConfig.getProperty("jira_D2S_ProjectName"));
            propertiesPojo.setJira_fhapp_proj(testConfig.getProperty("jira_FHAPP_ProjectName"));
            propertiesPojo.setJira_ca_proj(testConfig.getProperty("jira_CA_ProjectName"));
            propertiesPojo.setJira_fhweb_proj(testConfig.getProperty("jira_FHWeb_ProjectName"));
            propertiesPojo.setJira_fhnative_proj(testConfig.getProperty("jira_FHNative_ProjectName"));

            propertiesPojo.setBrowserStack_UserName(testConfig.getProperty("browserStack_UserName"));
            propertiesPojo.setBrowserStack_Password(testConfig.getProperty("browserStack_Password"));
            propertiesPojo.setBrowserStack_URI(testConfig.getProperty("browserStack_URI"));
            propertiesPojo.setAppCenter_URI(testConfig.getProperty("appCenter_URI"));
            propertiesPojo.setAppCenter_token(testConfig.getProperty("appCenter_token"));

            propertiesPojo.setMYT_Appcenter_Android_ProjectName(testConfig.getProperty("MYT_Appcenter_Android_ProjectName"));
            propertiesPojo.setMYT_Appcenter_IOS_ProjectName(testConfig.getProperty("MYT_Appcenter_IOS_ProjectName"));
            propertiesPojo.setMYT_Automation_Branch_Name(testConfig.getProperty("MYT_Automation_Branch_Name"));

            propertiesPojo.setD2S_Appcenter_Android_ProjectName(testConfig.getProperty("D2S_Appcenter_Android_ProjectName"));
            propertiesPojo.setD2S_Appcenter_IOS_ProjectName(testConfig.getProperty("D2S_Appcenter_IOS_ProjectName"));
            propertiesPojo.setD2S_Automation_Branch_Name(testConfig.getProperty("D2S_Automation_Branch_Name"));

            propertiesPojo.setFHApp_Appcenter_Android_ProjectName(testConfig.getProperty("FHApp_Appcenter_Android_ProjectName"));
            propertiesPojo.setFHApp_Appcenter_IOS_ProjectName(testConfig.getProperty("FHApp_Appcenter_IOS_ProjectName"));
            propertiesPojo.setFHApp_Automation_Branch_Name(testConfig.getProperty("FHApp_Automation_Branch_Name"));

            propertiesPojo.setCA_Appcenter_Android_ProjectName(testConfig.getProperty("CA_Appcenter_Android_ProjectName"));
            propertiesPojo.setCA_Appcenter_IOS_ProjectName(testConfig.getProperty("CA_Appcenter_IOS_ProjectName"));
            propertiesPojo.setCA_Automation_Branch_Name(testConfig.getProperty("CA_Automation_Branch_Name"));

            propertiesPojo.setMobile_Apps(testConfig.getProperty("Mobile_Apps"));
            propertiesPojo.setWeb_APPS(testConfig.getProperty("Web_Apps"));

        } catch (Exception e) {
            captureException(e);
        }
        return propertiesPojo;
    }

    public void testSuiteCollectionBeforeSuite(ITestContext iTestContext) {
        String testType = platformType;
        suiteParallel = iTestContext.getSuite().getParallel();
        totalTestModules = iTestContext.getSuite().getXmlSuite().getTests().size() - 1;
        if (extent != null) {
            extent.setSystemInfo("testType", testType);
            extent.setSystemInfo("testAppType", appType);
            extent.setSystemInfo("executedBy", System.getProperty("user.name"));
            extent.setSystemInfo("executedSystemOS", System.getProperty("os.name"));
            extent.setSystemInfo("parallel", suiteParallel);
            extent.setSystemInfo("totalTestModules", String.valueOf(totalTestModules));
        }

    }

    public void testModuleCollection(String testModuleName) {
        try {
            String testType = tEnv().getTestType();
            String moduleNameReplaced = testModuleName.replaceAll(" ", "_").replaceAll("-", "_").trim();
            String testEnvVariables = "NA";

            if (testType.equalsIgnoreCase("API")) {
                //  testEnvVariables = "<div>" + tEnv().getApiURI() + "</div><div>" + tEnv().getApiHost() + "</div><div>" + tEnv().getApiCountry() + "</div>";
                testEnvVariables = tEnv().getApiURI() + " : " + tEnv().getApiHost() + " : " + tEnv().getApiCountry();
            }
            if (webApps.contains(testType)) {
                // testEnvVariables = "<div>" + tEnv().getWebBrowser() + "</div><div>" + tEnv().getWebBrowserVersion() + "</div><div>" + tEnv().getWebUrl() + "</div>";
                testEnvVariables = tEnv().getWebBrowser() + " : " + tEnv().getWebBrowserVersion() + " : " + tEnv().getWebUrl();

            }
            if (mobileApps.contains(testType)) {
                if (appType.equalsIgnoreCase("android")) {
                    //testEnvVariables = "<div>" + tEnv().getAndroidDevice() + "</div><div>" + tEnv().getAndroidOSVersion() + "</div><div>" + tEnv().getAndroidBuildNumber() + "</div>";
                    testEnvVariables = tEnv().getAndroidDevice() + " : " + tEnv().getAndroidOSVersion() + " : " + tEnv().getAndroidBuildNumber();
                }
                if (appType.equalsIgnoreCase("androidBrowser")) {
                    //testEnvVariables = "<div>" + tEnv().getAndroidDevice() + "</div><div>" + tEnv().getAndroidOSVersion() + "</div><div>" + tEnv().getAndroidBuildNumber() + "</div>";
                    testEnvVariables = tEnv().getAndroidDevice() + " : " + tEnv().getAndroidOSVersion() + " : " + tEnv().getWebBrowserVersion();
                }
                try {
                    if (tEnv().getIosDevice() != null) {
                        if (appType.equalsIgnoreCase("ios")) {
                            // testEnvVariables = "<div>" + tEnv().getIosDevice() + "</div><div>" + tEnv().getIosOSVersion() + "</div><div>" + tEnv().getIosBuildNumber() + "</div>";
                            testEnvVariables = tEnv().getIosDevice() + " : " + tEnv().getIosOSVersion() + " : " + tEnv().getIosBuildNumber();

                        }
                        if (appType.equalsIgnoreCase("iosBrowser")) {
                            // testEnvVariables = "<div>" + tEnv().getIosDevice() + "</div><div>" + tEnv().getIosOSVersion() + "</div><div>" + tEnv().getIosBuildNumber() + "</div>";
                            testEnvVariables = tEnv().getIosDevice() + " : " + tEnv().getIosOSVersion() + " : " + tEnv().getWebBrowserVersion();

                        }
                    }
                } catch (NullPointerException ne) {

                }
            }
            createExtentTest(moduleNameReplaced, testEnvVariables);
        } catch (Exception e) {
            captureException(e);
        }
    }

    public void createExtentTest(String moduleNameReplaced, String testEnvVariables) {
        if (extent != null) {
            extentTest = extent.createTest(moduleNameReplaced + "<div style=\"color: #635959;font-size: 12px;\">" + testEnvVariables + "</div>");
            extentPojo = new ExtentPojo();
            extentPojo.setExtentTest(extentTest);
            extentTestNode.set(extentTest);
        }
    }


    public void classCollection(String ClassName, String testModuleName) {
        String classData = null;
        String moduleNameReplaced = testModuleName.replaceAll(" ", "_").replaceAll("-", "_").trim();
        String[] classNameReplaced = ClassName.replaceAll("-", "_").split("\\.");
        if (classNameReplaced.length > 1) {
            classData = classNameReplaced[classNameReplaced.length - 2] + "_" + classNameReplaced[classNameReplaced.length - 1];
        } else {
            classData = ClassName;
        }
    }


    public void logInitialization(String suiteName) {
        try {
            if (!suiteName.contains("adhoc")) {
                System.setProperty("logFilename", trigonPaths.getLogsPath() + "RunTimeExecutionLog.log");
                System.setProperty("loghtmlfile", trigonPaths.getLogsPath() + "RunTimeExecutionLog.html");
                LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
                ctx.reconfigure();
                cUtils().deleteFile("${sys:logFilename}");
                cUtils().deleteFile("${sys:loghtmlfile}");
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void remoteBrowserInit(ITestContext context, XmlTest xmlTest) {
        try {
            if (webApps.contains(tEnv().getTestType())) {
                tEnv().setElementLocator("Web");
                createBrowserInstance(context, xmlTest);
            }
        } catch (Exception e) {
            captureException(e);
        }
    }


    protected void remoteMobileInit(ITestContext context, XmlTest xmlTest) {
        try {

            if (mobileApps.contains(tEnv().getTestType())) {
                if (!executionType.equalsIgnoreCase("remote")) {
                    AppiumManager appiumManager = new AppiumManager();
                    appiumManager.startAppium();
                }
                if (tEnv().getAppType().equalsIgnoreCase("Android")) {
                    tEnv().setElementLocator("Android");
                    androidNative(context, xmlTest);
                }
                if (tEnv().getAppType().equalsIgnoreCase("AndroidBrowser")) {
                    tEnv().setElementLocator("Web");
                    androidNative(context, xmlTest);
                }
                if (tEnv().getAppType().equalsIgnoreCase("ios")) {
                    tEnv().setElementLocator("IOS");
                    nativeiOS(context, xmlTest);
                }
                if (tEnv().getAppType().equalsIgnoreCase("iOSBrowser")) {
                    tEnv().setElementLocator("Web");
                    nativeiOS(context, xmlTest);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void setMobileLocator() {
        try {
            if (mobileApps.contains(tEnv().getTestType())) {
                if (tEnv().getAppType().equalsIgnoreCase("Android")) {
                    tEnv().setElementLocator("Android");
                }
                if (tEnv().getAppType().equalsIgnoreCase("AndroidBrowser")) {
                    tEnv().setElementLocator("Web");
                }
                if (tEnv().getAppType().equalsIgnoreCase("ios")) {
                    tEnv().setElementLocator("IOS");
                }
                if (tEnv().getAppType().equalsIgnoreCase("iOSBrowser")) {
                    tEnv().setElementLocator("Web");
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void setWebLocator() {
        try {
            if (webApps.contains(tEnv().getTestType())) {
                tEnv().setElementLocator("Web");
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void setTestEnvironment(String fileName, String excelFilePath,
                                      String jsonFilePath, String jsonDirectory, String applicationType, String url, String browser, String browserVersion, String device, String os_version,
                                      String URI, String envType, String appSycURI, String appSycAuth, String version, String partnerURI, String token,
                                      String accessToken, String isJWT, String endpointPrefix,String authorization, String franchiseId, String dbType, String serviceType, String store, String host, String locale,
                                      String region, String country, String currency,
                                      String timezone, String phoneNumber, String emailId, String test_region, String browserstack_execution_local, String class_name, String bs_app_path, String productName, String grid_Hub_IP, String gps_location, String moduleNames,String test_email_recipients,String test_error_email_recipients,String test_failure_email_recipients,String browserstack_midSessionInstallApps) {
        try {
            Gson pGson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement testEnvElement = null;
            try {
                if (fileName != null) {
                    testEnvElement = JsonParser.parseReader(new FileReader(fileName));
                } else {
                    testEnvElement = JsonParser.parseReader(new FileReader("tenv/test-env.json"));
                }
            } catch (FileNotFoundException e) {
                captureException(e);
            }
            JsonElement remoteEnvElement = JsonParser.parseReader(new FileReader("tenv/remote-env.json"));
            RemoteEnvPojo tRemoteEnv = pGson.fromJson(remoteEnvElement, RemoteEnvPojo.class);

            TestEnvPojo tLocalEnv = pGson.fromJson(testEnvElement, TestEnvPojo.class);
            envThreadLocal.set(new TestEnv());
            tEnv().setTestType(platformType.toLowerCase());
            if (appType != null) {
                tEnv().setAppType(appType.toLowerCase());
            }
            tEnv().setExecution_type(tRemoteEnv.getExecution_type());
            tEnv().setGridExecution_type(tRemoteEnv.getGrid_execution_local());
            tEnv().setGps_location(tRemoteEnv.getGps_location());
            tEnv().setJenkins_execution(tRemoteEnv.getJenkins_execution());
            tEnv().setPipeline_execution(tRemoteEnv.getPipeline_execution());
            //tEnv().setTest_region(tRemoteEnv.getTest_region());
            tEnv().setApp_reset(tRemoteEnv.getApp_reset());

            if (tLocalEnv.getApi() != null) {
                tEnv().setApiURI(tLocalEnv.getApi().getURI());
                tEnv().setApiVersion(tLocalEnv.getApi().getVersion());
                tEnv().setApiEnvType(tLocalEnv.getApi().getEnvType());
                tEnv().setApiAppSycURI(tLocalEnv.getApi().getAppSycURI());
                tEnv().setApiAppSycAuth(tLocalEnv.getApi().getAppSycAuth());
                tEnv().setApiPartnerURI(tLocalEnv.getApi().getApiPartnerURI());
                tEnv().setModuleNames(tLocalEnv.getApi().getModuleNames());
            }

            if (tLocalEnv.getWeb() != null) {
                tEnv().setWebSystemOS(tLocalEnv.getWeb().getSystemOs());
                tEnv().setWebSystemOSVersion(tLocalEnv.getWeb().getSystemOsVersion());
                tEnv().setWebBrowser(tLocalEnv.getWeb().getBrowser());
                tEnv().setWebHeadless(tLocalEnv.getWeb().getHeadless());
                tEnv().setWebBrowserVersion(tLocalEnv.getWeb().getBrowserVersion());
                tEnv().setWebUrl(tLocalEnv.getWeb().getWebUrl());
                tEnv().setWebBuildNumber(tLocalEnv.getWeb().getWebBuildNumber());
                tEnv().setWebNetworkLogs(tLocalEnv.getWeb().getWebNetworkLogs());
                tEnv().setModuleNames(tLocalEnv.getApi().getModuleNames());
            }

            if (tLocalEnv.getAndroid() != null) {
                if (appType != null) {
                    if (appType.equalsIgnoreCase("Android")) {
                        tEnv().setAndroidAppPackage(tLocalEnv.getAndroid().getAppPackage());
                        tEnv().setAndroidAppActivity(tLocalEnv.getAndroid().getAppActivity());
                        try {
                            tEnv().setAndroidDevice(tLocalEnv.getAndroid().getRegion_android_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("device").getAsString());
                            tEnv().setAndroidOSVersion(tLocalEnv.getAndroid().getRegion_android_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("os").getAsString());
                        } catch (Exception e) {
                            if (tRemoteEnv.getTest_region() != null) {
                                logger.error("test_region is given as " + tRemoteEnv.getTest_region() + " in remote-env.json; Which is not present in test-env.json under android devices!! Hence proceeding with default devices");
                            } else {
                                logger.error("test_region is not given in remote-env.json; Hence proceeding with default devices");
                            }

                            tEnv().setAndroidDevice(tLocalEnv.getAndroid().getDevice());
                            tEnv().setAndroidOSVersion(tLocalEnv.getAndroid().getOs());
                        }
                    }
                    if (appType.equalsIgnoreCase("AndroidBrowser")) {
                        try {
                            tEnv().setAndroidDevice(tLocalEnv.getAndroid().getRegion_android_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("device").getAsString());
                            tEnv().setAndroidOSVersion(tLocalEnv.getAndroid().getRegion_android_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("os").getAsString());
                            tEnv().setWebBrowserVersion(tLocalEnv.getWeb().getBrowserVersion());
                        } catch (Exception e) {
                            if (tRemoteEnv.getTest_region() != null) {
                                logger.error("test_region is given as " + tRemoteEnv.getTest_region() + " in remote-env.json; Which is not present in test-env.json under Android devices!! Hence proceeding with default devices");
                            } else {
                                logger.error("test_region is not given in remote-env.json; Hence proceeding with default devices");
                            }
                            tEnv().setAndroidDevice(tLocalEnv.getAndroid().getDevice());
                            tEnv().setAndroidOSVersion(tLocalEnv.getAndroid().getOs());
                        }
                    }
                }
                tEnv().setModuleNames(tLocalEnv.getApi().getModuleNames());
            }

            if (tLocalEnv.getIos() != null) {
                if (appType != null) {
                    if (appType.equalsIgnoreCase("ios")) {

                        tEnv().setIosBundleId(tLocalEnv.getIos().getBundleid());
                        tEnv().setIosUDID(tLocalEnv.getIos().getUdid());
                        try {
                            tEnv().setIosDevice(tLocalEnv.getIos().getRegion_ios_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("device").getAsString());
                            tEnv().setIosOSVersion(tLocalEnv.getIos().getRegion_ios_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("os").getAsString());
                        } catch (Exception e) {
                            if (tRemoteEnv.getTest_region() != null) {
                                logger.error("test_region is given as " + tRemoteEnv.getTest_region() + " in remote-env.json; Which is not present in test-env.json under ios devices!! Hence proceeding with default devices");
                            } else {
                                logger.error("test_region is not given in remote-env.json; Hence proceeding with default devices");
                            }
                            tEnv().setIosDevice(tLocalEnv.getIos().getDevice());
                            tEnv().setIosOSVersion(tLocalEnv.getIos().getOs());
                        }
                    }
                    if (appType.equalsIgnoreCase("iosBrowser")) {
                        try {
                            tEnv().setIosDevice(tLocalEnv.getIos().getRegion_ios_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("device").getAsString());
                            tEnv().setIosOSVersion(tLocalEnv.getIos().getRegion_ios_devices().getAsJsonObject(tRemoteEnv.getTest_region()).get("os").getAsString());
                            tEnv().setWebBrowserVersion(tLocalEnv.getWeb().getBrowserVersion());
                        } catch (Exception e) {
                            if (tRemoteEnv.getTest_region() != null) {
                                logger.error("test_region is given as " + tRemoteEnv.getTest_region() + " in remote-env.json; Which is not present in test-env.json under ios devices!! Hence proceeding with default devices");
                            } else {
                                logger.error("test_region is not given in remote-env.json; Hence proceeding with default devices");
                            }
                            tEnv().setIosDevice(tLocalEnv.getIos().getDevice());
                            tEnv().setIosOSVersion(tLocalEnv.getIos().getOs());
                        }
                    }
                }
                tEnv().setModuleNames(tLocalEnv.getApi().getModuleNames());
            }


            if (tRemoteEnv.getDb_config() != null) {
                tEnv().setDbHost(tRemoteEnv.getDb_config().get("dbHost").getAsString());
                tEnv().setDbUserName(tRemoteEnv.getDb_config().get("dbUserName").getAsString());
                tEnv().setDbPassword(tRemoteEnv.getDb_config().get("dbPassword").getAsString());
                tEnv().setDbSSHHost(tRemoteEnv.getDb_config().get("dbSSHHost").getAsString());
                tEnv().setDbSSHUser(tRemoteEnv.getDb_config().get("dbSSHUser").getAsString());
                tEnv().setDbSSHFilePath(tRemoteEnv.getDb_config().get("dbSSHFilePath").getAsString());
                tEnv().setDbName(tRemoteEnv.getDb_config().get("dbName").getAsString());
            }

            if (tRemoteEnv.getProducts() != null) {
                try {
                    if (appType != null) {
                        if (appType.equalsIgnoreCase("Android")) {
                            tEnv().setAndroidBSAppPath(tRemoteEnv.getProducts().get(platformType).getAsJsonObject().get("android").getAsJsonObject().get("bs_app_path").getAsString());
                            tEnv().setAndroidBuildNumber(tRemoteEnv.getProducts().get(platformType).getAsJsonObject().get("android").getAsJsonObject().get("build_number").getAsString());
                            if (bs_app_path != null) {
                                logger.info("BS PATH is set for Android " + bs_app_path);
                                tEnv().setAndroidBSAppPath(bs_app_path);
                            }
                        } else if (appType.equalsIgnoreCase("Ios")) {
                            tEnv().setIosBSAppPath(tRemoteEnv.getProducts().get(platformType).getAsJsonObject().get("ios").getAsJsonObject().get("bs_app_path").getAsString());
                            tEnv().setIosBuildNumber(tRemoteEnv.getProducts().get(platformType).getAsJsonObject().get("ios").getAsJsonObject().get("build_number").getAsString());
                            if (bs_app_path != null) {
                                logger.info("BS PATH is set for iOS " + bs_app_path);
                                tEnv().setIosBSAppPath(bs_app_path);
                            }
                        }
                    }
                } catch (Exception e) {
                    Assert.fail(" Issue while picking correct Browserstack URL Path!! Check your testregion or testType or env json files");
                }

            }


            if (browser != null) {
                tEnv().setWebBrowser(browser);
            }
            if (grid_Hub_IP != null) {
                tEnv().setHubIP(grid_Hub_IP);
            }
            if (gps_location != null) {
                tEnv().setGps_location(gps_location);
            }
            if (browserVersion != null) {
                tEnv().setWebBrowserVersion(browserVersion);
            }
            if (url != null) {
                tEnv().setWebUrl(url);
            }
            if (device != null) {
                if ((tLocalEnv.getAndroid() != null) && ((appType.equalsIgnoreCase("Android") || appType.equalsIgnoreCase("AndroidBrowser")))) {
                    tEnv().setAndroidDevice(device);
                    tEnv().setAndroidOSVersion(os_version);
                }
                if ((tLocalEnv.getIos() != null) && ((appType.equalsIgnoreCase("ios") || appType.equalsIgnoreCase("iosBrowser")))) {
                    tEnv().setIosDevice(device);
                    tEnv().setIosOSVersion(os_version);
                }
            }
            if (URI != null) {
                tEnv().setApiURI(URI);
            }

            if (envType != null) {
                tEnv().setApiEnvType(envType);
            }
            if (appSycURI != null) {
                tEnv().setApiAppSycURI(appSycURI);
            }
            if (appSycAuth != null) {
                tEnv().setApiAppSycAuth(URI);
            }

            if (version != null) {
                tEnv().setApiVersion(version);
            }
            if (partnerURI != null) {
                tEnv().setApiPartnerURI(partnerURI);
            }
            if (token != null) {
                tEnv().setApiToken(AES.decrypt(token,"testautomation"));
            }
            if (accessToken != null) {
                tEnv().setApiAccessToken(accessToken);
            }
            if (store != null) {
                tEnv().setApiStore(store);
            }
            if (host != null) {
                tEnv().setApiHost(host);
            }
            if (locale != null) {
                tEnv().setApiLocale(locale);
            }
            if (region != null) {
                tEnv().setApiRegion(region);
            }
            if (country != null) {
                tEnv().setApiCountry(country);
            }
            if (currency != null) {
                tEnv().setApiCurrency(currency);
            }
            if (timezone != null) {
                tEnv().setApiTimeZone(timezone);
            }
            if (phoneNumber != null) {
                tEnv().setApiPhoneNumber(phoneNumber);
            }
            if (emailId != null) {
                tEnv().setApiEmailID(emailId);
            }
            if (excelFilePath != null) {
                tEnv().setExcelFilePath(excelFilePath);
            }
            if (isJWT != null) {
                tEnv().setIsJWT(isJWT);
            }
            if (endpointPrefix != null) {
                tEnv().setEndpointPrefix(endpointPrefix);
            }
            if (authorization != null) {
                tEnv().setAuthorization(authorization);
            }
            if (franchiseId != null) {
                tEnv().setFranchiseId(franchiseId);
            }
            if (dbType != null) {
                tEnv().setDbType(dbType);
            }

            if (serviceType != null) {
                tEnv().setServiceType(serviceType);
            }

            if (moduleNames != null) {
                tEnv().setModuleNames(moduleNames);
            }

            if (jsonFilePath != null) {
                logger.info("JSON File Path Set to " + jsonFilePath);
                tEnv().setJsonFilePath(jsonFilePath);
            }
            if (jsonDirectory != null) {
                tEnv().setJsonDirectory(jsonDirectory);
            } else {
                tEnv().setJsonDirectory("src/test/resources/TestData");
            }

            if (test_region != null) {
                tEnv().setTest_region(test_region);
                try {
//                    tEnv().setApiLocale(tLocalEnv.getRegion().getAsJsonObject(test_region).get("locale").getAsString());
//                    tEnv().setApiRegion(tLocalEnv.getRegion().getAsJsonObject(test_region).get("region").getAsString());
//                    tEnv().setApiStore(tLocalEnv.getRegion().getAsJsonObject(test_region).get("store").getAsString());
//                    tEnv().setApiHost(tLocalEnv.getRegion().getAsJsonObject(test_region).get("host").getAsString());
//                    tEnv().setApiToken(tLocalEnv.getRegion().getAsJsonObject(test_region).get("token").getAsString());
//                    tEnv().setApiAccessToken(tLocalEnv.getRegion().getAsJsonObject(test_region).get("accessToken").getAsString());
//                    tEnv().setApiCountry(tLocalEnv.getRegion().getAsJsonObject(test_region).get("country").getAsString());
//                    tEnv().setApiCurrency(tLocalEnv.getRegion().getAsJsonObject(test_region).get("currency").getAsString());
//                    tEnv().setApiTimeZone(tLocalEnv.getRegion().getAsJsonObject(test_region).get("timezone").getAsString());
//                    tEnv().setApiPhoneNumber(tLocalEnv.getRegion().getAsJsonObject(test_region).get("phoneNumber").getAsString());
//                    tEnv().setApiEmailID(tLocalEnv.getRegion().getAsJsonObject(test_region).get("emailId").getAsString());
                    setTakeawayDetails(tLocalEnv, tEnv().getTest_region());
                } catch (Exception e) {
                    Assert.fail("Provided test_region " + test_region + " in remote-env.json is not found in test-env.json");
                }
            } else {
                tEnv().setTest_region(tRemoteEnv.getTest_region());
                try {
//                    tEnv().setApiLocale(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("locale").getAsString());
//                    tEnv().setApiRegion(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("region").getAsString());
//                    tEnv().setApiStore(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("store").getAsString());
//                    tEnv().setApiHost(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("host").getAsString());
//                    tEnv().setApiToken(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("token").getAsString());
//                    tEnv().setApiAccessToken(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("accessToken").getAsString());
//                    tEnv().setApiCountry(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("country").getAsString());
//                    tEnv().setApiCurrency(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("currency").getAsString());
//                    tEnv().setApiTimeZone(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("timezone").getAsString());
//                    tEnv().setApiPhoneNumber(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("phoneNumber").getAsString());
//                    tEnv().setApiEmailID(tLocalEnv.getRegion().getAsJsonObject(tRemoteEnv.getTest_region()).get("emailId").getAsString());
                    setTakeawayDetails(tLocalEnv, tEnv().getTest_region());
                } catch (Exception e) {
                    Assert.fail("Provided test_region " + tRemoteEnv.getTest_region() + " in remote-env.json is not found in test-env.json or some mandatory keys in " + tRemoteEnv.getTest_region() + " are missed, Please check all keys");
                }
            }

//            if (tRemoteEnv.getEmail_recipients() != null) {
//                email_recipients = tRemoteEnv.getEmail_recipients();
//            }
//            if (tRemoteEnv.getError_email_recipients() != null) {
//                error_email_recipients = tRemoteEnv.getError_email_recipients();
//            }
//            if (tRemoteEnv.getFailure_email_recipients() != null) {
//                failure_email_recipients = tRemoteEnv.getFailure_email_recipients();
//            }

            if (test_email_recipients != null) {
                email_recipients = test_email_recipients;
            } else if (tRemoteEnv.getEmail_recipients() != null) {
                email_recipients = tRemoteEnv.getEmail_recipients();
            }
            if (test_error_email_recipients != null) {
                error_email_recipients = test_error_email_recipients;
            } else if (tRemoteEnv.getError_email_recipients() != null) {
                error_email_recipients = tRemoteEnv.getError_email_recipients();
            }
            if(test_failure_email_recipients !=null){
                failure_email_recipients = test_failure_email_recipients;
            } else if (tRemoteEnv.getFailure_email_recipients() != null) {
                failure_email_recipients = tRemoteEnv.getFailure_email_recipients();
            }
            
            if (tRemoteEnv.getBrowserstack_execution_local() != null) {
                tEnv().setBrowserstack_execution_local(tRemoteEnv.getBrowserstack_execution_local());
            }
            if (browserstack_execution_local != null) {
                tEnv().setBrowserstack_execution_local(browserstack_execution_local);
            }
            if(tRemoteEnv.getBrowserstack_midSessionInstallApps()!=null){
                tEnv().setBrowserstack_midSessionInstallApps((tRemoteEnv.getBrowserstack_midSessionInstallApps()));
            }
            if(browserstack_midSessionInstallApps!=null)
            {
                tEnv().setBrowserstack_midSessionInstallApps(browserstack_midSessionInstallApps);
            }
            if (productName != null) {
                tEnv().setProductName(productName);
            }
            tEnv().setCurrentTestClassName(class_name);

        } catch (Exception e) {
            captureException(e);
        }
    }

    public void setTakeawayDetails(TestEnvPojo tLocalEnv, String test_region) {
        try {
            tEnv().setApiLocale(tLocalEnv.getRegion().getAsJsonObject(test_region).get("locale").getAsString());
            tEnv().setApiRegion(tLocalEnv.getRegion().getAsJsonObject(test_region).get("region").getAsString());
            tEnv().setApiStore(tLocalEnv.getRegion().getAsJsonObject(test_region).get("store").getAsString());
            tEnv().setApiHost(tLocalEnv.getRegion().getAsJsonObject(test_region).get("host").getAsString());
            tEnv().setApiToken(tLocalEnv.getRegion().getAsJsonObject(test_region).get("token").getAsString());
            tEnv().setApiAccessToken(tLocalEnv.getRegion().getAsJsonObject(test_region).get("accessToken").getAsString());
            tEnv().setApiCountry(tLocalEnv.getRegion().getAsJsonObject(test_region).get("country").getAsString());
            tEnv().setApiCurrency(tLocalEnv.getRegion().getAsJsonObject(test_region).get("currency").getAsString());
            tEnv().setApiTimeZone(tLocalEnv.getRegion().getAsJsonObject(test_region).get("timezone").getAsString());
            tEnv().setApiPhoneNumber(tLocalEnv.getRegion().getAsJsonObject(test_region).get("phoneNumber").getAsString());
            tEnv().setApiEmailID(tLocalEnv.getRegion().getAsJsonObject(test_region).get("emailId").getAsString());
        } catch (Exception e) {
            System.out.println();
        }
    }

    public void hardWait(long delay, String... message) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            captureException(e);
        }
        if (message != null) {
            logger.info("Proceeding After waiting :" + delay + " milli Seconds to perform " + message);
        } else {
            logger.info("Proceeding After waiting :" + delay + " milli Seconds");
        }
    }

    private List<String> CommaDelimiter(String parameter) {
        List<String> parmeterList = new ArrayList<>();
        try {
            if (parameter != null) {
                parmeterList = Arrays.asList(parameter.split("\\,"));
            }
        } catch (Exception e) {
            captureException(e);
        }
        return parmeterList;
    }

    private String getFrameworkVersion() {
        String fVersion = "NA";
        try {
            String[] pathnames;
            File f = new File("gradleBuild");
            FilenameFilter filter = (f1, name) -> name.endsWith(".jar");
            pathnames = f.list(filter);
            fVersion = pathnames[0].substring(0, pathnames[0].length() - 4);
        } catch (NullPointerException e) {
            logger.error("gradleBuild Path Not Found");
        } catch (ArrayIndexOutOfBoundsException e1) {
            logger.warn("Missing framework libraries in gradleBuild Folder ! Try connecting to VPN and download Framework");
        }
        return fVersion;
    }


}

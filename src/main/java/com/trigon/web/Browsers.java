package com.trigon.web;

import com.browserstack.local.Local;
import com.trigon.mobile.Android;
import com.trigon.mobile.AppiumManager;
import io.appium.java_client.remote.options.UnhandledPromptBehavior;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.groovy.json.internal.Chr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v102.network.Network;
import org.openqa.selenium.devtools.v102.network.model.Request;
import org.openqa.selenium.devtools.v102.network.model.Response;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Browsers extends Android {
    private static final Logger logger = LogManager.getLogger(Browsers.class);
    Local bsLocal = null;
    private String browserType = "chrome";

    protected void createBrowserInstance(ITestContext context, XmlTest xmlTest) {

        String grid_Hub_IP = tEnv().getHubIP();
        if ((tEnv().getWebBrowser() != null) || tEnv().getWebBrowser().isEmpty()) {
            browserType = tEnv().getWebBrowser();
        }
        if (extent != null) {
            if (extentClassNode.get() != null) {
                extentClassNode.get().assignDevice(browserType);
            }
        }
        switch (browserType) {
            case "chrome":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        WebDriverManager.chromedriver().setup();
                        //System.setProperty("webdriver.chrome.driver", "src/test/resources/Utilities/chromedriver");
                        Map<String, Object> prefs = new HashMap<String, Object>();
                        Map<String, Object> profile = new HashMap<String, Object>();
                        Map<String, Object> contentSettings = new HashMap<String, Object>();
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("disable-geolocation");
                        // SET CHROME OPTIONS
                        // 0 - Default, 1 - Allow, 2 - Block
                      /*  String desired_path = System.getProperty("user.dir");
                        profile.put("download.default_directory",desired_path);    */                    contentSettings.put("geolocation", 1);
                        profile.put("managed_default_content_settings", contentSettings);
                        prefs.put("profile", profile);
                        options.setExperimentalOption("prefs", prefs);
                        options.addArguments("--remote-allow-origins=*");

                        if (tEnv().getWebHeadless().equalsIgnoreCase("true")) {
                            options.setHeadless(true);
                        }
                        // options.setLogLevel(OFF);
                        if(grid_execution_local.equalsIgnoreCase("true")){
                            webDriverThreadLocal.set(new RemoteWebDriver(new URL(grid_Hub_IP),options));
                        }else {
                            webDriverThreadLocal.set(new ChromeDriver(options));
                        }

                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL(context);
                } catch (Exception sn) {
                    captureException(sn);
                    sn.printStackTrace();
                    //hardFail("Chrome Session Not created !!");
                }
                break;
            case "firefox":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions options = new FirefoxOptions();
                        Map<String, Object> prefs = new HashMap<String, Object>();
                        Map<String, Object> profile = new HashMap<String, Object>();
                        Map<String, Object> contentSettings = new HashMap<String, Object>();
                        // SET CHROME OPTIONS
                        // 0 - Default, 1 - Allow, 2 - Block
                        contentSettings.put("geolocation", 1);
                        profile.put("managed_default_content_settings", contentSettings);
                        prefs.put("profile", profile);
                        options.setCapability("prefs", prefs);

                        if (tEnv().getWebHeadless().equalsIgnoreCase("true")) {
                            options.setHeadless(true);
                        }
                        if(grid_execution_local.equalsIgnoreCase("true")){
                            webDriverThreadLocal.set(new RemoteWebDriver(new URL(grid_Hub_IP),options));
                        }else {
                            webDriverThreadLocal.set(new FirefoxDriver(options));
                        }
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL(context);
                } catch (Exception sn) {
                    captureException(sn);
                    //hardFail("Firefox Session Not created !!");
                }
                break;
            case "safari":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        SafariOptions options = new SafariOptions();
                        options.setAutomaticInspection(true);
                        if(grid_execution_local.equalsIgnoreCase("true")){
                            webDriverThreadLocal.set(new RemoteWebDriver(new URL(grid_Hub_IP),options));
                        }else {
                            webDriverThreadLocal.set(new SafariDriver(options));
                        }
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL(context);
                } catch (Exception sn) {
                    captureException(sn);
                    //hardFail("Safari Session Not created !!");
                }
                break;
            case "edge":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        EdgeOptions options = new EdgeOptions();
                        //options.setAutomaticInspection(true);
                        if(grid_execution_local.equalsIgnoreCase("true")){
                            webDriverThreadLocal.set(new RemoteWebDriver(new URL(grid_Hub_IP),options));
                        }else {
                            webDriverThreadLocal.set(new EdgeDriver());
                        }
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL(context);
                } catch (Exception sn) {
                    captureException(sn);
                    sn.printStackTrace();
                    //hardFail("Chrome Session Not created !!");
                }
                break;
            case "opera":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        OperaOptions options = new OperaOptions();
                        //options.setAutomaticInspection(true);
                        if(grid_execution_local.equalsIgnoreCase("true")){
                            webDriverThreadLocal.set(new RemoteWebDriver(new URL(grid_Hub_IP), options));
                        }else {
                            webDriverThreadLocal.set(new OperaDriver());
                        }
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL(context);
                } catch (Exception sn) {
                    captureException(sn);
                    sn.printStackTrace();
                    //hardFail("Chrome Session Not created !!");
                }
                break;
            case "ie":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        InternetExplorerOptions options = new InternetExplorerOptions();
                        options.waitForUploadDialogUpTo(Duration.ofSeconds(2));
                        if(grid_execution_local.equalsIgnoreCase("true")){
                            webDriverThreadLocal.set(new RemoteWebDriver(new URL(grid_Hub_IP), options));
                        }else {
                            webDriverThreadLocal.set(new InternetExplorerDriver());
                        }
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL(context);
                } catch (Exception sn) {
                    captureException(sn);
                    sn.printStackTrace();
                    //hardFail("Chrome Session Not created !!");
                }
                break;
        }

    }


    private void remoteExecution(ITestContext context, XmlTest xmlTest) {
        DesiredCapabilities caps = new DesiredCapabilities();
        HashMap<String, Object> browserstackOptions = new HashMap<>();
//        caps.setCapability("project", context.getSuite().getName());
        caps.setCapability("platformName", tEnv().getWebSystemOS());
        caps.setCapability("build", tEnv().getWebBuildNumber() + "_" + tEnv().getTest_region());
        caps.setCapability("os_version", tEnv().getWebSystemOSVersion());
        caps.setCapability("browserName", tEnv().getWebBrowser());
        caps.setCapability("browserVersion", tEnv().getWebBrowserVersion());
        caps.setCapability("name", xmlTest.getName() + "_" + tEnv().getCurrentTestClassName());
//        caps.setCapability("language", "en");
        browserstackOptions.put("os", tEnv().getWebSystemOS());
        browserstackOptions.put("osVersion", tEnv().getWebSystemOSVersion());
        browserstackOptions.put("debug", "true");
        HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
        networkLogsOptions.put("captureContent", true);
        caps.setCapability("browserstack.networkLogs", true);
        caps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);
        //browserstackOptions.put("seleniumVersion", "4.0.0");
        browserstackOptions.put("consoleLogs", "errors");
        browserstackOptions.put("idleTimeout", "300");
        browserstackOptions.put("autoWait", "30");
        if(tEnv().getGps_location()!=null){
            browserstackOptions.put("gpsLocation", tEnv().getGps_location());
        }


        String location = tEnv().getApiCountry();
        if(location.equalsIgnoreCase("AUS")){
            location = "AU";
        }
        else if(location.equalsIgnoreCase("IRE")){
            location = "IE";
        }
        else if(location.equalsIgnoreCase("UK") || location.equalsIgnoreCase("GT")){
            location = "GB";
        }
        logger.info("Setting location to :: "+location);
       // caps.setCapability("browserstack.geoLocation",location);
        //browserstackOptions.put("geoLocation",location);
        ChromeOptions options = new ChromeOptions();
        Map < String, Object > prefs = new HashMap < String, Object > ();
        Map < String, Object > profile = new HashMap < String, Object > ();
        Map < String, Object > contentSettings = new HashMap < String, Object > ();

        contentSettings.put("geolocation", 1);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches",Arrays.asList("disable-popup-blocking"));

        caps.setCapability("browserstack.ie.enablePopups", "true");
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnhandledPromptBehavior.ACCEPT);
        if (tEnv().getBrowserstack_execution_local().equalsIgnoreCase("true")) {
            browserstackOptions.put("local", "true");
            caps.setCapability("forcelocal", "true");
            caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, "true");
            bsLocal = new Local();
            HashMap<String, String> bsLocalArgs = new HashMap<String, String>();
            bsLocalArgs.put("key", propertiesPojo.getBrowserStack_Password());
            try {
                bsLocal.start(bsLocalArgs);
                logger.info("BrowserStack local Instance Started");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    bsLocal.stop();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            try {
                caps.setCapability("bstack:options", browserstackOptions);
                webDriverThreadLocal.set(new RemoteWebDriver(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub.browserstack.com/wd/hub"), caps));
            } catch (Exception e) {
                captureException(e);
            }
        } else {
            try {
                caps.setCapability("bstack:options", browserstackOptions);
                webDriverThreadLocal.set(new RemoteWebDriver(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub-cloud.browserstack.com/wd/hub"), caps));
            } catch (Exception e) {
                captureException(e);
            }
        }
    }

    private void launchURL(ITestContext context) {
        try {
            if ((tEnv().getWebUrl() == null) || (tEnv().getWebUrl().isEmpty())) {
                Assert.fail("Please provide webURL in TestEnvironment File");
            } else {
                browser().manage().window().maximize();
                if (!context.getSuite().getName().contains("adhoc")) {

                    if(tEnv().getWebNetworkLogs()) {

                        if (browserType.contains("chrome") && executionType.contains("local")) {

                            DevTools devTools = ((HasDevTools) browser()).getDevTools();
                            devTools.createSession();
                            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

                            devTools.addListener(Network.requestWillBeSent(), request ->
                            {
                                Request req = request.getRequest();
                                // logReport("INFO",req.getMethod().toUpperCase());
                                //System.out.println(req.getMethod().toUpperCase());

                            });

                            devTools.addListener(Network.responseReceived(), response ->
                            {
                                Response res = response.getResponse();
                                //List<String> str = Collections.singletonList(res.getUrl());
                                if (res.getStatus().toString().startsWith("4") || res.getStatus().toString().startsWith("5")) {
                                    logger.info(res.getUrl() + "is failing with status code" + res.getStatus());
                                }
                            });
                        }
                    }
                    browser().get(tEnv().getWebUrl());
                    browser().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    browser().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
                } else {
                    logger.warn("Default URL is not loaded as It is adhoc task. please use navigateToUrl in your method level");
                }
            }
        } catch (Exception e) {
            captureException(e);
            hardFail("Unable to Launch Browser");
        }
    }

    protected void closeMobileClassLevel() {
        if (mobileApps.contains(tEnv().getTestType())) {
            try {
                if (ios() != null) {
                    JavascriptExecutor jse = ios();
                    if (executionType.equalsIgnoreCase("remote")) {
                        getBSVideoSession();
                        if (classFailAnalysisThread.get() != null) {
                            if (classFailAnalysisThread.get().size() > 0) {
                                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Check Assertions in Report\"}}");
                            }
                        } else {
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"NA\"}}");
                        }

                    }
                    ios().quit();
                    logger.info("IOS App Quit Successful");
                }
            } catch (Exception e) {
                captureException(e);
                logger.error("The IOS App session does not exist or App already closed or crashed");
            }
            try {
                if (android() != null) {
                    JavascriptExecutor jse = android();
                    if (executionType.equalsIgnoreCase("remote")) {
                        getBSVideoSession();
                        if (classFailAnalysisThread.get() != null) {
                            if (classFailAnalysisThread.get().size() > 0) {
                                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Check Assertions in Report\"}}");
                            }
                        } else {
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"NA\"}}");
                        }
                    }
                    android().quit();
                    logger.info("Android App Quit Successful");
                }
            } catch (Exception e) {
                captureException(e);
                logger.error("The Android App session does not exist or App already closed or crashed");
            }

            if (!executionType.equalsIgnoreCase("remote")) {
                try {
                    AppiumManager appiumManager = new AppiumManager();
                    appiumManager.stopAppium();
                } catch (Exception e) {
                    captureException(e);
                }
            }
        }
    }

    protected void closeBrowserClassLevel() {
        if (webApps.contains(tEnv().getTestType())) {
            try {
                if (browser() != null) {
                    JavascriptExecutor jse = browser();
                    if (executionType.equalsIgnoreCase("remote")) {
                        getBSVideoSession();
                        if (classFailAnalysisThread.get() != null) {
                            if (classFailAnalysisThread.get().size() > 0) {
                                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Check Assertions in Report\"}}");
                            }
                        } else {
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"NA\"}}");
                        }
                    }
                    browser().quit();
                    logger.info("Browser Session is closed");
                    if (tEnv().getBrowserstack_execution_local() != null) {
                        if (tEnv().getBrowserstack_execution_local().equalsIgnoreCase("true")) {
                            if (bsLocal != null) {
                                bsLocal.stop();
                                logger.info("BrowserStack Local Instance Stopped");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                captureException(e);
                logger.error("The browser session does not exist or browser already closed or crashed");
            }
        }
    }
}

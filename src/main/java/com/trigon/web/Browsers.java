package com.trigon.web;

import com.browserstack.local.Local;
import com.trigon.mobile.Android;
import com.trigon.mobile.AppiumManager;
import io.appium.java_client.remote.options.UnhandledPromptBehavior;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Browsers extends Android {
    private static final Logger logger = LogManager.getLogger(Browsers.class);
    Local bsLocal = null;

    protected void createBrowserInstance(ITestContext context, XmlTest xmlTest) {
        String browserType = "chrome";
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
                        contentSettings.put("geolocation", 1);
                        profile.put("managed_default_content_settings", contentSettings);
                        prefs.put("profile", profile);
                        options.setExperimentalOption("prefs", prefs);

                        if (tEnv().getWebHeadless().equalsIgnoreCase("true")) {
                            options.setHeadless(true);
                        }
                        // options.setLogLevel(OFF);
                        webDriverThreadLocal.set(new ChromeDriver(options));
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
                        //System.setProperty("webdriver.gecko.driver", "src/test/resources/Utilities/geckodriver");
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
                        webDriverThreadLocal.set(new FirefoxDriver(options));
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
                        webDriverThreadLocal.set(new SafariDriver(options));
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
                        EdgeDriver options = new EdgeDriver();
                        //options.setAutomaticInspection(true);
                        webDriverThreadLocal.set(new EdgeDriver());
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
                        OperaDriver options = new OperaDriver();
                        //options.setAutomaticInspection(true);
                        webDriverThreadLocal.set(new OperaDriver());
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
                        WebDriver driver = new RemoteWebDriver(options);
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
//        caps.setCapability("os_version", tEnv().getWebSystemOSVersion());
        caps.setCapability("browserName", tEnv().getWebBrowser());
        caps.setCapability("browserVersion", tEnv().getWebBrowserVersion());
        caps.setCapability("name", xmlTest.getName() + "_" + tEnv().getCurrentTestClassName());
//        caps.setCapability("language", "en");

        browserstackOptions.put("os", "Windows");
        browserstackOptions.put("osVersion", "11");
        browserstackOptions.put("debug", "true");
        browserstackOptions.put("networkLogs", "true");
        browserstackOptions.put("seleniumVersion", "4.0.0");
        browserstackOptions.put("consoleLogs", "errors");
        browserstackOptions.put("idleTimeout", "300");
        browserstackOptions.put("autoWait", "30");


        String location = tEnv().getApiCountry();
        if(location.equalsIgnoreCase("AUS")){
            location = "AU";
        }
        else if(location.equalsIgnoreCase("IRE")){
            location = "IE";
        }
        else if(location.equalsIgnoreCase("UK")){
            location = "GB";
        }
        logger.info("Setting location to :: "+location);
       // caps.setCapability("browserstack.geoLocation",location);
        browserstackOptions.put("geoLocation",location);
        caps.setCapability(CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR, UnhandledPromptBehavior.ACCEPT);
//        caps.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
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

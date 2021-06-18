package com.trigon.web;

import com.trigon.mobile.Android;
import com.trigon.mobile.AppiumManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class Browsers extends Android {
    private static final Logger logger = LogManager.getLogger(Browsers.class);

    protected void createBrowserInstance(ITestContext context, XmlTest xmlTest) {
        String browserType = "chrome";
        if ((tEnv().getWebBrowser() != null) || tEnv().getWebBrowser().isEmpty()) {
            browserType = tEnv().getWebBrowser();
        }
        switch (browserType) {
            case "chrome":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        WebDriverManager.chromedriver().setup();
                        //System.setProperty("webdriver.chrome.driver", "src/test/resources/Utilities/chromedriver");
                        ChromeOptions options = new ChromeOptions();
                        if (tEnv().getWebHeadless().equalsIgnoreCase("true")) {
                            options.setHeadless(true);
                        }
                        // options.setLogLevel(OFF);
                        webDriverThreadLocal.set(new ChromeDriver(options));
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL();
                } catch (Exception sn) {
                    captureException(sn);
                    hardFail("Chrome Session Not created !!");
                }
                break;
            case "firefox":
                try {
                    if (executionType.equalsIgnoreCase("local")) {
                        WebDriverManager.firefoxdriver().setup();
                        //System.setProperty("webdriver.gecko.driver", "src/test/resources/Utilities/geckodriver");
                        FirefoxOptions options = new FirefoxOptions();
                        if (tEnv().getWebHeadless().equalsIgnoreCase("true")) {
                            options.setHeadless(true);
                        }
                        webDriverThreadLocal.set(new FirefoxDriver(options));
                    } else {
                        remoteExecution(context, xmlTest);
                    }
                    launchURL();
                } catch (Exception sn) {
                    captureException(sn);
                    hardFail("Firefox Session Not created !!");
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
                    launchURL();
                } catch (Exception sn) {
                    captureException(sn);
                    hardFail("Safari Session Not created !!");
                }
                break;
        }

    }


    private void remoteExecution(ITestContext context, XmlTest xmlTest) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", context.getSuite().getName());
        caps.setCapability("os", tEnv().getWebSystemOS());
        caps.setCapability("build", tEnv().getWebBuildNumber());
        caps.setCapability("os_version", tEnv().getWebSystemOSVersion());
        caps.setCapability("browser", tEnv().getWebBrowser());
        caps.setCapability("browser_version", tEnv().getWebBrowserVersion());
        caps.setCapability("name", xmlTest.getName());
       // caps.setCapability("browserstack.selenium_version", "4.0.0-alpha-7");
        caps.setCapability("browserstack.networkLogs", true);
        caps.setCapability("browserstack.console", "errors");
        caps.setCapability("browserstack.idleTimeout", "300");
        caps.setCapability("browserstack.autoWait", "50");
        caps.setCapability("language", "en");
        caps.setCapability("locale", "en");
        try {
            webDriverThreadLocal.set(new RemoteWebDriver(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub-cloud.browserstack.com/wd/hub"), caps));
        } catch (Exception e) {
            captureException(e);
        }
    }

    private void launchURL() {
        try {
            if ((tEnv().getWebUrl() == null) || (tEnv().getWebUrl().isEmpty())) {
                Assert.fail("Please provide webURL in TestEnvironment File");
            } else {
                browser().manage().window().maximize();
                browser().get(tEnv().getWebUrl());
                browser().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
                    ios().getSessionId();
                    logger.info("IOS Session ID"+ ios().getSessionId());
                    JavascriptExecutor jse = ios();
                    if (executionType.equalsIgnoreCase("remote")) {
                        if (classFailAnalysisThread.get().size() > 0) {
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Check Assertions in Report\"}}");

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
                    android().getSessionId();
                    logger.info("Android Session ID"+ android().getSessionId());
                    JavascriptExecutor jse = android();
                    if (executionType.equalsIgnoreCase("remote")) {
                        if (classFailAnalysisThread.get().size() > 0) {
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Check Assertions in Report\"}}");

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
                    //browser().close();
                    browser().getSessionId();
                    JavascriptExecutor jse = browser();

                    if (executionType.equalsIgnoreCase("remote")){
                        if(classFailAnalysisThread.get().size()>0){
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Check Assertions in Report\"}}");

                        }else {
                            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"NA\"}}");

                        }
                    }
                    browser().quit();
                    logger.info("Browser Session is closed");
                }
            } catch (Exception e) {
                captureException(e);
                logger.error("The browser session does not exist or browser already closed or crashed");
            }
        }
    }


}

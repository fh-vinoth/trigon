package com.trigon.mobile;

import com.trigon.reports.ReportManager;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;


public class IOS extends ReportManager {
    private static final Logger logger = LogManager.getLogger(IOS.class);

    protected void nativeiOS(ITestContext context, XmlTest xmlTest) {
        MutableCapabilities iosCaps = new MutableCapabilities();
        HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
        long startTime = System.currentTimeMillis();
        try {
            if (extentTestNode.get() != null) {
                extentClassNode.get().assignDevice(tEnv().getIosDevice());
            }
            if (executionType.equalsIgnoreCase("remote")) {

                if (tEnv().getAppType().equalsIgnoreCase("iOSBrowser")) {
                    browserstackOptions.put("osVersion", tEnv().getIosOSVersion());
                    browserstackOptions.put("deviceName", tEnv().getIosDevice());
                    iosCaps.setCapability(MobileCapabilityType.BROWSER_NAME, tEnv().getWebBrowser());
                    iosCaps.setCapability(MobileCapabilityType.BROWSER_VERSION, tEnv().getWebBrowserVersion());
                    browserstackOptions.put("buildName", tEnv().getWebBuildNumber() + "_" + tEnv().getTest_region());

                } else {
                    logger.info("Setting IOS Native capabilities in BrowserStack Device : " + tEnv().getIosDevice());
                    iosCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, tEnv().getIosOSVersion());
                    iosCaps.setCapability(MobileCapabilityType.DEVICE_NAME, tEnv().getIosDevice());
                    iosCaps.setCapability(MobileCapabilityType.APP, tEnv().getIosBSAppPath());
                    browserstackOptions.put("buildName", tEnv().getIosBuildNumber() + "_" + tEnv().getTest_region());
                }
                browserstackOptions.put("projectName", context.getSuite().getName());
                browserstackOptions.put("sessionName", xmlTest.getName() + "_" + tEnv().getCurrentTestClassName());
                //browserstackOptions.put("appiumVersion", "1.21.0");
                browserstackOptions.put("realMobile", "true");
                browserstackOptions.put("acceptInsecureCerts", "true");
                iosCaps.setCapability("autoAcceptAlerts", true);
                iosCaps.setCapability("unicodeKeyboard", true);
                iosCaps.setCapability("resetKeyboard", true);
                iosCaps.setCapability("autoGrantPermissions",true);
                HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
                networkLogsOptions.put("captureContent", true);
                iosCaps.setCapability("browserstack.networkLogs", true);
                iosCaps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);
                iosCaps.setCapability("browserstack.networkProfile", tEnv().getNetworkProfile());
                browserstackOptions.put("networkProfile", "reset");
                iosCaps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);
                iosCaps.setCapability("browserstack.networkProfile", tEnv().getNetworkProfile());
                iosCaps.setCapability("browserstack.customNetwork", tEnv().getCustomNetwork());

                browserstackOptions.put("idleTimeout", "300");
                browserstackOptions.put("autoWait", "50");
                browserstackOptions.put("debug", "true");
                browserstackOptions.put("appiumLogs", "true");
                browserstackOptions.put("interactiveDebugging","true");
                if(tEnv().getGps_location()!=null){
                    browserstackOptions.put("gpsLocation", tEnv().getGps_location());
                }

                if(tEnv().getBrowserstack_midSessionInstallApps()!=null)
                {
                    try {
                        String obj[] = tEnv().getBrowserstack_midSessionInstallApps().split(",", 3);
                        switch (obj.length) {
                            case 1:
                                browserstackOptions.put("midSessionInstallApps", new String[]{obj[0].trim()});
                                break;
                            case 2:
                                browserstackOptions.put("midSessionInstallApps", new String[]{obj[0].trim(), obj[1].trim()});
                                break;
                            case 3:
                                browserstackOptions.put("midSessionInstallApps", new String[]{obj[0].trim(), obj[1].trim(), obj[2].trim()});
                                break;
                            case 0:
                                logStepAction("No valid BS apppaths param provided for browserstack_midSessionInstallApps");
                        }
                    }
                    catch (Exception e)
                    {
                        hardFail(e+"\nNote : In case of multiple app BS paths, use commas(,) to separate. Maximum 3 apps allowed to install mid session by BS");
                    }
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
//                iosCaps.setCapability("browserstack.geoLocation",location);
                browserstackOptions.put("geoLocation",location);
                /*//iosCaps.setCapability("browserstack.geoLocation","NZ");
                //iosCaps.setCapability("autoAcceptAlerts", "true");
                //iosCaps.setCapability("autoDissmissAlerts", "true");*/

                iosCaps.setCapability("bstack:options", browserstackOptions);
                iosDriverThreadLocal.set(new IOSDriver(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub-cloud.browserstack.com/wd/hub"), iosCaps));

            } else {
                iosCaps.setCapability(MobileCapabilityType.NO_RESET, false);
                if (tEnv().getAppType().equalsIgnoreCase("iOSBrowser")) {
                    iosCaps.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");

                }
                if (tEnv().getIosUDID() != null) {
                    iosCaps.setCapability("udid", tEnv().getIosUDID());
                }
                iosCaps.setCapability(MobileCapabilityType.DEVICE_NAME, tEnv().getIosDevice());
                logger.info("Setting IOS Native capabilities in Local Device : " + tEnv().getIosDevice());
                iosCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                iosCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, tEnv().getIosOSVersion());
                iosCaps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                //iosCaps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "10000");
                iosCaps.setCapability("autoDismissAlerts", true);
                iosCaps.setCapability("useFirstMatch", true);
                iosCaps.setCapability("skipLogCapture", false);
                iosCaps.setCapability("showIOSLog", false);
                iosCaps.setCapability("interKeyDelay", 50);
                iosCaps.setCapability("nativeInstrumentsLib", false);
                iosCaps.setCapability("showXcodeLog", false);
                iosCaps.setCapability("waitForQuiescence", false);
                iosCaps.setCapability("useJSONSource", false);
                iosCaps.setCapability("includeNonModalElements", true);
                iosCaps.setCapability("snapshotTimeout", 1);
                iosCaps.setCapability("bundleId", tEnv().getIosBundleId());
                if (tEnv().getAppType().equalsIgnoreCase("iOSBrowser")) {
                    iosCaps.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                }
/*                if (tEnv().getIos().getIosLocalAppPath() != null) {
                    File iosapp = new File(tEnv().getIos().getIosLocalAppPath());
                    iosCaps.setCapability("app", iosapp.getAbsolutePath());
                }
                if (tEnv().getIos().getIosAppReset() != null) {
                    if (tEnv().getIos().getIosAppReset().equalsIgnoreCase("fullReset")) { // reinstall new client version
                        logger.info("IOSDriver is starting with Fast Reset : App will Reinstall in device");
                        iosCaps.setCapability(MobileCapabilityType.FULL_RESET, true);
                        iosCaps.setCapability(MobileCapabilityType.NO_RESET, false);
                    } else if (tEnv().getIos().getIosAppReset().equalsIgnoreCase("fastReset")) { // clears cache without reinstall
                        logger.info("IOSDriver is starting with Fast Reset : Clears Cache without reinstalling App");
                        iosCaps.setCapability(MobileCapabilityType.FULL_RESET, false);
                        iosCaps.setCapability(MobileCapabilityType.NO_RESET, false);
                    } else {
                        logger.info("IOSDriver is starting Normally without Reset : App will NOT Reinstall : Cache will NOT Clear");
                        iosCaps.setCapability(MobileCapabilityType.FULL_RESET, false);
                        iosCaps.setCapability(MobileCapabilityType.NO_RESET, true);
                    }
                } else {
                    logger.info("IOSDriver is starting Normally without Reset : App will NOT Reinstall : Cache will NOT Clear");
                    iosCaps.setCapability(MobileCapabilityType.FULL_RESET, false);
                    iosCaps.setCapability(MobileCapabilityType.NO_RESET, true);
                }*/
                iosDriverThreadLocal.set(new IOSDriver(new URL(tEnv().getAppiumURL()), iosCaps));
            }
            ios().manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
            ios().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            String iOSType = "Native App";
            if (tEnv().getAppType().equalsIgnoreCase("iOSBrowser")) {
                ios().navigate().to(tEnv().getWebUrl());
                iOSType = tEnv().getWebBrowser() + " Browser on " + tEnv().getWebBrowserVersion();
            }
            logger.info("*****************************************");
            logger.info("IOS " + iOSType + " Launched Successfully in Device " + tEnv().getIosDevice());
            logger.info("*****************************************");
            long endTime = System.currentTimeMillis();
            logger.info("Time Taken to Launch IOS " + iOSType + " : : " + cUtils().getRunDuration(startTime, endTime));
        } catch (Exception e) {
            captureException(e);
            hardFail("Failed to Launch IOS Device : " + tEnv().getIosDevice() + " Check your Test Parameters");
        }
        if (ios() == null) {
            hardFail("Failed to Launch IOS Device : " + tEnv().getIosDevice() + " Check your Test Parameters");
        }
    }
}

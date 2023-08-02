package com.trigon.mobile;

import com.trigon.appcenter.AppCenterBS;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Android extends IOS {
    private static final Logger logger = LogManager.getLogger(Android.class);
    private static final String ANDROID = "Android";

    public void androidNative(ITestContext context, XmlTest xmlTest) {

        MutableCapabilities androidCaps = new MutableCapabilities();
        HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
        long startTime = System.currentTimeMillis();
        try {
            if (extentTestNode.get() != null) {
                extentClassNode.get().assignDevice(tEnv().getAndroidDevice());
            }
            if (executionType.equalsIgnoreCase("remote")) {

                if (tEnv().getAppType().equalsIgnoreCase("AndroidBrowser")) {
                    browserstackOptions.put("osVersion", tEnv().getAndroidOSVersion());
                    browserstackOptions.put("deviceName", tEnv().getAndroidDevice());
                    androidCaps.setCapability("browserName", tEnv().getWebBrowser());
                    androidCaps.setCapability("autoAcceptAlerts", true);
                    androidCaps.setCapability("unicodeKeyboard", true);
                    androidCaps.setCapability("resetKeyboard", true);
                    androidCaps.setCapability("autoGrantPermissions",true);
                    androidCaps.setCapability("browserVersion", tEnv().getWebBrowserVersion());
                    browserstackOptions.put("buildName", tEnv().getWebBuildNumber() + "_" + tEnv().getTest_region());

                } else {
                    androidCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, tEnv().getAndroidOSVersion());
                    androidCaps.setCapability(MobileCapabilityType.DEVICE_NAME, tEnv().getAndroidDevice());
                    androidCaps.setCapability(MobileCapabilityType.APP, tEnv().getAndroidBSAppPath());
                    browserstackOptions.put("buildName", tEnv().getAndroidBuildNumber() + "_" + tEnv().getTest_region());
                }
                browserstackOptions.put("projectName", context.getSuite().getName());
                browserstackOptions.put("sessionName", xmlTest.getName() + "_" + tEnv().getCurrentTestClassName());
                browserstackOptions.put("appiumVersion", "1.22.0");
                browserstackOptions.put("realMobile", "true");
                browserstackOptions.put("acceptInsecureCerts", "true");

                HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
                networkLogsOptions.put("captureContent", true);
                androidCaps.setCapability("browserstack.networkLogs", true);
                androidCaps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);
                androidCaps.setCapability("browserstack.realMobileInteraction", "true");
                browserstackOptions.put("networkProfile", "reset");
                androidCaps.setCapability("autoAcceptAlerts", true);
                androidCaps.setCapability("unicodeKeyboard", true);
                androidCaps.setCapability("resetKeyboard", true);
                androidCaps.setCapability("autoGrantPermissions",true);
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



               /* androidCaps.setCapability("browserstack.appium_version", "1.22.0");
                androidCaps.setCapability("browserstack.acceptInsecureCerts", "true");
                androidCaps.setCapability("browserstack.networkLogs",true);
                androidCaps.setCapability("browserstack.console","errors");
                androidCaps.setCapability("browserstack.idleTimeout","300");
                androidCaps.setCapability("browserstack.autoWait","50");
                androidCaps.setCapability("browserstack.debug", "true");
                androidCaps.setCapability("browserstack.networkLogs", "true");
                androidCaps.setCapability("browserstack.appiumLogs", "true");
                androidCaps.setCapability("autoGrantPermissions","true");*/

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
                //androidCaps.setCapability("browserstack.geoLocation",location);
                browserstackOptions.put("geoLocation",location);
                if (tEnv().getTestType().equalsIgnoreCase("digitalboard")) {
                    browserstackOptions.put("deviceOrientation", "landscape");
                }

                androidCaps.setCapability("bstack:options", browserstackOptions);

                //androidCaps.setCapability("browserstack.networkProfile", "4g-lte-good");
                androidDriverThreadLocal.set(new AndroidDriver(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub-cloud.browserstack.com/wd/hub"), androidCaps));

            } else {
                androidCaps.setCapability("osVersion", tEnv().getAndroidOSVersion());
                androidCaps.setCapability("deviceName", tEnv().getAndroidDevice());
                androidCaps.setCapability("automationName", "uiautomator2");
                //logger.info("Setting Android Native capabilities in Local Device :: " + tEnv().getAndroidDevice());

                androidCaps.setCapability("appPackage", tEnv().getAndroidAppPackage());
                androidCaps.setCapability("appActivity", tEnv().getAndroidAppActivity());
                androidCaps.setCapability("autoDismissAlerts", true);
                if (tEnv().getAppType().equalsIgnoreCase("AndroidBrowser")) {
                    androidCaps.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");

                }
                androidCaps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "10000");
/*                if (tEnv().getAndroidLocalAppPath() != null) {
                    File app = new File(tEnv().getAndroidLocalAppPath());
                    // androidCaps.setCapability("app", app.getAbsolutePath());
                }*/
                if (tEnv().getApp_reset() != null) {
                    if (tEnv().getApp_reset().equalsIgnoreCase("fullReset")) { // reinstall new client version
                        logger.info("  AndroidDriver DO FULL-RESET");
                        androidCaps.setCapability(MobileCapabilityType.FULL_RESET, true);
                        androidCaps.setCapability(MobileCapabilityType.NO_RESET, false);
                    } else if (tEnv().getApp_reset().equalsIgnoreCase("fastReset")) { // clears cache without reinstall
                        logger.info("  AndroidDriver DO FAST-RESET");
                        androidCaps.setCapability(MobileCapabilityType.FULL_RESET, false);
                        androidCaps.setCapability(MobileCapabilityType.NO_RESET, false);
                    } else {
                        logger.info("  AndroidDriver DO NORMAL start");
                        androidCaps.setCapability(MobileCapabilityType.FULL_RESET, false);
                        androidCaps.setCapability(MobileCapabilityType.NO_RESET, true);
                    }
                } else {
                    logger.info("  AndroidDriver DO NORMAL start");
                    androidCaps.setCapability(MobileCapabilityType.FULL_RESET, false);
                    androidCaps.setCapability(MobileCapabilityType.NO_RESET, true);
                }
                androidDriverThreadLocal.set(new AndroidDriver(new URL(tEnv().getAppiumURL()), androidCaps));
            }
            android().manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
            android().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            String AndroidType = "Native App";
            if (tEnv().getAppType().equalsIgnoreCase("AndroidBrowser")) {
                android().navigate().to(tEnv().getWebUrl());
                AndroidType = tEnv().getWebBrowser() + " Browser on " + tEnv().getWebBrowserVersion();
            }
            logger.info("*****************************************");
            logger.info("Android " + AndroidType + " Launched Successfully in Device " + tEnv().getAndroidDevice());
            logger.info("*****************************************");
            long endTime = System.currentTimeMillis();
            logger.info("Time Taken to Launch " + AndroidType + " App: : " + cUtils().getRunDuration(startTime, endTime));
        } catch (Exception e) {
            captureException(e);
            hardFail("Failed to Launch Android Device : " + tEnv().getAndroidDevice() + " Check your Test Parameters");
        }
        if (android() == null) {
            hardFail("Failed to Launch Android Device : " + tEnv().getAndroidDevice() + " Check your Test Parameters");
        }
    }

    private HashMap<String, String> get_bs_android_app_url() {
        AppCenterBS appCenter = new AppCenterBS();
        HashMap<String, String> buildData = new HashMap<>();
        if (platformType.equalsIgnoreCase("D2S")) {
            buildData = appCenter.getBSAppURL(propertiesPojo.getD2S_Appcenter_Android_ProjectName(), propertiesPojo.getD2S_Automation_Branch_Name());
        } else if (platformType.equalsIgnoreCase("MYT")) {
            buildData = appCenter.getBSAppURL(propertiesPojo.getMYT_Appcenter_Android_ProjectName(), propertiesPojo.getMYT_Automation_Branch_Name());
        } else if (platformType.equalsIgnoreCase("FHAPP")) {
            buildData = appCenter.getBSAppURL(propertiesPojo.getFHApp_Appcenter_Android_ProjectName(), propertiesPojo.getFHApp_Automation_Branch_Name());
        } else if (platformType.equalsIgnoreCase("CA")) {
            buildData = appCenter.getBSAppURL(propertiesPojo.getCA_Appcenter_Android_ProjectName(), propertiesPojo.getCA_Automation_Branch_Name());
        } else if (platformType.equalsIgnoreCase("MOBILE")) {
            if (tEnv().getAndroidBSAppPath() != null) {
                buildData.put("app_url", tEnv().getAndroidBSAppPath());
                buildData.put("releaseId", tEnv().getAndroidBuildNumber());
            } else {
                hardFail("Add App URL in TestEnv JSON file");
            }
        }

        if (buildData.size() == 0) {
            hardFail("Issue with Browserstack/Appcenter Configurations");
        }
        return buildData;
    }

}

package com.trigon.mobile;

import com.trigon.appcenter.AppCenterBS;
import com.trigon.reports.ReportManager;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class IOS extends ReportManager {
    private static final Logger logger = LogManager.getLogger(IOS.class);

    protected void nativeiOS(ITestContext context, XmlTest xmlTest) {
        MutableCapabilities iosCaps = new MutableCapabilities();
        long startTime = System.currentTimeMillis();
        try {
            extentClassNode.get().assignDevice(tEnv().getIosDevice());
            if (executionType.equalsIgnoreCase("remote")) {
                iosCaps.setCapability("os_version", tEnv().getIosOSVersion());
                iosCaps.setCapability("deviceName", tEnv().getIosDevice());
                logger.info("Setting IOS Native capabilities in BrowserStack Device : " + tEnv().getIosDevice());

                //HashMap<String,String> buildData = get_bs_ios_app_url();

                iosCaps.setCapability("app", tEnv().getIosBSAppPath());
                iosCaps.setCapability("project", context.getSuite().getName());
                iosCaps.setCapability("build", tEnv().getIosBuildNumber()+"_"+tEnv().getTest_region());
                iosCaps.setCapability("name", xmlTest.getName()+"_"+tEnv().getCurrentTestClassName());
                //iosCaps.setCapability("browserstack.appium_version", "1.17.0");
                iosCaps.setCapability("browserstack.acceptInsecureCerts", "true");
                iosCaps.setCapability("browserstack.networkLogs",true);
                iosCaps.setCapability("browserstack.console","errors");
                iosCaps.setCapability("browserstack.idleTimeout","300");
                iosCaps.setCapability("browserstack.autoWait","50");
                iosCaps.setCapability("browserstack.debug", "true");
                iosCaps.setCapability("browserstack.networkLogs", "true");
                iosCaps.setCapability("browserstack.appiumLogs", "true");
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
                iosCaps.setCapability("browserstack.geoLocation",location);
                //iosCaps.setCapability("browserstack.geoLocation","NZ");
                //iosCaps.setCapability("autoAcceptAlerts", "true");
                //iosCaps.setCapability("autoDissmissAlerts", "true");
                iosDriverThreadLocal.set(new IOSDriver<>(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub-cloud.browserstack.com/wd/hub"), iosCaps));

            } else {
                if (tEnv().getIosUDID() != null) {
                    iosCaps.setCapability("udid", tEnv().getIosUDID());
                }
                iosCaps.setCapability(MobileCapabilityType.DEVICE_NAME, tEnv().getIosDevice());
                logger.info("Setting IOS Native capabilities in Local Device : " + tEnv().getIosDevice());
                iosCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                iosCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, tEnv().getIosOSVersion());
                iosCaps.setCapability("automationName", "XCUITest");
                iosCaps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "10000");
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
                    iosDriverThreadLocal.set(new IOSDriver<>(new URL(tEnv().getAppiumURL()), iosCaps));
            }
            ios().manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
            ios().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            logger.info("*****************************************");
            logger.info("IOS Native App Launched Successfully in Device " + tEnv().getIosDevice());
            logger.info("*****************************************");
            long endTime = System.currentTimeMillis();
            logger.info("Time Taken to Launch IOS Native App: : " + cUtils().getRunDuration(startTime, endTime));
        } catch (Exception e) {
            captureException(e);
            hardFail("Failed to Launch IOS Device : "+tEnv().getIosDevice() +" Check your Test Parameters");
        }
        if (ios() == null) {
            hardFail("Failed to Launch IOS Device : "+tEnv().getIosDevice() +" Check your Test Parameters");
        }
    }


}

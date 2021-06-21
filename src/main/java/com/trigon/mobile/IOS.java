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
            if (executionType.equalsIgnoreCase("remote")) {
                iosCaps.setCapability("os_version", tEnv().getIosOSVersion());
                iosCaps.setCapability("deviceName", tEnv().getIosDevice());
                logger.info("Setting IOS Native capabilities in BrowserStack Device : " + tEnv().getIosDevice());

                //HashMap<String,String> buildData = get_bs_ios_app_url();

                iosCaps.setCapability("app", tEnv().getIosBSAppPath());
                iosCaps.setCapability("project", context.getSuite().getName());
                iosCaps.setCapability("build", tEnv().getIosBuildNumber());
                iosCaps.setCapability("name", xmlTest.getName());
                //iosCaps.setCapability("browserstack.appium_version", "1.17.0");
                iosCaps.setCapability("browserstack.acceptInsecureCerts", "true");
                iosCaps.setCapability("browserstack.networkLogs",true);
                iosCaps.setCapability("browserstack.console","errors");
                iosCaps.setCapability("browserstack.idleTimeout","300");
                iosCaps.setCapability("browserstack.autoWait","50");
                iosCaps.setCapability("browserstack.debug", "true");
                iosCaps.setCapability("browserstack.networkLogs", "true");
                iosCaps.setCapability("browserstack.appiumLogs", "true");

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

    private HashMap<String,String> get_bs_ios_app_url() {
        AppCenterBS appCenter = new AppCenterBS();

        HashMap<String,String> buildData=new HashMap<>();
        if(platformType.equalsIgnoreCase("D2S")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getD2S_Appcenter_IOS_ProjectName(),propertiesPojo.getD2S_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("MYT")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getMYT_Appcenter_IOS_ProjectName(),propertiesPojo.getMYT_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("FHAPP")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getFHApp_Appcenter_IOS_ProjectName(),propertiesPojo.getFHApp_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("CA")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getCA_Appcenter_IOS_ProjectName(),propertiesPojo.getCA_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("MOBILE")){
            if(tEnv().getIosBSAppPath()!=null){
                buildData.put("app_url",tEnv().getIosBSAppPath());
                buildData.put("releaseId",tEnv().getIosBuildNumber());
            }else{
                hardFail("Add App URL in TestEnv JSON file");
            }
        }

        if(buildData.size()==0){
            hardFail("Issue with Browserstack/Appcenter Configurations");
        }
        return buildData;
    }

}

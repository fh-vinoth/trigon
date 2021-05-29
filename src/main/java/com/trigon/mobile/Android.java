package com.trigon.mobile;

import com.trigon.appcenter.AppCenterBS;
import com.trigon.testbase.TestUtilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Android extends IOS {
    private static final Logger logger = LogManager.getLogger(Android.class);
    private static final String ANDROID = "Android";

    public void androidNative(ITestContext context, XmlTest xmlTest) {

        MutableCapabilities androidCaps = new MutableCapabilities();

        long startTime = System.currentTimeMillis();
        try {
            if (executionType.equalsIgnoreCase("remote")) {
                androidCaps.setCapability("os_version", tEnv().getAndroidOSVersion());
                androidCaps.setCapability("device", tEnv().getAndroidDevice());
                logger.info("Setting Android Native capabilities in BrowserStack Device : " + tEnv().getAndroidDevice());

                //HashMap<String,String> buildData = get_bs_android_app_url();

                androidCaps.setCapability("app", tEnv().getAndroidBSAppPath());
                androidCaps.setCapability("project", context.getSuite().getName());
                androidCaps.setCapability("build", tEnv().getAndroidBuildNumber());
                androidCaps.setCapability("name", xmlTest.getName());
                //androidCaps.setCapability("browserstack.appium_version", "1.17.0");
                androidCaps.setCapability("browserstack.acceptInsecureCerts", "true");
                androidCaps.setCapability("browserstack.networkLogs",true);
                androidCaps.setCapability("browserstack.console","errors");
                androidCaps.setCapability("browserstack.idleTimeout","300");
                androidCaps.setCapability("browserstack.autoWait","50");
                androidCaps.setCapability("browserstack.debug", "true");
                androidCaps.setCapability("browserstack.networkLogs", "true");
                androidCaps.setCapability("browserstack.appiumLogs", "true");
                //androidCaps.setCapability("browserstack.networkProfile", "4g-lte-good");
                TestUtilities.androidDriverThreadLocal.set(new AndroidDriver<>(new URL("http://" + propertiesPojo.getBrowserStack_UserName() + ":" + propertiesPojo.getBrowserStack_Password() + "@hub-cloud.browserstack.com/wd/hub"), androidCaps));

            } else {
                androidCaps.setCapability(MobileCapabilityType.PLATFORM_NAME, ANDROID);
                androidCaps.setCapability(MobileCapabilityType.PLATFORM_VERSION, tEnv().getAndroidOSVersion());
                androidCaps.setCapability(MobileCapabilityType.DEVICE_NAME, tEnv().getAndroidDevice());
                logger.info("Setting Android Native capabilities in Local Device :: " + tEnv().getAndroidDevice());

                androidCaps.setCapability("appPackage", tEnv().getAndroidAppPackage());
                androidCaps.setCapability("appActivity",tEnv().getAndroidAppActivity());
                androidCaps.setCapability("autoDismissAlerts", true);
                androidCaps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "10000");
/*                if (TestUtilities.tEnv().getAndroidLocalAppPath() != null) {
                    File app = new File(TestUtilities.tEnv().getAndroidLocalAppPath());
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
                androidDriverThreadLocal.set(new AndroidDriver<>(new URL(tEnv().getAppiumURL()), androidCaps));
            }
            TestUtilities.android().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            logger.info("*****************************************");
            logger.info("Android Native App Launched Successfully in Device " + tEnv().getAndroidDevice());
            logger.info("*****************************************");
            long endTime = System.currentTimeMillis();
            logger.info("Time Taken to Launch Android Native App: : " + TestUtilities.cUtils().getRunDuration(startTime, endTime));
        } catch (Exception e) {
            captureException(e);
            hardFail("Failed to Launch Android Device : "+tEnv().getAndroidDevice() +" Check your Test Parameters");        }
        if (android() == null) {
            hardFail("Failed to Launch Android Device : "+tEnv().getAndroidDevice() +" Check your Test Parameters");        }
    }

    private HashMap<String,String> get_bs_android_app_url() {
        AppCenterBS appCenter = new AppCenterBS();
        HashMap<String,String> buildData=new HashMap<>();
        if(platformType.equalsIgnoreCase("D2S")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getD2S_Appcenter_Android_ProjectName(),propertiesPojo.getD2S_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("MYT")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getMYT_Appcenter_Android_ProjectName(),propertiesPojo.getMYT_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("FHAPP")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getFHApp_Appcenter_Android_ProjectName(),propertiesPojo.getFHApp_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("CA")){
            buildData = appCenter.getBSAppURL(propertiesPojo.getCA_Appcenter_Android_ProjectName(),propertiesPojo.getCA_Automation_Branch_Name());
        }else if(platformType.equalsIgnoreCase("MOBILE")){
            if(tEnv().getAndroidBSAppPath()!=null){
                buildData.put("app_url",tEnv().getAndroidBSAppPath());
                buildData.put("releaseId",tEnv().getAndroidBuildNumber());
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

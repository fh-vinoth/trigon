package com.trigon.mobile;


import com.trigon.bean.PropertiesPojo;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.io.File;

import static com.trigon.reports.ReportManager.cUtils;
import static com.trigon.reports.ReportManager.tEnv;


public class AppiumManager {

    private static final Logger logger = LogManager.getLogger(AppiumManager.class);
    private AvailablePorts ap = new AvailablePorts();
    private PropertiesPojo propertiesPojo = new PropertiesPojo();
    private AppiumDriverLocalService service;
    private AppiumServiceBuilder builder;
    private DesiredCapabilities cap;

    public AppiumServiceBuilder startAppium() {
        logger.info("Starting Appium Server Programmatically......");
        long startTime1 = System.currentTimeMillis();
        try {
            //Set Capabilities
            cap = new DesiredCapabilities();
            cap.setCapability("noReset", "false");
//            Map<String, String> env = new HashMap<>(System.getenv());
//            env.put("PATH", "/usr/local/bin:" + env.get("PATH"));

            //Build the Appium service
            builder = new AppiumServiceBuilder();
            builder.withIPAddress("0.0.0.0");
            builder.usingPort(ap.getPort());
            builder.withCapabilities(cap);
            builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
            builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
            if(tEnv().getAppType().equalsIgnoreCase("AndroidBrowser")){
                builder.usingAnyFreePort().withArgument(()-> "--allow-insecure", "chromedriver_autodownload");
            }
            if (tEnv().getAppType().equalsIgnoreCase("ios")) {
                // builder.withEnvironment(env);
                builder.usingDriverExecutable(new File("/usr/local/bin/node"));
                builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));
//                builder.withAppiumJS(new File("/Applications/Appium Server GUI.app/Contents/Resources/app/node_modules/appium/build/lib/main.js"));
            }

            //Start the server with the builder
            service = AppiumDriverLocalService.buildService(builder);
            service.start();


//            AppiumServiceBuilder builder = new AppiumServiceBuilder()
//                    .withIPAddress("0.0.0.0")
//                    .usingPort(4723)
//                    .withEnvironment(env)
//                    .usingDriverExecutable(new File("/usr/local/bin/node"))
//                    .withAppiumJS(new File("/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js"));
//
//            service = AppiumDriverLocalService.buildService(builder);
//            service.start();

            long endTime1 = System.currentTimeMillis();
            logger.info("******************************************************");
            logger.info("STARTED APPIUM SUCCESSFULLY in " + cUtils().getRunDuration(startTime1, endTime1));
            logger.info("APPIUM STARTED AT URL : " + service.getUrl().toString());
            tEnv().setAppiumURL(service.getUrl().toString());
            logger.info("******************************************************");

        } catch (Exception e) {
            logger.info("******************************************************");
            logger.error("FAILED TO START APPIUM");
            logger.info("******************************************************");
            Assert.fail("FAILED TO START APPIUM, Ensure that you have given correct APPIUM_JS_PATH in Execution Properties File ");
        }
        return builder;
    }


    /**
     * Destroys Appium nodes.
     */
    public void stopAppium() {

        if (service != null) {
            long startTime2 = System.currentTimeMillis();
            try {
                service.stop();
                long endTime2 = System.currentTimeMillis();
                logger.info("******************************************************");
                logger.info("Appium Stopped Successfully in " + cUtils().getRunDuration(startTime2, endTime2));
                logger.info("******************************************************");
            } catch (Exception e) {
                if (service.isRunning()) {
                    logger.info("AppiumServer didn't shutdown... Trying to quit again....");
                    service.stop();
                    logger.info("******************************************************");
                    logger.info("Appium Stopped Successfully During Retry!!");
                    logger.info("******************************************************");
                }
            }
        }
    }


}
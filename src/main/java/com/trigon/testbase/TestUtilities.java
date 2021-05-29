package com.trigon.testbase;


import com.trigon.api.APIController;
import com.trigon.bean.testenv.TestEnv;
import com.trigon.database.SSHSQLConnection;
import com.trigon.utils.CommonUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.LinkedHashMap;

public class TestUtilities extends APIController {
    protected static ThreadLocal<AndroidDriver<AndroidElement>> androidDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<IOSDriver<IOSElement>> iosDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<RemoteWebDriver> webDriverThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<SSHSQLConnection> dBThreadLocal = new ThreadLocal<>();
    protected static CommonUtils commonUtils = new CommonUtils();
    protected static String getSuiteNameWithTime;
    protected static String platformType;
    protected static String appType;


    public static SSHSQLConnection dbConnection(){
        return dBThreadLocal.get();
    }

    public static RemoteWebDriver browser() {
        return webDriverThreadLocal.get();
    }

    public static AndroidDriver<AndroidElement> android() {
        return androidDriverThreadLocal.get();
    }

    public static IOSDriver<IOSElement> ios() {
        return iosDriverThreadLocal.get();
    }

    public static TestEnv tEnv() {
        return envThreadLocal.get();
    }

    public static CommonUtils cUtils() {
        return commonUtils;
    }


    public void author_ScenarioName(String author, String scenario) {
        try{
            testThreadMethodReporter.get().setTestAuthor(author);
            testThreadMethodReporter.get().setTestScenarioName(scenario);
        }catch (Exception e){
            captureException(e);
        }
    }

    public void author_ScenarioName(String author, LinkedHashMap tData) {
        try{
            testThreadMethodReporter.get().setTestAuthor(author);
            testThreadMethodReporter.get().setTestScenarioName(tData.get("Scenario").toString());
        }catch (Exception e){
            captureException(e);
        }
    }

    public void testStatus(String status, String message) {
        try{
            testThreadMethodReporter.get().setTestStatus(status);
            if (status.equalsIgnoreCase("FAIL")) {
                failAnalysisThread.get().add(message);
            } else {

            }
        }catch (Exception e){
            captureException(e);
        }
    }

    public void testTearDown() {
        if (failAnalysisThread.get().size() > 0) {
            hardFail("Test Failed !! Look for errors : " + failAnalysisThread.get());
        }
    }

}

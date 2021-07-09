package com.fh.core;

import com.trigon.testbase.TestController;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

public class TestLocalController extends TestController {
    public static LocalAPIController api() {
        return new LocalAPIController();
    }



    @BeforeTest(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType","url", "browser", "browser_version", "device", "os_version", "URI", "version", "token", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId","test_region"})
    public void moduleInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String url, @Optional String browser,
                           @Optional String browserVersion, @Optional String device, @Optional String os_version,
                           @Optional String URI, @Optional String version, @Optional String token,
                           @Optional String store, @Optional String host, @Optional String locale,
                           @Optional String region, @Optional String country, @Optional String currency,
                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region) {
        moduleInitilalization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType","url", "browser", "browser_version", "device", "os_version", "URI", "version", "token", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId","test_region"})
    public void classInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                          @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String url, @Optional String browser,
                          @Optional String browserVersion, @Optional String device, @Optional String os_version,
                          @Optional String URI, @Optional String version, @Optional String token,
                          @Optional String store, @Optional String host, @Optional String locale,
                          @Optional String region, @Optional String country, @Optional String currency,
                          @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region) {
        classInitialization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region);
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url","browser", "browser_version", "device", "os_version", "URI", "version", "token", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId","test_region"})
    public void methodInit(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,
                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url,@Optional String browser,
                           @Optional String browserVersion, @Optional String device, @Optional String os_version,
                           @Optional String URI, @Optional String version, @Optional String token,
                           @Optional String store, @Optional String host, @Optional String locale,
                           @Optional String region, @Optional String country, @Optional String currency,
                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region) {
        setUp(context, xmlTest, method, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url,browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region);
    }
}
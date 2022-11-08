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
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url", "browser", "browser_version", "device", "os_version", "URI","envType","appSycURI","appSycAuth", "version","partnerURI", "token", "accessToken", "isJWT", "endpointPrefix","franchiseId","dbType","serviceType", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId", "test_region", "browserstack_execution_local","bs_app_path","productName","grid_Hub_IP","moduleNames"})
    public void moduleInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                           @Optional String browserVersion, @Optional String device, @Optional String os_version,
                           @Optional String URI,@Optional String envType,@Optional String appSycURI,@Optional String appSycAuth, @Optional String version,@Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,@Optional String franchiseId,@Optional String dbType,@Optional String serviceType,
                           @Optional String store, @Optional String host, @Optional String locale,
                           @Optional String region, @Optional String country, @Optional String currency,
                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local,@Optional String bs_app_path,@Optional String productName, @Optional String grid_Hub_IP,@Optional String moduleNames) {
        moduleInitilalization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType,appSycURI,appSycAuth,version,partnerURI, token, accessToken, isJWT, endpointPrefix,franchiseId,dbType,serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local,bs_app_path,productName,grid_Hub_IP,moduleNames);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url", "browser", "browser_version", "device", "os_version", "URI", "envType","appSycURI","appSycAuth","version","partnerURI", "token", "accessToken", "isJWT", "endpointPrefix","franchiseId","dbType","serviceType","store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId", "test_region", "browserstack_execution_local","bs_app_path","productName","grid_Hub_IP","moduleNames"})
    public void classInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                          @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                          @Optional String browserVersion, @Optional String device, @Optional String os_version,
                          @Optional String URI,@Optional String envType,@Optional String appSycURI,@Optional String appSycAuth, @Optional String version,@Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,@Optional String franchiseId,@Optional String dbType,@Optional String serviceType,
                          @Optional String store, @Optional String host, @Optional String locale,
                          @Optional String region, @Optional String country, @Optional String currency,
                          @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local,@Optional String bs_app_path,@Optional String productName, @Optional String grid_Hub_IP,@Optional String moduleNames) {
        classInitialization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI,envType,appSycURI,appSycAuth, version,partnerURI, token, accessToken, isJWT, endpointPrefix,franchiseId,dbType,serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local,bs_app_path,productName,grid_Hub_IP,moduleNames);

    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url", "browser", "browser_version", "device", "os_version", "URI","envType","appSycURI","appSycAuth", "version","partnerURI", "token","accessToken", "isJWT", "endpointPrefix", "franchiseId","dbType","serviceType","store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId", "test_region", "browserstack_execution_local","bs_app_path","productName","grid_Hub_IP","moduleNames"})
    public void methodInit(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,
                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                           @Optional String browserVersion, @Optional String device, @Optional String os_version,
                           @Optional String URI,@Optional String envType,@Optional String appSycURI,@Optional String appSycAuth, @Optional String version,@Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix,@Optional String franchiseId,@Optional String dbType,@Optional String serviceType,
                           @Optional String store, @Optional String host, @Optional String locale,
                           @Optional String region, @Optional String country, @Optional String currency,
                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local,@Optional String bs_app_path,@Optional String productName,@Optional String grid_Hub_IP,@Optional String moduleNames) {
        setUp(context, xmlTest, method, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType,appSycURI,appSycAuth,version,partnerURI, token, accessToken, isJWT, endpointPrefix,franchiseId,dbType,serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local,bs_app_path,productName,grid_Hub_IP,moduleNames);
    }
}
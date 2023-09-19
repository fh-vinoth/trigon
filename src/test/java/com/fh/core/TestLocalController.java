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
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url", "browser", "browser_version", "device", "os_version", "URI", "envType", "appSycURI", "appSycAuth", "version", "partnerURI", "token", "accessToken", "isJWT", "endpointPrefix", "authorization", "franchiseId", "dbType", "serviceType", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId", "test_region", "browserstack_execution_local", "bs_app_path", "productName", "grid_Hub_IP", "gps_location", "moduleNames", "email_recipients", "error_email_recipients", "failure_email_recipients", "browserstack_midSessionInstallApps","unblockToken"})
    public void moduleInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                           @Optional String browserVersion, @Optional String device, @Optional String os_version,
                           @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix, @Optional String authorization, @Optional String franchiseId, @Optional String dbType, @Optional String serviceType,
                           @Optional String store, @Optional String host, @Optional String locale,
                           @Optional String region, @Optional String country, @Optional String currency,
                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps,@Optional String unblockToken) {
        moduleInitilalization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, authorization, franchiseId, dbType, serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, bs_app_path, productName, grid_Hub_IP, gps_location, moduleNames, email_recipients, error_email_recipients, failure_email_recipients, browserstack_midSessionInstallApps,unblockToken);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url", "browser", "browser_version", "device", "os_version", "URI", "envType", "appSycURI", "appSycAuth", "version", "partnerURI", "token", "accessToken", "isJWT", "endpointPrefix", "authorization", "franchiseId", "dbType", "serviceType", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId", "test_region", "browserstack_execution_local", "bs_app_path", "productName", "grid_Hub_IP", "gps_location", "moduleNames", "email_recipients", "error_email_recipients", "failure_email_recipients", "browserstack_midSessionInstallApps","unblockToken"})
    public void classInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,
                          @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                          @Optional String browserVersion, @Optional String device, @Optional String os_version,
                          @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix, @Optional String authorization, @Optional String franchiseId, @Optional String dbType, @Optional String serviceType,
                          @Optional String store, @Optional String host, @Optional String locale,
                          @Optional String region, @Optional String country, @Optional String currency,
                          @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps,@Optional String unblockToken) {
        classInitialization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, authorization, franchiseId, dbType, serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, bs_app_path, productName, grid_Hub_IP, gps_location, moduleNames, email_recipients, error_email_recipients, failure_email_recipients, browserstack_midSessionInstallApps,unblockToken);

    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"testEnvPath", "excelFilePath", "jsonFilePath", "jsonDirectory", "applicationType", "url", "browser", "browser_version", "device", "os_version", "URI", "envType", "appSycURI", "appSycAuth", "version", "partnerURI", "token", "accessToken", "isJWT", "endpointPrefix", "authorization", "franchiseId", "dbType", "serviceType", "store", "host", "locale", "region", "country", "currency", "timezone", "phoneNumber", "emailId", "test_region", "browserstack_execution_local", "bs_app_path", "productName", "grid_Hub_IP", "gps_location", "moduleNames", "email_recipients", "error_email_recipients", "failure_email_recipients", "browserstack_midSessionInstallApps","unblockToken"})
    public void methodInit(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,
                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url, @Optional String browser,
                           @Optional String browserVersion, @Optional String device, @Optional String os_version,
                           @Optional String URI, @Optional String envType, @Optional String appSycURI, @Optional String appSycAuth, @Optional String version, @Optional String partnerURI, @Optional String token, @Optional String accessToken, @Optional String isJWT, @Optional String endpointPrefix, @Optional String authorization, @Optional String franchiseId, @Optional String dbType, @Optional String serviceType,
                           @Optional String store, @Optional String host, @Optional String locale,
                           @Optional String region, @Optional String country, @Optional String currency,
                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId, @Optional String test_region, @Optional String browserstack_execution_local, @Optional String bs_app_path, @Optional String productName, @Optional String grid_Hub_IP, @Optional String gps_location, @Optional String moduleNames, @Optional String email_recipients, @Optional String error_email_recipients, @Optional String failure_email_recipients, @Optional String browserstack_midSessionInstallApps,@Optional String unblockToken) {
        setUp(context, xmlTest, method, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, url, browser, browserVersion, device, os_version, URI, envType, appSycURI, appSycAuth, version, partnerURI, token, accessToken, isJWT, endpointPrefix, authorization, franchiseId, dbType, serviceType, store, host, locale, region, country, currency, timezone, phoneNumber, emailId, test_region, browserstack_execution_local, bs_app_path, productName, grid_Hub_IP, gps_location, moduleNames, email_recipients, error_email_recipients, failure_email_recipients, browserstack_midSessionInstallApps,unblockToken);
    }
}
package com.trigon.bean.testenv;

import org.testng.ITestContext;

import java.io.File;

public class TestEnv {

    // Data belongs to test-env.json
    private String testType;
    private String apiURI;
    private String apiVersion;
    private String appType;

    private String apiLocale;
    private String apiRegion;
    private String apiStore;
    private String apiHost;
    private String apiToken;
    private String apiAccessToken;
    private String apiCountry;
    private String apiCurrency;
    private String apiTimeZone;
    private String apiPhoneNumber;
    private String apiEmailID;


    private String webSystemOS;
    private String webSystemOSVersion;
    private String webBrowser;
    private String webHeadless;
    private String webBrowserVersion;
    private String webUrl;
    private String webBuildNumber;

    private String app_reset;
    private String random_device;
    private String androidAppPackage;
    private String androidAppActivity;
    private String androidDevice;
    private String androidOSVersion;
    private String androidBSAppPath;
    private String androidBuildNumber;

    private String iosBundleId;
    private String iosUDID;
    private String iosDevice;
    private String iosOSVersion;
    private String iosBSAppPath;
    private String iosBuildNumber;

    // Data belongs to remote-env.json
    private String execution_type;
    private String jenkins_execution;
    private String pipeline_execution;
    private String test_region;


    private String dbHost;
    private String dbUserName;
    private String dbPassword;
    private String dbSSHHost;
    private String dbSSHUser;
    private String dbSSHFilePath;
    private String dbName;

    // common variables

    private String excelFilePath;
    private String jsonFilePath;
    private String jsonDirectory;
    private String elementLocator;
    private String appiumURL;

    private File pagesJsonFile;
    private String currentTestMethodName;
    private ITestContext context;
    private String currentTestClassName;
    private File screenshotPath;
    private String browserstack_execution_local;
    private String isJWT;
    private String endpointPrefix;
    private String productName;

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getApiURI() {
        return apiURI;
    }

    public void setApiURI(String apiURI) {
        this.apiURI = apiURI;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getApiLocale() {
        return apiLocale;
    }

    public void setApiLocale(String apiLocale) {
        this.apiLocale = apiLocale;
    }

    public String getApiRegion() {
        return apiRegion;
    }

    public void setApiRegion(String apiRegion) {
        this.apiRegion = apiRegion;
    }

    public String getApiStore() {
        return apiStore;
    }

    public void setApiStore(String apiStore) {
        this.apiStore = apiStore;
    }

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiAccessToken() {
        return apiAccessToken;
    }

    public void setApiAccessToken(String apiAccessToken) {
        this.apiAccessToken = apiAccessToken;
    }

    public String getApiCountry() {
        return apiCountry;
    }

    public void setApiCountry(String apiCountry) {
        this.apiCountry = apiCountry;
    }

    public String getApiCurrency() {
        return apiCurrency;
    }

    public void setApiCurrency(String apiCurrency) {
        this.apiCurrency = apiCurrency;
    }

    public String getApiTimeZone() {
        return apiTimeZone;
    }

    public void setApiTimeZone(String apiTimeZone) {
        this.apiTimeZone = apiTimeZone;
    }

    public String getApiPhoneNumber() {
        return apiPhoneNumber;
    }

    public void setApiPhoneNumber(String apiPhoneNumber) {
        this.apiPhoneNumber = apiPhoneNumber;
    }

    public String getApiEmailID() {
        return apiEmailID;
    }

    public void setApiEmailID(String apiEmailID) {
        this.apiEmailID = apiEmailID;
    }

    public String getWebSystemOS() {
        return webSystemOS;
    }

    public void setWebSystemOS(String webSystemOS) {
        this.webSystemOS = webSystemOS;
    }

    public String getWebSystemOSVersion() {
        return webSystemOSVersion;
    }

    public void setWebSystemOSVersion(String webSystemOSVersion) {
        this.webSystemOSVersion = webSystemOSVersion;
    }

    public String getWebBrowser() {
        return webBrowser;
    }

    public void setWebBrowser(String webBrowser) {
        this.webBrowser = webBrowser;
    }

    public String getWebHeadless() {
        return webHeadless;
    }

    public void setWebHeadless(String webHeadless) {
        this.webHeadless = webHeadless;
    }

    public String getWebBrowserVersion() {
        return webBrowserVersion;
    }

    public void setWebBrowserVersion(String webBrowserVersion) {
        this.webBrowserVersion = webBrowserVersion;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebBuildNumber() {
        return webBuildNumber;
    }

    public void setWebBuildNumber(String webBuildNumber) {
        this.webBuildNumber = webBuildNumber;
    }

    public String getApp_reset() {
        return app_reset;
    }

    public void setApp_reset(String app_reset) {
        this.app_reset = app_reset;
    }

    public String getRandom_device() {
        return random_device;
    }

    public void setRandom_device(String random_device) {
        this.random_device = random_device;
    }

    public String getAndroidAppPackage() {
        return androidAppPackage;
    }

    public void setAndroidAppPackage(String androidAppPackage) {
        this.androidAppPackage = androidAppPackage;
    }

    public String getAndroidAppActivity() {
        return androidAppActivity;
    }

    public void setAndroidAppActivity(String androidAppActivity) {
        this.androidAppActivity = androidAppActivity;
    }

    public String getAndroidDevice() {
        return androidDevice;
    }

    public void setAndroidDevice(String androidDevice) {
        this.androidDevice = androidDevice;
    }

    public String getAndroidOSVersion() {
        return androidOSVersion;
    }

    public void setAndroidOSVersion(String androidOSVersion) {
        this.androidOSVersion = androidOSVersion;
    }

    public String getAndroidBSAppPath() {
        return androidBSAppPath;
    }

    public void setAndroidBSAppPath(String androidBSAppPath) {
        this.androidBSAppPath = androidBSAppPath;
    }

    public String getAndroidBuildNumber() {
        return androidBuildNumber;
    }

    public void setAndroidBuildNumber(String androidBuildNumber) {
        this.androidBuildNumber = androidBuildNumber;
    }

    public String getIosBundleId() {
        return iosBundleId;
    }

    public void setIosBundleId(String iosBundleId) {
        this.iosBundleId = iosBundleId;
    }

    public String getIosUDID() {
        return iosUDID;
    }

    public void setIosUDID(String iosUDID) {
        this.iosUDID = iosUDID;
    }

    public String getIosDevice() {
        return iosDevice;
    }

    public void setIosDevice(String iosDevice) {
        this.iosDevice = iosDevice;
    }

    public String getIosOSVersion() {
        return iosOSVersion;
    }

    public void setIosOSVersion(String iosOSVersion) {
        this.iosOSVersion = iosOSVersion;
    }

    public String getIosBSAppPath() {
        return iosBSAppPath;
    }

    public void setIosBSAppPath(String iosBSAppPath) {
        this.iosBSAppPath = iosBSAppPath;
    }

    public String getIosBuildNumber() {
        return iosBuildNumber;
    }

    public void setIosBuildNumber(String iosBuildNumber) {
        this.iosBuildNumber = iosBuildNumber;
    }

    public String getExecution_type() {
        return execution_type;
    }

    public void setExecution_type(String execution_type) {
        this.execution_type = execution_type;
    }

    public String getJenkins_execution() {
        return jenkins_execution;
    }

    public void setJenkins_execution(String jenkins_execution) {
        this.jenkins_execution = jenkins_execution;
    }

    public String getPipeline_execution() {
        return pipeline_execution;
    }

    public void setPipeline_execution(String pipeline_execution) {
        this.pipeline_execution = pipeline_execution;
    }

    public String getTest_region() {
        return test_region;
    }

    public void setTest_region(String test_region) {
        this.test_region = test_region;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbSSHHost() {
        return dbSSHHost;
    }

    public void setDbSSHHost(String dbSSHHost) {
        this.dbSSHHost = dbSSHHost;
    }

    public String getDbSSHUser() {
        return dbSSHUser;
    }

    public void setDbSSHUser(String dbSSHUser) {
        this.dbSSHUser = dbSSHUser;
    }

    public String getDbSSHFilePath() {
        return dbSSHFilePath;
    }

    public void setDbSSHFilePath(String dbSSHFilePath) {
        this.dbSSHFilePath = dbSSHFilePath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getExcelFilePath() {
        return excelFilePath;
    }

    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    public String getJsonFilePath() {
        return jsonFilePath;
    }

    public void setJsonFilePath(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    public String getJsonDirectory() {
        if(jsonDirectory==null){
            jsonDirectory = "src/test/resources/TestData";
        }
        return jsonDirectory;
    }

    public void setJsonDirectory(String jsonDirectory) {
        this.jsonDirectory = jsonDirectory;
    }

    public String getElementLocator() {
        return elementLocator;
    }

    public void setElementLocator(String elementLocator) {
        this.elementLocator = elementLocator;
    }

    public String getAppiumURL() {
        return appiumURL;
    }

    public void setAppiumURL(String appiumURL) {
        this.appiumURL = appiumURL;
    }

    public File getPagesJsonFile() {
        return pagesJsonFile;
    }

    public void setPagesJsonFile(File pagesJsonFile) {
        this.pagesJsonFile = pagesJsonFile;
    }

    public String getCurrentTestMethodName() {
        return currentTestMethodName;
    }

    public void setCurrentTestMethodName(String currentTestMethodName) {
        this.currentTestMethodName = currentTestMethodName;
    }

    public String getCurrentTestClassName() {
        return currentTestClassName;
    }

    public void setCurrentTestClassName(String currentTestClassName) {
        this.currentTestClassName = currentTestClassName;
    }

    public File getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(File screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public String getBrowserstack_execution_local() {
        return browserstack_execution_local;
    }

    public void setBrowserstack_execution_local(String browserstack_execution_local) {
        this.browserstack_execution_local = browserstack_execution_local;
    }

    public String getIsJWT() {
        return isJWT;
    }

    public void setIsJWT(String isJWT) {
        this.isJWT = isJWT;
    }

    public String getEndpointPrefix() {
        return endpointPrefix;
    }

    public void setEndpointPrefix(String endpointPrefix) {
        this.endpointPrefix = endpointPrefix;
    }

    public ITestContext getContext() {
        return context;
    }

    public void setContext(ITestContext context) {
        this.context = context;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

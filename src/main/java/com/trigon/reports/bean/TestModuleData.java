package com.trigon.reports.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestModuleData implements Serializable {

    @SerializedName("testEnvFile")
    @Expose
    private String testEnvFile;
    @SerializedName("testModuleStartTime")
    @Expose
    private String testModuleStartTime;
    @SerializedName("threadId")
    @Expose
    private Integer threadId;
    @SerializedName("testExecutionType")
    @Expose
    private String testExecutionType;
    @SerializedName("testModuleName")
    @Expose
    private String testModuleName;
    @SerializedName("testApiURL")
    @Expose
    private String testApiURL;
    @SerializedName("testApiHost")
    @Expose
    private String testApiHost;
    @SerializedName("testApiCountry")
    @Expose
    private String testApiCountry;
    @SerializedName("testSystemOS")
    @Expose
    private String testSystemOS;
    @SerializedName("testSystemOSVersion")
    @Expose
    private String testSystemOSVersion;
    @SerializedName("testBrowser")
    @Expose
    private String testBrowser;
    @SerializedName("testApiVersion")
    @Expose
    private String testApiVersion;
    @SerializedName("testBrowserVersion")
    @Expose
    private String testBrowserVersion;
    @SerializedName("testWebUrl")
    @Expose
    private String testWebUrl;
    @SerializedName("testWebBuildNumber")
    @Expose
    private String testWebBuildNumber;
    @SerializedName("testModuleEndTime")
    @Expose
    private String testModuleEndTime;
    @SerializedName("testModuleDuration")
    @Expose
    private String testModuleDuration;

    @SerializedName("testAndroidDevice")
    @Expose
    private String testAndroidDevice;
    @SerializedName("testAndroidVersion")
    @Expose
    private String testAndroidVersion;
    @SerializedName("testAndroidBuildNumber")
    @Expose
    private String testAndroidBuildNumber;
    @SerializedName("testIosDevice")
    @Expose
    private String testIosDevice;
    @SerializedName("testIosVersion")
    @Expose
    private String testIosVersion;
    @SerializedName("testIosBuildNumber")
    @Expose
    private String testIosBuildNumber;


    private List<TestClassData> classDataList = new ArrayList<>();

    public String getTestEnvFile() {
        return testEnvFile;
    }

    public void setTestEnvFile(String testEnvFile) {
        this.testEnvFile = testEnvFile;
    }

    public String getTestModuleStartTime() {
        return testModuleStartTime;
    }

    public void setTestModuleStartTime(String testModuleStartTime) {
        this.testModuleStartTime = testModuleStartTime;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public String getTestExecutionType() {
        return testExecutionType;
    }

    public void setTestExecutionType(String testExecutionType) {
        this.testExecutionType = testExecutionType;
    }

    public String getTestModuleName() {
        return testModuleName;
    }

    public void setTestModuleName(String testModuleName) {
        this.testModuleName = testModuleName;
    }

    public String getTestApiVersion() {
        return testApiVersion;
    }

    public void setTestApiVersion(String testApiVersion) {
        this.testApiVersion = testApiVersion;
    }

    public String getTestApiURL() {
        return testApiURL;
    }

    public void setTestApiURL(String testApiURL) {
        this.testApiURL = testApiURL;
    }

    public String getTestApiHost() {
        return testApiHost;
    }

    public void setTestApiHost(String testApiHost) {
        this.testApiHost = testApiHost;
    }

    public String getTestApiCountry() {
        return testApiCountry;
    }

    public void setTestApiCountry(String testApiCountry) {
        this.testApiCountry = testApiCountry;
    }

    public String getTestSystemOS() {
        return testSystemOS;
    }

    public void setTestSystemOS(String testSystemOS) {
        this.testSystemOS = testSystemOS;
    }

    public String getTestSystemOSVersion() {
        return testSystemOSVersion;
    }

    public void setTestSystemOSVersion(String testSystemOSVersion) {
        this.testSystemOSVersion = testSystemOSVersion;
    }

    public String getTestBrowser() {
        return testBrowser;
    }

    public void setTestBrowser(String testBrowser) {
        this.testBrowser = testBrowser;
    }

    public String getTestBrowserVersion() {
        return testBrowserVersion;
    }

    public void setTestBrowserVersion(String testBrowserVersion) {
        this.testBrowserVersion = testBrowserVersion;
    }

    public String getTestWebUrl() {
        return testWebUrl;
    }

    public void setTestWebUrl(String testWebUrl) {
        this.testWebUrl = testWebUrl;
    }

    public String getTestWebBuildNumber() {
        return testWebBuildNumber;
    }

    public void setTestWebBuildNumber(String testWebBuildNumber) {
        this.testWebBuildNumber = testWebBuildNumber;
    }

    public String getTestModuleEndTime() {
        return testModuleEndTime;
    }

    public void setTestModuleEndTime(String testModuleEndTime) {
        this.testModuleEndTime = testModuleEndTime;
    }

    public String getTestModuleDuration() {
        return testModuleDuration;
    }

    public void setTestModuleDuration(String testModuleDuration) {
        this.testModuleDuration = testModuleDuration;
    }

    public List<TestClassData> getClassDataList() {
        return classDataList;
    }

    public void setClassDataList(List<TestClassData> classDataList) {
        this.classDataList = classDataList;
    }

    public String getTestAndroidDevice() {
        return testAndroidDevice;
    }

    public void setTestAndroidDevice(String testAndroidDevice) {
        this.testAndroidDevice = testAndroidDevice;
    }

    public String getTestAndroidVersion() {
        return testAndroidVersion;
    }

    public void setTestAndroidVersion(String testAndroidVersion) {
        this.testAndroidVersion = testAndroidVersion;
    }

    public String getTestAndroidBuildNumber() {
        return testAndroidBuildNumber;
    }

    public void setTestAndroidBuildNumber(String testAndroidBuildNumber) {
        this.testAndroidBuildNumber = testAndroidBuildNumber;
    }

    public String getTestIosDevice() {
        return testIosDevice;
    }

    public void setTestIosDevice(String testIosDevice) {
        this.testIosDevice = testIosDevice;
    }

    public String getTestIosVersion() {
        return testIosVersion;
    }

    public void setTestIosVersion(String testIosVersion) {
        this.testIosVersion = testIosVersion;
    }

    public String getTestIosBuildNumber() {
        return testIosBuildNumber;
    }

    public void setTestIosBuildNumber(String testIosBuildNumber) {
        this.testIosBuildNumber = testIosBuildNumber;
    }
}

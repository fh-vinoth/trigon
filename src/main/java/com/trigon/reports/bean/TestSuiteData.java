package com.trigon.reports.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestSuiteData implements Serializable {

    @SerializedName("testSuiteName")
    @Expose
    private String testSuiteName;
    @SerializedName("testSuiteNameWithTime")
    @Expose
    private String testSuiteNameWithTime;
    @SerializedName("frameworkVersion")
    @Expose
    private String frameworkVersion;
    @SerializedName("testStartTime")
    @Expose
    private String testStartTime;
    @SerializedName("executedBy")
    @Expose
    private String executedBy;
    @SerializedName("executedSystemOS")
    @Expose
    private String executedSystemOS;
    @SerializedName("testEndTime")
    @Expose
    private String testEndTime;
    @SerializedName("testType")
    @Expose
    private String testType;
    @SerializedName("testAppType")
    @Expose
    private String testAppType;

    @SerializedName("testRailProjectID")
    @Expose
    private String testRailProjectID;

    private int totalTests;
    private int passed;
    private int failed;
    private int skipped;
    private int totalEndPoints;
    @SerializedName("timeTaken")
    @Expose
    private String timeTaken;
    @SerializedName("moduleData")
    @Expose
    private List<TestModuleData> moduleData = new ArrayList<>();
    private float passPercentage;
    private float failPercentage;
    private float skipPercentage;
    private boolean checkFailStatus = false;
    private String apiVersion;

    private List<TestFailedSummary> failSummary;

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public String getTestSuiteNameWithTime() {
        return testSuiteNameWithTime;
    }

    public void setTestSuiteNameWithTime(String testSuiteNameWithTime) {
        this.testSuiteNameWithTime = testSuiteNameWithTime;
    }

    public String getFrameworkVersion() {
        return frameworkVersion;
    }

    public void setFrameworkVersion(String frameworkVersion) {
        this.frameworkVersion = frameworkVersion;
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(String testStartTime) {
        this.testStartTime = testStartTime;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public String getExecutedSystemOS() {
        return executedSystemOS;
    }

    public void setExecutedSystemOS(String executedSystemOS) {
        this.executedSystemOS = executedSystemOS;
    }

    public String getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(String testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public List<TestModuleData> getModuleData() {
        return moduleData;
    }

    public void setModuleData(List<TestModuleData> moduleData) {
        this.moduleData = moduleData;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public float getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(float passPercentage) {
        this.passPercentage = passPercentage;
    }

    public float getFailPercentage() {
        return failPercentage;
    }

    public void setFailPercentage(float failPercentage) {
        this.failPercentage = failPercentage;
    }

    public float getSkipPercentage() {
        return skipPercentage;
    }

    public void setSkipPercentage(float skipPercentage) {
        this.skipPercentage = skipPercentage;
    }

    public int getTotalEndPoints() {
        return totalEndPoints;
    }

    public void setTotalEndPoints(int totalEndPoints) {
        this.totalEndPoints = totalEndPoints;
    }

    public List<TestFailedSummary> getFailSummary() {
        return failSummary;
    }

    public void setFailSummary(List<TestFailedSummary> failSummary) {
        this.failSummary = failSummary;
    }

    public boolean isCheckFailStatus() {
        return checkFailStatus;
    }

    public void setCheckFailStatus(boolean checkFailStatus) {
        this.checkFailStatus = checkFailStatus;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getTestAppType() {
        return testAppType;
    }

    public void setTestAppType(String testAppType) {
        this.testAppType = testAppType;
    }

    public String getTestRailProjectID() {
        return testRailProjectID;
    }

    public void setTestRailProjectID(String testRailProjectID) {
        this.testRailProjectID = testRailProjectID;
    }
}

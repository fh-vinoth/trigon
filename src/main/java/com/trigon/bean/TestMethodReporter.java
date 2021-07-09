package com.trigon.bean;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestMethodReporter implements Serializable {


    private String testScenarioName = "Positive Scenario";
    private String testVerificationPoint = "Not Provided";
    private String testAuthor = "Automation";
    private String testExecutionTime = "None";
    private String testStatus = "SKIPPED";
    private List<String> testAnalysis = new ArrayList<>();
    private long testMethodStartTime;
    private String testMethodName;
    private List<JsonObject> testSteps = new ArrayList<>();
    private String testRetry = "NA";
    public String getTestScenarioName() {
        return testScenarioName;
    }

    public void setTestScenarioName(String testScenarioName) {
        this.testScenarioName = testScenarioName;
    }

    public String getTestAuthor() {
        return testAuthor;
    }

    public void setTestAuthor(String testAuthor) {
        this.testAuthor = testAuthor;
    }

    public String getTestExecutionTime() {
        return testExecutionTime;
    }

    public void setTestExecutionTime(String testExecutionTime) {
        this.testExecutionTime = testExecutionTime;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public List<String> getTestAnalysis() {
        return testAnalysis;
    }

    public void setTestAnalysis(List<String> testAnalysis) {
        this.testAnalysis = testAnalysis;
    }

    public long getTestMethodStartTime() {
        return testMethodStartTime;
    }

    public void setTestMethodStartTime(long testMethodStartTime) {
        this.testMethodStartTime = testMethodStartTime;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public List<JsonObject> getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(List<JsonObject> testSteps) {
        this.testSteps = testSteps;
    }

    public String getTestVerificationPoint() {
        return testVerificationPoint;
    }

    public void setTestVerificationPoint(String testVerificationPoint) {
        this.testVerificationPoint = testVerificationPoint;
    }

    public String getTestRetry() {
        return testRetry;
    }

    public void setTestRetry(String testRetry) {
        this.testRetry = testRetry;
    }
}

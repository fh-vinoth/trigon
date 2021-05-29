package com.trigon.reports.bean;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestMethodDatum implements Serializable {

    @SerializedName("threadId")
    @Expose
    private Integer threadId;
    @SerializedName("testMethodName")
    @Expose
    private String testMethodName;
    @SerializedName("testMethodStartTime")
    @Expose
    private String testMethodStartTime;
    @SerializedName("testMethodAuthorName")
    @Expose
    private String testMethodAuthorName;
    @SerializedName("testMethodScenario")
    @Expose
    private String testMethodScenario;
    @SerializedName("testMethodStatus")
    @Expose
    private String testMethodStatus;
    @SerializedName("testMethodAnalysis")
    @Expose
    private List<String> testMethodAnalysis;
    @SerializedName("testMethodEndTime")
    @Expose
    private String testMethodEndTime;
    @SerializedName("testMethodDuration")
    @Expose
    private String testMethodDuration;

    @SerializedName("testSteps")
    @Expose
    private List<JsonObject> testStepsData = new ArrayList<>();
    @SerializedName("apiTableData")
    @Expose
    private List<TestAPIData> apiTableData = new ArrayList<>();

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public String getTestMethodStartTime() {
        return testMethodStartTime;
    }

    public void setTestMethodStartTime(String testMethodStartTime) {
        this.testMethodStartTime = testMethodStartTime;
    }

    public String getTestMethodAuthorName() {
        return testMethodAuthorName;
    }

    public void setTestMethodAuthorName(String testMethodAuthorName) {
        this.testMethodAuthorName = testMethodAuthorName;
    }

    public String getTestMethodScenario() {
        return testMethodScenario;
    }

    public void setTestMethodScenario(String testMethodScenario) {
        this.testMethodScenario = testMethodScenario;
    }

    public String getTestMethodStatus() {
        return testMethodStatus;
    }

    public void setTestMethodStatus(String testMethodStatus) {
        this.testMethodStatus = testMethodStatus;
    }

    public List<String> getTestMethodAnalysis() {
        return testMethodAnalysis;
    }

    public void setTestMethodAnalysis(List<String> testMethodAnalysis) {
        this.testMethodAnalysis = testMethodAnalysis;
    }

    public String getTestMethodEndTime() {
        return testMethodEndTime;
    }

    public void setTestMethodEndTime(String testMethodEndTime) {
        this.testMethodEndTime = testMethodEndTime;
    }

    public String getTestMethodDuration() {
        return testMethodDuration;
    }

    public void setTestMethodDuration(String testMethodDuration) {
        this.testMethodDuration = testMethodDuration;
    }

    public List<JsonObject> getTestStepsData() {
        return testStepsData;
    }

    public void setTestStepsData(List<JsonObject> testStepsData) {
        this.testStepsData = testStepsData;
    }

    public List<TestAPIData> getApiTableData() {
        return apiTableData;
    }

    public void setApiTableData(List<TestAPIData> apiTableData) {
        this.apiTableData = apiTableData;
    }
}
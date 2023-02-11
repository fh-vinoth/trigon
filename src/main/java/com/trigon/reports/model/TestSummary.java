package com.trigon.reports.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestSummary {

    @SerializedName("suite-name")
    @Expose
    private String suiteName;
    @SerializedName("run-id")
    @Expose
    private String runId;
    @SerializedName("start-time")
    @Expose
    private String startTime;
    @SerializedName("end-time")
    @Expose
    private String endTime;
    @SerializedName("total-time")
    @Expose
    private String totalTime;
    @SerializedName("build")
    @Expose
    private String build;
    @SerializedName("executed-by")
    @Expose
    private String executedBy;
    @SerializedName("execution-type")
    @Expose
    private String executionType;
    @SerializedName("test-type")
    @Expose
    private String testType;
    @SerializedName("platform-type")
    @Expose
    private String platformType;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("takeaway")
    @Expose
    private String takeaway;
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("api-uri")
    @Expose
    private String apiUri;
    @SerializedName("appsync-uri")
    @Expose
    private String appsyncUri;
    @SerializedName("tpi-uri")
    @Expose
    private String tpiUri;
    @SerializedName("suite-status")
    @Expose
    private String suiteStatus;
    @SerializedName("results")
    @Expose
    private List<JsonObject> results = null;

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTakeaway() {
        return takeaway;
    }

    public void setTakeaway(String takeaway) {
        this.takeaway = takeaway;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public String getAppsyncUri() {
        return appsyncUri;
    }

    public void setAppsyncUri(String appsyncUri) {
        this.appsyncUri = appsyncUri;
    }

    public String getTpiUri() {
        return tpiUri;
    }

    public void setTpiUri(String tpiUri) {
        this.tpiUri = tpiUri;
    }

    public String getSuiteStatus() {
        return suiteStatus;
    }

    public void setSuiteStatus(String suiteStatus) {
        this.suiteStatus = suiteStatus;
    }

    public List<JsonObject> getResults() {
        return results;
    }

    public void setResults(List<JsonObject> results) {
        this.results = results;
    }
}
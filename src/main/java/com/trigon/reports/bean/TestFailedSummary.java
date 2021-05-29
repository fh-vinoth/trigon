package com.trigon.reports.bean;

import java.util.List;

public class TestFailedSummary {
    private String testStatus;
    private String testMethod;
    private List testAnalysis;

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public List getTestAnalysis() {
        return testAnalysis;
    }

    public void setTestAnalysis(List testAnalysis) {
        this.testAnalysis = testAnalysis;
    }
}

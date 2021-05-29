package com.trigon.api.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class APIInputs {

    @SerializedName("authorHistory")
    @Expose
    private String authorHistory;
    @SerializedName("testMethodData")
    @Expose
    private List<TestMethods> testMethodData = null;

    public String getAuthorHistory() {
        return authorHistory;
    }

    public void setAuthorHistory(String authorHistory) {
        this.authorHistory = authorHistory;
    }

    public List<TestMethods> getTestMethodData() {
        return testMethodData;
    }

    public void setTestMethodData(List<TestMethods> testMethodData) {
        this.testMethodData = testMethodData;
    }

}
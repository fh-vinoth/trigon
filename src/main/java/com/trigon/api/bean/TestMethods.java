package com.trigon.api.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TestMethods {

    @SerializedName("testMethodName")
    @Expose
    private String testMethodName;
    @SerializedName("apiInputData")
    @Expose
    private List<APIInputData> apiInputData = null;

    public String getTestMethodName() {
        return testMethodName;
    }

    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public List<APIInputData> getApiInputData() {
        return apiInputData;
    }

    public void setApiInputData(List<APIInputData> apiInputData) {
        this.apiInputData = apiInputData;
    }
}
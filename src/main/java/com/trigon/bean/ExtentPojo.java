package com.trigon.bean;

import com.aventstack.extentreports.ExtentTest;

public class ExtentPojo {
    ExtentTest extentTest;
    private boolean parallelDataProvider = false;

    public ExtentTest getExtentTest() {
        return extentTest;
    }

    public void setExtentTest(ExtentTest extentTest) {
        this.extentTest = extentTest;
    }

    public boolean isParallelDataProvider() {
        return parallelDataProvider;
    }

    public void setParallelDataProvider(boolean parallelDataProvider) {
        this.parallelDataProvider = parallelDataProvider;
    }
}
package com.trigon.bean;

import java.io.Serializable;

public class TestModuleReporter implements Serializable {

    private long testModuleStartTime;

    public long getTestModuleStartTime() {
        return testModuleStartTime;
    }

    public void setTestModuleStartTime(long testModuleStartTime) {
        this.testModuleStartTime = testModuleStartTime;
    }
}

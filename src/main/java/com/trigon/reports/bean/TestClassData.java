package com.trigon.reports.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestClassData implements Serializable {

    private final static long serialVersionUID = -3127701439475399840L;
    @SerializedName("testEnvPath")
    @Expose
    private String testEnvPath;
    @SerializedName("testModuleName")
    @Expose
    private String testModuleName;
    @SerializedName("testClassName")
    @Expose
    private String testClassName;
    @SerializedName("testMethodData")
    @Expose
    private List<TestMethodDatum> testMethodData = new ArrayList<>();

    public String getTestEnvPath() {
        return testEnvPath;
    }

    public void setTestEnvPath(String testEnvPath) {
        this.testEnvPath = testEnvPath;
    }

    public String getTestModuleName() {
        return testModuleName;
    }

    public void setTestModuleName(String testModuleName) {
        this.testModuleName = testModuleName;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public List<TestMethodDatum> getTestMethodData() {
        return testMethodData;
    }

    public void setTestMethodData(List<TestMethodDatum> testMethodData) {
        this.testMethodData = testMethodData;
    }

}

package com.trigon.dataprovider.json.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestModules {

    @SerializedName("modules")
    @Expose
    private List<TestListOfModules> modules;

    public List<TestListOfModules> getModules() {
        return modules;
    }

    public void setModules(List<TestListOfModules> modules) {
        this.modules = modules;
    }
}

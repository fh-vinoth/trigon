package com.trigon.dataprovider.json.bean;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestListOfModules {

    @SerializedName("modulename")
    @Expose
    private String modulename;
    @SerializedName("authorname")
    @Expose
    private String authorname;
    @SerializedName("testdata")
    @Expose
    private JsonObject testdata;


    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public JsonObject getTestdata() {
        return testdata;
    }

    public void setTestdata(JsonObject testdata) {
        this.testdata = testdata;
    }

}

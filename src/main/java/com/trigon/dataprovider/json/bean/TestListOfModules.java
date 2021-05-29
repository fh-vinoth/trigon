package com.trigon.dataprovider.json.bean;

import com.google.gson.JsonObject;

public class TestListOfModules {

    private String modulename;
    private String authorname;
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

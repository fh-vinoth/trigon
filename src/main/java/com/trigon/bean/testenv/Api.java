package com.trigon.bean.testenv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Api {

    @SerializedName("URI")
    @Expose
    private String uRI;
    @SerializedName("version")
    @Expose
    private String version;

    public String getURI() {
        return uRI;
    }

    public void setURI(String uRI) {
        this.uRI = uRI;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
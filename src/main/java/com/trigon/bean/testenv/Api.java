package com.trigon.bean.testenv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Api {

    @SerializedName("URI")
    @Expose
    private String uRI;
    @SerializedName("envType")
    @Expose
    private String envType;
    @SerializedName("appSycURI")
    @Expose
    private String appSycURI;
    @SerializedName("appSycAuth")
    @Expose
    private String appSycAuth;
    @SerializedName("version")
    @Expose
    private String version;

    public String getURI() {
        return uRI;
    }

    public void setURI(String uRI) {
        this.uRI = uRI;
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public String getAppSycURI() {
        return appSycURI;
    }

    public void setAppSycURI(String appSycURI) {
        this.appSycURI = appSycURI;
    }

    public String getAppSycAuth() {
        return appSycAuth;
    }

    public void setAppSycAuth(String appSycAuth) {
        this.appSycAuth = appSycAuth;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
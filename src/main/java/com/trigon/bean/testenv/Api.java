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
    @SerializedName("partnerURI")
    @Expose
    private String partnerURI;
    @SerializedName("moduleNames")
    @Expose
    private String moduleNames;
    @SerializedName("appType")
    @Expose
    private String appType;
    @SerializedName("productName")
    @Expose
    private String productName;

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

    public String getApiPartnerURI() {
        return partnerURI;
    }

    public void setApiPartnerURI(String apiPartnerURI) {
        this.partnerURI = apiPartnerURI;
    }

    public String getModuleNames() {
        return moduleNames;
    }

    public String getproductName() {
        return productName;
    }

    public void setModuleNames(String moduleNames) {
        this.moduleNames = moduleNames;
    }

}

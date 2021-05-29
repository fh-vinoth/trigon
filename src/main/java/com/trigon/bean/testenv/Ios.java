package com.trigon.bean.testenv;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ios {

    @SerializedName("bundleid")
    @Expose
    private String bundleid;
    @SerializedName("udid")
    @Expose
    private String udid;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("region_ios_devices")
    @Expose
    private JsonObject region_ios_devices;

    public String getBundleid() {
        return bundleid;
    }

    public void setBundleid(String bundleid) {
        this.bundleid = bundleid;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public JsonObject getRegion_ios_devices() {
        return region_ios_devices;
    }

    public void setRegion_ios_devices(JsonObject region_ios_devices) {
        this.region_ios_devices = region_ios_devices;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
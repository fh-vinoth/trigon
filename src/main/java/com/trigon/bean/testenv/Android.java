package com.trigon.bean.testenv;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Android {

    @SerializedName("app_package")
    @Expose
    private String appPackage;
    @SerializedName("app_activity")
    @Expose
    private String appActivity;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("region_android_devices")
    @Expose
    private JsonObject region_android_devices;

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public void setAppActivity(String appActivity) {
        this.appActivity = appActivity;
    }

    public JsonObject getRegion_android_devices() {
        return region_android_devices;
    }

    public void setRegion_android_devices(JsonObject region_android_devices) {
        this.region_android_devices = region_android_devices;
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

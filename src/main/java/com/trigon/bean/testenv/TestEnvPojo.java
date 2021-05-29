package com.trigon.bean.testenv;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestEnvPojo {

    @SerializedName("api")
    @Expose
    private Api api;
    @SerializedName("android")
    @Expose
    private Android android;
    @SerializedName("ios")
    @Expose
    private Ios ios;
    @SerializedName("web")
    @Expose
    private Web web;
    @SerializedName("region")
    @Expose
    private JsonObject region;




    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }

    public Ios getIos() {
        return ios;
    }

    public void setIos(Ios ios) {
        this.ios = ios;
    }

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public JsonObject getRegion() {
        return region;
    }

    public void setRegion(JsonObject region) {
        this.region = region;
    }

}

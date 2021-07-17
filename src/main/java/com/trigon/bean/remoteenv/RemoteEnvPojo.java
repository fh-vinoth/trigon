package com.trigon.bean.remoteenv;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RemoteEnvPojo implements Serializable {
    @SerializedName("execution_type")
    @Expose
    private String execution_type;
    @SerializedName("browserstack_execution_local")
    @Expose
    private String browserstack_execution_local;
    @SerializedName("jenkins_execution")
    @Expose
    private String jenkins_execution;
    @SerializedName("pipeline_execution")
    @Expose
    private String pipeline_execution;
    @SerializedName("application_type")
    @Expose
    private String application_type;
    @SerializedName("test_region")
    @Expose
    private String test_region;
    @SerializedName("app_reset")
    @Expose
    private String app_reset;
    @SerializedName("random_device")
    @Expose
    private String random_device;
    @SerializedName("email_recipients")
    @Expose
    private String email_recipients;
    @SerializedName("error_email_recipients")
    @Expose
    private String error_email_recipients;
    @SerializedName("failure_email_recipients")
    @Expose
    private String failure_email_recipients;
    @SerializedName("products")
    @Expose
    private JsonObject products;
    @SerializedName("db_config")
    @Expose
    private JsonObject db_config;

    public String getExecution_type() {
        return execution_type;
    }

    public void setExecution_type(String execution_type) {
        this.execution_type = execution_type;
    }

    public String getBrowserstack_execution_local() {
        return browserstack_execution_local;
    }

    public void setBrowserstack_execution_local(String browserstack_execution_local) {
        this.browserstack_execution_local = browserstack_execution_local;
    }

    public String getJenkins_execution() {
        return jenkins_execution;
    }

    public void setJenkins_execution(String jenkins_execution) {
        this.jenkins_execution = jenkins_execution;
    }

    public String getPipeline_execution() {
        return pipeline_execution;
    }

    public void setPipeline_execution(String pipeline_execution) {
        this.pipeline_execution = pipeline_execution;
    }

    public String getApp_reset() {
        return app_reset;
    }

    public void setApp_reset(String app_reset) {
        this.app_reset = app_reset;
    }

    public String getApplication_type() {
        return application_type;
    }

    public void setApplication_type(String application_type) {
        this.application_type = application_type;
    }

    public String getTest_region() {
        return test_region;
    }

    public void setTest_region(String test_region) {
        this.test_region = test_region;
    }

    public JsonObject getProducts() {
        return products;
    }

    public void setProducts(JsonObject products) {
        this.products = products;
    }

    public JsonObject getDb_config() {
        return db_config;
    }

    public void setDb_config(JsonObject db_config) {
        this.db_config = db_config;
    }

    public String getRandom_device() {
        return random_device;
    }

    public void setRandom_device(String random_device) {
        this.random_device = random_device;
    }

    public String getEmail_recipients() {
        return email_recipients;
    }

    public void setEmail_recipients(String email_recipients) {
        this.email_recipients = email_recipients;
    }

    public String getError_email_recipients() {
        return error_email_recipients;
    }

    public void setError_email_recipients(String error_email_recipients) {
        this.error_email_recipients = error_email_recipients;
    }

    public String getFailure_email_recipients() {
        return failure_email_recipients;
    }

    public void setFailure_email_recipients(String failure_email_recipients) {
        this.failure_email_recipients = failure_email_recipients;
    }
}

package com.trigon.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

public class Result {

    @SerializedName("module-name")
    @Expose
    private String moduleName;
    @SerializedName("class-name")
    @Expose
    private String className;
    @SerializedName("method-name")
    @Expose
    private String methodName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("author")
    @Expose
    private Boolean author;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("failure-reason")
    @Expose
    private String failureReason;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAuthor() {
        return author;
    }

    public void setAuthor(Boolean author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

}

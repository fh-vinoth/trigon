package com.trigon.reports.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class TestAPIData implements Serializable {

    @SerializedName("URI")
    @Expose
    private String uRI;
    @SerializedName("headers")
    @Expose
    private LinkedHashMap headers;
    @SerializedName("queryParams")
    @Expose
    private LinkedHashMap queryParams;
    @SerializedName("formParams")
    @Expose
    private LinkedHashMap formParams;
    @SerializedName("httpMethod")
    @Expose
    private String httpMethod;
    @SerializedName("endPoint")
    @Expose
    private String endPoint;
    @SerializedName("responseTime")
    @Expose
    private String responseTime;
    @SerializedName("responseJSON")
    @Expose
    private String responseJSON;
    @SerializedName("statusCode")
    @Expose
    private LinkedHashMap statusCode;
    @SerializedName("expectedResponse")
    @Expose
    private LinkedHashMap expectedResponse;

    @SerializedName("actualResponse")
    @Expose
    private LinkedHashMap actualResponse;

    @SerializedName("apiTestStatus")
    @Expose
    private String subTestStatus;

    public String getuRI() {
        return uRI;
    }

    public void setuRI(String uRI) {
        this.uRI = uRI;
    }

    public LinkedHashMap getHeaders() {
        return headers;
    }

    public void setHeaders(LinkedHashMap headers) {
        this.headers = headers;
    }

    public LinkedHashMap getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(LinkedHashMap queryParams) {
        this.queryParams = queryParams;
    }

    public LinkedHashMap getFormParams() {
        return formParams;
    }

    public void setFormParams(LinkedHashMap formParams) {
        this.formParams = formParams;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseJSON() {
        return responseJSON;
    }

    public void setResponseJSON(String responseJSON) {
        this.responseJSON = responseJSON;
    }

    public LinkedHashMap getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(LinkedHashMap statusCode) {
        this.statusCode = statusCode;
    }

    public String getSubTestStatus() {
        return subTestStatus;
    }

    public void setSubTestStatus(String subTestStatus) {
        this.subTestStatus = subTestStatus;
    }

    public LinkedHashMap getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(LinkedHashMap expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public LinkedHashMap getActualResponse() {
        return actualResponse;
    }

    public void setActualResponse(LinkedHashMap actualResponse) {
        this.actualResponse = actualResponse;
    }
}

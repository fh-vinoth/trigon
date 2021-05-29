package com.trigon.api.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class APIInputData {

    @SerializedName("httpMethod")
    @Expose
    private String httpMethod;
    @SerializedName("endPoint")
    @Expose
    private String endPoint;
    @SerializedName("headers")
    @Expose
    private HashMap headers;
    @SerializedName("cookies")
    @Expose
    private HashMap cookies;
    @SerializedName("queryParams")
    @Expose
    private HashMap queryParams;
    @SerializedName("formParams")
    @Expose
    private HashMap formParams;
    @SerializedName("pathParams")
    @Expose
    private HashMap pathParams;
    @SerializedName("requestBody")
    @Expose
    private String requestBody;
    @SerializedName("expectedStatusCode")
    @Expose
    private String expectedStatusCode;
    @SerializedName("expectedResponse")
    @Expose
    private HashMap expectedResponse;
    @SerializedName("actualResponseVariables")
    @Expose
    private HashMap actualResponseVariables;

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

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public void setExpectedStatusCode(String expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public HashMap getHeaders() {
        return headers;
    }

    public HashMap getCookies() {
        return cookies;
    }

    public HashMap getQueryParams() {
        return queryParams;
    }

    public HashMap getFormParams() {
        return formParams;
    }

    public HashMap getPathParams() {
        return pathParams;
    }

    public HashMap getExpectedResponse() {
        return expectedResponse;
    }

    public HashMap getActualResponseVariables() {
        return actualResponseVariables;
    }
}
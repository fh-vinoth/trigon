package com.fh.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APITest {

    public static void main(String[] args) {

        Response response;

        RestAssured.baseURI = "https://sit-api.t2scdn.com/";
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);

        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> formParams = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        ArrayList<String> qList = new ArrayList<>();
        qList.add("3ed9c5899bdebce5ff4596b88fc471a7");
        qList.add("9d125e11a80004ff1f3480f4fab3f29a");

        queryParams.put("api_token", qList);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String endpoint = "/television/store";

        formParams.put("tv_id", "9388");
        ArrayList<String> fList = new ArrayList<>();
        fList.add("8050565");
        fList.add("8050666");
        fList.add("8050624");

        formParams.put("config_id[]", fList);

        requestSpecification.headers(headers);
        requestSpecification.queryParams(queryParams);
        requestSpecification.formParams(formParams);

        response = requestSpecification.post(endpoint).then().extract().response();

        System.out.println(response.getBody().asString());
    }

}

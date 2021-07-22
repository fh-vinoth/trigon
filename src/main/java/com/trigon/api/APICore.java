package com.trigon.api;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigon.reports.ReportManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.collections.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class APICore extends ReportManager {
    private static final Logger logger = LogManager.getLogger(APICore.class);

    private Response executeHttpMethod(String HttpMethod, String Endpoint, RequestSpecification requestSpecification) {
        return executeAPIMethod(HttpMethod, Endpoint, requestSpecification);
    }

    private Response executeHttpMethod(LinkedHashMap<String, Object> tData, RequestSpecification requestSpecification) {
        return executeAPIMethod(mapKeyFinder(tData, "HttpMethod"), mapKeyFinder(tData, "Endpoint"), requestSpecification);
    }

    protected Response sendInternalRequest(LinkedHashMap<String, Object> tData) {
        Response response = null;
        try {
            if (!((tEnv().getApiURI() == null) || (tEnv().getApiURI().isEmpty()))) {
                RestAssured.baseURI = tEnv().getApiURI();
                logger.info("Picked URI from TestEnvironment: " + tEnv().getApiURI());
            }
            logger.info("Executing API: " + Thread.currentThread().getStackTrace()[3].getMethodName() + "  and  " + Thread.currentThread().getStackTrace()[4].getMethodName());
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            Map<String, Object> headers = new HashMap<>();
            Map<String, Object> cookies = new HashMap<>();
            Map<String, Object> queryParams = new HashMap<>();
            Map<String, Object> formParams = new HashMap<>();
            Map<String, Object> pathParams = new HashMap<>();
            tData.entrySet().stream().filter(entry -> entry.getKey().contains("HDR#"))
                    .forEach(entry -> {
                        String key = entry.getKey().replace("HDR#", "");
                        logger.info(key + "  " + entry.getValue());
                        headers.put(key, entry.getValue());
                    });

            if (!((tEnv().getApiStore() == null) || (tEnv().getApiStore().isEmpty()))) {
                headers.put("store", tEnv().getApiStore());
                logger.info("Picked Store from TestEnvironment: " + tEnv().getApiStore());
            }
            tData.entrySet().stream().filter(entry -> entry.getKey().contains("CK#"))
                    .forEach(entry -> {
                        String key = entry.getKey().replace("CK#", "");
                        logger.info(key + "  " + entry.getValue());
                        cookies.put(key, entry.getValue());
                    });
            tData.entrySet().stream().filter(entry -> entry.getKey().contains("QP#"))
                    .forEach(entry -> {
                        String key = entry.getKey().replace("QP#", "");
                        logger.info(key + "  " + entry.getValue());
                        queryParams.put(key, entry.getValue());
                    });

            if (!((tEnv().getApiToken() == null) || (tEnv().getApiToken().isEmpty()))) {
                queryParams.put("api_token", tEnv().getApiToken());
                logger.info("Picked APIToken from TestEnvironment: " + tEnv().getApiToken());
            }
            tData.entrySet().stream().filter(entry -> entry.getKey().contains("FP#"))
                    .forEach(entry -> {
                        String key = entry.getKey().replace("FP#", "");
                        logger.info(key + "  " + entry.getValue());
                        formParams.put(key, entry.getValue());
                    });
            tData.entrySet().stream().filter(entry -> entry.getKey().contains("PP#"))
                    .forEach(entry -> {
                        String key = entry.getKey().replace("PP#", "");
                        logger.info(key + "  " + entry.getValue());
                        pathParams.put(key, entry.getValue());
                    });

            requestPreparation(headers, cookies, queryParams, formParams, pathParams, null, requestSpecification);
            response = executeHttpMethod(tData, requestSpecification);
        } catch (Exception e) {
            captureException(e);
        }
        return response;
    }

    protected Response customRequest(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, Map<String, Object> multiPartMap) {
        try {
            String URI = tEnv().getApiURI();
            RestAssured.baseURI = URI;
            dataToJSON("URI", URI);
        } catch (Exception e) {
            captureException(e);
        }
        return requestHandling(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, multiPartMap);
    }


    private Response requestHandling(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, Map<String, Object> multiPartMap) {
        RequestSpecification requestSpecification = null;
        try {
            RestAssured.useRelaxedHTTPSValidation();
            requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            requestPreparation(headers, cookies, queryParams, formParams, pathParams, requestBody, requestSpecification);
            if (CollectionUtils.hasElements(multiPartMap)) {
                dataToJSON("multiPart", multiPartMap);
                for (Map.Entry<String, Object> entry : multiPartMap.entrySet()) {
                    String k = entry.getKey();
                    Object v = entry.getValue();
                    requestSpecification.multiPart(k, new File(v.toString()));
                }
            }
            if (requestBody != null) {
                if (CollectionUtils.hasElements(Collections.singleton(requestBody))) {
                    dataToJSON("requestBody", requestBody);
                    requestSpecification.body(requestBody);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return executeHttpMethod(HttpMethod, Endpoint, requestSpecification);
    }

    private void requestPreparation(Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, RequestSpecification requestSpecification) {
        try {
            if (headers != null && headers.size() > 0) {
                dataToJSON("headers", new LinkedHashMap<>(headers));
                requestSpecification.headers(new LinkedHashMap<>(headers));
            }
            if ((pathParams != null && pathParams.size() > 0)) {
                dataToJSON("pathParams", new LinkedHashMap<>(pathParams));
                requestSpecification.pathParams(new LinkedHashMap<>(pathParams));
            }
            if ((queryParams != null && queryParams.size() > 0)) {
                dataToJSON("queryParams", new LinkedHashMap<>(queryParams));
                requestSpecification.queryParams(new LinkedHashMap<>(queryParams));
            }
            if ((formParams != null && formParams.size() > 0)) {
                dataToJSON("formParams", new LinkedHashMap<>(formParams));
                requestSpecification.formParams(new LinkedHashMap<>(formParams));
            }
            if ((cookies != null && cookies.size() > 0)) {
                dataToJSON("cookies", new LinkedHashMap<>(cookies));
                requestSpecification.cookies(new LinkedHashMap<>(cookies));
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    private String validateResponse(Response response, LinkedHashMap<String, Object> tData, String expectMapKey) {
        return validateCustomResponse(response, mapKeyFinder(tData, "StatusCode"), expectMapKey);
    }

    private String validateCustomResponse(Response response, String StatusCode, String expectMapKey) {
        Map<String, Object> responseMapActualExpanded = customResponseValidation(response, StatusCode);
        return String.valueOf(responseMapActualExpanded.get(expectMapKey));
    }

    private Map<String, Object> validateResponse(Response response, LinkedHashMap<String, Object> tData) {
        return responseCheck(response, mapKeyFinder(tData, "StatusCode"), null, null);
    }

    protected Map<String, Object> customResponseValidation(Response response, String StatusCode, Map<String, Object> expectedResponse, Map<String, Object> modifiedExpectedResponse) {
        return responseCheck(response, StatusCode, expectedResponse, modifiedExpectedResponse);
    }

    protected Map<String, Object> customResponseValidation(Response response, String StatusCode) {
        return responseCheck(response, StatusCode, null, null);
    }

    protected Map<String, Object> responseAsMap(Response response) {
        return JsonFlattener.flattenAsMap(response.asString());
    }

    private Map<String, Object> responseCheck(Response response, String StatusCode, Map<String, Object> expectedResponse, Map<String, Object> modifiedExpectedResponse) {
        Map<String, Object> flattenMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> statusCode = new LinkedHashMap<>();
        try {
            statusCode.put("ACT", String.valueOf(response.getStatusCode()));
            statusCode.put("EXP", StatusCode);
            dataToJSON("statusCode", statusCode);
            List<String> failStatus = new ArrayList<>();
            if (StatusCode != null) {
                if (!StatusCode.equals(String.valueOf(response.getStatusCode()))) {
                    failStatus.add("FAILED");
                    failAnalysisThread.get().add("[ACT : " + response.getStatusCode() + "] " + " [EXP : " + StatusCode + "]");
                    dataToJSON("responseJSON", response.getBody().asString());
                    dataToJSON("apiTestStatus", "FAILED");
                    dataToJSON("failReason","[ACT : " + response.getStatusCode() + "] " + " [EXP : " + StatusCode + "]");
                } else {
                    flattenMap = JsonFlattener.flattenAsMap(response.asString());
                    flattenMap.put("actualStatusCode", response.getStatusCode());
                    dataToJSON("responseJSON", response.getBody().asString());
                    if (expectedResponse != null) {
                        if (modifiedExpectedResponse != null) {
                            validateResponseFromMap(flattenMap, modifiedExpectedResponse, failStatus);
                        } else {
                            validateResponseFromMap(flattenMap, expectedResponse, failStatus);
                        }
                    } else {
                        if (failStatus.size() > 0) {
                            dataToJSON("apiTestStatus", "FAILED");
                        } else {
                            dataToJSON("apiTestStatus", "PASSED");
                        }
                    }
                }
            } else {
                dataToJSON("apiTestStatus", "PASSED");
            }
        } catch (Exception e) {
            dataToJSON("apiTestStatus", "FAILED");
            captureException(e);
        }
        return flattenMap;
    }

    private void validateResponseFromMap(Map<String, Object> actual, Map<String, Object> expectedResponse, List<String> failStatus) {
        LinkedHashMap<String, Object> actualResponse = new LinkedHashMap<>();
        try {
            if (expectedResponse.size() > 0 && actual.size() > 0) {
                for (String k : expectedResponse.keySet()) {
                    try {
                        if (actual.get(k).toString().equals(expectedResponse.get(k).toString())) {
                            actualResponse.put(k, actual.get(k).toString());
                            logger.info("Actual Text:" + actual.get(k).toString() + " Expected Exact Text:" + expectedResponse.get(k).toString());
                        } else {
                            failStatus.add("FAILED");
                            failAnalysisThread.get().add("[Actual value : " + actual.get(k).toString() + "] " + " [Expected Value : " + expectedResponse.get(k).toString() + "]");
                            actualResponse.put(k, actual.get(k).toString());
                            dataToJSON("failReason","Actual Text:" + actual.get(k).toString() + " Expected Exact Text:" + expectedResponse.get(k).toString());
                            sAssert.assertEquals(actual.get(k).toString(), expectedResponse.get(k).toString());
                        }
                    } catch (NullPointerException e) {
                        failStatus.add("FAILED");
                        failAnalysisThread.get().add("Expected or Actual Response Key " + k + " NOT Found");
                        dataToJSON("failReason","Expected or Actual Response Key " + k + " NOT Found");
                    }
                }
            } else {
                dataToJSON("failReason","Expected or Actual Response Map is Empty or null");
            }
            dataToJSON("expectedResponse", new HashMap<>(expectedResponse));
            dataToJSON("actualResponse", actualResponse);
            if (failStatus.size() > 0) {
                dataToJSON("apiTestStatus", "FAILED");
            } else {
               dataToJSON("apiTestStatus", "PASSED");
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    private Response executeAPIMethod(String HttpMethod, String Endpoint, RequestSpecification requestSpecification) {
        Response response = null;
        try {
            dataToJSON("httpMethod", HttpMethod);
            dataToJSON("endPoint", Endpoint);
            apiCoverage.add(Endpoint);
            double respTime;
            try {
                switch (HttpMethod) {
                    case "GET":
                        response = requestSpecification.get(Endpoint).then().extract().response();
                        break;
                    case "POST":
                        response = requestSpecification.post(Endpoint).then().extract().response();
                        break;
                    case "PUT":
                        response = requestSpecification.put(Endpoint).then().extract().response();
                        break;
                    case "PATCH":
                        response = requestSpecification.patch(Endpoint).then().extract().response();
                        break;
                    case "DELETE":
                        response = requestSpecification.delete(Endpoint).then().extract().response();
                        break;
                    default:
                        logger.error("Method " + HttpMethod + " is not yet implemented");
                        logApiReport("FAIL", "Method " + HttpMethod + " is not yet implemented");
                        break;
                }

            } catch (Exception e) {
                dataToJSON("apiTestStatus", "FAILED");
                failAnalysisThread.get().add("Please check your Internet Connection or Host URL");
                apiTearDown(null, null, null, null, null, null, null);
            }
            if (response != null) {
                respTime = response.getTimeIn(TimeUnit.MILLISECONDS) / 1000.0;
                dataToJSON("responseTime", String.valueOf(respTime));
            }
        } catch (Exception e) {
            captureException(e);
        }
        return response;
    }

    protected Response plainRequest(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {
        Response response = null;
        try {
            RestAssured.baseURI = tEnv().getApiURI();
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put("URI", tEnv().getApiURI());
            dataMap.put("HTTPMethod", HttpMethod);
            dataMap.put("Endpoint", Endpoint);
            apiCoverage.add(Endpoint);
            dataMap.put("Expected Status Code", expectedStatusCode);

            requestPreparation(headers, cookies, queryParams, formParams, pathParams, requestBody, requestSpecification);
            if (requestBody != null) {
                if (CollectionUtils.hasElements(Collections.singleton(requestBody))) {
                    requestSpecification.body(requestBody);
                    dataMap.put("body", requestBody);
                }
            }
            switch (HttpMethod) {
                case "GET":
                    response = requestSpecification.get(Endpoint).then().extract().response();
                    break;
                case "POST":
                    response = requestSpecification.post(Endpoint).then().extract().response();
                    break;
                case "PUT":
                    response = requestSpecification.put(Endpoint).then().extract().response();
                    break;
                case "PATCH":
                    response = requestSpecification.patch(Endpoint).then().extract().response();
                    break;
                case "DELETE":
                    response = requestSpecification.delete(Endpoint).then().extract().response();
                    break;
                default:
                    logger.error("Method " + HttpMethod + " is not yet implemented");
                    break;
            }
            if (response != null) {
                logger.info(response.asString());
                dataMap.put("response", response.asString());
                dataMap.put("Actual Status Code", String.valueOf(response.getStatusCode()));
                dataTableCollectionApi.get().add(new LinkedHashMap<>(dataMap));
            }
        } catch (Exception e) {
            captureException(e);
        }
        return response;
    }

    protected Response plainRequest(String HttpMethod, String URI, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {
        Response response = null;
        try {
            RestAssured.baseURI = URI;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put("URI", URI);
            dataMap.put("HTTPMethod", HttpMethod);
            dataMap.put("Endpoint", Endpoint);
            apiCoverage.add(Endpoint);
            dataMap.put("Expected Status Code", expectedStatusCode);

            requestPreparation(headers, cookies, queryParams, formParams, pathParams, requestBody, requestSpecification);
            if (requestBody != null) {
                if (CollectionUtils.hasElements(Collections.singleton(requestBody))) {
                    requestSpecification.body(requestBody);
                    dataMap.put("body", requestBody);
                }
            }
            switch (HttpMethod) {
                case "GET":
                    response = requestSpecification.get(Endpoint).then().extract().response();
                    break;
                case "POST":
                    response = requestSpecification.post(Endpoint).then().extract().response();
                    break;
                case "PUT":
                    response = requestSpecification.put(Endpoint).then().extract().response();
                    break;
                case "PATCH":
                    response = requestSpecification.patch(Endpoint).then().extract().response();
                    break;
                case "DELETE":
                    response = requestSpecification.delete(Endpoint).then().extract().response();
                    break;
                default:
                    logger.error("Method " + HttpMethod + " is not yet implemented");
                    break;
            }
            if (response != null) {
                logger.info(response.asString());
                dataMap.put("response", response.asString());
                dataMap.put("Actual Status Code", String.valueOf(response.getStatusCode()));
                dataTableCollectionApi.get().add(new LinkedHashMap<>(dataMap));
            }
        } catch (Exception e) {
            captureException(e);
        }
        return response;
    }

    protected Map<String, Object> filterDataToMap(Map<String, Object> actualResponseMap, String contains) {
        Map<String, Object> returnResponse = new HashMap<>();
        try {
            actualResponseMap.entrySet().stream().filter(entry -> entry.getKey().contains(contains))
                    .forEach(entry -> {
                        String key = entry.getKey();
                        logger.info(key + "  " + entry.getValue());
                        returnResponse.put(key, entry.getValue());
                    });
        } catch (Exception e) {
            captureException(e);
        }
        return returnResponse;
    }

    protected Map<String, Object> filterDataToMap(Map<String, Object> actualResponseMap, String contains, String knownKey, String knownValue) {
        Map<String, Object> returnResponse = new HashMap<>();
        try {
            actualResponseMap.entrySet().stream().filter(entry -> entry.getKey().contains(contains))
                    .forEach(entry -> {
                        String key = entry.getKey();
                        if (knownKey != null) {
                            if (key.contains(knownKey)) {
                                returnResponse.put(key, entry.getValue());
                            }
                        }
                        if (knownValue != null) {
                            if (entry.getValue().toString().contains(knownValue)) {
                                returnResponse.put(key, entry.getValue());
                            }
                        }

                    });
        } catch (Exception e) {
            captureException(e);
        }
        return returnResponse;
    }

    protected Map<String, Object> filterDynamicDataToMapImpl(Map<String, Object> actualResponseMap, String knownKey, String knownValue,String... requiredKeys) {
        Map<String, Object> modifiedExpectedResponse = new HashMap<>();
        try {
            actualResponseMap.forEach((k, v) -> {
                if ((v != null) && (knownKey != null)) {
                    if (v.toString().equals(knownValue)) {
                        if(requiredKeys.length>0){
                            Arrays.stream(requiredKeys).parallel().forEach(item->{
                                String replacedKey = k.replace(knownKey, item);
                                modifiedExpectedResponse.put(replacedKey, actualResponseMap.get(replacedKey));
                            });
                        }
                    }
                }
            });
        } catch (Exception e) {
            captureException(e);
        }
        return modifiedExpectedResponse;
    }

    protected Map<String, Object> responseValidationFromTestData(LinkedHashMap<String, Object> tData) {
        Response response = null;
        Map<String, Object> expectedResponse = new HashMap<>();
        try {
            tData.entrySet().stream().filter(entry -> entry.getKey().contains("EXP#"))
                    .forEach(entry -> {
                        String key = entry.getKey().replace("EXP#", "");
                        logger.info(key + "  " + entry.getValue());
                        expectedResponse.put(key, entry.getValue());
                    });
            response = sendInternalRequest(tData);
        } catch (Exception e) {
            captureException(e);
        }
        return customResponseValidation(response, mapKeyFinder(tData, "StatusCode"), expectedResponse, null);
    }

    protected HashSet<Object> responseKeys(Response response) {
        return new HashSet<>(responseAsMap(response).keySet());
    }

    protected HashMap replaceTestEnvVariables(HashMap map){

        if(map!=null){

            for (Object key : map.keySet()) {
                try{
                    if(map.get(key)!=null & map.get(key).equals("URI")){
                        map.replace(key,tEnv().getApiURI());
                    }
                    if(map.get(key)!=null & map.get(key).equals("version")){
                        map.replace(key,tEnv().getApiVersion());
                    }
                    if(map.get(key)!=null & map.get(key).equals("locale")){
                        map.replace(key,tEnv().getApiLocale());
                    }
                    if(map.get(key)!=null & map.get(key).equals("region")){
                        map.replace(key,tEnv().getApiRegion());
                    }
                    if(map.get(key)!=null & map.get(key).equals("store")){
                        map.replace(key,tEnv().getApiStore());
                    }
                    if(map.get(key)!=null & map.get(key).equals("host")){
                        map.replace(key,tEnv().getApiHost());
                    }
                    if(map.get(key)!=null & map.get(key).equals("token")){
                        map.replace(key,tEnv().getApiToken());
                    }
                    if(map.get(key)!=null & map.get(key).equals("country")){
                        map.replace(key,tEnv().getApiCountry());
                    }
                    if(map.get(key)!=null & map.get(key).equals("currency")){
                        map.replace(key,tEnv().getApiCurrency());
                    }
                    if(map.get(key)!=null & map.get(key).equals("timezone")){
                        map.replace(key,tEnv().getApiTimeZone());
                    }
                    if(map.get(key)!=null & map.get(key).equals("phoneNumber")){
                        map.replace(key,tEnv().getApiPhoneNumber());
                    }
                    if(map.get(key)!=null & map.get(key).equals("emailId")){
                        map.replace(key,tEnv().getApiEmailID());
                    }
                }catch (Exception e){
                e.printStackTrace();
                }

            }

        }
        return map;
    }


    protected void apiTearDown(Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, Map<String, Object> expectedResponse) {
        try {
            dataTableCollectionApi.get().add(new LinkedHashMap<>(dataTableMapApi.get()));
            dataTableMapApi.get().clear();
            if (headers != null) {
                headers.clear();
            }
            if (cookies != null) {
                cookies.clear();
            }
            if (queryParams != null) {
                queryParams.clear();
            }
            if (formParams != null) {
                formParams.clear();
            }
            if (pathParams != null) {
                pathParams.clear();
            }
            if (requestBody != null) {
                requestBody = null;
            }
            if (expectedResponse != null) {
                expectedResponse.clear();
            }
            if (dataTableCollectionApi.get().size() > 0) {
                Gson pGson = new GsonBuilder().setPrettyPrinting().create();
                String responseJSON = dataTableCollectionApi.get().get(0).get("responseJSON").toString();

                dataTableCollectionApi.get().get(0).remove("responseJSON");

                if(dataTableCollectionApi.get().get(0).get("apiTestStatus").equals("FAILED")){
                    logMultipleJSON("FAIL",pGson.toJson(dataTableCollectionApi.get()),responseJSON);
                    logApiReport("FAIL",dataTableCollectionApi.get().get(0).get("failReason").toString());
                }else{
                    logMultipleJSON("PASS",pGson.toJson(dataTableCollectionApi.get()),responseJSON);
                }
                if(tEnv().getTestType().equalsIgnoreCase("api")){
                    logger.info(responseJSON);
                }
            }
            dataTableCollectionApi.get().clear();
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected Map<String, Object> replaceWithDynamicKeys(Map<String, Object> flattenMap, Map<String, Object> expectedResponse, String knownKey, String knownValue) {
        Map<String, Object> modifiedExpectedResponse = new HashMap<>();
        try {
            flattenMap.forEach((k, v) -> {
                if ((v != null) && (knownKey != null)) {
                    if (v.toString().equals(knownValue)) {
                        expectedResponse.forEach((k1, v1) -> {
                            logger.info("Replacing existing key " + k + " to " + k.replace(knownKey, k1));
                            modifiedExpectedResponse.put(k.replace(knownKey, k1), v1);
                        });
                    }
                }
            });
        } catch (Exception e) {
            captureException(e);
        }
        return modifiedExpectedResponse;
    }

    protected Map<String, Object> basicBSAuthRequest(String URI, String Endpoint, Map<String, Object> headers) {
        Map<String, Object> respMap = null;
        try {
            String userName = propertiesPojo.getBrowserStack_UserName();
            String password = propertiesPojo.getBrowserStack_Password();
            RestAssured.baseURI = URI;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            if (headers != null) {
                requestSpecification.headers(headers);
            }
            Response response = requestSpecification.auth().basic(userName, password).get(Endpoint).then().extract().response();
            respMap = JsonFlattener.flattenAsMap(response.asString());
        } catch (Exception e) {
            captureException(e);
        }
        return respMap;
    }

}

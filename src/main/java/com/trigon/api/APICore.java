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
            String curl = getCurl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, multiPartMap);
            dataToJSON("curl", curl);
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

                //This code is added to pass random html tags
//                List<String> a = new ArrayList();
//                a.add("<b>##</b>");
//                a.add("<script>##</script>");
//                a.add("<h3>##</h3>");
//                a.add("<h1>##</h1>");
//                a.add("##</br>");
//
//
//                LinkedHashMap formparam1 = new LinkedHashMap<>();
//                formParams.forEach((k,v)->{
//                    String y = a.get(cUtils().getRandomNumber(0,4)).replace("##",v.toString());
//                    formparam1.put(k,y);
//                });
//                dataToJSON("formParams", new LinkedHashMap<>(formparam1));
//                requestSpecification.formParams(new LinkedHashMap<>(formparam1));
//
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
        Map<String, Object> htmlMap = new LinkedHashMap<>();
        List<String> responseHtmlTagKeys = new ArrayList<>();
        List<String> responseNullKeys = new ArrayList<>();
        List<String> responseEmptyKeys = new ArrayList<>();
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
                    dataToJSON("failReason", "[ACT : " + response.getStatusCode() + "] " + " [EXP : " + StatusCode + "]");
                    flattenMap = JsonFlattener.flattenAsMap(response.asString());
                    flattenMap.put("actualStatusCode", response.getStatusCode());
                    flattenMap.put("actualResponseTime", response.time());
                } else {
                    flattenMap = JsonFlattener.flattenAsMap(response.asString());
                    flattenMap.put("actualStatusCode", response.getStatusCode());
                    flattenMap.put("actualResponseTime", response.time());
                    dataToJSON("responseJSON", response.getBody().asString());

                    // Added for custom variable extractions
//                    flattenMap.forEach((k,v)->{
//                        if(v!=null){
//                            if(v.toString().startsWith("<")){
//                                htmlMap.put(k,v);
//                                responseHtmlTagKeys.add(k);
//                            }
//                            if(v.toString().isEmpty()||v.toString().equals("")){
//                                responseEmptyKeys.add(k);
//                            }
//                        }
//                        if(v==null){
//                            responseNullKeys.add(k);
//                        }
//                    });
//                    if(htmlMap.size()>0){
//                        dataToJSON("responseHtmlTagKeysAndValues", htmlMap);
//                        dataToJSON("responseHtmlTagKeys",responseHtmlTagKeys.toString());
//                    }
//                    if(responseEmptyKeys.size()>0){
//                        dataToJSON("responseEmptyKeys", responseEmptyKeys.toString());
//                    }
//                    if(responseNullKeys.size()>0){
//                        dataToJSON("responseNullKeys", responseNullKeys.toString());
//                    }

                    // Expected Vs Actual verification
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
                            logger.info("Actual Data :" + k + " : " + actual.get(k).toString() + " Expected Data: " + k + " : " + expectedResponse.get(k).toString());
                        } else {
                            failStatus.add("FAILED");
                            failAnalysisThread.get().add("[Actual value : " + actual.get(k).toString() + "] " + " [Expected Value : " + expectedResponse.get(k).toString() + "]");
                            actualResponse.put(k, actual.get(k).toString());
                            dataToJSON("failReason", "Actual Text :" + actual.get(k).toString() + " <br> Expected Exact Text :" + expectedResponse.get(k).toString());
                            //sAssert.assertEquals(actual.get(k).toString(), expectedResponse.get(k).toString());
                            logReport("FAIL", "Actual Data :" + k + " : " + actual.get(k).toString() + " Expected Data: " + k + " : " + expectedResponse.get(k).toString());
                        }
                    } catch (NullPointerException e) {
                        failStatus.add("FAILED");
                        failAnalysisThread.get().add("Expected or Actual Response Key " + k + " NOT Found");
                        dataToJSON("failReason", "Expected or Actual Response Key " + k + " NOT Found");
                    }
                }
            } else {
                dataToJSON("failReason", "Expected or Actual Response Map is Empty or null");
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
            dataMap.put("Expected Status Code", expectedStatusCode);
            String curl = getCurl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            dataToJSON("curl", curl);
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
                dataMap.put("cURL", curl);
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

    protected Map<String, Object> filterDynamicDataToMapImpl(Map<String, Object> actualResponseMap, String knownKey, String knownValue, String... requiredKeys) {
        Map<String, Object> modifiedExpectedResponse = new HashMap<>();
        try {
            actualResponseMap.forEach((k, v) -> {
                if ((v != null) && (knownKey != null)) {
                    if (v.toString().equals(knownValue)) {
                        if (requiredKeys.length > 0) {
                            Arrays.stream(requiredKeys).parallel().forEach(item -> {
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

    protected HashMap replaceTestEnvVariables(HashMap map) {

        if (map != null) {

            for (Object key : map.keySet()) {
                try {
                    if (map.get(key) != null & map.get(key).equals("URI")) {
                        map.replace(key, tEnv().getApiURI());
                    }
                    if (map.get(key) != null & map.get(key).equals("version")) {
                        map.replace(key, tEnv().getApiVersion());
                    }
                    if (map.get(key) != null & map.get(key).equals("locale")) {
                        map.replace(key, tEnv().getApiLocale());
                    }
                    if (map.get(key) != null & map.get(key).equals("region")) {
                        map.replace(key, tEnv().getApiRegion());
                    }
                    if (map.get(key) != null & map.get(key).equals("store")) {
                        map.replace(key, tEnv().getApiStore());
                    }
                    if (map.get(key) != null & map.get(key).equals("host")) {
                        map.replace(key, tEnv().getApiHost());
                    }
                    if (map.get(key) != null & map.get(key).equals("token")) {
                        map.replace(key, tEnv().getApiToken());
                    }
                    if (map.get(key) != null & map.get(key).equals("country")) {
                        map.replace(key, tEnv().getApiCountry());
                    }
                    if (map.get(key) != null & map.get(key).equals("currency")) {
                        map.replace(key, tEnv().getApiCurrency());
                    }
                    if (map.get(key) != null & map.get(key).equals("timezone")) {
                        map.replace(key, tEnv().getApiTimeZone());
                    }
                    if (map.get(key) != null & map.get(key).equals("phoneNumber")) {
                        map.replace(key, tEnv().getApiPhoneNumber());
                    }
                    if (map.get(key) != null & map.get(key).equals("emailId")) {
                        map.replace(key, tEnv().getApiEmailID());
                    }
                } catch (Exception e) {
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
                Object responseJSON = dataTableCollectionApi.get().get(0).get("responseJSON");
                String curl = dataTableCollectionApi.get().get(0).get("curl").toString();
                Object statusCodeData = dataTableCollectionApi.get().get(0).get("statusCode");
                Object responseTime = dataTableCollectionApi.get().get(0).get("responseTime");
                String apiTestStatus = dataTableCollectionApi.get().get(0).get("apiTestStatus").toString();

                LinkedHashMap<String, Object> respValidation = new LinkedHashMap();
                respValidation.put("statusCode",statusCodeData);

                if (dataTableCollectionApi.get().get(0).containsKey("actualResponse")) {
                    respValidation.put("actualResponse",dataTableCollectionApi.get().get(0).get("actualResponse"));
                    logStepAction("Actual Response :"+dataTableCollectionApi.get().get(0).get("actualResponse"));
                    dataTableCollectionApi.get().get(0).remove("actualResponse");
                }
                if (dataTableCollectionApi.get().get(0).containsKey("expectedResponse")) {
                    respValidation.put("expectedResponse",dataTableCollectionApi.get().get(0).get("expectedResponse"));
                    logStepAction("Expected Response :"+dataTableCollectionApi.get().get(0).get("expectedResponse"));
                    dataTableCollectionApi.get().get(0).remove("expectedResponse");
                }

                respValidation.put("responseTime",responseTime);
                respValidation.put("apiTestStatus",apiTestStatus);

                logStepAction("Status Code Validation : "+statusCodeData);

                dataTableCollectionApi.get().get(0).remove("responseJSON");
                dataTableCollectionApi.get().get(0).remove("curl");
                dataTableCollectionApi.get().get(0).remove("statusCode");
                dataTableCollectionApi.get().get(0).remove("responseTime");
                dataTableCollectionApi.get().get(0).remove("apiTestStatus");


                if (apiTestStatus.equals("FAILED")) {
                    logMultipleJSON("FAIL", dataTableCollectionApi.get().get(0), responseJSON, curl, respValidation);
                    logApiReport("FAIL", dataTableCollectionApi.get().get(0).get("failReason").toString());
                } else {
                    logMultipleJSON("PASS", dataTableCollectionApi.get().get(0), responseJSON, curl, respValidation);
                }
                if (tEnv().getTestType().equalsIgnoreCase("api")) {
                    logger.info(responseJSON);
                }


//                String responseHtmlTagKeysAndValues = "No Html tag or values";
//                String responseHtmlTagKeys = "No Html tag matching keys";
//                String responseEmptyKeys = "No Empty Values Keys";
//                String responseNullKeys = "No NULL keys";
//
//
//                if(dataTableCollectionApi.get().get(0).get("responseHtmlTagKeysAndValues")!=null){
//                    responseHtmlTagKeysAndValues = "<textarea rows=\"4\" cols=\"50\">\n" +
//                            "  "+dataTableCollectionApi.get().get(0).get("responseHtmlTagKeysAndValues").toString()+"\n" +
//                            "  </textarea>";
//                }
//
//                if(dataTableCollectionApi.get().get(0).get("responseHtmlTagKeys")!=null){
//                    responseHtmlTagKeys = dataTableCollectionApi.get().get(0).get("responseHtmlTagKeys").toString();
//                }
//                if(dataTableCollectionApi.get().get(0).get("responseEmptyKeys")!=null){
//                    responseEmptyKeys = dataTableCollectionApi.get().get(0).get("responseEmptyKeys").toString();
//                }
//                if(dataTableCollectionApi.get().get(0).get("responseNullKeys")!=null){
//                    responseNullKeys = dataTableCollectionApi.get().get(0).get("responseNullKeys").toString();
//                }
//
//                addRowToCustomReport(dataTableCollectionApi.get().get(0).get("httpMethod").toString(),dataTableCollectionApi.get().get(0).get("endPoint").toString(),responseEmptyKeys,responseNullKeys,responseHtmlTagKeys,responseHtmlTagKeysAndValues);


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

    protected String getCurl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, Map<String, Object> multiPart) {
        StringBuffer sb = new StringBuffer();
        String curl = "";
        //boolean pathParamFlag = false;
        try {
            String URI = tEnv().getApiURI();
            if (!URI.endsWith("/")) {
                URI = URI.concat("/");
            }

            sb.append("curl --location --request " + HttpMethod.toUpperCase() + " '" + URI + Endpoint);

            if ((pathParams != null && pathParams.size() > 0)) {
                pathParams.forEach((k, v) -> sb.append("/" + v));
                //pathParamFlag = true;
            }
            if ((queryParams != null && queryParams.size() > 0)) {
                sb.append("?");
                sb.append(queryParams.toString().replace("{", "").replace("}", "").replaceAll(", ", "&"));
                //sb.append("' ");
            } //else if (pathParamFlag) {
                //sb.append("'");
            //}
            if(!sb.toString().trim().endsWith("'")){
                sb.append("' ");
            }


            if (headers != null && headers.size() > 0) {
                headers.forEach((k, v) -> sb.append("--header '" + k + ": " + v + "' "));
            }
            if ((cookies != null && cookies.size() > 0)) {
                cookies.forEach((k, v) -> sb.append("--header '" + k + ": " + v + "' "));
            }

            if ((formParams != null && formParams.size() > 0)) {
                if (headers.containsKey("Content-Type")) {
                    if (headers.get("Content-Type").toString().endsWith("x-www-form-urlencoded")) {
                        formParams.forEach((k, v) -> sb.append("--data-urlencode '" + k + "=" + v + "' "));
                    } else {
                        formParams.forEach((k, v) -> sb.append("--form '" + k + "=" + v + "' "));
                    }
                } else {
                    formParams.forEach((k, v) -> sb.append("--form '" + k + "=" + v + "' "));
                }
            }

            if (requestBody != null && !requestBody.equalsIgnoreCase("")) {
                sb.append("--data-raw '" + requestBody + "'");
            }

            if ((multiPart != null && multiPart.size() > 0)) {
                if (headers.containsKey("Content-Type")) {
                    if (headers.get("Content-Type").toString().contains("application/x-www-form-urlencoded")) {
                        multiPart.forEach((k, v) -> sb.append("--data-urlencode '" + k + "=" + v + "' "));
                    } else {
                        multiPart.forEach((k, v) -> sb.append("--form '" + k + "=" + v + "' "));
                    }
                } else {
                    multiPart.forEach((k, v) -> sb.append("--form '" + k + "=" + v + "' "));
                }
            }

        } catch (Exception e) {
            captureException(e);
        }

        curl = commonUtils.escapeCharacters(sb.toString());
        logger.info(curl);
        return curl;
    }


}

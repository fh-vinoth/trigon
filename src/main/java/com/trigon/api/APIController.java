package com.trigon.api;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.api.bean.APIInputData;
import com.trigon.api.bean.APIInputs;
import com.trigon.api.bean.TestMethods;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import static com.trigon.testbase.TestUtilities.cUtils;
import static com.trigon.testbase.TestUtilities.tEnv;

public class APIController extends APICore {
    private static final Logger logger = LogManager.getLogger(APIController.class);

    public static APIInputData apiInputData = null;
    protected Map<String, Object> sendRequestWithOutReportImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {

        Map<String, Object> responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response response = plainRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);
            responseMapActualExpanded = customResponseValidation(response, expectedStatusCode, expectedResponse, null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded == null) {
            hardFail("Error while sending request!! Check your request params or configurations");
        }

        return responseMapActualExpanded;
    }

    protected void addDataToMapWithKeyImpl(HashMap<String, Object> map, Map<String, Object> map1, String Key) {
        try {
            map.entrySet().stream().filter((entry) -> {
                return (entry.getKey()).equals(Key);
            }).forEach((entry) -> {
                map1.put(entry.getKey(), entry.getValue());
                logger.info("Added Keys to Map : " + entry.getKey() + " : " + entry.getValue());
            });
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected Map<String, Object> validateMultiPartResponseImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse, Map<String, Object> multiPartMap) {
        Map<String, Object> responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response resp = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, multiPartMap);
            responseMapActualExpanded = customResponseValidation(resp, expectedStatusCode, expectedResponse, null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded == null) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseMapActualExpanded;
    }

    protected Map<String, Object> validateStaticResponseImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {
        Map<String, Object> responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response resp = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            responseMapActualExpanded = customResponseValidation(resp, expectedStatusCode, expectedResponse, null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded == null) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseMapActualExpanded;
    }

    protected Response validateStaticRespImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {
        Response responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            responseMapActualExpanded = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            customResponseValidation(responseMapActualExpanded, expectedStatusCode, expectedResponse, null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded.getBody() == null) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseMapActualExpanded;
    }

    protected APIInputData getApiInputs(String path) {
        APIInputData apiInputData = null;
        boolean methodStatus = false;
        try{

            try {



                List<File> allFiles = cUtils().listAllFiles(new File(path));
                for (File file : allFiles) {
                    if (!file.isDirectory()) {
                        if (file.getName().contains(".json")) {
                            Gson pGson = new GsonBuilder().setPrettyPrinting().create();
                            JsonElement apiElements = JsonParser.parseReader(new FileReader(file));
                            APIInputs apiInputs = pGson.fromJson(apiElements, APIInputs.class);
                            if (apiInputs.getTestMethodData().size() > 0) {
                                for (int i = 0; i < apiInputs.getTestMethodData().size(); i++) {
                                    TestMethods testMethods = apiInputs.getTestMethodData().get(i);
                                    if (testMethods.getTestMethodName().equals(tEnv().getCurrentTestMethodName())) {
                                        logger.info("Identified Method " + tEnv().getCurrentTestMethodName() + " in File " + file);
                                        methodStatus = true;
                                        for(int j=0;j<testMethods.getApiInputData().size();j++){
                                            apiInputData = testMethods.getApiInputData().get(j);
                                            HashMap headers = replaceTestEnvVariables(apiInputData.getHeaders());
                                            HashMap cookies = replaceTestEnvVariables(apiInputData.getCookies());
                                            HashMap queryParams = replaceTestEnvVariables(apiInputData.getQueryParams());
                                            HashMap formParams = replaceTestEnvVariables(apiInputData.getFormParams());
                                            HashMap pathParams = replaceTestEnvVariables(apiInputData.getPathParams());
                                            HashMap expectedResponse = replaceTestEnvVariables(apiInputData.getExpectedResponse());
                                        }
                                        break;
                                    }

                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                captureException(e);
            }

            if (!methodStatus) {
               // logger.error("Matching method name " + tEnv().getCurrentTestMethodName() + " is not found in testdata or Directory ");
            }



        }catch (Exception e){

            e.printStackTrace();
        }

        return apiInputData;
    }

    protected Response validateStaticRespImpl() {
        Response responseMapActualExpanded = null;

        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());

            HashMap headers = apiInputData.getHeaders();
            HashMap cookies = apiInputData.getCookies();
            HashMap queryParams = apiInputData.getQueryParams();
            HashMap formParams = apiInputData.getFormParams();
            HashMap pathParams = apiInputData.getPathParams();
            HashMap expectedResponse = apiInputData.getExpectedResponse();
            responseMapActualExpanded = customRequest(apiInputData.getHttpMethod(), apiInputData.getEndPoint(), headers, cookies, queryParams, formParams, pathParams, apiInputData.getRequestBody(), null);
            customResponseValidation(responseMapActualExpanded, apiInputData.getExpectedStatusCode(), apiInputData.getExpectedResponse(), null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, apiInputData.getRequestBody(), expectedResponse);


        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded.getBody() == null) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseMapActualExpanded;
    }


    protected Map<String, Object> validateStaticResponseKeysImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {
        Map<String, Object> responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response resp = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            responseMapActualExpanded = customResponseValidation(resp, expectedStatusCode, expectedResponse, null);
            HashSet<Object> unionKeys = new HashSet<>(responseMapActualExpanded.keySet());
            unionKeys.addAll(expectedResponse.values());
            unionKeys.removeAll(responseMapActualExpanded.keySet());
            int actualMapSize = responseMapActualExpanded.keySet().size();
            int expectedMapSize = expectedResponse.values().size();
            if (unionKeys.isEmpty()) {
                logger.info("Actual Text:" + responseMapActualExpanded.keySet() + "ActualMapSize :" + actualMapSize + "Expected Text:" + expectedResponse.values() + "ExpectedMapSize :" + expectedMapSize);
            } else {
                logger.error("Actual Text:" + responseMapActualExpanded.keySet() + "ActualSize :" + actualMapSize + "Expected Text:" + expectedResponse.values() + "ExpectedSize :" + expectedMapSize + "Additional values in Expected List" + unionKeys);
            }
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }
        if (responseMapActualExpanded == null) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseMapActualExpanded;
    }

    protected Map<String, Object> validateDynamicResponseImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String knownKey, String knownValue, String expectedStatusCode, Map<String, Object> expectedResponse) {
        Map<String, Object> responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response response = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            Map<String, Object> flattenMap = JsonFlattener.flattenAsMap(response.asString());
            responseMapActualExpanded = customResponseValidation(response, expectedStatusCode, expectedResponse, replaceWithDynamicKeys(flattenMap, expectedResponse, knownKey, knownValue));
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded == null) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseMapActualExpanded;
    }

    protected Map<String, Object> sendRequestImpl(String HttpMethod, String URI, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {

        Map<String, Object> responseMapActualExpanded = null;
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response response = plainRequest(HttpMethod, URI, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);
            responseMapActualExpanded = customResponseValidation(response, expectedStatusCode, expectedResponse, null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, expectedResponse);
        } catch (Exception e) {
            captureException(e);
        }

        if (responseMapActualExpanded == null) {
            hardFail("Error while sending request!! Check your request params or configurations");
        }

        return responseMapActualExpanded;
    }

    protected List<Object> getStaticResponseListImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, String expectedKeyFromList) {
        List<Object> responseDataAsList = new ArrayList<>();
        try {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            dataToJSON("Step", stackTrace[2].getMethodName());
            Response resp = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            responseDataAsList = resp.jsonPath().getList(expectedKeyFromList);
            customResponseValidation(resp, expectedStatusCode, null, null);
            apiTearDown(headers, cookies, queryParams, formParams, pathParams, requestBody, null);
        } catch (Exception e) {
            captureException(e);
        }
        if (responseDataAsList.size() == 0) {
            hardFail("Error while sending request!! Check your request params or other test env configurations");
        }
        return responseDataAsList;
    }

    protected Map<String, Object> filterStaticDataFromActualResponseImpl(Map<String, Object> actualResponseMap, String contains) {
        return filterDataToMap(actualResponseMap, contains);
    }

    protected Map<String, Object> filterDynamicDataFromActualResponseImpl(Map<String, Object> actualResponseMap, String knownKey, String knownValue, String... requiredKeys) {
        return filterDynamicDataToMapImpl(actualResponseMap, knownKey, knownValue, requiredKeys);
    }

}


package com.trigon.api;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.api.bean.APIInputData;
import com.trigon.api.bean.APIInputs;
import com.trigon.api.bean.TestMethods;
import com.trigon.exceptions.ThrowableTypeAdapter;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.InflaterOutputStream;

public class APICoreController extends APICore {
    private static final Logger logger = LogManager.getLogger(APICoreController.class);

    public static APIInputData apiInputData = null;
    protected Map<String, Object> sendRequestWithOutReportImpl(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {

        Map<String, Object> responseMapActualExpanded = null;
        try {
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
                            Gson pGson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
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
            Response resp = customRequest(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, null);
            responseMapActualExpanded = customResponseValidation(resp, expectedStatusCode, expectedResponse, null);
            HashSet<Object> unionKeys = new HashSet<>(responseMapActualExpanded.keySet());
            unionKeys.addAll(expectedResponse.values());
            unionKeys.removeAll(responseMapActualExpanded.keySet());
            int actualMapSize = responseMapActualExpanded.keySet().size();
            int expectedMapSize = expectedResponse.values().size();
            if (unionKeys.isEmpty()) {
                logger.info("Actual Text :" + responseMapActualExpanded.keySet() + "ActualMapSize :" + actualMapSize + "Expected Text:" + expectedResponse.values() + "ExpectedMapSize :" + expectedMapSize);
            } else {
                logger.error("Actual Text :" + responseMapActualExpanded.keySet() + "ActualSize :" + actualMapSize + "Expected Text:" + expectedResponse.values() + "ExpectedSize :" + expectedMapSize + "Additional values in Expected List" + unionKeys);
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

    protected  Map<String, Object> decodeResponseByteImpl(String byteString){
        Map<String, Object> flattenMap = new HashMap<>();
        try {
            byte[] base64Decoded = DatatypeConverter.parseBase64Binary(byteString);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (OutputStream ios = new InflaterOutputStream(os)) {
                ios.write(base64Decoded);
            }
            flattenMap = JsonFlattener.flattenAsMap(new String(os.toByteArray(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flattenMap;
    }

    protected String encodeStringImpl(String plainString){
        return DatatypeConverter.printBase64Binary(plainString.getBytes());
    }

}


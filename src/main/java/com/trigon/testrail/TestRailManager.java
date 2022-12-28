package com.trigon.testrail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestRailManager {
    public static String TEST_USERNAME = "testrail@foodhub.com";
    public static String TEST_PASSWORD = "2Success";
    public static String RAILS_ENGINE_URL = "https://touch2success.testrail.com/";
    public static final int TEST_CASE_PASSED_STATUS = 1;
    public static final int TEST_CASE_FAILED_STATUS = 4;
    public static final int TEST_CASE_SKIPPED_STATUS = 5;
    static APIClient client = new APIClient(RAILS_ENGINE_URL);


    public static void addTestResultForTestCase(String testRunId, String testcaseId, int status) throws IOException, APIException {
        System.out.println("testRunId:" + testRunId);
        System.out.println("testcaseId:" + testcaseId);
        System.out.println("status:" + status);
        client.setUser(TEST_USERNAME);
        client.setPassword(TEST_PASSWORD);
        Map data = new HashMap();
        data.put("status_id", status);
        data.put("comment", "Test Executed and add the result into testrail");
        client.sendPost("add_result_for_case/" + testRunId + "/" + testcaseId + "", data);
    }

    public static void getResultOfTestCase(String testRunId, String testcaseId) throws IOException, APIException {
        System.out.println("testcaseId:" + testcaseId);
        System.out.println("testRunId:" + testRunId);
        Object ob = client.sendGet("get_results_for_case/" + testRunId + "/" + testcaseId);
        System.out.println(ob.toString());
    }

    public static void getTestCasesBySuite(String projectId, String suiteId) throws IOException, APIException {
        System.out.println("projectId:" + projectId);
        System.out.println("suiteId:" + suiteId);
        client.setUser(TEST_USERNAME);
        client.setPassword(TEST_PASSWORD);
        Object ob = client.sendGet("get_cases/" + projectId + "&suite_id=" + suiteId);
        System.out.println(ob.toString());
    }

    public static void getTestCasesById(String testcaseId) throws IOException, APIException {
        client.setUser(TEST_USERNAME);
        client.setPassword(TEST_PASSWORD);
        System.out.println("testcaseId:" + testcaseId);
        Object ob = client.sendGet("get_case/" + testcaseId);
        System.out.println(ob.toString());
    }

    public static void getTestCaseByUser(String projectId, String suiteId, String userId) throws IOException, APIException {
        Object ob = client.sendGet("get_cases/" + projectId + "&suite_id=" + suiteId + "&created_by=" + userId);
        System.out.println(ob.toString());
    }
}


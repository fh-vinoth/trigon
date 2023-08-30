package com.trigon.testlink;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIResults;

public class TestLinkIntegration {
    public static final String TESTLINK_KEY = "";
    //public static final String TESTLINK_KEY = "";
    //public static final String TESTLINK_URL = "";
    public static final String TESTLINK_URL = "";
    public static final String TEST_PROJECT_NAME = "APIQA:API QA";//APIQA:API QA
    public static final String TEST_PLAN_NAME = "TestAutomationIntegrationPlan";
    public static final String TEST_CASE_NAME = "Fusion needs clients logo in the activation Request - Version1";
    public static final String BUILD_NAME = "TestAutomationIntegrationBuild";



    public void updateResults(String testcaseName,String exception,String results) throws Exception{
        TestLinkAPIClient testlink = new TestLinkAPIClient(TESTLINK_KEY,TESTLINK_URL);
        boolean isConn = testlink.isConnected;
        System.out.println(testlink.connectErrorMsg);
        System.out.println(isConn);
        testlink.reportTestCaseResult(TEST_PROJECT_NAME,TEST_PLAN_NAME,testcaseName,BUILD_NAME,exception,results);
        testlink.reportTestCaseResult(TEST_PROJECT_NAME,TEST_PLAN_NAME,testcaseName,BUILD_NAME,exception, results);
        //testlink.createBuild(TEST_PROJECT_NAME,TEST_PLAN_NAME,"Automation Build Plan1","Test Integration");
       // testlink.addTestCaseToTestPlan(TEST_PROJECT_NAME,TEST_PLAN_NAME,testcaseName);



    }


}

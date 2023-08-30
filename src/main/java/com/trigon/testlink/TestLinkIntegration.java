package com.trigon.testlink;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIResults;

public class TestLinkIntegration {
    public static final String TESTLINK_KEY = "8132979bf23254fb6d2462c4b7329e16";
    //public static final String TESTLINK_KEY = "96605cb80ff2c01b6ce4f453ed2b5254cf2db00dcc98cbf6b3d81bc5006243d8";
    //public static final String TESTLINK_URL = "https://testlink.stage.t2sonline.com/testlink/lib/api/xmlrpc.php";
    public static final String TESTLINK_URL = "https://testlink.stage.t2sonline.com/index.php";
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

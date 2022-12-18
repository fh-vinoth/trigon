package com.trigon.testrail;

import org.testng.ITestResult;

public class TestRailBaseClass {
    protected static String TestCaseId;
    protected static String TestRunId;

    // @AfterMethod
    public static void tearDown(ITestResult result) throws Throwable {
        if(result.getStatus()==ITestResult.SUCCESS){
            TestRailManager.addTestResultForTestCase(TestRunId,TestCaseId,TestRailManager.TEST_CASE_PASSED_STATUS);
        }else if(result.getStatus()==ITestResult.FAILURE){
            TestRailManager.addTestResultForTestCase(TestRunId,TestCaseId,TestRailManager.TEST_CASE_FAILED_STATUS);
        }
    }
}

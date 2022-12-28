package com.fh.api;

import com.fh.core.TestLocalController;
import com.trigon.testrail.APIException;
import com.trigon.testrail.TestRailBaseClass;
import com.trigon.testrail.TestRailManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRailClass extends TestLocalController implements Runnable {
    String TestCaseId = "";
    String TestRunId = "8818";
    List<String> testcaseIds = new ArrayList<>(Arrays.asList("170051", "170052", "170053"));
    @Test
    public void getTvBoard() {
        List<String> testcaseIds = new ArrayList<>(Arrays.asList("170051", "170052", "170053"));
        try {
            //TestCaseId = "265935";
            //TestRunId = "8828";
            Thread t1 = new Thread(new TestRailClass(), "FirstThread");
           // logStepAction("Get testcase11","535,767");
            t1.start();

            // Check to see if the first thread is alive or not.
            if (t1.isAlive()) {
                System.out.format("%s is alive.%n", t1.getName());
            } else {
                System.out.format("%s is not alive.%n", t1.getName());
            }

            logStepAction("Get testcase1");
            TestCaseId = "170051";
            System.out.println("SUCCESS");
            logStepAction("Get testcase2");
            TestCaseId = "170052";
            System.out.println("SUCCESS");
            logStepAction("Get testcase2");
            TestCaseId = "170053";
            System.out.println("FAILED");

        } catch(Exception e){
          System.out.println(e.toString());
        }
        finally {
            String result = "PASS";
            try {
                if (result.equalsIgnoreCase("PASS")) {
                    TestRailManager.addTestResultForTestCase(TestRunId, TestCaseId, TestRailManager.TEST_CASE_PASSED_STATUS);
                } else if (result.equalsIgnoreCase("FAIL")) {
                    TestRailManager.addTestResultForTestCase(TestRunId, TestCaseId, TestRailManager.TEST_CASE_FAILED_STATUS);
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getTvBoard1() throws IOException, APIException {
        String TestCaseId = "";
        String TestRunId = "";
        try {
            // TestRunId = "8818";
            logStepAction("Get testcase1");
            TestCaseId = "170051";
            System.out.println("SUCCESS");
            logStepAction("Get testcase2");
            TestCaseId = "170052";
            System.out.println("FAILED due to compare text");
            logStepAction("Get testcase3");
            TestCaseId = "170052";
            System.out.println("SUCCESS");
        } finally {
            String result = "FAIL";
            try {
                if (result.equalsIgnoreCase("PASS")) {
                    TestRailManager.addTestResultForTestCase(TestRunId, TestCaseId, TestRailManager.TEST_CASE_PASSED_STATUS);
                } else if (result.equalsIgnoreCase("FAIL")) {
                    TestRailManager.addTestResultForTestCase(TestRunId, TestCaseId, TestRailManager.TEST_CASE_FAILED_STATUS);
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Running [" +
                Thread.currentThread().getName() + "].");
    }
}

package com.trigon.testrail;

import java.io.IOException;

public class TestRailTest {
    public static void main(String[] args) throws IOException, APIException {
        Projects p = new Projects();
        System.out.println(p.getProjects());
//        Runs r = new Runs();
//        String runId = r.createRunAndGetRunId("25","CategoryAutomationTest");
//        System.out.println(runId);
////        Cases c = new Cases();
////        System.out.println(c.getTestCasesOfRun("3695"));
//        BaseMethods b = new BaseMethods();
//        //1 - passed
//        //2- Non Automated
//        //3- Invalid
//        //4 - Failed
//        //5 - blocked
//        //6 - Not Applicable
//        //7 - Automated
//        //8 - Automation -Yet to Start
//
//        b.setTestCaseFinalStatus(runId,1,"Valid JSONTEST","TC_001_Get list of Categories");
//        b.setTestCaseFinalStatus(runId,1,"Valid JSONTEST","TC_002_Get Category");
//        b.setTestCaseFinalStatus(runId,1,"Valid JSONTEST","TC_003_Add Category");
//        b.setTestCaseFinalStatus(runId,1,"Valid JSONTEST","TC_004_Update Category");
//        b.setTestCaseFinalStatus(runId,1,"Valid JSONTEST","TC_005_Delete Category");
//        b.setTestCaseFinalStatus(runId,1,"Valid JSONTEST","TC_006_Search Category");

        //r.closeRunById("3692");
    }
}

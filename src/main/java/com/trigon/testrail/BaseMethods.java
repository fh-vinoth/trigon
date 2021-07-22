package com.trigon.testrail;

import com.google.gson.stream.JsonWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;


public class BaseMethods {
    private static final Logger logger = LogManager.getLogger(BaseMethods.class);

    /*
    result Params
    //1 - passed
    //2- Non Automated
    //3- Invalid
    //4 - Failed
    //5 - blocked
    //6 - Not Applicable
    //7 - Automated
    //8 - Automation -Yet to Start
     */

    public void setTestCaseFinalStatus(String runId, int result, String comment, String methodName) {
        try {
            String testCaseId = getTestCaseIdByMethodName(runId, methodName);
            if (testCaseId != null) {
                reportTestCaseStatus(result, comment, runId, testCaseId);
            } else {
                logger.error("The Test Method Not found in TestRail" + methodName);
            }

        } catch (IOException | APIException e) {
            e.printStackTrace();
        }

    }


    private String getTestCaseIdByMethodName(String runId, String methodName) throws IOException, APIException {
        Cases cases = new Cases();
        JSONArray testCases = cases.getTestCasesOfRun(runId);
        for (Object testCase : testCases) {
            JSONObject tCase = (JSONObject) testCase;
            if (methodName.equals(tCase.get("title").toString())) {
                return tCase.get("case_id").toString();
            }
        }
        return null;
    }


    private void reportTestCaseStatus(int result, String comment, String runId, String testCaseId)
            throws IOException, APIException {
        Cases cases = new Cases();
        cases.setTestCaseStatus(result, comment, runId, testCaseId);
    }

}

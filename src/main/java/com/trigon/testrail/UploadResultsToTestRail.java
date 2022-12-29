package com.trigon.testrail;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trigon.testbase.TestController;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UploadResultsToTestRail extends TestController {
    @Parameters({"TestRailStatusPath", "product"})
    @Test
    public void updateTestRailTestStatus(String testRailStatusPath, String product) {
        try {
            String testRunId = getTestRunId(product);
            uploadTestcaseStatus(testRunId, testRailStatusPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}

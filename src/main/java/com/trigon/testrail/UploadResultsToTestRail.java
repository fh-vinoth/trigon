package com.trigon.testrail;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileReader;

public class UploadResultsToTestRail {
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

    public void uploadTestcaseStatus(String testRunId, String path) {
        TestRailManager t = new TestRailManager();
        Gson gson = new Gson();
        try {
            JsonElement ele = JsonParser.parseReader(new FileReader(path));
            JsonObject result = gson.fromJson(ele, JsonObject.class);
            result.getAsJsonObject().entrySet().forEach(class_methodName -> {
                class_methodName.getValue().getAsJsonObject().get("Passed").getAsJsonArray().forEach(passedCase -> {
                    try {
                        System.out.println(String.valueOf(passedCase.getAsNumber()).substring(1));
                        t.addTestResultForTestCase(testRunId, String.valueOf(passedCase.getAsNumber()).substring(1), TestRailManager.TEST_CASE_PASSED_STATUS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                class_methodName.getValue().getAsJsonObject().get("Failed").getAsJsonObject().entrySet().forEach(failedCase -> {
                    try {
                        t.addTestResultForTestCase(testRunId, failedCase.getKey().substring(1), TestRailManager.TEST_CASE_FAILED_STATUS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                class_methodName.getValue().getAsJsonObject().get("Skipped").getAsJsonArray().forEach(skippedCase -> {
                    try {
                        t.addTestResultForTestCase(testRunId, String.valueOf(skippedCase.getAsNumber()).substring(1), TestRailManager.TEST_CASE_SKIPPED_STATUS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTestRunId(String product) {
        final String[] runId = {""};
        Gson gson = new Gson();
        try {
            JsonElement ele = JsonParser.parseReader(new FileReader("src/test/resources/Configuration/TestRunIds.json"));
            JsonObject result = gson.fromJson(ele, JsonObject.class);
            result.entrySet().stream()
                    .filter(MainProduct -> MainProduct.getKey().equalsIgnoreCase(product.split("_")[0]))
                    .forEach(subProduct -> {
                        subProduct.getValue().getAsJsonObject().entrySet().stream().filter(getSubProduct -> getSubProduct.getKey().equalsIgnoreCase(product.split("_")[1]))
                                .forEach(subProductRunId -> {
                                    runId[0] = String.valueOf(subProductRunId.getValue());
                                });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return runId[0];
    }

}

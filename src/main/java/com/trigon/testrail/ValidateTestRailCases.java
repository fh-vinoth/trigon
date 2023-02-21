package com.trigon.testrail;

import com.google.gson.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidateTestRailCases {

    @Parameters({"runId","jsonPath"})
    @Test()
    public static void validateCases(String runId, String jsonPath){
        List<String> invalidTcs = new ArrayList<>();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setLenient().create();
        try {
            JsonElement ele = JsonParser.parseReader(new FileReader(jsonPath));
            JsonObject result = gson.fromJson(ele, JsonObject.class);
            final List<String> overallTcs = new ArrayList<>();
            result.getAsJsonObject().entrySet().forEach(class_methodName -> {
                class_methodName.getValue().getAsJsonObject().get("Passed").getAsJsonArray().forEach(tc -> {
                    overallTcs.add(tc.getAsString());
                });
            });
            result.getAsJsonObject().entrySet().forEach(class_methodName -> {
                class_methodName.getValue().getAsJsonObject().get("Failed").getAsJsonObject().entrySet().forEach(elem -> {
                    overallTcs.add(elem.getKey());
                });
            });
            result.getAsJsonObject().entrySet().forEach(class_methodName -> {
                class_methodName.getValue().getAsJsonObject().get("Skipped").getAsJsonArray().forEach(tc -> {
                    overallTcs.add(tc.getAsString());
                });
            });
            List<String> finalOverallTcs = overallTcs.stream().distinct().collect(Collectors.toList()).stream().map(s -> s.substring(1)).collect(Collectors.toList());
            TestRailManager trm = new TestRailManager();
            finalOverallTcs.forEach(tc -> {
                try {
                    trm.getResultOfTestCase(runId, tc);
                } catch (Exception e) {
                    invalidTcs.add("C" + tc);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("********** INVALID TCS ***********");
            System.out.println("Total invalid cases are :: " + invalidTcs.size());
            invalidTcs.forEach(System.out::println);
            System.out.println("**********************************");
        }
    }
}

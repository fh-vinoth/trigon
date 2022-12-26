package com.trigon.testrail;


import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestRailJsonCreationNewMap {
    public static JsonWriter writer;
    JsonFileCreation j = new JsonFileCreation();
    String previousTestcaseId = "";
    List<String> passedTest = new ArrayList<>();
    List<String> failedTest = new ArrayList<>();
    List<String> skippedTest = new ArrayList<>();

    @Test
    public void method(Method m) throws IOException {
       /* HashMap<String, String> passed = new HashMap<>();
        passed.put("Coupon_" + getClass().getSimpleName(), "C1234454");
        passed.put("Coupon_" + getClass().getSimpleName(), "C1234455");
        passed.put("Coupon2_" + getClass().getSimpleName(), "C1234456");
        passed.put("Coupon2_" + getClass().getSimpleName(), "C1234457");

        HashMap<String, String> failed = new HashMap<>();
        failed.put("Statistics_" + getClass().getSimpleName(), "C1234452");
        failed.put("Statistics_" + getClass().getSimpleName(), "C1234450");

        HashMap<String, String> skipped = new HashMap<>();
        skipped.put("Statistics_" + getClass().getSimpleName(), "C1234452");
        HashMap<String, String> failedReason = new HashMap<>();
        failedReason.put("C1234452", "401 Issue");*/
        setJson("C1234452",getClass().getSimpleName(),m.getName());
    }

    public void setJson(String testCaseIds,String reasonScenario,String testStatus, String className, String m) {
        className = className;
        String methodName = m;

        HashMap<String,String> passedList = new HashMap<>();
        HashMap<String,String> failedList = new HashMap<>();
        HashMap<String,String> skippedList = new HashMap<>();

        if(testStatus.equalsIgnoreCase("Passed")){
            passedList.put("C1234","Passed");
        }

        passedList.put("C1235","Passed");
        failedList.put("C1236","401 Failed");
        failedList.put("C12370","504 Failure");
        skippedList.put("C12371","Skipped Test Class Name");

        HashMap<String, HashMap<String,String>> passedFailed = new HashMap<>();
        passedFailed.put("Passed",passedList);
        passedFailed.put("Failed",failedList);
        passedFailed.put("Skipped",skippedList);

        HashMap<String,HashMap<String, HashMap<String,String>>> methodNames = new HashMap<>();
        methodNames.put(methodName,passedFailed);

        HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> classNameArray = new HashMap<>();
        classNameArray.put(className,methodNames);

        generateJsonFile(classNameArray);
    }

    public void generateJsonFile( HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> json){
        Gson gson = new Gson();
        String val = gson.toJson(json, HashMap.class);
        System.out.println(val);
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestCaseStatusAfterExeNew1.json")));
            writer.jsonValue(val);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

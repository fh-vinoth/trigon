package com.trigon.testrail;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.sun.javafx.collections.MappingChange;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TestRailJsonCreationNewMap extends IOException{
    public static JsonWriter writer;
    HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> classNameArray = new HashMap<>();
    HashMap<String,String> passedList = new HashMap<>();
    HashMap<String,String> failedList = new HashMap<>();
    HashMap<String,String> skippedList = new HashMap<>();
    static {
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestCaseStatusAfterExeNew1.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    JsonFileCreation j = new JsonFileCreation();
    String previousTestcaseId = "";


    @Test
    public void getTestRail(Method m) throws IOException {
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
        setJson("C1234452","Get statistics1","Passed",getClass().getSimpleName(),m.getName());
        setJson("C1234453","Get statistics2","Passed",getClass().getSimpleName(),m.getName());
        setJson("C1234454","Get statistics3","Failed",getClass().getSimpleName(),m.getName());
        setJson("C1234455","Get order1","Failed","OrderPlacement","OrderPlacement1");
        setJson("C1234456","Get order2","Passed","OrderPlacement","OrderPlacement2");
        setJson("C1234457","Get driver","Skipped","Driver","Skipped");
        generateJsonFile(classNameArray);
        updateTestRailStatus();
    }

    public void setJson(String testCaseIds,String reasonOrScenario,String testStatus, String className, String methodName) {

            if(classNameArray.containsKey(testCaseIds)){

            }
            if(testStatus.equalsIgnoreCase("Passed")){
                passedList.put(testCaseIds,reasonOrScenario);
            }
            else if(testStatus.equalsIgnoreCase("Failed")){
                failedList.put(testCaseIds,reasonOrScenario);
            }else{
                skippedList.put(testCaseIds,reasonOrScenario);
            }


        HashMap<String, HashMap<String,String>> passedFailed = new HashMap<>();
        passedFailed.put("Passed",passedList);
        passedFailed.put("Failed",failedList);
        passedFailed.put("Skipped",skippedList);

        HashMap<String,HashMap<String, HashMap<String,String>>> methodNames = new HashMap<>();
        methodNames.put(methodName,passedFailed);

        classNameArray.put(className,methodNames);
        System.out.println(classNameArray);

    }

    public void generateJsonFile( HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> json){
        Gson gson = new Gson();
        String val = gson.toJson(json, HashMap.class);
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestCaseStatusAfterExeNew1.json")));
            writer.jsonValue(val);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateTestRailStatus() {
        ObjectMapper mapper = new ObjectMapper();
        try{
            File fileObj = new File("src/test/resources/Configuration/TestCaseStatusAfterExeNew1.json");
            String result = new String(Files.readAllBytes(Paths.get(String.valueOf(fileObj))));
            Map<String, Object> map = new Gson().fromJson(result, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            List<String> a = new ArrayList<>();
            map.forEach((k,v)->{
                if(k.endsWith("].passed")){
                    System.out.println(v);
                    a.add(String.valueOf(v));
                }
            });
            for(Map.Entry<String,Object> ap : map.entrySet()){
                String k = ap.getKey();
                Object v = ap.getValue();
                System.out.println(k+", "+v);
            }
            System.out.println(a);
           /* Map<String, Object> testCaseId = mapper.readValue(
                    fileObj, new TypeReference<Map<String, Object>>() {
                    });*/
           /* System.out.println(userData.get("API"));
            System.out.println(userData.get("MYT"));*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

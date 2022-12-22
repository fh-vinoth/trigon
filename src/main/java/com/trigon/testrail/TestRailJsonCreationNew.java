package com.trigon.testrail;

import com.google.gson.stream.JsonWriter;
import org.asynchttpclient.ClientStats;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;


import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TestRailJsonCreationNew {
    public static JsonWriter writer;
    JsonFileCreation j = new JsonFileCreation();

    @Test
    public void method() throws IOException {

       /* Map<String, String> map = new HashMap<>();
        String mainProduct = "";
        String subProduct = "";
        String runId = "";
        for(Map.Entry<String,String> s: map.entrySet()){
            String k = s.getKey();
            String v = s.getValue();
            if(k.endsWith("."+subProduct)){
                runId = v;
            }
        }*/

        /*** Call inside testcreation method ***/
       /* j.createJson();
        String className="Coupon",methodName="Coupon_positive",status="PASS",testcaseId="C134587";
        j.writeValueToJson(className,methodName,status,testcaseId);
        className="Status";methodName="OrderStatus";status="PASS";testcaseId="C134566";
        j.writeValueToJson(className,methodName,status,testcaseId);
        className="Statistics";methodName="Statistics_Positive";status="FAIL";testcaseId="C134566";
        j.writeValueToJson(className,methodName,status,testcaseId);
        className="Statistics";methodName="Statistics_Negative";status="FAIL";testcaseId="C134563";
        j.writeValueToJson(className,methodName,status,testcaseId);
        className="Statistics";methodName="Statistics_Validation";status="PASS";testcaseId="C134565";
        j.writeValueToJson(className,methodName,status,testcaseId);
        j.endOfJson();*/
        startJsonFile();

    }

    public void startJsonFile() {
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestCaseStatusAfterExe.json")));
            writer.beginObject();
            ObjectMapper mapper = new ObjectMapper();
            File fileObj = new File("src/test/resources/Configuration/TestcaseStatusOnExe.json");
            Map<String, Object> response = mapper.readValue(
                    fileObj, new TypeReference<Map<String, Object>>() {
                    });
            String testCaseId = "", testStatus = "", className = "", methodName = "";
            List<String> addedTestcaseClass = new ArrayList<>();
            List<String> addedTestcaseTestMethod = new ArrayList<>();
            int size = 0;
            for (Map.Entry<String, Object> m : response.entrySet()) {
                testCaseId = m.getKey();
                String val = m.getValue().toString();
                testStatus = val.split(",")[0];
                className = val.split(",")[1];
                methodName = val.split(",")[2];
                if(addedTestcaseClass.size()==0){
                    writeValueToJson(className,methodName,testCaseId,testStatus,"startNewClass","startNewMethod");
                    addedTestcaseClass.add(className);
                    addedTestcaseTestMethod.add(methodName);
                }else{
                    if(addedTestcaseClass.contains(className)){
                            if(addedTestcaseTestMethod.contains(methodName)){
                                writeValueToJson(className,methodName,testCaseId,testStatus,"sameClass","sameMethod");
                            }else{
                                writeValueToJson(className,methodName,testCaseId,testStatus,"sameClass","nextNewMethod");
                            }
                        }
                    else{
                        if(addedTestcaseTestMethod.contains(methodName)){
                            writeValueToJson(className,methodName,testCaseId,testStatus,"nextNewClass","sameMethod");
                        }else {
                            if (size == 0) {
                                writeValueToJson(className, methodName, testCaseId, testStatus, "nextNewClass", "startNewMethod");
                                size++;
                            } else {
                                writeValueToJson(className, methodName, testCaseId, testStatus, "nextNewClass", "nextNewMethod");
                            }
                        }
                        addedTestcaseClass.add(className);
                        addedTestcaseTestMethod.add(methodName);
                    }
                }
            }
            writer.endObject();
            writer.endObject();
            writer.endObject();
            writer.flush();
        } catch (Exception e) {
            Assert.fail();
        }
    }

    public void addTestCaseToJson(String testcaseName, String testId, String status) throws IOException {
        writer.name(testcaseName);
        writer.beginObject();
        writer.name("TestcaseId").value(testId);
        writer.name("status").value(status);
        writer.endObject();
    }

    public void jsonCreationAfterTest() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File fileObj = new File("src/test/resources/Configuration/TestcaseStatusOnExe.json");
            Map<String, Object> response = mapper.readValue(
                    fileObj, new TypeReference<Map<String, Object>>() {
                    });
            System.out.println(response);
            //  System.out.println(response.get("MYT"));
        } catch (Exception e) {

        }
    }

    public JsonWriter writeValueToJson(String className,String methodName,String name, String value, String isNewClass,String isNewMethod) {
        try {
            if(isNewClass.equalsIgnoreCase("startNewClass")){
                writer.name(className);
                writer.beginObject();
                if(isNewMethod.equalsIgnoreCase("startNewMethod")){
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(name).value(value);
                }else if(isNewMethod.equalsIgnoreCase("nextNewMethod")){
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(name).value(value);
                }else{
                    writer.name(name).value(value);
                }
            }else if(isNewClass.equalsIgnoreCase("nextNewClass")){
                writer.endObject();
                writer.endObject();
                writer.name(className);
                writer.beginObject();
                if(isNewMethod.equalsIgnoreCase("startNewMethod")){
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(name).value(value);
                }else if(isNewMethod.equalsIgnoreCase("nextNewMethod")){
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(name).value(value);
                }else{
                    writer.name(name).value(value);
                }
            }else{
                if(isNewMethod.equalsIgnoreCase("startNewMethod")){
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(name).value(value);
                }else if(isNewMethod.equalsIgnoreCase("nextNewMethod")){
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(name).value(value);
                }else{
                    writer.name(name).value(value);
                }
            }
            System.out.println(writer);
            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }


}

package com.trigon.testrail;


import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;


import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TestRailJsonCreationNew {
    public static JsonWriter writer;
    JsonFileCreation j = new JsonFileCreation();
    String previousTestcaseId = "";
    List<String> passedTest = new ArrayList<>();
    List<String> failedTest = new ArrayList<>();
    List<String> skippedTest = new ArrayList<>();

    @Test
    public void method() throws IOException {
        // creating an exact replica of the above pictorial N-ary Tree

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
        /*j.createJson();
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
        j.endOfJson();
        startJsonFile();
*/

        HashMap<String, String> passed = new HashMap<>();
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
        failedReason.put("C1234452", "401 Issue");
        setJson(passed, failed, skipped, failedReason);
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
                if (addedTestcaseClass.size() == 0) {
                    writeValueToJson(className, methodName, testCaseId, testStatus, "startNewClass", "startNewMethod");
                    addedTestcaseClass.add(className);
                    addedTestcaseTestMethod.add(methodName);
                } else {
                    if (addedTestcaseClass.contains(className)) {
                        if (addedTestcaseTestMethod.contains(methodName)) {
                            writeValueToJson(className, methodName, testCaseId, testStatus, "sameClass", "sameMethod");
                        } else {
                            writeValueToJson(className, methodName, testCaseId, testStatus, "sameClass", "nextNewMethod");
                        }
                    } else {
                        if (addedTestcaseTestMethod.contains(methodName)) {
                            writeValueToJson(className, methodName, testCaseId, testStatus, "nextNewClass", "sameMethod");
                        } else {
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
            // writer.endObject();
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

    public JsonWriter writeValueToJson1(String className, String methodName, String testcaseId, String value, String isNewClass, String isNewMethod) {
        try {
            if (isNewClass.equalsIgnoreCase("startNewClass")) {
                writer.name(className);
                writer.beginObject();
                if (isNewMethod.equalsIgnoreCase("startNewMethod")) {
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(testcaseId).value(value);
                } else if (isNewMethod.equalsIgnoreCase("nextNewMethod")) {
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(testcaseId).value(value);
                } else {
                    writer.name(testcaseId).value(value);
                }
            } else if (isNewClass.equalsIgnoreCase("nextNewClass")) {
                writer.endObject();
                writer.endObject();
                writer.name(className);
                writer.beginObject();
                if (isNewMethod.equalsIgnoreCase("startNewMethod")) {
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(testcaseId).value(value);
                } else if (isNewMethod.equalsIgnoreCase("nextNewMethod")) {
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(testcaseId).value(value);
                } else {
                    writer.name(testcaseId).value(value);
                }
            } else {
                if (isNewMethod.equalsIgnoreCase("startNewMethod")) {
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(testcaseId).value(value);
                } else if (isNewMethod.equalsIgnoreCase("nextNewMethod")) {
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    writer.name(testcaseId).value(value);
                } else {
                    writer.name(testcaseId).value(value);
                }
            }
            System.out.println(writer);
            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    public JsonWriter writeValueToJson(String className, String methodName, String testcaseId, String status, String isNewClass, String isNewMethod) {
        try {

            if (isNewClass.equalsIgnoreCase("startNewClass")) {
                writer.name(className);
                writer.beginObject();
                if (isNewMethod.equalsIgnoreCase("startNewMethod")) {
                    writer.name(methodName);
                    writer.beginObject();
                    setStatusForBegin(status, testcaseId, methodName);
                  /* if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId);
                       passedTest.add(testcaseId+"_"+methodName);
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId);
                       failedTest.add(testcaseId+"_"+methodName);
                   }else{
                       writer.name("Skipped").value(testcaseId);
                       skippedTest.add(testcaseId+"_"+methodName);
                   }*/
                } else if (isNewMethod.equalsIgnoreCase("nextNewMethod")) {
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    setStatusForBegin(status, testcaseId, methodName);
                    /* writer.name(testcaseId).value(status);*/
                  /* if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId);
                       passedTest.add(testcaseId+"className");
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId);
                       failedTest.add(testcaseId+"_"+methodName);
                   }else{
                       writer.name("Skipped").value(testcaseId);
                       skippedTest.add(testcaseId+"_"+methodName);
                   }*/
                } else {
                    addPreviousTestCaseStatus(status, methodName, testcaseId);
                    /* writer.name(testcaseId).value(status);*/
                /*   if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId+","+passedTest.get((passedTest.size())-1));
                       passedTest.add(testcaseId+"_"+methodName);
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId+","+failedTest.get((failedTest.size())-1));
                       failedTest.add(testcaseId+"_"+methodName);
                   }else{
                       writer.name("Skipped").value(testcaseId+","+skippedTest.get((skippedTest.size())-1));
                       skippedTest.add(testcaseId+"_"+methodName);
                   }*/
                }
            } else if (isNewClass.equalsIgnoreCase("nextNewClass")) {
                writer.endObject();
                writer.endObject();
                writer.name(className);
                writer.beginObject();
                if (isNewMethod.equalsIgnoreCase("startNewMethod")) {
                    writer.name(methodName);
                    writer.beginObject();
                    setStatusForBegin(status, testcaseId, methodName);
                    /* writer.name(testcaseId).value(status);*/
                   /*if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId);
                       passedTest.add(testcaseId+"_"+methodName);
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId);
                       failedTest.add(testcaseId+"_"+methodName);
                   }else{
                       writer.name("Skipped").value(testcaseId);
                       skippedTest.add(testcaseId+"_"+methodName);
                   }*/
                } else if (isNewMethod.equalsIgnoreCase("nextNewMethod")) {
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    setStatusForBegin(status, testcaseId, methodName);
                    /* writer.name(testcaseId).value(status);*/
                 /*  if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId);
                       passedTest.add(testcaseId+"_"+methodName);
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId);
                       failedTest.add(testcaseId+"_"+methodName);
                   }else{
                       writer.name("Skipped").value(testcaseId);
                       skippedTest.add(testcaseId+"_"+methodName);
                   }*/
                } else {
                    addPreviousTestCaseStatus(status, methodName, testcaseId);
                    /*writer.name(testcaseId).value(status);*/

                  /* if(status.equalsIgnoreCase("PASS")){
                       String testcase = passedTest.get((passedTest.size())-1);
                       String methodNamee = testcase.split("_")[1];
                       String passedTestCase = testcase.split("_")[0];
                       if((methodNamee.equals(methodName))){
                           writer.name("Passed").value(testcaseId+","+passedTestCase);
                       }else{
                           writer.name("Passed").value(testcaseId);
                       }
                       passedTest.add(testcaseId+"_"+methodName);
                   }else if(status.equalsIgnoreCase("FAIL")){
                       String testcase = failedTest.get((failedTest.size())-1);
                       String methodNamee = testcase.split("_")[1];
                       String failedTestCase = testcase.split("_")[0];
                       if((methodNamee.equals(className))){
                           writer.name("Failed").value(testcaseId+","+failedTestCase);
                       }else{
                           writer.name("Failed").value(testcaseId);
                       }
                       failedTest.add(testcaseId+"_"+methodName);
                   }else{
                       String testcase = skippedTest.get((skippedTest.size())-1);
                       String methodNamee = testcase.split("_")[1];
                       String skippedTestCase = testcase.split("_")[0];
                       if((methodNamee.equals(className))){
                           writer.name("Skipped").value(testcaseId+","+skippedTestCase);
                       }else{
                           writer.name("Skipped").value(testcaseId);
                       }
                       skippedTest.add(testcaseId+"_"+methodName);
                   }*/
                }
            } else {
                if (isNewMethod.equalsIgnoreCase("startNewMethod")) {
                    writer.name(methodName);
                    writer.beginObject();
                    /* writer.name(testcaseId).value(status);*/
                  /* if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId);
                       passedTest.add(testcaseId+"className");
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId);
                       failedTest.add(testcaseId+"className");
                   }else{
                       writer.name("Skipped").value(testcaseId);
                       skippedTest.add(testcaseId+"className");
                   }*/
                    setStatusForBegin(status, testcaseId, methodName);
                } else if (isNewMethod.equalsIgnoreCase("nextNewMethod")) {
                    writer.endObject();
                    writer.name(methodName);
                    writer.beginObject();
                    setStatusForBegin(status, testcaseId, methodName);
                    /* writer.name(testcaseId).value(status);*/
                /*   if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId);
                       passedTest.add(testcaseId+"className");
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId);
                       failedTest.add(testcaseId+"className");
                   }else{
                       writer.name("Skipped").value(testcaseId);
                       skippedTest.add(testcaseId+"className");
                   }*/
                } else {
                    /* writer.name(testcaseId).value(status);*/
                 /*  if(status.equalsIgnoreCase("PASS")){
                       writer.name("Passed").value(testcaseId+","+passedTest.get((passedTest.size())-1));
                       passedTest.add(testcaseId+"className");
                   }else if(status.equalsIgnoreCase("FAIL")){
                       writer.name("Failed").value(testcaseId+","+failedTest.get((failedTest.size())-1));
                       failedTest.add(testcaseId+"className");
                   }else{
                       writer.name("Skipped").value(testcaseId+","+skippedTest.get((skippedTest.size())-1));
                       skippedTest.add(testcaseId+"className");
                   }*/
                    addPreviousTestCaseStatus(status, methodName, testcaseId);
                }
            }
            previousTestcaseId = testcaseId;
            System.out.println(writer);
            System.out.println(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    public void addPreviousTestCaseStatus(String status, String methodName, String testcaseId) {
        try {
            if (status.equalsIgnoreCase("PASS")) {
                setTestStatus(passedTest, methodName, testcaseId, "Passed");
            } else if (status.equalsIgnoreCase("FAIL")) {
                setTestStatus(failedTest, methodName, testcaseId, "Failed");
            } else {
                setTestStatus(skippedTest, methodName, testcaseId, "Skipped");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTestStatus(List<String> lists, String methodName, String testcaseId, String status) {
        try {
          /* String testcase = lists.get((lists.size()) - 1);
           String methodNamee = testcase.split("_")[1];
           String skippedTestCase = testcase.split("_")[0];
           if ((methodNamee.equals(methodName))) {
               writer.name(status).value(testcaseId + "," + skippedTestCase);
           } else {
               writer.name(status).value(testcaseId);
           }
           lists.add(testcaseId + "_" + methodName);*/
            if (status.equals("PASS")) {
                String testcase = passedTest.get((passedTest.size()) - 1);
                String methodNamee = testcase.split("_")[1];
                String testcasee = testcase.split("_")[0];
                if ((methodNamee.equals(methodName))) {
                    writer.name("Passed").value(testcaseId + "," + testcasee);
                } else {
                    writer.name("Passed").value(testcaseId);
                }
                passedTest.add(testcaseId + "_" + methodName);
            } else if (status.equals("FAIL")) {
                String testcase = failedTest.get((failedTest.size()) - 1);
                String methodNamee = testcase.split("_")[1];
                String testcasee = testcase.split("_")[0];
                if ((methodNamee.equals(methodName))) {
                    writer.name("Failed").value(testcaseId + "," + testcasee);
                } else {
                    writer.name("Failed").value(testcaseId);
                }
                failedTest.add(testcaseId + "_" + methodName);
            } else {
                if (skippedTest.size() > 0) {
                    String testcase = skippedTest.get((skippedTest.size()) - 1);
                    String methodNamee = testcase.split("_")[1];
                    String testcasee = testcase.split("_")[0];
                    if ((methodNamee.equals(methodName))) {
                        writer.name("Skipped").value(testcaseId + "," + testcasee);
                    } else {
                        writer.name("Skipped").value(testcaseId);
                    }
                }
                skippedTest.add(testcaseId + "_" + methodName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatusForBegin(String status, String testcaseId, String methodName) {
        try {
            if (status.equalsIgnoreCase("PASS")) {
                writer.name("Passed").value(testcaseId);
                passedTest.add(testcaseId + "_" + methodName);
            } else if (status.equalsIgnoreCase("FAIL")) {
                writer.name("Failed").value(testcaseId);
                failedTest.add(testcaseId + "_" + methodName);
            } else {
                writer.name("Skipped").value(testcaseId);
                skippedTest.add(testcaseId + "_" + methodName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setJson(HashMap<String, String> passed, HashMap<String, String> failed, HashMap<String, String> skipped, HashMap<String, String> failedReason) {
        System.out.println("Passed : " + passed);
        System.out.println("Failed : " + failed);
        System.out.println("Skipped : " + skipped);

        HashMap<String, List<HashMap<String, ArrayList<String>>>> q = new HashMap<>();
        /**
         * ClassName{
         * MethodName{
         * passed:"",
         * failed :"",
         * skipped : ""
         */

        HashMap<String, HashMap<String, String>> m = new HashMap<>();
        m.put("passed", passed);
        m.put("Failed", failedReason);
        m.put("Skipped", skipped);

        HashMap<String, HashMap<String, HashMap<String, String>>> mm = new HashMap<>();
        mm.put(getClass().getName(), m);
        System.out.println(mm);
        Gson gson = new Gson();
        String val = gson.toJson(mm, HashMap.class);
        System.out.println(val);
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestCaseStatusAfterExeNew.json")));
            writer.jsonValue(val);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

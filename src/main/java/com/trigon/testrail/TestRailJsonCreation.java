package com.trigon.testrail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class TestRailJsonCreation{
    public static JsonWriter writer;

    @Test
    public void method() throws IOException {
       /* ObjectMapper mapper = new ObjectMapper();
        File fileObj = new File("src/test/resources/Configuration/TestRunIds.json");
        String result = new String(Files.readAllBytes(Paths.get(String.valueOf(fileObj))));
        Map<String, Object> userData1 = new Gson().fromJson(result, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        Map<String, Object> userData = mapper.readValue(
                fileObj, new TypeReference<Map<String, Object>>() {
                });
        System.out.println(userData.get("API"));
        System.out.println(userData.get("MYT"));*/

       /* JsonParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("tenv/test-env.json"));
        Map<String, Object> map = JsonFlattener.flattenAsMap(String.valueOf(fileObj));
        System.out.println(map);*/
        Map<String, String> map = new HashMap<>();
        String mainProduct = "";
        String subProduct = "";
        String runId = "";
        for(Map.Entry<String,String> s: map.entrySet()){
            String k = s.getKey();
            String v = s.getValue();
            if(k.endsWith("."+subProduct)){
                runId = v;
            }
        }
        writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestCaseStatus1.json")));
        writer.beginObject();
        writer.name("Product").value(mainProduct);
        writer.name("RunId").value(runId);
        addTestCase("TestCase1","8818","PASS");
        writer.endObject();
        writer.flush();
    }
    public void addTestCase(String testcaseName,String testId,String status) throws IOException {
        writer.name(testcaseName);
        writer.beginObject();
        writer.name("TestcaseId").value(testId);
        writer.name("status").value(status);
        writer.endObject();
    }
}

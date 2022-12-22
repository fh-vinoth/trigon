package com.trigon.testrail;

import com.google.gson.stream.JsonWriter;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

public class JsonFileCreation {
    public static JsonWriter writer;

    public void createJson(){
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter("src/test/resources/Configuration/TestcaseStatusOnExe.json")));
            writer.beginObject();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void endOfJson() {
        try {
            writer.endObject();
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public JsonWriter writeValueToJson(String name,String value) {
        try {
            writer.name(name).value(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return writer;
    }
    public void writeValueToJson(String className, String methodName, String status, String testcaseId){
        try {
            writer.name(testcaseId).value(status+","+className+","+methodName);
        }catch (Exception e){
            Assert.fail();
        }
    }
}
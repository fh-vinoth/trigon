package com.trigon.testrail;

/*import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;*/

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class TestRailTestStatusMap  {
    static ArrayList<String> passed = new ArrayList<>();
    static HashMap<String,String> failed = new HashMap<>();
    static ArrayList<String> skipped = new ArrayList<>();
    static HashMap<String,Object> result = new HashMap<>();
    LinkedHashMap<String,HashMap> collectionMap = new LinkedHashMap<>();
    static JsonWriter writer;


    @BeforeMethod
    public void beforeM(){
        passed = new ArrayList<>();
        failed = new HashMap<>();
        skipped = new ArrayList<>();
    }

    @Test
    public void updateTestRailStatus() {
        try{
            checkHeapSize();
            /** First class **/
            passed.add("C123451");
            passed.add("C123461");
            passed.add("C123471");
            passed.add("C123481");
            failed.put("C111111","Null pointer exception");
            failed.put("C111121","Actual and Expected failed");
            skipped.add("C000011");
            skipped.add("C000021");
            skipped.add("C000031");
            setJsonMap("Order_OrderPlacement");

            /** Second class **/

            passed.add("C123452");
            passed.add("C123462");
            passed.add("C123472");
            passed.add("C123482");
            failed.put("C111112","Failed due to 402");
            failed.put("C111122","Bad Gateway 502");
            skipped.add("C000012");
            skipped.add("C000022");
            skipped.add("C000032");
            setJsonMap("Statistics_ValidateStats");

            /** Third class **/

            passed.add("C123453");
            passed.add("C123463");
            passed.add("C123473");
            passed.add("C123483");
            failed.put("C111113","Failed due to 500");
            failed.put("C111123","Element not found");
            skipped.add("C000013");
            skipped.add("C000023");
            skipped.add("C000033");
            setJsonMap("Driver_GetDriver");
            createJson();

        }catch (Exception e){
            e.printStackTrace();
        }

        }
    public  void setJsonMap(String ClassMethodName){

        result.put("passed",passed);
        result.put("failed",failed);
        result.put("skipped",skipped);
        collectionMap.put(ClassMethodName,new HashMap(result));
        //passed.removeAll(passed);

       /* failed.clear();
        skipped.clear();
        result.clear();*/
        System.out.println("Test Executed");
        checkHeapSize();
    }

    public  void createJson(){
        Gson gson = new Gson();
        String val = gson.toJson(collectionMap,LinkedHashMap.class);
        try {
            writer = new JsonWriter(new BufferedWriter(new FileWriter( trigonPaths.getTestResultsPath()+"/TestCaseAnalysis.json")));
            writer.jsonValue(val);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void checkHeapSize() {
            long heapSize = Runtime.getRuntime().totalMemory();

            // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
            long heapMaxSize = Runtime.getRuntime().maxMemory();

            // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
            long heapFreeSize = Runtime.getRuntime().freeMemory();

            System.out.println("heap size: " + formatSize(heapSize));
            System.out.println("heap max size: " + formatSize(heapMaxSize));
            System.out.println("heap free size: " + formatSize(heapFreeSize));

        }
        public static String formatSize(long v) {
            if (v < 1024) return v + " B";
            int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
            return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));

    }
    @Test
    public void updateTestRailStatus2() {
        try {
            checkHeapSize();
            /** Second class **/

            passed.add("C123452");
            passed.add("C123462");
            passed.add("C123472");
            passed.add("C123482");
            failed.put("C111112", "Failed due to 402");
            failed.put("C111122", "Bad Gateway 502");
            skipped.add("C000012");
            skipped.add("C000022");
            skipped.add("C000032");
            setJsonMap("Statistics_ValidateStats");

            /** Third class **/

            passed.add("C123453");
            passed.add("C123463");
            passed.add("C123473");
            passed.add("C123483");
            failed.put("C111113", "Failed due to 500");
            failed.put("C111123", "Element not found");
            skipped.add("C000013");
            skipped.add("C000023");
            skipped.add("C000033");
            setJsonMap("Driver_GetDriver");
            createJson();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        @Test
        public void updateTestRailStatus3() {
            try{
                checkHeapSize();


                /** Third class **/

                passed.add("C123453");
                passed.add("C123463");
                passed.add("C123473");
                passed.add("C123483");
                failed.put("C111113","Failed due to 500");
                failed.put("C111123","Element not found");
                skipped.add("C000013");
                skipped.add("C000023");
                skipped.add("C000033");
                setJsonMap("Driver_GetDriver");
                createJson();

            }catch (Exception e){
                e.printStackTrace();
            }
    }
}

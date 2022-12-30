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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

            DateTimeFormatter dateAndTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZoneId zoneid = null;
            zoneid = ZoneId.of("GMT+1");
            String currentDateAndTime = LocalDateTime.now(zoneid).format(dateAndTimeFormat);
            System.out.println("*** Current Time Before Execution : "+currentDateAndTime);
            System.out.println("*** Heap Size : ");
            checkHeapSize();
            /** First class **/
            passed.add("C222391");
            passed.add("C222392");
            passed.add("C222393");
            passed.add("C222394");
            failed.put("C222395","Null pointer exception");
            failed.put("C222396","Actual and Expected failed");
            skipped.add("C222760");
            skipped.add("C222761");
            skipped.add("C222762");
            setJsonMap("Order_OrderPlacement");

            /** Second class **/

            passed.add("C222763");
            passed.add("C222936");
            passed.add("C222937");
            passed.add("C222938");
            failed.put("C223754","Failed due to 402");
            failed.put("C223755","Bad Gateway 502");
            skipped.add("C223756");
            skipped.add("C223757");
            skipped.add("C223758");
            setJsonMap("Statistics_ValidateStats");

            /** Third class **/

            passed.add("C223759");
            passed.add("C223760");
            passed.add("C223761");
            passed.add("C223764");
            failed.put("C223765","Failed due to 500");
            failed.put("C223762","Element not found");
            skipped.add("C223763");
            skipped.add("C223769");
            skipped.add("C223770");
            setJsonMap("Driver_GetDriver");

            /** Fourth class **/
            passed.add("C223771");
            passed.add("C223772");
            failed.put("C223773","Null pointer exception");
            failed.put("C188865","Actual and Expected failed");
            failed.put("C188867","Element not found");
            skipped.add("C191635");
            skipped.add("C191636");
            skipped.add("C191637");
            setJsonMap("Order_OrderPlacementDelivery");

            /** Fifth class **/
            passed.add("C191638");
            passed.add("C123461");
            passed.add("C123462");
            passed.add("C123463");
            failed.put("C208976","Null pointer exception");
            failed.put("C205525","Actual and Expected failed");
            failed.put("C205526","Element not found");
            failed.put("C205527","Element not found");
            skipped.add("C208886");
            skipped.add("C206350");
            skipped.add("C206351");
            setJsonMap("DB_Printer");

            /** Sixth class **/
            passed.add("C205536");
            passed.add("C205541");
            passed.add("C196265");
            passed.add("C196266");
            failed.put("C196267","Null pointer exception");
            failed.put("C196268","Actual and Expected failed");
            failed.put("C196269","Element not found");
            failed.put("C196270","Element not found");
            skipped.add("C196271");
            skipped.add("C196272");
            skipped.add("C222935");
            setJsonMap("KDS_Printer");

            /** Seventh class **/
            passed.add("C223022");
            passed.add("C223023");
            passed.add("C257162");
            passed.add("C257163");
            failed.put("C257164","Null pointer exception");
            failed.put("C257165","Actual and Expected failed");
            failed.put("C257176","Element not found");
            failed.put("C257177","Element not found");
            skipped.add("C257181");
            skipped.add("C257178");
            skipped.add("C257180");
            setJsonMap("Driver_Assign");

            createJson();

            currentDateAndTime = LocalDateTime.now(zoneid).format(dateAndTimeFormat);
            System.out.println("*** Current Time After Execution : "+currentDateAndTime);
            System.out.println("*** Heap Size : ");
            checkHeapSize();

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
            writer = new JsonWriter(new BufferedWriter(new FileWriter( "src/test/resources/Configuration/TestCaseAnalysis.json")));
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
}

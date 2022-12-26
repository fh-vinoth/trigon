package com.trigon.testrail;

import com.github.wnameless.json.flattener.JsonFlattener;
/*import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;*/
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateTestRailTestStatus {
    @Test
    public static void updateTestRailStatus() {
        try{
            String TestRunId = "8812";
            //String TestRunId = tEnv().getRunID();
            TestRailManager t = new TestRailManager();
            List<String> passedList = new ArrayList<>();
            List<String> failedList = new ArrayList<>();
            List<String> skippedList = new ArrayList<>();
           /* JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("src/test/java/com/fh/api/adhoc/TestCaseStatusAfterExeNew.json"));
            Map<String,Object> map = JsonFlattener.flattenAsMap(((JSONObject) obj).toJSONString());
            map.forEach((k,v)->{
                if(k.contains(".Passed")){
                    String testCaseId =  k.split(".Passed.")[1];
                    passedList.add(testCaseId);
                    try {
                       // t.addTestResultForTestCase(TestRunId, testCaseId, TestRailManager.TEST_CASE_PASSED_STATUS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(k.contains(".Failed")){
                    String testCaseId =  k.split(".Failed.")[1];
                    failedList.add(testCaseId);
                    try {
                       // t.addTestResultForTestCase(TestRunId, testCaseId, TestRailManager.TEST_CASE_FAILED_STATUS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    String testCaseId =  k.split(".Skipped.")[1];
                    skippedList.add(testCaseId);
                    try {
                       // t.addTestResultForTestCase(TestRunId, testCaseId, TestRailManager.TEST_CASE_BLOCKED_STATUS);//Add Skipped Status/Not execute status in testrail
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(passedList);
            System.out.println(failedList);
            System.out.println(skippedList);*/

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

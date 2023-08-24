package com.fh.unittests.testrail;

import com.fh.api.Category_SEARCH;
import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.testlink.TestLinkIntegration;
import com.trigon.utils.TrigonUtils;
import org.testng.annotations.Test;
import testlink.api.java.client.TestLinkAPIResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class testRailDemoTestData extends TestLocalController {

   // @Test
    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, groups = {"Sanity", "Regression"})
    @ExcelSheet(name = "TestRailDemo")
    public void methodNameDemo(LinkedHashMap<String,Object> tData) {
       // ArrayList<String> tcIDs= (ArrayList<String>) tData.get("tcIDs");
        //ArrayList<String> tcIDs = getTestIdsInArray(tData.get("tcIDs").toString());
        author_ScenarioName("Nisha","Testing");
        try {
            TestLinkIntegration i = new TestLinkIntegration();
            i.updateResults("12321",null,TestLinkAPIResults.TEST_FAILED);
            i.updateResults("API-2583: FUS - Fusion requires merchantId in the activation Request - Version1",null,TestLinkAPIResults.TEST_PASSED);


            Category_SEARCH c = new Category_SEARCH();
            c.createCategory();

           /* TrigonUtils tu = new TrigonUtils();
            tu.logStepAction("Place order ");
            tu.logStepAction("Place order ","C123","C128","C129","C127");
            logStepAction("Place order ");
            //logStepAction("Place order ", "C123,C2344");
           // logStepAction("Place order ", "C126");
            logStepAction("Place order ", "C123","C128","C129","C127");
            logStepAction("Place order ", tcIDs.get(0));
            logStepAction("Place order ", tcIDs.get(0),tcIDs.get(1),tcIDs.get(0));
            hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");
            System.out.println(tData.get("sending").toString());
            if(tData.get("sending").toString().equalsIgnoreCase("delivery")){
                logStepAction("Assign order", tcIDs.get(3));

                logReport("PASS", "2nd 3rd step");
                logReport("FAIL", "2nd 4th step");

            }

            if(tData.get("sending").toString().equalsIgnoreCase("Collection")){
                logStepAction("Collect", tcIDs.get(3) );
                logReport("PASS", "3rd 5th step");
                logReport("PASS", "3rd 6th step");

            }


            logStepAction("Hide order", tcIDs.get(1));
            //hardFail("Error Message");
            logReport("PASS", "3rd 7th step");
            logReport("PASS", "3rd 8th step");

            logStepAction("Cancel Order", tcIDs.get(2));

            logReport("PASS", "3rd 7th step");
            logReport("FAIL", "3rd 8th step");
            hardFail("Error Message");*/

        }
        catch (Exception e)
        {
           e.printStackTrace();

        }
        finally {
            testTearDown();
        }
    }
   // @Test
    public void methodNameDemo1() {
       ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C222761","C222762","C222763"));
        author_ScenarioName("Nisha","Testing");
        try {
            logStepAction("1st TC", "C222761,C222762");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C222763");

            logReport("PASS", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");




        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
            testTearDown(tcIDs);
        }
    }
   @Test
    public void methodNameDemo2() {
        ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C222764","C222936","C222937"));
        author_ScenarioName("Nisha","Testing");
        try {
            logStepAction("1st TC", "C222764,C222936");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C222937");

            logReport("PASS", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");

        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
            testTearDown(tcIDs);
        }
    }

}

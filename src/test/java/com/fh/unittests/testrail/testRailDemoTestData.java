package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testRailDemoTestData extends TestLocalController {

    //@Test
    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, groups = {"Sanity", "Regression"})
    @ExcelSheet(name = "TR")
    public void methodNameDemo() {

        ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C222391","C222393","C222394","C222392","C222395","C222396","C222937","C222938","C222760"));

        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C222391");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C222393,C222394,C222392");

            logReport("PASS", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");


            logStepAction("3rd TC", "C222395,C222396");

            logReport("PASS", "3rd 5th step");
            logReport("FAIL", "3rd 6th step");

            logStepAction("4th TC", "C222937,C222938,C222760");
            hardFail("Error Message");
            logReport("PASS", "3rd 7th step");
            logReport("PASS", "3rd 8th step");

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
    public void methodNameDemo1() {
       ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C222761","C222762","C222763"));
        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C222761,C222762");
            hardFail("Error Message");
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
        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C222764,C222936");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("FAIL", "1st test 2nd step");

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

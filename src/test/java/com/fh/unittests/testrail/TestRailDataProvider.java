package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class TestRailDataProvider extends TestLocalController {
    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, groups = {"Sanity", "Regression"})
    @ExcelSheet(name = "TR")
    public void methodNameDemo(LinkedHashMap<String, Object> tData) {
        ArrayList<String> tcIDs= (ArrayList<String>) tData.get("tcIDs");
        author_ScenarioName("Vikram","Demo");
        try {
            logStepAction("1st TC", "C0001");

            logReport("PASS", "1st step");
            logReport("PASS", "2nd step");

            logStepAction("2nd TC", "C0002,C0003");

            logReport("PASS", "3rd step");
            logReport("PASS", "step");
        }
        catch (Exception e)
        {}
        finally {
//            testTearDown(tcIDs,tData.get("dataProviderKey").toString());
        }
    }

}

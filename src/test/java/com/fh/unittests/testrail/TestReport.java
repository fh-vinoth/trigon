package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import com.trigon.reports.TestRailReport;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TestReport extends TestLocalController {

    @Test
    public void testReport() {

        author_ScenarioName("Nisha","Test Report");
        try {
            TestRailReport r = new TestRailReport();
            r.initTestRailReport();
            r.addRowToTestRailReport("OrderPlacemnet","C12344, C12345, C12346, C12346 , C12346, C12346, C12346","C12346,C12343","C12343, C12345");
            //r.addRowToTestRailReport("","C12344","","C12343","https://www.testrail.com");
            //r.addRowToTestRailReport("","C12344","","","https://www.testrail.com");
            r.addRowToTestRailReport("Settings","C12344","C12346","C12343");

        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
            testTearDown();
        }
    }


}

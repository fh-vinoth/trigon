package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TestReport extends TestLocalController {

    @Test
    public void testReport() {

        author_ScenarioName("Nisha","Test Report");
        try {

           addHeaderToCustomReport("ClassName","Passed","Failed","Skipped","TestRailLink");
           addRowToCustomReport("OrderPlacemnet","C12344","C12346","C12343","https://www.testrail.com");

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

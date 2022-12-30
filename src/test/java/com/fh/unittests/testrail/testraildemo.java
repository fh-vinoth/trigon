package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo extends TestLocalController {

    public static ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C10001","C10002","C10002A","C10002B","C10003","C10003A","C10004","C10004A","C10004B"));

    @Test
    public void methodNameDemo() {
        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C10001");

            logReport("PASS", "1st step");
            logReport("PASS", "2nd step");


            logStepAction("2nd TC", "C10002,C10002A,C10002B");

            logReport("PASS", "3rd step");
            logReport("PASS", "4th step");
            hardFail("Error Message");

            logStepAction("3rd TC", "C10003,C10003A");

            logReport("PASS", "5th step");
            logReport("PASS", "6th step");

            logStepAction("4th TC", "C10004,C10004A,C10004B");

        }
        catch (Exception e)
        {

        }
        finally {
            testTearDown(tcIDs);
        }
    }

}

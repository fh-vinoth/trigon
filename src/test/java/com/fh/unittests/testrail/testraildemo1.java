package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo1 extends TestLocalController {

    @Test

    public void methodNameDemo4() {
         ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C222938","C223754","C223755","C223756"));
        author_ScenarioName("Vikram","Testing2");
        try {
            logStepAction("1st TC", "C222938");

            logReport("PASS", "1st step");
            logReport("PASS", "2nd step");

            logStepAction("2nd TC", "C223754");

            logReport("PASS", "3rd step");
            logReport("FAIL", "4th step");

            logStepAction("3rd TC", "C223755");

            logReport("PASS", "5th step");
            logReport("PASS", "6th step");

            //hardFail("Error Message");

            logStepAction("4th TC", "C223756");

        }
        catch (Exception e)
        {

        }
        finally {
//            testTearDown(tcIDs);
        }
    }

}

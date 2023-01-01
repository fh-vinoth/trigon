package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo1 extends TestLocalController {

    @Test

    public void methodNameDemo4() {
         ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C100012","C100022","C100032","C100042"));
        author_ScenarioName("Vikram","Testing2");
        try {
            logStepAction("1st TC", "C100012");

            logReport("PASS", "1st step");
            logReport("PASS", "2nd step");

            logStepAction("2nd TC", "C100042");

            logReport("PASS", "3rd step");
            logReport("PASS", "4th step");

            logStepAction("3rd TC", "C100022");

            logReport("PASS", "5th step");
            logReport("PASS", "6th step");

            hardFail("Error Message");

            logStepAction("4th TC", "C100032");

        }
        catch (Exception e)
        {

        }
        finally {
            testTearDown(tcIDs);
        }
    }

}

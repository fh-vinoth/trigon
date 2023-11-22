package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo4 extends TestLocalController {

    @Test
    public void methodNameDemo() {

        ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C188874","C188875","C188876","C188877","C191635","C191636","C191637","C191638","C208976"));

        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C188874");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C188875,C188876,C188877");

            logReport("FAIL", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");
           // hardFail("Error Message");

            logStepAction("3rd TC", "C191635,C191636");

           /* logReport("PASS", "3rd 5th step");
            logReport("PASS", "3rd 6th step");
*/
            logStepAction("4th TC", "C191637,C191638,C208976");

            /*logReport("PASS", "3rd 5th step");
            logReport("PASS", "3rd 6th step");
*/
        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
//            testTearDown(tcIDs);
        }
    }
    @Test
    public void methodNameDemo1() {
       ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C205525","C205526","C205527"));
        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C205525,C205526");
           // hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C205527");

            logReport("PASS", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");

        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
//            testTearDown(tcIDs);
        }
    }
    @Test
    public void methodNameDemo2() {
        ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C257178","C257180","C223561"));
        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C257178,C257180");
            hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C223561");

            logReport("PASS", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");

        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
//            testTearDown(tcIDs);
        }
    }

}

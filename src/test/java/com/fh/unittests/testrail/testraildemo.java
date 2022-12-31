package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo extends TestLocalController {

    @Test
    public void methodNameDemo() {
        ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C222391","C222393","C222394","C222392","C222763","C222936","C222937","C222938","C223759"));

        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C222391");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C222393,C222394,C222392");

            logReport("FAIL", "2nd 3rd step");
            logReport("PASS", "2nd 4th step");
           // hardFail("Error Message");

            logStepAction("3rd TC", "C222763,C222936");

           /* logReport("PASS", "3rd 5th step");
            logReport("PASS", "3rd 6th step");
*/
            logStepAction("4th TC", "C222937,C222938,C223759");

            /*logReport("PASS", "3rd 5th step");
            logReport("PASS", "3rd 6th step");
*/
        }
        catch (Exception e)
        {
            hardFail();

        }
        finally {
            testTearDown(tcIDs);
        }
    }
   // @Test
    public void methodNameDemo1() {
       ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C223760","C223764","C223761"));
        author_ScenarioName("Vikram","Testing");
        try {
            logStepAction("1st TC", "C223760,C223764");
            //hardFail("Error Message");
            logReport("PASS", "1st test 1st step");
            logReport("PASS", "1st test 2nd step");

            logStepAction("2nd TC", "C223761");

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

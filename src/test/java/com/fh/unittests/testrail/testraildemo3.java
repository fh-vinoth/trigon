package com.fh.unittests.testrail;

import com.fh.api.Category_SEARCH;
import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo3 extends TestLocalController {

    @Test
    public void methodNameDemo3() {
        ArrayList<String> tcIDs = new ArrayList<>(Arrays.asList("C223765", "C223769", "C223770", "C223771"));
        try {
            Category_SEARCH c = new Category_SEARCH();
            c.createCategory();
           /* logStepAction("1st TC", "C223765");

            logReport("PASS", "1st step");
            logReport("PASS", "2nd step");

            logStepAction("2nd TC", "C223769");

            logReport("PASS", "3rd step");
            logReport("PASS", "4th step");

            logStepAction("3rd TC", "C223770");

            logReport("PASS", "5th step");
            logReport("PASS", "6th step");

            hardFail("Error Message");

            logStepAction("4th TC", "C223771");*/

        } catch (Exception e) {

        } finally {
//            testTearDown(tcIDs);
        }
    }
      /*  @Test
        public void methodNameDemo8() {
            ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C223772","C223773","C188865","C188867"));
            try {
                logStepAction("1st TC", "C223772");

                logReport("PASS", "1st step");
                logReport("FAIL", "2nd step");

                logStepAction("2nd TC", "C223773");

                logReport("PASS", "3rd step");
                logReport("PASS", "4th step");

                logStepAction("3rd TC", "C188865");

                logReport("PASS", "5th step");
                logReport("FAIL", "6th step");

                //hardFail("Error Message");

                logStepAction("4th TC", "C188867");

            }
            catch (Exception e)
            {

            }
            finally {
                testTearDown(tcIDs);
            }
    }*/

}

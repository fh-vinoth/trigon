package com.fh.unittests.testrail;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testraildemo2 extends TestLocalController {

    @Test
    public void methodNameDemo3() {
        ArrayList<String> tcIDs = new ArrayList<>(Arrays.asList("C223757", "C223758", "C223759", "C223760"));
        try {
            logStepAction("1st TC", "C223757");

            logReport("PASS", "1st step");
            logReport("PASS", "2nd step");

            logStepAction("2nd TC", "C223758");

            logReport("PASS", "3rd step");
            logReport("PASS", "4th step");

            logStepAction("3rd TC", "C223759");

            logReport("PASS", "5th step");
            logReport("PASS", "6th step");

            hardFail("Error Message");

            logStepAction("4th TC", "C223760");

        } catch (Exception e) {

        } finally {
            testTearDown(tcIDs);
        }
    }
        @Test
        public void methodNameDemo8() {
            ArrayList<String> tcIDs=new ArrayList<>(Arrays.asList("C223761","C223762","C223763","C223764"));
            try {
                logStepAction("1st TC", "C223761");

                logReport("PASS", "1st step");
                logReport("PASS", "2nd step");

                logStepAction("2nd TC", "C223762");

                logReport("PASS", "3rd step");
                logReport("PASS", "4th step");

                logStepAction("3rd TC", "C223763");

                logReport("PASS", "5th step");
                logReport("PASS", "6th step");

                //hardFail("Error Message");

                logStepAction("4th TC", "C223764");
                logReport("PASS", "7th step");
                logReport("PASS", "8th step");

            }
            catch (Exception e)
            {

            }
            finally {
                testTearDown(tcIDs);
            }
    }

}

package com.fh.unittests.frameworktest;


import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

public class ObjFactTest extends TestLocalController {



    @Test(groups = { "Regression","sanity"})
    public void abcTest() {
        author_ScenarioName("Bhaskar", "Object Factory Test   1");
        logStepAction(" Performing Search");
        //tObj().searchPage().searchTextBox_enterText("iphone");
        logStepAction(" Performing Search click");
        //tObj().searchPage().click1_click();
    }

    @Test(groups = { "Regression","sanity"})
    public void abcTest1() {
        author_ScenarioName("Bhaskar", "Object Factory Test   2");
        logStepAction(" Performing Search");
        //tObj().searchPage().searchTextBox_enterText("iphone");
        logStepAction(" Performing Search click");
        //tObj().searchPage().click1_click();

    }
}

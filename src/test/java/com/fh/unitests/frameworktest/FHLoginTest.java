package com.fh.unitests.frameworktest;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

public class FHLoginTest extends TestLocalController {

    @Test(groups = { "Regression","sanity"},priority = 1)
    public void fhLogin() {
        author_ScenarioName("Bhaskar", "Login Test");
        logScenario(" Performing Login");
//        tObj().fhLoginPage().loginLink_click();
//        tObj().fhLoginPage().userNameTextBox_enterText("reddy.perform@gmail.com");
//        tObj().fhLoginPage().passwordTextBox_enterText("Nexus1000$");
//        tObj().fhLoginPage().loginButton_click("wait_0");


    }
    @Test(groups = { "Regression","sanity"},priority = 2)
    public void fhLogOut() {
        author_ScenarioName("Bhaskar", "Logout Test");
        logScenario(" Performing Logout");
//        tObj().fhLogOutPage().userLink_click();
//        tObj().fhLogOutPage().logoutLink_click("wait_0");
    }
}

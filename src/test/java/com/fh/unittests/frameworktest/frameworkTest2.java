package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;

import com.trigon.wrapper.TestModels;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class frameworkTest2 extends TestLocalController {

    @Test
    public void trailTest() {
//        browser().get("https://www.amazon.com/");
//        browser().findElement(By.id("twotabsearchtextbox")).sendKeys("iphone");
//        browser().quit();
        try {
            author_ScenarioName("bhaskar_parallelClass1_Method3", "parallelClass1_Method3 scenario");
            logReport("FAIL", "parallelClass1_Method3 PASSED");
            TestModels testModels = new TestModels();
            testModels.verifyTitle("Amazon.com. Spend less. Smile more.","Exact");
            testModels.verifyTitle("Google","Exact");
        }
        catch (Exception e)
        {
            hardFail(e);
        }
        finally {
            testTearDown();
        }
    }
}

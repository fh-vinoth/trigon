package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BrowserTest extends TestLocalController {

    @Test(groups = { "Regression"})
    public void abcTest() {
        try{
            author_ScenarioName("Bhaskar", "Browser Test   1");
            logStepAction(" Performing Search");
            browser().findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]")).sendKeys("iphone");
            logStepAction(" Performing Search click");
            browser().findElement(By.xpath("(//input[@type=\"submit\"])[1]")).click();
            logReport("PASS","Test Passed");
            //hardFail("test failed");
        }catch (Exception e){
            hardFail(e);
        }finally {
            testTearDown();
        }



    }

    @Test(groups = {"sanity", "Regression"})
    public void abcTest1() {
        author_ScenarioName("Bhaskar", "Browser Test   1");
        logStepAction(" Performing Search");
        browser().findElement(By.xpath("//input[@id=\"twotabsearchtextbox\"]")).sendKeys("iphone");
        logStepAction(" Performing Search click");
        browser().findElement(By.xpath("(//input[@type=\"submit\"])[1]")).click();

    }
}

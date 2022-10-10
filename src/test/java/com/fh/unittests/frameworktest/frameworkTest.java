package com.fh.unittests.frameworktest;


import com.fh.core.TestLocalController;

import org.openqa.selenium.By;
import org.testng.annotations.Test;



public class frameworkTest extends TestLocalController {

    /*@Test(enabled = false)
    public void unitTest() {
        browser().findElement(By.id("twotabsearchtextbox")).sendKeys("iphone");
        author_ScenarioName("bhaskar_parallelClass1_Method1", "parallelClass1_Method1 scenario");
        logReport("PASS", "parallelClass1_Method1 PASSED");
    }*/

    @Test
    public void testGoogleSearch() {
//        WebElement element = browser().findElement(By.xpath("(//*[@type=\"text2\"])[1]"));
//        element.sendKeys("iPhone");
        //element.submit();
        //browser().findElement(By.linkText("Web")).click();
        //tObj().frameworkTest().searchTextBox_enterText("bhaskar");
        author_ScenarioName("bhaskar_parallelClass1_Method2", "parallelClass1_Method2 scenario");
        logReport("PASS", "parallelClass1_Method2 PASSED");


    }
}

package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class frameworkTest2 extends TestLocalController {

    @Test
    public void unitTest() {
//        browser().get("https://www.amazon.com/");
        browser().findElement(By.id("twotabsearchtextbox")).sendKeys("iphone");
//        browser().quit();
        author_ScenarioName("bhaskar_parallelClass1_Method3", "parallelClass1_Method3 scenario");
        logReport("FAIL", "parallelClass1_Method3 PASSED");
    }
}

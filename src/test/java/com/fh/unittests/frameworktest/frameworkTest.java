package com.fh.unittests.frameworktest;


import com.fh.core.TestLocalController;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;



public class frameworkTest extends TestLocalController {

    @Test
    public void testGoogleSearch() {
        author_ScenarioName("Vinoth", "Java scenario test");
        logReport("PASS", "Java 17 upgrade");
        try{
            db.sendQuery("select * from config where phone ='07033441234'","id");
            WebElement ele = browser().findElement(By.xpath("//label[contains(text(),'United Kingdo')]"));
            ele.click();
        }catch(Exception e){
            hardFail("Error line ",e);
        }finally {
            testTearDown();
        }
    }
}

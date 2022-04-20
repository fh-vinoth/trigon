package com.fh.unittests.frameworktest;


import com.fh.core.LocalAPIController;
import com.fh.core.TestLocalController;

import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.apache.logging.log4j.core.config.Order;
import org.openqa.selenium.By;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class frameworkTest extends TestLocalController {

    @Test
    public void unitTest() {

        author_ScenarioName("bhaskar_parallelClass1_Method1", "parallelClass1_Method1 scenario");
//        browser().findElement(By.id("twotabsearchtextbox1")).sendKeys("iphone");
      try {
          TestModels testModels = new TestModels();
//          testModels.navigateToUrl("https://www.google.com");
//          logReport("FAIL", "parallelClass1_Method1 PASSED");
//          Scanner sc=new Scanner(System.in);
//          System.out.println("Enter zxc for passing");
//        String test= tEnv().getApiEmailID();
//         testModels.compareText(test,"teamautomation17@gmail.com","Exact");
          testModels.verifyTitle("Amazon.com. Spend less. Smile more.","Exact");
          hardWait(500);
        testModels.verifyTitle("Google","Exact");
//        System.out.println(testModels.getCurrentPageURL());--
//         testModels.compareText(tEnv().getJsonFilePath(),"2","Exact");

      }
      catch(Exception e){
          hardFail(e);

      }
      finally {
//          tEnv().setJsonFilePath("2");
//          tEnv().setApiEmailID("teamautomation17@gmail.com");
          testTearDown();
      }
    }

//    @Test
//    public void testGoogleSearch() {
////        WebElement element = browser().findElement(By.xpath("(//*[@type=\"text2\"])[1]"));
////        element.sendKeys("iPhone");
//        //element.submit();
//        //browser().findElement(By.linkText("Web")).click();
//        //tObj().frameworkTest().searchTextBox_enterText("bhaskar");
//
//        author_ScenarioName("bhaskar_parallelClass1_Method2", "parallelClass1_Method2 scenario");
//        logReport("PASS", "parallelClass1_Method2 PASSED");
//        try {
//            TestModels testModels = new TestModels();
//            testModels.navigateToUrl("https://www.flipkart.com/");
//            hardWait(1000);
//            testModels.verifyTitle("Online Shopping Site for Mobiles, Electronics, Furniture, Grocery, Lifestyle, Books & More. Best Offers!","Exact");
//            testModels.verifyTitle("Google","Exact");
//
//        }
//        catch(Exception e){
//            hardFail(e);
//
//        }
//        finally {
////          tEnv().setJsonFilePath("2");
////          tEnv().setApiEmailID("teamautomation17@gmail.com");
//            testTearDown();
//        }
//
//    }
}

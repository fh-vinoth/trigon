package com.fh.mobile;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.Map;

public class MobileTest extends TestLocalController {

    @Test(groups = {"Sanity_MYT, Sanity_Fusion"})
    public void sanity_Orders_AllRegions() {
        try {
            author_ScenarioName("Gayathri,Srikrishna", "Sanity Check for "+tEnv().getApiCountry()+" to check if the build fulfills the acceptance criteria");

            logStepAction("Checking Login first");
            String a = "test";
            String b = "test11";
            //android().findElement(By.xpath("//div[@class='Test']"));
            //String a = db.sendQuery("select * from config where host = 'mytautomation-uk1.t2scdn.com'","host");
            logStepAction("Checking Login for map");
            //Map b = db.sendQueryReturnMap("select host from config where host = 'mytautomation-uk2.t2scdn.com'");
//            String c = db.sendQuery("select * from config where host = 'mytautomation-uk1.t2scdn.com'","host");
//            String d = db.sendQuery("select * from config where host = 'mytautomation-uk2.t2scdn.com'","host");
//            String e = db.sendQuery("select * from config where host = 'mytautomation-uk1.t2scdn.com'","host");
            logStepAction("Printing query 1");
            System.out.println(a);
            logStepAction("Printing query 2");
            System.out.println(b);
//            System.out.println(c);
//            System.out.println(d);
//            System.out.println(e);
        } catch (Exception e) {
            hardFail(e);
        } finally {
            testTearDown();
        }
    }
}

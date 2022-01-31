package com.fh.mobile;

import com.fh.core.TestLocalController;
import org.testng.annotations.Test;

import java.util.Map;

public class MobileTest extends TestLocalController {

    @Test(groups = {"Sanity_MYT, Sanity_Fusion"})
    public void sanity_Orders_AllRegions() {
        try {
            author_ScenarioName("Gayathri,Srikrishna", "Sanity Check for "+tEnv().getApiCountry()+" to check if the build fulfills the acceptance criteria");

            logStepAction("Checking Login");

            //android().findElement(By.xpath("//div[@class='Test']"));
            String a = db.sendQuery("select * from config where host = 'mytautomation-uk1.t2scdn.com'","host");
            Map b = db.sendQueryReturnMap("select * from config where host = 'mytautomation-uk2.t2scdn.com'");
//            String c = db.sendQuery("select * from config where host = 'mytautomation-uk1.t2scdn.com'","host");
//            String d = db.sendQuery("select * from config where host = 'mytautomation-uk2.t2scdn.com'","host");
//            String e = db.sendQuery("select * from config where host = 'mytautomation-uk1.t2scdn.com'","host");
            System.out.println(a);
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

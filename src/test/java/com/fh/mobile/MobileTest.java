package com.fh.mobile;

import com.fh.core.TestLocalController;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.Map;

public class MobileTest extends TestLocalController {

    @Test(groups = {"Sanity_MYT, Sanity_Fusion"})
    public void sanity_Orders_AllRegions() {
        try {
            author_ScenarioName("Gayathri,Srikrishna", "Sanity Check for "+tEnv().getApiCountry()+" to check if the build fulfills the acceptance criteria");

            logScenario("Checking Login");
            android().findElement(By.xpath("//div[@class='Test']"));

        } catch (Exception e) {
            hardFail(e.getCause().getMessage());
            e.printStackTrace();
        } finally {
            testTearDown();
        }
    }
}

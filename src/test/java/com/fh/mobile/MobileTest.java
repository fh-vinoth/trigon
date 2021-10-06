package com.fh.mobile;

import com.fh.core.TestLocalController;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Map;

public class MobileTest extends TestLocalController {

    @Test(groups = {"Sanity_MYT, Sanity_Fusion"})
    public void sanity_Orders_AllRegions() {
        try {
            author_ScenarioName("Gayathri,Srikrishna", "Sanity Check for "+tEnv().getApiCountry()+" to check if the build fulfills the acceptance criteria");

            logScenario("Checking Login");
            android().findElement(By.xpath("//div[@class='Test']"));

        } catch (Exception e) {
            hardFail(e);
        } finally {
            testTearDown();
        }
    }
}

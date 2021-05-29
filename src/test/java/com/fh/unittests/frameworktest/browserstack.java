package com.fh.unittests.frameworktest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class browserstack {
    // Sample test in Java to run Automate session.

    public static final String AUTOMATE_USERNAME = "touchsuccess1";
    public static final String AUTOMATE_ACCESS_KEY = "UjBRLss9ATaTCeaHwtdc";
    public static final String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static void main(String[] args) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os_version", "Catalina");
        caps.setCapability("resolution", "1024x768");
        caps.setCapability("browser", "chrome");
        caps.setCapability("browser_version", "84.0");
        caps.setCapability("os", "OS X");
        WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
        driver.get("https://www.google.com");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("BrowserStack");
        element.submit();
        System.out.println(driver.getTitle());
        driver.quit();
    }
}

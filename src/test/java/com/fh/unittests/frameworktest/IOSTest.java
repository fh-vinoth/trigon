package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class IOSTest extends TestLocalController {

    @Test
    public void test() throws Exception {


        WebElement textButton = ios().findElement(AppiumBy.accessibilityId("Text Button"));
        textButton.click();
        WebElement textInput = ios().findElement(AppiumBy.accessibilityId("Text Input"));
        textInput.sendKeys("hello@browserstack.com"+"\n");

        Thread.sleep(5000);

        WebElement textOutput = ios().findElement(AppiumBy.accessibilityId("Text Output"));

        Assert.assertEquals(textOutput.getText(),"hello@browserstack.com");
    }
}

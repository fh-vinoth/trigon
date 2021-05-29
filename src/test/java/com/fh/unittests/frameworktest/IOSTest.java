package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import io.appium.java_client.ios.IOSElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class IOSTest extends TestLocalController {

    @Test
    public void test() throws Exception {


        IOSElement textButton = ios().findElementByAccessibilityId("Text Button");
        textButton.click();
        IOSElement textInput = ios().findElementByAccessibilityId("Text Input");
        textInput.sendKeys("hello@browserstack.com"+"\n");

        Thread.sleep(5000);

        IOSElement textOutput = ios().findElementByAccessibilityId("Text Output");

        Assert.assertEquals(textOutput.getText(),"hello@browserstack.com");
    }
}

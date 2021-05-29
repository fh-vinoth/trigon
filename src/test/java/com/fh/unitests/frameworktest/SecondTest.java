package com.fh.unitests.frameworktest;

import com.trigon.testbase.TestController;
import com.trigon.testbase.TestUtilities;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ONUR on 03.12.2016.
 */
public class SecondTest extends TestController {

    @Test
    public void GOOGLE4() {
        System.out.println("Google4 Test Started! " + "Thread Id: " + Thread.currentThread().getId());
        TestUtilities.browser().navigate().to("http://www.google.com");
        System.out.println("Google4 Test's Page title is: " + TestUtilities.browser().getTitle() + " " + "Thread Id: " + Thread.currentThread().getId());
        Assert.assertEquals(TestUtilities.browser().getTitle(), "Google");
        System.out.println("Google4 Test Ended! " + "Thread Id: " + Thread.currentThread().getId());
    }

    @Test
    public void BHASKAR() {
        System.out.println("Yandex Test Started! " + "Thread Id: " + Thread.currentThread().getId());
        TestUtilities.browser().navigate().to("http://www.bhaskar.com");
        System.out.println("Yandex Test's Page title is: " + TestUtilities.browser().getTitle() + " " + "Thread Id: " + Thread.currentThread().getId());
        Assert.assertEquals(TestUtilities.browser().getTitle(), "Yandex");
        System.out.println("Yandex Test Ended! " + "Thread Id: " + Thread.currentThread().getId());
    }
}
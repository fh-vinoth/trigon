package com.fh.unitests.frameworktest;

import com.trigon.testbase.TestController;
import com.trigon.testbase.TestUtilities;
import org.testng.Assert;
import org.testng.annotations.Test;


public class FirstTest extends TestController {

    @Test
    public void GOOGLE1() {
        System.out.println("Google1 Test Started! " + "Thread Id: " + Thread.currentThread().getId());
        TestUtilities.browser().navigate().to("http://www.google.com");
        System.out.println("Google1 Test's Page title is: " + TestUtilities.browser().getTitle() + " " + "Thread Id: " + Thread.currentThread().getId());
        Assert.assertEquals(TestUtilities.browser().getTitle(), "Google");
        System.out.println("Google1 Test Ended! " + "Thread Id: " + Thread.currentThread().getId());

    }

    @Test
    public void GOOGLE2() {
        System.out.println("Google2 Test Started! " + "Thread Id: " + Thread.currentThread().getId());
        TestUtilities.browser().navigate().to("http://www.google.com");
        System.out.println("Google2 Test's Page title is: " + TestUtilities.browser().getTitle() + " " + "Thread Id: " + Thread.currentThread().getId());
        Assert.assertEquals(TestUtilities.browser().getTitle(), "Google");
        System.out.println("Google2 Test Ended! " + "Thread Id: " + Thread.currentThread().getId());
    }

    @Test
    public void GOOGLE3() {
        System.out.println("Google3 Test Started! " + "Thread Id: " + Thread.currentThread().getId());
        TestUtilities.browser().navigate().to("http://www.google.com");
        System.out.println("Google3 Test's Page title is: " + TestUtilities.browser().getTitle() + " " + "Thread Id: " + Thread.currentThread().getId());
        Assert.assertEquals(TestUtilities.browser().getTitle(), "Google");
        System.out.println("Google3 Test Ended! " + "Thread Id: " + Thread.currentThread().getId());
    }
}
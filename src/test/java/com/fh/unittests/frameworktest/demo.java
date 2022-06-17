package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class demo extends TestLocalController {

    @Test
    public void add() {
        try {
//                Boolean pages;
            int count = 1;
            do {
                List<WebElement> main1 =
                        browser().findElements(By.xpath("//*[@id='search']//parent::h3//parent::a"));

                for (WebElement a : main1) {

                    String b = a.getAttribute("href");
                    if (b.startsWith("https")) {
                        System.out.println("ssl certified   " + b);
                    } else {
                        System.out.println("ssl not certified   " + b);
                    }

                }
                browser().findElement(By.linkText("Next")).click();
                count = count + 1;
                System.out.println("google page " + count);
            }
            while
            (browser().findElement(By.linkText("Next")).isDisplayed());
            List<WebElement> main1 =
                    browser().findElements(By.xpath("//*[@id='search']//parent::h3//parent::a"));

            for (WebElement a : main1) {

                String b = a.getAttribute("href");
                if (b.startsWith("https")) {
                    System.out.println("ssl certified   " + b);
                } else {
                    System.out.println("ssl not certified   " + b);
                }
            }
//

        } catch (Exception e) {


        } finally {
            hardWait(5000);
        }
    }
}
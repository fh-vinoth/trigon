package com.trigon.elements;

import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface IElementStrategy {

    static void staticMethod() {
        System.out.println("It is a static method");
    }

    default void defaultMethod() {
        System.out.println("It is a default method");
    }

    AndroidElement getAndroidElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues);

    WebElement getWebElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues);

    IOSElement getIOSElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues);

    List<AndroidElement> getAndroidElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues);

    List<WebElement> getWebElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues);

    List<IOSElement> getIOSElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues);

}

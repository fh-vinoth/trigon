package com.trigon.elements;


import com.trigon.constants.Message;
import com.trigon.exceptions.RetryOnException;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


public class ElementStrategyImpl extends TestModelCore implements IElementStrategy {
    private static final Logger logger = LogManager.getLogger(ElementStrategyImpl.class);

    @Override
    public AndroidElement getAndroidElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        AndroidElement result = null;
        try{
            if (android() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "accessibilityid":
                                result = android().findElementByAccessibilityId(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "id":
                                result = android().findElementById(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "uiautomator":
                                result = android().findElementByAndroidUIAutomator(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "name":
                                result = android().findElementByName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = android().findElementByXPath(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "datamatcher":
                                result = android().findElementByAndroidDataMatcher(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "classname":
                                result = android().findElementByClassName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "viewtag":
                                result = android().findElementByAndroidViewTag(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "image":
                                result = android().findElementByImage(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "custom":
                                result = android().findElementByCustom(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = android().findElementByPartialLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = android().findElementByLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = android().findElementByTagName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "css":
                                result = android().findElementByCssSelector(locatorArr[1]);
                                elementStatus = false;
                                break;

                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) == 0) {
                            logger.warn(Message.ELEMENT_WAIT_0);
                            elementStatus = false;
                        } else {
                            if (scrollDownCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(0.6, 0.3, 0.5);
                            }
                            if (scrollUpCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(-0.6, -0.3, -0.5);
                            }
                            if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                                if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            } else {
                                if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            }
                        }
                    }

                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus)) {
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }

    @Override
    public WebElement getWebElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;
        try{
            if (browser() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "name":
                                result = browser().findElement(By.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = browser().findElement(By.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = browser().findElement(By.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = browser().findElement(By.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = browser().findElement(By.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = browser().findElement(By.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = browser().findElement(By.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = browser().findElement(By.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException |NoClassDefFoundError e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) == 0) {
                            logger.warn(Message.ELEMENT_WAIT_0);
                            break;
                        } else {
                            //scrollToElement(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                            if (scrollDownCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                JavascriptExecutor js = (JavascriptExecutor) browser();
                                js.executeScript("window.scrollBy(0,450)", "");
                            }
                            if (scrollUpCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                JavascriptExecutor js = (JavascriptExecutor) browser();
                                js.executeScript("window.scrollBy(0,-450)", "");
                            }
                            if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                                if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            } else {
                                if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            }
                        }
                    }

                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus)) {
                    logger.error(Message.ELEMENT_NOT_FOUND);
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }

    @Override
    public IOSElement getIOSElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        IOSElement result = null;
        try{
            if (ios() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "predicate":
                                result = ios().findElementByIosNsPredicate(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "classchain":
                                result = ios().findElementByIosClassChain(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "accessibilityid":
                                result = ios().findElementByAccessibilityId(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "name":
                                result = ios().findElementByName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "image":
                                result = ios().findElementByImage(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "custom":
                                result = ios().findElementByCustom(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = ios().findElementByXPath(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "classname":
                                result = ios().findElementByClassName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = ios().findElementByPartialLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = ios().findElementByLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = ios().findElementByTagName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "css":
                                result = ios().findElementByCssSelector(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "id":
                                result = ios().findElementById(locatorArr[1]);
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) == 0) {
                            logger.warn(Message.ELEMENT_WAIT_0);
                            elementStatus = false;
                        } else {
                            if (scrollDownCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(0.6, 0.3, 0.5);
                            }
                            if (scrollUpCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(-0.6, -0.3, -0.5);
                            }
                            if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                                if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            } else {
                                if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            }
                        }
                    }
                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus)) {
                    logger.error(Message.ELEMENT_NOT_FOUND);
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }

    @Override
    public List<AndroidElement> getAndroidElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        List<AndroidElement> result = new ArrayList<>();
        try{
            if (android() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "datamatcher":
                                result = android().findElementsByAndroidDataMatcher(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "uiautomator":
                                result = android().findElementsByAndroidUIAutomator(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "viewtag":
                                result = android().findElementsByAndroidViewTag(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "accessibilityid":
                                result = android().findElementsByAccessibilityId(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "name":
                                result = android().findElementsByName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "image":
                                result = android().findElementsByImage(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "custom":
                                result = android().findElementsByCustom(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = android().findElementsByXPath(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "classname":
                                result = android().findElementsByClassName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = android().findElementsByPartialLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = android().findElementsByLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = android().findElementsByTagName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "css":
                                result = android().findElementsByCssSelector(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "id":
                                result = android().findElementsById(locatorArr[1]);
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) == 0) {
                            logger.warn(Message.ELEMENT_WAIT_0);
                            elementStatus = false;
                        } else {
                            if (scrollDownCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(0.6, 0.3, 0.5);
                            }
                            if (scrollUpCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(-0.6, -0.3, -0.5);
                            }
                            if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                                if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            } else {
                                if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            }
                        }
                    }

                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus)) {
                    logger.error(Message.ELEMENT_NOT_FOUND);
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }

    @Override
    public List<WebElement> getWebElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        List<WebElement> result = new ArrayList<>();
        try{
            if (browser() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "name":
                                result = browser().findElements(By.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = browser().findElements(By.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = browser().findElements(By.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = browser().findElements(By.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = browser().findElements(By.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = browser().findElements(By.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = browser().findElements(By.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = browser().findElements(By.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            default:

                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) == 0) {
                            logger.warn(Message.ELEMENT_WAIT_0);
                            elementStatus = false;
                        } else {
                            //scrollToElement(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                            if (scrollDownCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                JavascriptExecutor js = (JavascriptExecutor) browser();
                                js.executeScript("window.scrollBy(0,450)", "");
                            }
                            if (scrollUpCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                JavascriptExecutor js = (JavascriptExecutor) browser();
                                js.executeScript("window.scrollBy(0,-450)", "");
                            }
                            if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                                if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            } else {
                                if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            }
                        }
                    }

                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus)) {
                    logger.error(Message.ELEMENT_NOT_FOUND);
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }

    @Override
    public List<IOSElement> getIOSElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        List<IOSElement> result = new ArrayList<>();
        try{
            if (ios() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "predicate":
                                result = ios().findElementsByIosNsPredicate(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "classchain":
                                result = ios().findElementsByIosClassChain(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "accessibilityid":
                                result = ios().findElementsByAccessibilityId(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "name":
                                result = ios().findElementsByName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "image":
                                result = ios().findElementsByImage(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "custom":
                                result = ios().findElementsByCustom(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = ios().findElementsByXPath(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "classname":
                                result = ios().findElementsByClassName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = ios().findElementsByPartialLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = ios().findElementsByLinkText(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = ios().findElementsByTagName(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "css":
                                result = ios().findElementsByCssSelector(locatorArr[1]);
                                elementStatus = false;
                                break;
                            case "id":
                                result = ios().findElementsById(locatorArr[1]);
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) == 0) {
                            logger.warn(Message.ELEMENT_WAIT_0);
                            elementStatus = false;
                        } else {
                            if (scrollDownCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(0.6, 0.3, 0.5);
                            }
                            if (scrollUpCheck(wait_logReport_isPresent_Up_Down_XpathValues)) {
                                verticalSwipe(-0.6, -0.3, -0.5);
                            }
                            if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                                if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            } else {
                                if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                    elementStatus = false;
                                }
                            }
                        }
                    }

                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus)) {
                    logger.error(Message.ELEMENT_NOT_FOUND);
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }

}

package com.trigon.elements;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.ElementRepoPojo;
import com.trigon.constants.Message;
import com.trigon.exceptions.RetryOnException;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class ElementStrategyImpl extends TestModelCore implements IElementStrategy {
    private static final Logger logger = LogManager.getLogger(ElementStrategyImpl.class);

    public WebElement getAndroidElement(String locatorString, String action, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;
        List<String> locatorFallbacks = new ArrayList<>();
        String locatorsFromJSON = null;
        try{
            if (android() != null) {
                RetryOnException retryHandler = new RetryOnException();
                RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                if (locatorArr[1].contains("{")) {
                    locatorsFromJSON = locatorArr[1];
                    locatorFallbacks = Arrays.stream(StringUtils.substringsBetween(locatorArr[1], "{", "}")).collect(Collectors.toList());
                    locatorArr[1] = locatorFallbacks.get(0);
                    locatorFallbacks.remove(0);
                } else {
                    locatorsFromJSON = locatorArr[1];
                }
                long startTime5 = System.currentTimeMillis();
                locatorArr[0] = locatorArr[0].toLowerCase().trim();
                logger.info("Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                boolean elementStatus = true;
                while (elementStatus) {
                    try {
                        switch (locatorArr[0]) {
                            case "accessibilityid":
                                result = android().findElement(AppiumBy.accessibilityId(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = android().findElement(AppiumBy.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "uiautomator":
                                result = android().findElement(AppiumBy.androidUIAutomator((locatorArr[1])));
                                elementStatus = false;
                                break;
                            case "name":
                                result = android().findElement(AppiumBy.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = android().findElement(AppiumBy.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "datamatcher":
                                result = android().findElement(AppiumBy.androidDataMatcher(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = android().findElement(By.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "viewtag":
                                result = android().findElement(AppiumBy.androidViewTag(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "image":
                                result = android().findElement(AppiumBy.image(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "custom":
                                result = android().findElement(AppiumBy.custom(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = android().findElement(AppiumBy.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = android().findElement(AppiumBy.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = android().findElement(By.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = android().findElement(By.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;

                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        elementStatus = isElementStatus(locatorString, retryHandler, retryHandler1, elementStatus, wait_logReport_isPresent_Up_Down_XpathValues);
                    }

                }
                long endTime5 = System.currentTimeMillis();
                logger.info(Message.TIME_TAKEN_TO_IDENTIFY_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
                if ((result == null) && (!isPresentStatus) ) {
                    if(!locatorArr[1].contains("$XpathValue$") && !Arrays.stream(wait_logReport_isPresent_Up_Down_XpathValues).anyMatch(key -> key.equals("isPresent"))) {
                        String newLocatorFallbacks = "";
                        logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                        System.out.println("The Primary LOCATOR ELEMENT IS NOT FOUND.");
                        if (locatorFallbacks.size() > 0) {
                            System.out.println("Trying Other existing fallbacks available");
                            List<String> removeLocators = new ArrayList<>();
                            for (String xpath : locatorFallbacks) {
                                try {
                                    result = browser().findElement(By.xpath(xpath));
                                    System.out.println("Existing Fallback xpath " + xpath + " used to locate element.");
                                    locatorFallbacks.removeAll(removeLocators);
                                    //rewrite JSON
                                    if (locatorFallbacks.size() == 1) {
                                        newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorFallbacks.get(0)).concat("}");
                                    } else if (locatorFallbacks.size() > 1) {
                                        for (int i = 0; i < locatorFallbacks.size(); i++) {
                                            if (i == locatorFallbacks.size() - 1) {
                                                newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorFallbacks.get(i)).concat("}");
                                            } else {
                                                newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorFallbacks.get(i)).concat("}, ");
                                            }
                                        }
                                    }
                                    locatorStringRemoveInJSON(locatorString, newLocatorFallbacks);
                                    return result;
                                } catch (Exception exception) {
                                    removeLocators.add(xpath);
                                    System.out.println("Existing Fallback xpath " + xpath + " DID NOT work. Looking for next Fallback to locate element.");
                                }
                            }
                            //locatorStringRemoveInJSON(locatorString, newLocatorFallbacks);
                            //Presence of a fallback definitely means it has BEFORE AFTER elements fetched during the Previous Self healing mechanism. Thus follows
                            result = beforeAfterElementCheck(locatorString, locatorsFromJSON, action, wait_logReport_isPresent_Up_Down_XpathValues);
                        } else {
                            //No fallbacks meaning there is no need to check fallback but needs Before and After element check if Present.
                            System.out.println("NO Fallbacks available to locate element.");
                            // locatorStringRemoveInJSON(locatorString, newLocatorFallbacks);
                            result = beforeAfterElementCheck(locatorString, locatorsFromJSON, action, wait_logReport_isPresent_Up_Down_XpathValues);
                        }
                    }
                    else if(locatorArr[1].contains("$XpathValue$")) {
                        System.out.println("Healing NOT DONE for Dynamic locators containing XpathValue. -"+locatorString);
                        hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    }
                }
            }
            if ((result == null) && (!isPresentStatus) && !Arrays.stream(wait_logReport_isPresent_Up_Down_XpathValues).anyMatch(key -> key.equals("isPresent"))) {
                System.out.println("Healing Xpaths NOT FOUND for the element through healing process.");
                hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
            }
        }catch (Exception e){
            captureException(e);
        }
        return result;
    }
    @Override
    public WebElement getAndroidElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;
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
                                result = android().findElement(AppiumBy.accessibilityId(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = android().findElement(AppiumBy.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "uiautomator":
                                result = android().findElement(AppiumBy.androidUIAutomator((locatorArr[1])));
                                elementStatus = false;
                                break;
                            case "name":
                                result = android().findElement(AppiumBy.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = android().findElement(AppiumBy.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "datamatcher":
                                result = android().findElement(AppiumBy.androidDataMatcher(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = android().findElement(By.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "viewtag":
                                result = android().findElement(AppiumBy.androidViewTag(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "image":
                                result = android().findElement(AppiumBy.image(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "custom":
                                result = android().findElement(AppiumBy.custom(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = android().findElement(AppiumBy.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = android().findElement(AppiumBy.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = android().findElement(By.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = android().findElement(By.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;

                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        elementStatus = isElementStatus(locatorString, retryHandler, retryHandler1, elementStatus, wait_logReport_isPresent_Up_Down_XpathValues);
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
    public WebElement getWebElement(String locatorString, String action, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;
        List<String> locatorFallbacks = new ArrayList<>();
        String locatorsFromJSON = null;
                try {
                    if (browser() != null) {
                        RetryOnException retryHandler = new RetryOnException();
                        RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
                        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                        if (locatorArr[1].contains("{")) {
                            locatorsFromJSON = locatorArr[1];
                            locatorFallbacks = Arrays.stream(StringUtils.substringsBetween(locatorArr[1], "{", "}")).collect(Collectors.toList());
                            locatorArr[1] = locatorFallbacks.get(0);
                            locatorFallbacks.remove(0);
                        } else {
                            locatorsFromJSON = locatorArr[1];
                        }
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
                            } catch (WebDriverException | NoClassDefFoundError e) {
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
                            if (!locatorArr[1].contains("$XpathValue$")) {
                                String newLocatorFallbacks = "";
                                logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                                System.out.println("The Primary LOCATOR ELEMENT IS NOT FOUND.");
                                if (locatorFallbacks.size() > 0) {
                                    System.out.println("Trying Other existing fallbacks available");
                                    List<String> removeLocators = new ArrayList<>();
                                    for (String xpath : locatorFallbacks) {
                                        try {
                                            result = browser().findElement(By.xpath(xpath));
                                            System.out.println("Existing Fallback xpath " + xpath + " used to locate element.");
                                            locatorFallbacks.removeAll(removeLocators);
                                            //rewrite JSON
                                            if (locatorFallbacks.size() == 1) {
                                                newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorFallbacks.get(0)).concat("}");
                                            } else if (locatorFallbacks.size() > 1) {
                                                for (int i = 0; i < locatorFallbacks.size(); i++) {
                                                    if (i == locatorFallbacks.size() - 1) {
                                                        newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorFallbacks.get(i)).concat("}");
                                                    } else {
                                                        newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorFallbacks.get(i)).concat("}, ");
                                                    }
                                                }
                                            }
                                            locatorStringRemoveInJSON(locatorString, newLocatorFallbacks);
                                            return result;
                                        } catch (Exception exception) {
                                            removeLocators.add(xpath);
                                            System.out.println("Existing Fallback xpath " + xpath + " DID NOT work. Looking for next Fallback to locate element.");
                                        }
                                    }
                                    //locatorStringRemoveInJSON(locatorString, newLocatorFallbacks);
                                    //Presence of a fallback definitely means it has BEFORE AFTER elements fetched during the Previous Self healing mechanism. Thus follows
                                    result = beforeAfterElementCheck(locatorString, locatorsFromJSON, action, wait_logReport_isPresent_Up_Down_XpathValues);
                                } else {
                                    //No fallbacks meaning there is no need to check fallback but needs Before and After element check if Present.
                                    System.out.println("NO Fallbacks available to locate element.");
                                    // locatorStringRemoveInJSON(locatorString, newLocatorFallbacks);
                                    result = beforeAfterElementCheck(locatorString, locatorsFromJSON, action, wait_logReport_isPresent_Up_Down_XpathValues);
                                }
                            } else if (locatorArr[1].contains("$XpathValue$")) {
                                System.out.println("Healing NOT DONE for Dynamic locators containing XpathValue. -" + locatorString);
                                hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                            }
                        }
                        if ((result == null) && (!isPresentStatus)) {
                            System.out.println("Healing Xpaths NOT FOUND for the element through healing process.");
                            hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                        }
                    }
                } catch (Exception e) {
                    captureException(e);
                }
        return result;
    }

    public WebElement getWebElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;

        try {
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
                    logReport("WARN", "Performing " + locatorString + " : " + locatorArr[0] + "=" + locatorArr[1] + " JSON File : " + tEnv().getPagesJsonFile());
                    hardFail(Message.ELEMENT_NOT_FOUND, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return result;
    }

    private WebElement selfHealValidation(String locatorString, String action, String[] wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;
        System.out.println("ELEMENT NOT FOUND THROUGH ANY EXISTING LOCATORS- Hence Trying to find the best matched locator through Self Healing.");
        String locator = locatorString(locatorString);
        String compareString = "";
        if(locator.contains("accessibilityid=")){
            compareString = StringUtils.substringAfter(locator,"=");
        } else {
            compareString = !(StringUtils.substringBetween(locator, "='", "'").isEmpty())
                    ? StringUtils.substringBetween(locator, "='", "'")
                    : StringUtils.substringBetween(locator, "=\"", "\"") ;
        }
        if(compareString!=null && !compareString.isEmpty()) {
            List<String> nameSplit = Arrays.stream(compareString.replaceAll("[^A-Za-z0-9]", " ").trim().split(" ")).toList();
            compareString ="";
            for (String name : nameSplit) {
                if (name.isEmpty()) {
                    continue;
                } else {
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    compareString = compareString.concat(name);
                }
            }
            compareString = StringUtils.uncapitalize(compareString);
            String fallbacks = selfHeal(locatorString, compareString, action);
            if (fallbacks != null || !fallbacks.equals("") || !fallbacks.isEmpty()) {
                List<String> selfhealXpaths = Arrays.stream(StringUtils.substringsBetween(fallbacks, "{", "}")).collect(Collectors.toList());
                for (String selfHealxpath : selfhealXpaths) {
                    try {
                        if (tEnv().getElementLocator().equalsIgnoreCase("Web")) {
                            result = browser().findElement(By.xpath(selfHealxpath));
                        } else if (tEnv().getElementLocator().equalsIgnoreCase("Android")) {
                            result = android().findElement(By.xpath(selfHealxpath));
                        }
                        System.out.println("New heal Xpath {" + selfHealxpath + "} is used. .");
                        return result;
                    } catch (Exception ex) {
                        System.out.println("New heal Xpath {" + selfHealxpath + "} was found to be incorrect.");
                    }
                }
            } else {
                System.out.println("Healing Xpaths NOT FOUND.");
            }
        }
        return result;
    }

    private WebElement beforeAfterElementCheck(String locatorString, String locatorsFromJSON, String action, String[] wait_logReport_isPresent_Up_Down_XpathValues){
        String beforeLocator = fetchBeforeAfterLocators(locatorString)[0];
        String afterLocator = fetchBeforeAfterLocators(locatorString)[1];
        if (!beforeLocator.isEmpty()) {
            try {
                WebElement currentUsingBefore = browser().findElement(By.xpath(beforeLocator + "//following::" + StringUtils.substringBetween(beforeLocator, "//", "=") + "]"));
                System.out.println("Existing BEFORE xpath " + beforeLocator + " used to locate element.");
                return currentUsingBefore;
            } catch (Exception incorrectBefore) {
                if (!afterLocator.isEmpty()) {
                    List<WebElement> beforeElements = browser().findElements(By.xpath(afterLocator + "//preceding::" + StringUtils.substringBetween(afterLocator, "//", "=") + "]"));
                    if(beforeElements.size()>1) {
                        WebElement currentUsingAfter = beforeElements.get(beforeElements.size() - 1);
                        System.out.println("Existing AFTER xpath " + afterLocator + " used to locate element.");
                        return currentUsingAfter;
                    }
                    if(beforeElements.size()==1) {
                        WebElement currentUsingAfter = beforeElements.get(0);
                        System.out.println("Existing AFTER xpath " + afterLocator + " used to locate element.");
                        return currentUsingAfter;
                    }
                    if(beforeElements.size()==0) {
                        System.out.println("Self Healing. . .");
                        return selfHealValidation(locatorString, action, wait_logReport_isPresent_Up_Down_XpathValues);
                    }
                }
            }
        }
        if (!afterLocator.isEmpty()) {
            List<WebElement> beforeElements = browser().findElements(By.xpath(afterLocator + "//preceding::" + StringUtils.substringBetween(afterLocator, "//", "=") + "]"));
            if(beforeElements.size()>1) {
                WebElement currentUsingAfter = beforeElements.get(beforeElements.size() - 1);
                System.out.println("Existing AFTER xpath " + afterLocator + " used to locate element.");
                return currentUsingAfter;
            }
            if(beforeElements.size()==1) {
                WebElement currentUsingAfter = beforeElements.get(0);
                System.out.println("Existing AFTER xpath " + afterLocator + " used to locate element.");
                return currentUsingAfter;
            }
            if(beforeElements.size()==0) {
                System.out.println("Incorrect BEFORE or AFTER element. Hence finding the New best Match locator through Self Healing process.");
                return selfHealValidation(locatorString, action, wait_logReport_isPresent_Up_Down_XpathValues);
            }
        }
        if (beforeLocator.isEmpty() && afterLocator.isEmpty()) {
            System.out.println("No BEFORE OR AFTER elements available. Hence finding the New best Match locator through Self Healing process.");
            return selfHealValidation(locatorString, action, wait_logReport_isPresent_Up_Down_XpathValues);
        }

        return null;
    }

    @Override
    public WebElement getIOSElement(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        WebElement result = null;
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
                                result = ios().findElement(AppiumBy.iOSNsPredicateString(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classchain":
                                result = ios().findElement(AppiumBy.iOSClassChain(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "accessibilityid":
                                result = ios().findElement(AppiumBy.accessibilityId(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "name":
                                result = ios().findElement(AppiumBy.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "image":
                                result = ios().findElement(AppiumBy.image(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "custom":
                                result = ios().findElement(AppiumBy.custom(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = ios().findElement(AppiumBy.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = ios().findElement(AppiumBy.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = ios().findElement(AppiumBy.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = ios().findElement(AppiumBy.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = ios().findElement(AppiumBy.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = ios().findElement(AppiumBy.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = ios().findElement(AppiumBy.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        elementStatus = isElementStatus(locatorString, retryHandler, retryHandler1, elementStatus, wait_logReport_isPresent_Up_Down_XpathValues);
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
    public List<WebElement> getAndroidElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        List<WebElement> result = new ArrayList<>();
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
                                result = android().findElements(AppiumBy.androidDataMatcher(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "uiautomator":
                                result = android().findElements(AppiumBy.androidUIAutomator(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "viewtag":
                                result = android().findElements(AppiumBy.androidViewTag(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "accessibilityid":
                                result = android().findElements(AppiumBy.accessibilityId(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "name":
                                result = android().findElements(AppiumBy.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "image":
                                result = android().findElements(AppiumBy.image(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "custom":
                                result = android().findElements(AppiumBy.custom(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = android().findElements(AppiumBy.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = android().findElements(AppiumBy.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = android().findElements(AppiumBy.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = android().findElements(AppiumBy.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = android().findElements(AppiumBy.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = android().findElements(AppiumBy.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = android().findElements(AppiumBy.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        elementStatus = isElementStatus(locatorString, retryHandler, retryHandler1, elementStatus, wait_logReport_isPresent_Up_Down_XpathValues);
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
    public List<WebElement> getIOSElements(String locatorString, boolean isPresentStatus, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        List<WebElement> result = new ArrayList<>();
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
                                result = ios().findElements(AppiumBy.iOSNsPredicateString(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classchain":
                                result = ios().findElements(AppiumBy.iOSClassChain(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "accessibilityid":
                                result = ios().findElements(AppiumBy.accessibilityId(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "name":
                                result = ios().findElements(AppiumBy.name(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "image":
                                result = ios().findElements(AppiumBy.image(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "custom":
                                result = ios().findElements(AppiumBy.custom(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "xpath":
                                result = ios().findElements(AppiumBy.xpath(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "classname":
                                result = ios().findElements(AppiumBy.className(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "partiallinktext":
                                result = ios().findElements(AppiumBy.partialLinkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "linktext":
                                result = ios().findElements(AppiumBy.linkText(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "tagname":
                                result = ios().findElements(AppiumBy.tagName(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "css":
                                result = ios().findElements(AppiumBy.cssSelector(locatorArr[1]));
                                elementStatus = false;
                                break;
                            case "id":
                                result = ios().findElements(AppiumBy.id(locatorArr[1]));
                                elementStatus = false;
                                break;
                            default:
                                logger.error(Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                logReport("FAIL", Message.LOCATOR_NOT_AVAILABLE + locatorArr[0]);
                                elementStatus = false;
                                break;
                        }
                    } catch (WebDriverException e) {
                        elementStatus = isElementStatus(locatorString, retryHandler, retryHandler1, elementStatus, wait_logReport_isPresent_Up_Down_XpathValues);
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

    private boolean isElementStatus(String locatorString, RetryOnException retryHandler, RetryOnException retryHandler1, boolean elementStatus, String[] wait_logReport_isPresent_Up_Down_XpathValues) {
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
        return elementStatus;
    }

}

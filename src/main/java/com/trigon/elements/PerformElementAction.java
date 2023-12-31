package com.trigon.elements;

import com.trigon.constants.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PerformElementAction extends ElementStrategyImpl {
    private static final Logger logger = LogManager.getLogger(PerformElementAction.class);

    protected String performElementAction(String locatorString, String action, String getElementValue, String... wait_logReport_isPresent_Up_Down_XpathValues) {

        String returnvalue = null;
        long startTime5 = System.currentTimeMillis();
        WebElement iOSElement = getIOSElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        WebElement androidElement = getAndroidElement(locatorString, action,false, wait_logReport_isPresent_Up_Down_XpathValues);
        WebElement webElement = getWebElement(locatorString, action,false, wait_logReport_isPresent_Up_Down_XpathValues);
        String navigation = Message.NAVIGATED_TO_PAGE + Thread.currentThread().getStackTrace()[3].getFileName().replace(".java", "");

        switch (action) {
            case "click":
                try {
                    if (webElement != null) {
                        try {
                            if (webElement.isEnabled()) {
                                browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                                WebDriverWait wait = new WebDriverWait(browser(), Duration.ofSeconds(5));
                                wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                webElement.click();
                            }
                        } catch (WebDriverException e) {
                            try {
                                WebDriverWait wait = new WebDriverWait(browser(), Duration.ofSeconds(5));
                                wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                browser().executeScript("arguments[0].click();", webElement);
                                browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                            } catch (Exception e3) {
                                try {
                                    logReport("INFO", "Element NOT intractable Hence Scrolling" + locatorString);
                                    logger.error("Element NOT intractable Hence Scrolling...");
                                    scrollToElement(webElement);
                                    hardWait(1000);
                                    if (webElement.isEnabled()) {
                                        browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                                        WebDriverWait wait = new WebDriverWait(browser(), Duration.ofSeconds(5));
                                        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                        webElement.click();
                                    }
                                } catch (WebDriverException e1) {
                                    for (int b = 0; b < 10; b++) {
                                        try {
                                            JavascriptExecutor js = browser();
                                            js.executeScript("window.scrollBy(0,-450)", "");
                                            if (webElement.isEnabled()) {
                                                browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                                                WebDriverWait wait = new WebDriverWait(browser(), Duration.ofSeconds(5));
                                                wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                                webElement.click();
                                            }
                                            break;
                                        } catch (WebDriverException e2) {

                                        }
                                    }
                                }
                            }

                        }
                        browser().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                        browser().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                        logReportWithScreenShot("PASS",
                                navigation + Message.CLICKED + locatorString);
                    }
                    if (androidElement != null) {
                        WebDriverWait wait = new WebDriverWait(android(), Duration.ofSeconds(5));
                        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(androidElement)));
                        androidElement.click();
                        logReportWithScreenShot("PASS",
                                navigation + Message.CLICKED + locatorString);
                    }
                    if (iOSElement != null) {
                        WebDriverWait wait = new WebDriverWait(ios(), Duration.ofSeconds(5));
                        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(iOSElement)));
                        iOSElement.click();
                        logReportWithScreenShot("PASS",
                                navigation + Message.CLICKED + locatorString);
                    }

                } catch (WebDriverException e) {
                    hardFail(navigation + Message.ELEMENT_NOT_INTRACTABLE_CLICK, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
                break;

            case "getAttribute":
                try {
                    if (webElement != null) {
                        returnvalue = webElement.getAttribute(getElementValue);
                    }
                    if (androidElement != null) {
                        returnvalue = androidElement.getAttribute(getElementValue);
                    }
                    if (iOSElement != null) {
                        returnvalue = iOSElement.getAttribute(getElementValue);
                    }
                    logReportWithScreenShot("PASS",
                            navigation + Message.GET_ATTRIBUTE + locatorString);
                } catch (WebDriverException e) {
                    hardFail(navigation + Message.ELEMENT_NOT_INTRACTABLE_GET_ATTRIBUTE, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
                break;
            case "verifyDisplayed":
                try {
                    if (webElement != null) {
                        returnvalue = String.valueOf(webElement.isDisplayed());

                        browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                    }
                    if (androidElement != null) {
                        returnvalue = String.valueOf(androidElement.isDisplayed());
                    }
                    if (iOSElement != null) {
                        returnvalue = String.valueOf(iOSElement.isDisplayed());
                    }
                    logReportWithScreenShot("PASS",
                            navigation + Message.ELEMENT_VERIFY_DISPLAYED + locatorString);
                } catch (WebDriverException e) {
                    hardFail(navigation + Message.ELEMENT_NOT_INTRACTABLE_VERIFY_DISPLAYED, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
                break;

            case "clearText":
                try {
                    if (webElement != null) {
                        webElement.clear();
                    }
                    if (androidElement != null) {
                        androidElement.clear();
                    }
                    if (iOSElement != null) {
                        iOSElement.clear();
                    }
                    hardWait(3000);
                    logReportWithScreenShot("PASS",
                            navigation + Message.CLEAR_TEXT + locatorString);
                } catch (WebDriverException e) {
                    hardFail(navigation + Message.ELEMENT_NOT_INTRACTABLE_CLEAR_TEXT, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
                break;

            case "enterText":
                try {
                    if (webElement != null) {
                        try {
                            if (webElement.isEnabled()) {
                                WebDriverWait wait = new WebDriverWait(browser(), Duration.ofSeconds(5));
                                wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                webElement.clear();
                                webElement.sendKeys(getElementValue);
                                browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                            }
                        } catch (WebDriverException e) {
                            logReport("INFO", "Element NOT intractable Hence Scrolling" + locatorString);
                            try {
                                scrollToElement(webElement);
                                hardWait(1000);
                                if (webElement.isEnabled()) {
                                    WebDriverWait wait = new WebDriverWait(browser(), Duration.ofSeconds(5));
                                    wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                    webElement.clear();
                                    webElement.sendKeys(getElementValue);
                                }
                            } catch (WebDriverException e1) {
                                for (int b = 0; b < 10; b++) {
                                    try {
                                        JavascriptExecutor js = browser();
                                        js.executeScript("window.scrollBy(0,-450)", "");
                                        webElement.clear();
                                        webElement.sendKeys(getElementValue);
                                        break;
                                    } catch (WebDriverException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                    if (androidElement != null) {
                        WebDriverWait wait = new WebDriverWait(android(), Duration.ofSeconds(5));
                        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(androidElement)));
                        androidElement.clear();
                        androidElement.sendKeys(getElementValue);
                    }
                    if (iOSElement != null) {
                        WebDriverWait wait = new WebDriverWait(ios(), Duration.ofSeconds(5));
                        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(iOSElement)));
                        iOSElement.clear();
                        iOSElement.sendKeys(getElementValue);
                    }
                    logReportWithScreenShot("PASS",
                            navigation + "and entered " + getElementValue + "in text/input field for element : " + locatorString);
                    long endTime5 = System.currentTimeMillis();
                    logger.info(Message.TIME_TAKEN_TO_PERFORM_ACTION_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));

                    // logTimeReport("PASS","Testing Time Taken ",cUtils().getRunDuration(startTime5, endTime5));
                } catch (WebDriverException | NoClassDefFoundError e) {
                    hardFail(navigation + Message.ELEMENT_NOT_INTRACTABLE_ENTER_TEXT, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
                break;
            case "getText":
                try {
                    if (webElement != null) {
                        try {
                            returnvalue = webElement.getText();
                        } catch (WebDriverException e) {
                            scrollToElement(webElement);
                            returnvalue = webElement.getText();

                            browser().executeScript("arguments[0].style.border='4px solid red'", webElement);
                        }
                    }
                    if (androidElement != null) {
                        returnvalue = androidElement.getText();
                    }
                    if (iOSElement != null) {
                        returnvalue = iOSElement.getText();
                    }
                    logReportWithScreenShot("PASS",
                            navigation + " and captured the Text as : " + returnvalue + " for element " + locatorString);
                } catch (WebDriverException e) {
                    hardFail(navigation + Message.ELEMENT_NOT_INTRACTABLE_GET_TEXT, locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
                break;

            default:
                logger.error("Method " + action + " is not yet implemented in framework");
                logReport("FAIL", "Method " + action + " is not yet implemented in framework");
                break;

        }
        return returnvalue;
    }

    protected boolean performElementActionWithPresent(String locatorString, String action, String getElementValue, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        boolean returnvalue = false;
        WebElement iOSElement1 = getIOSElement(locatorString, true, wait_logReport_isPresent_Up_Down_XpathValues);
        WebElement androidElement1 = getAndroidElement(locatorString, true, wait_logReport_isPresent_Up_Down_XpathValues);
        WebElement webElement1 = getWebElement(locatorString, action, true, wait_logReport_isPresent_Up_Down_XpathValues);
        long startTime5 = System.currentTimeMillis();

        switch (action) {
            case "isPresent":
                if ((webElement1 != null) || (androidElement1 != null) || (iOSElement1 != null)) {
                    returnvalue = true;
                }
                break;
            case "isNotPresent":
                if ((webElement1 != null) || (androidElement1 != null) || (iOSElement1 != null)) {
                    returnvalue = true;
                    logReportWithScreenShot("FAIL",
                            "Element is Present : " + locatorString);
                } else {
                    returnvalue = false;
                    logReportWithScreenShot("PASS",
                            "Element is NOT Present : " + locatorString);
                }
                break;
            case "isNotDisplayed":
                if (webElement1 != null) {
                    try {
                        returnvalue = !(webElement1.isDisplayed());
                    } catch (NoSuchElementException e) {
                        returnvalue = true;
                    } catch (StaleElementReferenceException e) {
                        returnvalue = true;
                    }
                } else {
                    returnvalue = true;
                }
                if (androidElement1 != null) {
                    try {
                        returnvalue = !(androidElement1.isDisplayed());
                    } catch (NoSuchElementException e) {
                        returnvalue = true;
                    } catch (StaleElementReferenceException e) {
                        returnvalue = true;
                    }
                } else {
                    returnvalue = true;
                }
                if (iOSElement1 != null) {
                    try {
                        returnvalue = !(iOSElement1.isDisplayed());
                    } catch (NoSuchElementException e) {
                        returnvalue = true;
                    } catch (StaleElementReferenceException e) {
                        returnvalue = true;
                    }
                } else {
                    returnvalue = true;
                }

                if (returnvalue) {
                    logReportWithScreenShot("PASS",
                            "The element is NOT Displayed  :  " + locatorString);
                } else {
                    logReportWithScreenShot("FAIL",
                            "The element is Displayed  :  " + locatorString);
                }
                break;
            case "isEnabled":
                if (webElement1 != null) {
                    returnvalue = webElement1.isEnabled();
                }
                if (androidElement1 != null) {
                    returnvalue = androidElement1.isEnabled();
                }
                if (iOSElement1 != null) {
                    returnvalue = iOSElement1.isEnabled();
                }
                if (returnvalue) {
                    logReportWithScreenShot("PASS",
                            "The element is Enabled :  " + locatorString);
                } else {
                    logReportWithScreenShot("FAIL",
                            "The element is NOT Enabled  :  " + locatorString);
                }
                break;
            case "isSelected":
                if (webElement1 != null) {
                    returnvalue = webElement1.isSelected();
                }
                if (androidElement1 != null) {
                    returnvalue = androidElement1.isSelected();
                }
                if (iOSElement1 != null) {
                    returnvalue = iOSElement1.isSelected();
                }
                if (returnvalue) {
                    logReportWithScreenShot("PASS",
                            "The element is Selected :  " + locatorString);
                } else {
                    logReportWithScreenShot("FAIL",
                            "The element is NOT Selected  :  " + locatorString);
                }
                break;
            default:
                break;
        }
        long endTime5 = System.currentTimeMillis();
        logger.info(Message.TIME_TAKEN_TO_PERFORM_ACTION_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));

        return returnvalue;
    }

    protected List<String> performElementsAction(String locatorString, String action, String expected, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        long startTime5 = System.currentTimeMillis();
        List<String> Values = new ArrayList<>();
        startTime = System.currentTimeMillis();
        List<WebElement> iOSElements = getIOSElements(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        List<WebElement> androidElements = getAndroidElements(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        List<WebElement> webElements = getWebElements(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);

        switch (action) {
            case "lisofvalues":
                try {
                    if (webElements != null) {
                        for (WebElement tablevalues : webElements) {
                            Values.add(tablevalues.getText());
                            if (!(expected.equals("NA"))) {
                                textVerification(tablevalues.getText(), expected, textAction);
                            }
                        }
                    }
                    if (androidElements != null) {
                        for (WebElement tablevalues : androidElements) {
                            Values.add(tablevalues.getText());
                            if (!(expected.equals("NA"))) {
                                textVerification(tablevalues.getText(), expected, textAction);
                            }
                        }
                    }
                    if (iOSElements != null) {
                        for (WebElement tablevalues : iOSElements) {
                            Values.add(tablevalues.getText());
                            if (!(expected.equals("NA"))) {
                                textVerification(tablevalues.getText(), expected, textAction);
                            }
                        }
                    }
                    break;
                } catch (WebDriverException e) {
                    hardFail("Element is Visible, but NOT able to get elements ", locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                    break;
                }
            default:
                logger.error("Method " + action + " is not yet implemented in framework");
                logReport("FAIL", "Method " + action + " is not yet implemented in framework");
                break;
        }

        long endTime5 = System.currentTimeMillis();
        logger.info(Message.TIME_TAKEN_TO_PERFORM_ACTION_ELEMENT + locatorString + " : " + cUtils().getRunDuration(startTime5, endTime5));
        return Values;
    }

    public void horizontalSwipeToElement1(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        try {
            if (browser() == null) {
                WebElement element = android()!=null?getAndroidElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues):getIOSElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence scroll = new Sequence(finger, 0);
                int screenWidth =  Objects.requireNonNullElse(android(), ios()).manage().window().getSize().width;
                while (true) {
                    try {
                        int chosenElementY = element.getLocation().getY();
                        int chosenElementX = element.getLocation().getX();

                        int MoveX = screenWidth - chosenElementX;
                        int MoveY = chosenElementX + chosenElementY;

                        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), chosenElementX, chosenElementY));
                        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), MoveX, MoveY));
                        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                        Objects.requireNonNullElse(android(), ios()).perform(List.of(scroll));

                        logReportWithScreenShot("PASS",
                                "Horizontal Swiped to Element  :  " + "Pressed X Location : " + chosenElementX + "Pressed Y Location : " + chosenElementY + " Moved X Location : " + MoveX + " Moved Y Location : " + MoveY);
                        break;
                    } catch (WebDriverException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InvalidArgumentException iae) {
            logger.error("The given element visibility is beyond screen range : Make sure you have taken correct element : " + locatorString);
        }
    }

    public void hideKeyBorad() {
        try {
            if (android() != null) {
                android().hideKeyboard();

                if (android().isKeyboardShown()) {
                    android().hideKeyboard();
                }
            }
            if (ios() != null) {
                if (ios().isKeyboardShown()) {
                    ios().hideKeyboard();
                }
            }
        } catch (WebDriverException we) {
            try {
                if (ios() != null) {
                    if (ios().isKeyboardShown()) {
                        ios().hideKeyboard("Return");
                    } else {
                        logger.error("Keyboard is not visible or Application must be closed.");
                    }
                }
            } catch (WebDriverException wde) {
                hardFail("Failed to Hide Keyboard!! Application Crashed", "");
            }
        }
    }

}

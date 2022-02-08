package com.trigon.elements;

import com.trigon.constants.Message;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.touch.WaitOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.offset.PointOption.point;

public class PerformElementAction extends ElementStrategyImpl {
    private static final Logger logger = LogManager.getLogger(PerformElementAction.class);

    protected String performElementAction(String locatorString, String action, String getElementValue, String... wait_logReport_isPresent_Up_Down_XpathValues) {

        String returnvalue = null;
        long startTime5 = System.currentTimeMillis();
        IOSElement iOSElement = getIOSElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        AndroidElement androidElement = getAndroidElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        WebElement webElement = getWebElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        String navigation = Message.NAVIGATED_TO_PAGE + Thread.currentThread().getStackTrace()[3].getFileName().replace(".java", "");

        switch (action) {
            case "click":
                try {
                    if (webElement != null) {
                        try {
                            if (webElement.isEnabled()) {
                                JavascriptExecutor executor = (JavascriptExecutor) browser();
                                executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
                                webElement.click();
                            }
                        } catch (WebDriverException e) {
                            try {
                                WebDriverWait wait = new WebDriverWait(browser(), 5);
                                wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement)));
                                JavascriptExecutor executor = (JavascriptExecutor) browser();
                                executor.executeScript("arguments[0].click();", webElement);
                                executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
                            } catch (Exception e3) {
                                try {
                                    logReport("INFO", "Element NOT intractable Hence Scrolling" + locatorString);
                                    logger.error("Element NOT intractable Hence Scrolling...");
                                    scrollToElement(webElement);
                                    hardWait(1000);
                                    if (webElement.isEnabled()) {
                                        JavascriptExecutor executor = (JavascriptExecutor) browser();
                                        executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
                                        webElement.click();
                                    }
                                } catch (WebDriverException e1) {
                                    for (int b = 0; b < 10; b++) {
                                        try {
                                            JavascriptExecutor js = browser();
                                            js.executeScript("window.scrollBy(0,-450)", "");
                                            if (webElement.isEnabled()) {
                                                JavascriptExecutor executor = (JavascriptExecutor) browser();
                                                executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
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
                        androidElement.click();
                        logReportWithScreenShot("PASS",
                                navigation + Message.CLICKED + locatorString);
                    }
                    if (iOSElement != null) {
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
                        JavascriptExecutor executor = (JavascriptExecutor) browser();
                        executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
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
                                webElement.clear();
                                webElement.sendKeys(getElementValue);
                                JavascriptExecutor executor = (JavascriptExecutor) browser();
                                executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
                            }
                        } catch (WebDriverException e) {
                            logReport("INFO", "Element NOT intractable Hence Scrolling" + locatorString);
                            try {
                                scrollToElement(webElement);
                                hardWait(1000);
                                if (webElement.isEnabled()) {
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
                        androidElement.clear();
                        androidElement.sendKeys(getElementValue);
                    }
                    if (iOSElement != null) {
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
                            JavascriptExecutor executor = (JavascriptExecutor) browser();
                            executor.executeScript("arguments[0].style.border='4px solid red'", webElement);
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
        IOSElement iOSElement1 = getIOSElement(locatorString, true, wait_logReport_isPresent_Up_Down_XpathValues);
        AndroidElement androidElement1 = getAndroidElement(locatorString, true, wait_logReport_isPresent_Up_Down_XpathValues);
        WebElement webElement1 = getWebElement(locatorString, true, wait_logReport_isPresent_Up_Down_XpathValues);
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
        List<IOSElement> iOSElements = getIOSElements(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        List<AndroidElement> androidElements = getAndroidElements(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
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
                        for (AndroidElement tablevalues : androidElements) {
                            Values.add(tablevalues.getText());
                            if (!(expected.equals("NA"))) {
                                textVerification(tablevalues.getText(), expected, textAction);
                            }
                        }
                    }
                    if (iOSElements != null) {
                        for (IOSElement tablevalues : iOSElements) {
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
        AndroidElement androidElement = getAndroidElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        IOSElement iOSElement = getIOSElement(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
        try {
            if (android() != null) {
                int screenWidth = android().manage().window().getSize().width;
                TouchAction touchAction = new TouchAction(android());
                while (true) {
                    try {
                        int chosenElementY = androidElement.getLocation().getY();
                        int chosenElementX = androidElement.getLocation().getX();

                        int MoveX = screenWidth - chosenElementX;
                        int MoveY = chosenElementX + chosenElementY;

                        touchAction.press(point(chosenElementX, chosenElementY))
                                .moveTo(point(MoveX, MoveY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000)))
                                .release()
                                .perform();

                        logReportWithScreenShot("PASS",
                                "Horizontal Swiped to Element  :  " + "Pressed X Location : " + chosenElementX + "Pressed Y Location : " + chosenElementY + " Moved X Location : " + MoveX + " Moved Y Location : " + MoveY);
                        break;
                    } catch (WebDriverException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (ios() != null) {
                int screenWidth = ios().manage().window().getSize().width;
                TouchAction touchAction = new TouchAction(ios());
                while (true) {
                    try {
                        int chosenElementY = iOSElement.getLocation().getY();
                        int chosenElementX = iOSElement.getLocation().getX();

                        int MoveX = screenWidth - chosenElementX;
                        int MoveY = chosenElementX + chosenElementY;

                        logger.info("Performing Horizontal Swiping in IOS ");

                        touchAction.press(point(chosenElementX, chosenElementY))
                                .moveTo(point(MoveX, MoveY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(3000)))
                                .release()
                                .perform();

                        logReportWithScreenShot("PASS",
                                "Horizontal Swiped to Element  :  " + "Pressed X Location : " + chosenElementX + " Pressed Y Location : " + chosenElementY + " Moved X Location : " + MoveX + " Moved Y Location : " + MoveY);
                        break;
                    } catch (WebDriverException e) {
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

                if (android().getKeyboard() != null) {
                    android().hideKeyboard();
                }
            }
            if (ios() != null) {
                if (ios().getKeyboard() != null) {
                    ios().hideKeyboard();
                }
            }
        } catch (WebDriverException we) {
            hardFail("Failed to Hide Keyboard!! Application Crashed", "");
        }
    }

}

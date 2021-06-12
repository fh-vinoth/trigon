package com.trigon.elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.ElementRepoPojo;
import com.trigon.exceptions.RetryOnException;

import com.trigon.reports.ReportManager;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

public class TestModelCore extends ReportManager {

    private static final Logger logger = LogManager.getLogger(TestModelCore.class);
    protected long startTime;
    protected long endTime;
    protected WebDriverWait webDriverWait;

    private String locatorString(String s) {
        String locator = s;
        try {
            long startTime5 = System.currentTimeMillis();
            Gson pGson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement element1 = JsonParser.parseReader(new FileReader(tEnv().getPagesJsonFile()));
            ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);
            locator = eRepo.getElements().get(s).getAsJsonObject().get(tEnv().getElementLocator()).getAsString();

//            String p = tEnv().getElementLocator();
//            JSONArray jsonArray = JsonPath.read(tEnv().getPagesJsonFile(), "$.." + s + ".*." + tEnv().getElementLocator());
//            if (jsonArray.size() == 0) {
//
//
//                jsonArray = JsonPath.read(tEnv().getPagesJsonFile(), "$.." + s + "." + p);
//            }
//
//            locator = jsonArray.get(0).toString();

            logger.info("Picked locator for the element : " + s + " : " + locator);
            logger.info("Time taken to pick element from JSON" + cUtils().getRunDuration(startTime5, System.currentTimeMillis()));
        } catch (Exception e) {
            captureException(e);
        }
        return locator;
    }

    protected String[] getLocatorTypeAndContent(String s, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        String[] locArray = new String[2];
        try {
            String rawLocator = locatorString(s);
            if (wait_logReport_isPresent_Up_Down_XpathValues.length == 0) {

                locArray[0] = rawLocator.substring(0, rawLocator.indexOf('='));
                locArray[1] = rawLocator.substring(rawLocator.indexOf('=') + 1);

            } else {
                String[] result = rawLocator.split("\\$");
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < wait_logReport_isPresent_Up_Down_XpathValues.length; j++) {
                    if (wait_logReport_isPresent_Up_Down_XpathValues.length > 0) {
                        if ((wait_logReport_isPresent_Up_Down_XpathValues[0].startsWith("wait_")) || (wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("noreport")) || (wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("isPresent")) || (wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("Up")) || (wait_logReport_isPresent_Up_Down_XpathValues[0].equalsIgnoreCase("Down"))) {
                            j++;
                        }
                    }
                    if (wait_logReport_isPresent_Up_Down_XpathValues.length > 1) {
                        if ((wait_logReport_isPresent_Up_Down_XpathValues[1].startsWith("wait_")) || (wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("noreport")) || (wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("isPresent")) || (wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("Up")) || (wait_logReport_isPresent_Up_Down_XpathValues[1].equalsIgnoreCase("Down"))) {
                            j++;
                        }
                    }
                    if (wait_logReport_isPresent_Up_Down_XpathValues.length > 2) {
                        if ((wait_logReport_isPresent_Up_Down_XpathValues[2].startsWith("wait_")) || (wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("noreport")) || (wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("isPresent")) || (wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("Up")) || (wait_logReport_isPresent_Up_Down_XpathValues[2].equalsIgnoreCase("Down"))) {
                            j++;
                        }
                    }
                    if (wait_logReport_isPresent_Up_Down_XpathValues.length > 3) {
                        if ((wait_logReport_isPresent_Up_Down_XpathValues[3].startsWith("wait_")) || (wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("noreport")) || (wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("isPresent")) || (wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("Up")) || (wait_logReport_isPresent_Up_Down_XpathValues[3].equalsIgnoreCase("Down"))) {
                            j++;
                        }
                    }
                    if (wait_logReport_isPresent_Up_Down_XpathValues.length > 4) {
                        if ((wait_logReport_isPresent_Up_Down_XpathValues[4].startsWith("wait_")) || (wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("noreport")) || (wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("isPresent")) || (wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("Up")) || (wait_logReport_isPresent_Up_Down_XpathValues[4].equalsIgnoreCase("Down"))) {
                            j++;
                        }
                    }

                    for (int i = 0; i < result.length; i++) {
                        if (result[i].equalsIgnoreCase("XpathValue")) {
                            String x = result[i];
                            sb.append(result[i].replace(x, wait_logReport_isPresent_Up_Down_XpathValues[j]));
                            i++;
                            j++;
                        }
                        if (i < result.length) {
                            sb.append(result[i]);
                        }
                    }
                }
                String rawLocator1 = sb.toString();
                locArray[0] = rawLocator1.substring(0, rawLocator1.indexOf('='));
                if (wait_logReport_isPresent_Up_Down_XpathValues == null) {
                    locArray[1] = rawLocator1.substring(rawLocator1.indexOf('=') + 1);
                } else {
                    locArray[1] = rawLocator1.substring(rawLocator1.indexOf('=') + 1);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return locArray;
    }

    protected void alertHandling(String expectedText, String alertAction, String textAction) {
        if (browser() != null) {
            webDriverWait = new WebDriverWait(browser(), 30);
        } else if (android() != null) {
            webDriverWait = new WebDriverWait(android(), 30);
        } else if (ios() != null) {
            webDriverWait = new WebDriverWait(ios(), 30);
        }
        String text = "";
        if (browser() != null) {
            try {
                text = ""; //webDriverWait.until(ExpectedConditions.alertIsPresent()).getText();
                switch (alertAction.toLowerCase()) {
                    case "accept":
                        textVerification(text, expectedText, textAction);
                        //webDriverWait.until(ExpectedConditions.alertIsPresent()).accept();
                        logReport("PASS", "The alert " + text + " is accepted.");
                        break;
                    case "dismiss":
                        textVerification(text, expectedText, textAction);
                        // webDriverWait.until(ExpectedConditions.alertIsPresent()).dismiss();
                        logReport("PASS", "The alert " + text + " is dismissed.");
                        break;
                    default:
                        logger.error("The alert action " + alertAction + " is not available, framework supports only Accept and Dismiss");
                        logReport("FAIL", "The alert action " + alertAction + " is not available, framework supports only Accept and Dismiss");
                        break;
                }

            } catch (NoAlertPresentException e) {
                logger.error("There is NO Alert Present" + e);
                logReport("FAIL", "There is no alert present.");
            }
        } else if (android() != null) {
            try {
                text = "";//webDriverWait.until(ExpectedConditions.alertIsPresent()).getText();
                switch (alertAction.toLowerCase()) {
                    case "accept":
                        textVerification(text, expectedText, textAction);
                        //webDriverWait.until(ExpectedConditions.alertIsPresent()).accept();
                        logReport("PASS", "The alert " + text + " is accepted.");
                        break;
                    case "dismiss":
                        textVerification(text, expectedText, textAction);
                        //webDriverWait.until(ExpectedConditions.alertIsPresent()).dismiss();
                        logReport("PASS", "The alert " + text + " is dismissed.");
                        break;
                    default:
                        logger.error("The alert action " + alertAction + " is not available, framework supports only Accept and Dismiss");
                        logReport("FAIL", "The alert action " + alertAction + " is not available, framework supports only Accept and Dismiss");
                        break;
                }

            } catch (NoAlertPresentException e) {
                logger.error("There is NO Alert Present" + e);
                logReport("FAIL", "There is no alert present.");
            }
        }
    }

    protected void textVerification(String actual, String expected, String textAction) {
        try {
            if ((actual != null) && (expected != null)) {
                switch (textAction.toLowerCase()) {
                    case "partial":
                        if (!actual.isEmpty()) {
                            customAssertPartialEquals(actual, expected);
                        } else {
                            if (expected.equals(actual)) {
                                customAssertPartialEquals(actual, expected);
                            } else {
                                logger.error("Actual or Expected value is empty");
                                logReport("FAIL", "Actual or Expected value is empty");
                            }
                        }
                        break;
                    case "notequal":
                        customAssertNotEquals(actual, expected);
                        break;

                    default:
                        customAssertEquals(actual, expected);
                        break;
                }

            } else {
                if (expected.equals(actual)) {
                    customAssertEquals(actual, expected);
                } else {
                    logger.error("Actual or Expected value is null");
                    //logReport("FAIL", "Actual or Expected value is null");
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void textVerification(Collection<?> actual, Collection<?> expected, String textAction) {
        try {
            if ((actual != null) && (expected != null)) {
                switch (textAction.toLowerCase()) {
                    case "partial":
                        if (!actual.isEmpty()) {
                            customAssertPartialEquals(actual, expected);
                        } else {
                            if (expected.equals(actual)) {
                                customAssertPartialEquals(actual, expected);
                            } else {
                                logger.error("Actual or Expected value is empty");
                                logReport("FAIL", "Actual or Expected value is empty");
                            }
                        }
                        break;
                    case "notequal":
                        customAssertNotEquals(actual, expected);
                        break;
                    default:
                        customAssertEquals(actual, expected);
                        break;
                }
            } else {
                if (expected.equals(actual)) {
                    customAssertEquals(actual, expected);
                } else {
                    logger.error("Actual or Expected value is null");
                    //logReport("FAIL", "Actual or Expected value is null");
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void textVerificationWithScreenShot(String actual, String expected, String textAction) {
        try {
            if ((actual != null) && (expected != null)) {
                switch (textAction.toLowerCase()) {
                    case "partial":
                        if (!actual.isEmpty()) {
                            customAssertPartialWithScreenShot(actual, expected);
                        } else {
                            if (expected == actual) {
                                customAssertPartialEquals(actual, expected);
                            } else {
                                logger.error("Actual or Expected value is empty");
                                logReport("FAIL", "Actual or Expected value is empty");
                            }
                        }
                        break;
                    case "notequal":
                        customAssertNotEquals(actual, expected);
                        break;
                    default:
                        customAssertEqualsWithScreenShot(actual, expected);
                        break;
                }

            } else {
                if (expected == actual) {
                    customAssertEquals(actual, expected);
                } else {
                    logger.error("Actual or Expected value is null");
                    //logReport("FAIL", "Actual or Expected value is null");
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected List<String> dropDownHandling(String locatorString, String value, String dropDownAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        RetryOnException retryHandler = new RetryOnException();
        RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
        By elementType = elementCapture(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
        startTime = System.currentTimeMillis();
        List<String> dropDownValues = new ArrayList<>();
        WebElement element = null;
        try {
            if (browser() != null) {

                while (true) {
                    try {
                        element = browser().findElement(elementType);
                        break;
                    } catch (WebDriverException e) {
                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {
                            if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                break;
                            }
                        } else {
                            if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {
                                break;
                            }
                        }
                    }
                }

                Select dropDown = new Select(element);

                switch (dropDownAction.toLowerCase()) {
                    case "getalloptions":
                        for (WebElement option : dropDown.getOptions()) {
                            if (option.getAttribute("value") != "")
                                dropDownValues.add(option.getText());
                        }
                        break;
                    case "selectbyvalue":
                        dropDown.selectByValue(value);
                        break;
                    case "selectbyindex":
                        dropDown.selectByIndex(Integer.parseInt(value));
                        break;
                    case "selectbyvisibletext":
                        dropDown.selectByVisibleText(value);
                        break;
                    case "deselectbyvalue":
                        dropDown.deselectByValue(value);
                        break;
                    case "deselectbyindex":
                        dropDown.deselectByIndex(Integer.parseInt(value));
                        break;
                    case "deselectbyvisibletext":
                        dropDown.deselectByVisibleText(value);
                        break;
                    case "getselectedoptions":
                        for (WebElement option : dropDown.getAllSelectedOptions()) {
                            String txt = option.getText();
                            if (option.getAttribute("value") != "")
                                dropDownValues.add(option.getText());
                        }
                        break;
                    default:
                        logger.error("The DropDown Action " + dropDownAction + " is not yet implemented in framework");
                        logReport("FAIL", "The DropDown Action " + dropDownAction + " is not yet implemented in framework");
                        break;
                }
                endTime = System.currentTimeMillis();
                logger.info("Time Taken to capture DropDown for :  " + locatorString + " : " + cUtils().getRunDuration(startTime, endTime));

            }
        } catch (Exception e) {
            captureException(e);
        }
        return dropDownValues;
    }


    protected void keyBoard(String pressKey) {
        Robot rb = null;
        try {
            rb = new Robot();
            switch (pressKey) {
                case "ENTER":
                    hardWait(3000);
                    rb.keyPress(KeyEvent.VK_ENTER);
                    rb.keyRelease(KeyEvent.VK_ENTER);
                    logReport("INFO", "Pressed ENTER KEY");
                    break;
                case "ESC":
                    hardWait(3000);
                    rb.keyPress(KeyEvent.VK_ESCAPE);
                    rb.keyRelease(KeyEvent.VK_ESCAPE);
                    logReport("INFO", "Pressed ESC KEY");
                    break;
                case "PAGEUP":
                    hardWait(3000);
                    rb.keyPress(KeyEvent.VK_M);
                    rb.keyRelease(KeyEvent.VK_PAGE_UP);
                    logReport("INFO", "Pressed PAGEUP KEY");
                    break;
                case "PAGEDOWN":
                    hardWait(3000);
                    rb.keyPress(KeyEvent.VK_PAGE_DOWN);
                    rb.keyRelease(KeyEvent.VK_PAGE_DOWN);
                    logReport("INFO", "Pressed PAGEDOWN KEY");
                    break;
                default:
                    logger.error("The Key Event  " + pressKey + " is not available, framework supports only ENTER,PAGEUP,ESC and PAGEDOWN");
                    logReport("FAIL", "The Key Event  " + pressKey + " is not available, framework supports only ENTER,PAGEUP and PAGEDOWN");
                    break;
            }

        } catch (Exception e) {
            captureException(e);
        }
    }


    protected By elementCapture(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        By elementType = null;
        try {
            if (locatorString.startsWith("//") || locatorString.startsWith("(//")) {
                elementType = By.xpath(locatorString);
            } else {
                String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
                try {
                    //elementType = getObjectLocatorBy(locatorArr[0], locatorArr[1]);
                } catch (NullPointerException e) {
                    elementType = null;
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return elementType;
    }

    protected void scrollbypixel(int x, int y) {
        try {
            hardWait(1000);
            String scroll = "window.scrollBy(" + x + "," + y + ")";
            if (browser() != null) {
                JavascriptExecutor js = browser();
                logger.info("Scrolled to : " + scroll);
                js.executeScript(scroll);
            }
            //hardWait(3000);
        } catch (Exception e) {
            captureException(e);
        }
    }


    
    protected void verticalSwipe(double startPercentage, double endPercentage, double anchorPercentage) {
        try {
            if (android() != null) {
                Dimension size = android().manage().window().getSize();
                int anchor = (int) (size.width * anchorPercentage);
                int startPoint = (int) (size.height * startPercentage);
                int endPoint = (int) (size.height * endPercentage);

                new TouchAction(android())
                        .press(point(anchor, startPoint))
                        .waitAction(waitOptions(ofMillis(1000)))
                        .moveTo(point(anchor, endPoint))
                        .release().perform();
            }
            if (ios() != null) {
                Dimension size = ios().manage().window().getSize();
                int anchor = (int) (size.width * anchorPercentage);
                int startPoint = (int) (size.height * startPercentage);
                int endPoint = (int) (size.height * endPercentage);

                new TouchAction(ios())
                        .press(point(anchor, startPoint))
                        .waitAction(waitOptions(ofMillis(1000)))
                        .moveTo(point(anchor, endPoint))
                        .release().perform();
            }
        } catch (InvalidArgumentException iae) {
            captureException(iae);
            logger.error("Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : " + startPercentage + " : " + endPercentage + " : " + anchorPercentage);
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) browser()).executeScript("arguments[0].scrollIntoView();", element);
    }

    protected void showNotifications() {
        manageNotifications(true);
    }

    protected void hideNotifications() {
        manageNotifications(false);
    }

    private void manageNotifications(Boolean show) {
        try {
            if (android() != null) {
                Dimension screenSize = android().manage().window().getSize();
                int yMargin = 5;
                int xMid = screenSize.width / 2;
                PointOption top = PointOption.point(xMid, yMargin);
                PointOption bottom = PointOption.point(xMid, screenSize.height - yMargin);

                TouchAction action = new TouchAction(android());
                if (show) {
                    action.press(top);
                } else {
                    action.press(bottom);
                }
                action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
                if (show) {
                    action.moveTo(bottom);
                } else {
                    action.moveTo(top);
                }
                action.perform();
            }
            if (ios() != null) {
                Dimension screenSize = ios().manage().window().getSize();
                int yMargin = 5;
                int xMid = screenSize.width / 2;
                PointOption top = PointOption.point(xMid, yMargin);
                PointOption bottom = PointOption.point(xMid, screenSize.height - yMargin);

                TouchAction action = new TouchAction(ios());
                if (show) {
                    action.press(top);
                } else {
                    action.press(bottom);
                }
                action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
                if (show) {
                    action.moveTo(bottom);
                } else {
                    action.moveTo(top);
                }
                action.perform();
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void scrollss() {

        /*
        https://www.360logica.com/blog/multiple-ways-to-scroll-a-page-using-selenium-webdriver/
        If you want to scroll the page vertically to perform some action, you can do it using the following JavaScript.
        ((JavascriptExecutor)

driver).executeScript(“window.scrollTo(0,

document.body.scrollHeight)”);

            Where ‘JavascriptExecutor’ is an interface, which helps executing JavaScript through Selenium WebDriver. You can use the following code to import.

import org.openqa.selenium.JavascriptExecutor;

2.      If you want to scroll at a particular element, you need to use the following JavaScript.

WebElement element =

driver.findElement(By.xpath(“//input [@id=’email’]”));

((JavascriptExecutor)

driver).executeScript(“arguments[0].scrollIntoView();”, element);

Where ‘element’ is the locator where you want to scroll.

3.      If you want to scroll at a particular coordinate, use the following JavaScript.

((JavascriptExecutor)

driver).executeScript(“window.scrollBy(200,300)”);

Where ‘200,300’ are the coordinates.

If you want to scroll up in a vertical direction, you can use the following JavaScript.
((JavascriptExecutor)

driver).executeScript(“window.scrollTo(document.body.scrollHeight,0)”);

If you want to scroll horizontally in the right direction, use the following JavaScript.
((JavascriptExecutor)

driver).executeScript(“window.scrollBy(2000,0)”);

If you want to scroll horizontally in the left direction, use the following JavaScript.
((JavascriptExecutor)

driver).executeScript(“window.scrollBy(-2000,0)”);
         */
    }




}

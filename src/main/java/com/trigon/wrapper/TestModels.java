package com.trigon.wrapper;

import com.trigon.annotations.Obfuscation;
import com.trigon.elements.PerformElementAction;
import com.trigon.exceptions.RetryOnException;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Obfuscation(exclude = false, applyToMembers = false)
public class TestModels extends PerformElementAction {

    private static final Logger logger = LogManager.getLogger(TestModels.class);


    public void click(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        performElementAction(locatorString, "click", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public void clearText(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        performElementAction(locatorString, "clearText", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public void enterText(String locatorString, String text, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        performElementAction(locatorString, "enterText", text, wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public String getText(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementAction(locatorString, "getText", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public String getAttribute(String locatorString, String attribute, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementAction(locatorString, "getAttribute", attribute, wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public boolean isEnabled(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementActionWithPresent(locatorString, "isEnabled", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public void verifyDisplayed(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        performElementAction(locatorString, "verifyDisplayed", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public boolean isSelected(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementActionWithPresent(locatorString, "isSelected", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public boolean isPresent(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementActionWithPresent(locatorString, "isPresent", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public boolean isNotPresent(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementActionWithPresent(locatorString, "isNotPresent", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public boolean isNotDisplayed(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementActionWithPresent(locatorString, "isNotDisplayed", "", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public List<String> getDropDownValues(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return dropDownHandling(locatorString, "", "GetAllOptions", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public List<String> getSelectedDropDownValues(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return dropDownHandling(locatorString, "", "GetSelectedOptions", wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public void verifyValueFromList(String locatorString, String expected, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        performElementsAction(locatorString, "lisofvalues", expected, textAction, wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public List<String> getListOfElements(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementsAction(locatorString, "lisofvalues", "NA", null, wait_logReport_isPresent_Up_Down_XpathValues);
    }

    public List<WebElement> findElements(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return getWebElements(locatorString, false, wait_logReport_isPresent_Up_Down_XpathValues);
    }

    public List<String> getValueFromListByIndex(String locatorString, String i, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return performElementsAction(locatorString, "lisofvalues", "NA", null, wait_logReport_isPresent_Up_Down_XpathValues);
    }


    public List<String> dropDown(String locatorString, String value, String dropDownAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        return dropDownHandling(locatorString, value, dropDownAction, wait_logReport_isPresent_Up_Down_XpathValues);
    }

    /**
     * ########################################################################################################
     * START OF VERIFICATION POINTS
     * ########################################################################################################
     */

    public void verifyAlert(String locatorString, String expectedText, String alertAction, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        alertHandling(expectedText, alertAction, textAction);
    }


    public void verifyText(String locatorString, String expectedText, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        textVerificationWithScreenShot(getText(locatorString, wait_logReport_isPresent_Up_Down_XpathValues), expectedText, textAction);
    }


    public void compareText(String actualText, String expectedText, String textAction, String... description) {
        textVerification(actualText, expectedText, textAction, description);
    }


    public void compareText(Collection<?> actualText, Collection<?> expectedText, String textAction) {
        textVerification(actualText, expectedText, textAction);
    }


    public void verifyAttribute(String locatorString, String attribute, String value, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        textVerificationWithScreenShot(getAttribute(locatorString, attribute, wait_logReport_isPresent_Up_Down_XpathValues), value, textAction);
    }


    public void verifyTitle(String expectedTitle, String textAction) {
        if (browser() != null) {
            String screenTitle = browser().getTitle();
            logger.info("The title is " + screenTitle);
            textVerificationWithScreenShot(screenTitle, expectedTitle, textAction);
        }
    }
    /**
     * ########################################################################################################
     * END OF OF VERIFICATION POINTS
     * ########################################################################################################
     */

    /**
     * ########################################################################################################
     * START OF SWITCHING/ NAVIGATING ELEMENTS
     * ########################################################################################################
     */

    public void switchToWindow(int index) {
        if (browser() != null) {
            try {
                Set<String> allWindowHandles = browser().getWindowHandles();
                List<String> allHandles = new ArrayList<>();
                allHandles.addAll(allWindowHandles);
                browser().switchTo().window(allHandles.get(index));
            } catch (NoSuchWindowException e) {
                logReport("FAIL", "The browser() could not move to the given window by index " + index);
            } catch (WebDriverException e) {
                logReport("FAIL", "WebDriverException : " + e.getMessage());
            }
        }
    }

    public void switchToWindow_VerifyTitle_SwitchBack(int index, String expecteTitle, String textAction) {
        if (browser() != null) {
            try {
                String currentWindow = browser().getWindowHandle();
                for (String window : browser().getWindowHandles()) {
                    if (!window.equals(currentWindow)) {
                        browser().switchTo().window(window);
                        browser().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                        compareText(browser().getTitle(), expecteTitle, textAction);
                        browser().close();
                    }
                }
                browser().switchTo().defaultContent();

            } catch (NoSuchWindowException e) {
                logReport("FAIL", "No Windows present");
            } catch (WebDriverException e) {
                logReport("FAIL", "WebDriverException : " + e.getMessage());
            }
        }
    }


    public void switchToFrame(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {

        RetryOnException retryHandler = new RetryOnException();
        RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 500);
        By elementType = elementCapture(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
        startTime = System.currentTimeMillis();

        if (browser() != null) {
            hardWait(2000);
            try {
                while (true) {
                    try {
                        if (browser() != null) {
                            WebElement element = browser().findElement(elementType);
                            browser().switchTo().frame(element);
                        }
                        logReport("PASS", "switch In to the Frame ");
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
            } catch (NoSuchFrameException e) {
                logReport("FAIL", "WebDriverException : " + e.getMessage());
            } catch (WebDriverException e) {
                logReport("FAIL", "WebDriverException : " + e.getMessage());
            }
        }
    }


    public String getCurrentPageURL() {
        String appURL = null;
        if (browser() != null) {
            logger.info("Get the current page url");
            appURL = browser().getCurrentUrl();
            logReportWithScreenShot("PASS",
                    "Captured current page url : " + appURL);
        }
        return appURL;
    }


    public String navigateToUrl(String appURL) {
        if (browser() != null) {
            logger.info("Set Current Page url");
            browser().get(appURL);
            browser().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            logReportWithScreenShot("PASS",
                    "Set page URL to : " + appURL);
        }
        return appURL;
    }


    public void tabOut(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        if (browser() != null) {
            WebElement element = null; //relook
            logger.info("Press the TAB");
            element.sendKeys(Keys.TAB);
            logReport("PASS", "Pressed on keyboard tab successfully");
        }
    }


    public void refreshBrowser() {
        if (browser() != null) {
            logger.info("Browser refreshed");
            browser().navigate().refresh();
            logReport("PASS", "Browser Refreshed");
        }
    }


    public void backNavigation() {

        try {
            if (android() != null) {
                logger.info("Mobile App Navigated Back");
                android().navigate().back();
                logReport("PASS", "Mobile App Navigated Back");
            }
            if (ios() != null) {
                logger.info("Mobile App Navigated Back");
                ios().navigate().back();
                logReport("PASS", "Mobile App Navigated Back");
            }
            if (browser() != null) {
                logger.info("Browser Back");
                browser().navigate().back();
                logReport("PASS", "Browser Navigated Back");
            }
        } catch (WebDriverException we) {
            hardFail("Failed to perform Back navigation!! Application Crashed", "");
        }

    }


    public void forwardNavigation() {
        if (android() != null) {
            logger.info("Mobile App Forwarded");
            android().navigate().forward();
            logReport("PASS", "Mobile App Forwarded");
        }
        if (ios() != null) {
            logger.info("Mobile App Forwarded");
            ios().navigate().forward();
            logReport("PASS", "Mobile App Forwarded");
        }
        if (browser() != null) {
            logger.info("Browser Forwarded");
            browser().navigate().forward();
            logReport("PASS", "Browser Forwarded");
        }
    }


    /**
     * ########################################################################################################
     * END OF SWITCHING/ NAVIGATING ELEMENTS
     * ########################################################################################################
     */

    /**
     * ########################################################################################################
     * START OF WEB SCROLLS
     * ########################################################################################################
     */

    //To scroll down the web page by pixel.
    //x-pixels is the number at x-axis, it moves to the left if number is positive and it move to the right if number is negative .y-pixels is the number at y-axis, it moves to the down if number is positive and it move to the up if number is in negative
    public void scrollByPixel(int x, int y) {
        scrollbypixel(x, y);
    }

    //To scroll down the web page by the visibility of the element.

    public void scrollByVisibleElement(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {

        hardWait(2000);
        if (browser() != null) {
            WebElement element = null; //relook
            //JavascriptExecutor js = (JavascriptExecutor) browser();
            browser().executeScript("arguments[0].scrollIntoView();", element);
        }
        hardWait(3000);
    }

    //To scroll down the web page at the bottom of the page.

    public void scrollToBottomPage() {
        hardWait(2000);
        //JavascriptExecutor js = (JavascriptExecutor) browser();
        browser().executeScript("window.scrollTo(0, document.body.scrollHeight)");
        hardWait(3000);
    }


    public void keyBoardActions(String pressKey) {
        keyBoard(pressKey);
    }

    /**
     * ########################################################################################################
     * END OF WEB SCROLLS
     * ########################################################################################################
     */


    public By getLocatorFromJson(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {

        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
        //By elementType = getObjectLocatorBy(locatorArr[0], locatorArr[1]);
        By elementType = null;
        return elementType;

    }


    // Started for Mobile Actions


    /**
     * method to set the context to required view.
     * <p>
     * Views are NATIVE_APP , WEBVIEW_1
     *
     * @param context view to be set
     */

    public void setContext(String context) {
        hardWait(4000);
        Set<String> contextNames = android().getContextHandles();
        logger.info("Context Names : " + contextNames);
        if (context.contains("NATIVE")) {
            android().context((String) contextNames.toArray()[0]);
        } else if (context.contains("WEBVIEW")) {
            android().context((String) contextNames.toArray()[1]);
        }
        logger.info("Current context" + android().getContext());
    }


    /**
     * method scroll to visible text in a list and it will click on that element
     *
     * @param elementName
     */

    public void androidScrollToVisibleTextInListAndClick(String elementName) {
        WebElement element = android().findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()"
                // +".resourceId(\"android:id/list\")).scrollIntoView("
                + ".className(\"android.widget.ListView\")).scrollIntoView("
                + "new UiSelector().text(\"" + elementName + "\"));"));
        element.click();
        logReport("INFO", "Scrolled to element in list succesfully");
    }


    /**
     * Generic scroll using send keys Pass in values to be selected as a String
     * array to the list parameter Method will loop through looking for scroll
     * wheels based on the number of values you supply For instance Month, Day,
     * Year for a birthday would have this loop 3 times dynamically selecting
     * each scroll wheel
     *
     * @param list Example : {"Apr","27","1999"}
     */

    public void setDateOrTimeInIos(String[] list) {
        for (int i = 0; i < list.length; i++) {
            WebElement me = ios().findElement(AppiumBy.xpath("//UIAPickerWheel[" + (i + 1) + "]"));
            me.sendKeys(list[i]);
        }
        logReport("INFO",
                "IOS Date setted succesfully to : " + list);
    }


    /**
     * method to scroll to the text and clicks on the text
     *
     * @param text
     * @return true if it clicked successfully
     */

    public boolean androidScrollToTextAndClick(String text) {
        try {
            WebElement el = android()
                    .findElement(MobileBy
                            .AndroidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
                                    + "new UiSelector().text(\""
                                    + text
                                    + "\"));"));
            el.click();
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }


    public boolean iOSScrollToTextAndClick(String text) {
        try {
            WebElement el = ios()
                    .findElement(AppiumBy.iOSNsPredicateString(text));
            el.click();
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }


    public boolean iOSScrollToText(String text) {
        try {
            WebElement el = ios()
                    .findElement(AppiumBy.iOSNsPredicateString(text));
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }

    /**
     * method to scroll to the text in the page
     *
     * @param text
     * @return true if it scroll to text successfully
     */

    public boolean androidScrollToText(String text) {
        try {
            android()
                    .findElement(AppiumBy
                            .androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
                                    + "new UiSelector().text(\""
                                    + text
                                    + "\"));"));
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }


    /**
     * method to scroll to Text and return an element
     *
     * @param text
     * @return element
     */

    public WebElement androidScrollToTextAndGetElement(String text) {
        return android()
                .findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView("
                        + "new UiSelector().text(\"" + text + "\"));"));
    }


    /**
     * converts the full string of the month to Android short form name
     *
     * @param month
     * @return
     */

    public String getAndroidMonthName(String month) {
        if (month != null && !month.isEmpty())
            return month.substring(0, 3);
        return month;
    }


    public void tapByCoordinates(int x, int y) {
        try {
            if (android() != null) {

                Map<String, Object> args = new HashMap<>();
                args.put("x", x);
                args.put("y", y);
                android().executeScript("mobile: tap", args);
            }
            if (ios() != null) {

                Map<String, Object> args = new HashMap<>();
                args.put("x", x);
                args.put("y", y);
                ios().executeScript("mobile: tap", args);
            }
        } catch (InvalidArgumentException iae) {
            logger.error("Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : " + x + " : " + y);
        }

    }

    //Press by element

    public void pressByElement(WebElement element) {
        try {
            if (browser() == null) {
                Point sourceLocation = element.getLocation();
                Dimension sourceSize = element.getSize();
                int centerX = sourceLocation.getX() + sourceSize.getWidth() / 2;
                int centerY = sourceLocation.getY() + sourceSize.getHeight() / 2;
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
                tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                Objects.requireNonNullElse(android(), ios()).perform(List.of(tap));
            }
        }catch (Exception e){
            logger.error("pressByElement Failed !!!"+" : "+e);
        }


    }

    //Press by coordinates

    public void pressByCoordinates(int x, int y) {
        try {
            if (browser() == null) {
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
                tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                Objects.requireNonNullElse(android(), ios()).perform(List.of(tap));
            }
        } catch (InvalidArgumentException iae) {
            logger.error("Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : " + x + " : " + y);
        }

    }

    //Horizontal Swipe by percentages

    public void horizontalSwipeByPercentage(double startPercentage, double endPercentage, double anchorPercentage) {
        try {
            if (browser() == null) {
                Dimension size = tEnv().getDeviceDimension();
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence scroll = new Sequence(finger, 0);
                int anchor = (int) (size.height * anchorPercentage);
                int startPoint = (int) (size.width * startPercentage);
                int endPoint = (int) (size.width * endPercentage);

                scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startPoint, anchor));
                scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endPoint, anchor));
                scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                Objects.requireNonNullElse(android(), ios()).perform(List.of(scroll));
            }
        } catch (InvalidArgumentException iae) {
            logger.error("Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : " + startPercentage + " : " + endPercentage + " : " + anchorPercentage);
        }

    }

    //Vertical Swipe by percentages

    public void verticalSwipeByPercentages(double startPercentage, double endPercentage, double anchorPercentage) {
        verticalSwipe(startPercentage, endPercentage, anchorPercentage);
    }


    //Swipe by elements

    public void swipeByElements(WebElement startElement, WebElement endElement) {
        try {
            if(browser()==null) {
                int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
                int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);

                int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
                int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence scroll = new Sequence(finger, 0);
                scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
                scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY));
                scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                Objects.requireNonNullElse(android(), ios()).perform(List.of(scroll));
            }
        } catch (InvalidArgumentException iae) {
            logger.error("The given Element Co-Ordinates crossed beyond screen range : Check you have picked correct elements in range ");
        }

    }

    public void singleTap(WebElement element) {
        try {
            if(browser()==null) {
                Point sourceLocation = element.getLocation();
                Dimension sourceSize = element.getSize();
                int centerX = sourceLocation.getX() + sourceSize.getWidth() / 2;
                int centerY = sourceLocation.getY() + sourceSize.getHeight() / 2;
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence doubleTap = new Sequence(finger, 1);
                doubleTap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
                doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                Objects.requireNonNullElse(android(), ios()).perform(List.of(doubleTap));
            }
        }catch (Exception e){
            logger.error("Couldn't find the element or Single tap Failed!!!");
        }

    }

    public void doubleTap(WebElement element) {
        try {
            if(browser()==null) {
                Point sourceLocation = element.getLocation();
                Dimension sourceSize = element.getSize();
                int centerX = sourceLocation.getX() + sourceSize.getWidth() / 2;
                int centerY = sourceLocation.getY() + sourceSize.getHeight() / 2;
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence doubleTap = new Sequence(finger, 1);
                doubleTap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX, centerY));
                doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                doubleTap.addAction(new Pause(finger, Duration.ofMillis(100)));
                doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                doubleTap.addAction(new Pause(finger, Duration.ofMillis(50)));
                doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                doubleTap.addAction(new Pause(finger, Duration.ofMillis(100)));
                doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
               Objects.requireNonNullElse(android(), ios()).perform(List.of(doubleTap));
            }
        }catch (Exception e){
            logger.error("Couldn't find the element or Double tap Failed!!!");
        }

    }


    public List<String> getElementsFromGroup(String className1, String className2, String className3, String SearchText) {
        WebElement superParent = android().findElement(By.className(className1));
        List<WebElement> parent = superParent.findElements(By.className(className2));
        List<String> returnList = new ArrayList<>();
        int i;
        int j;
        int k = 0;
        for (i = 0; i < parent.size(); i++) {
            List<WebElement> child = parent.get(i).findElements(By.className(className3));
            for (j = 0; j < child.size(); j++) {
                if (child.get(j).getText().contains(SearchText)) {
                    for (k = 0; k < i + 2; k++) {
                        String y = parent.get(i).findElements(By.className(className3)).get(k).getText();
                        System.out.println(y);
                        returnList.add(y);
                    }
                }
            }
        }
        return returnList;
    }


    public String mapKeyFinder(HashMap<String, Object> map, String KeyName) {
        Object KeyName1 = map.get(KeyName);
        String returnValue = "";
        if (KeyName1 != null) {
            returnValue = String.valueOf(KeyName1);
        } else {
            Assert.fail(" " + KeyName + " Key is Not Found in Map: Check your map values or Excel header keys !!!");
            logReport("FAIL", " " + KeyName + " Key is Not Found in Map: Check your map values or Excel header keys !!!");
        }
        return returnValue;
    }


    public void switchAndroidApp(String appPackage, String appActivity) {
        Activity activity = new Activity(appPackage, appActivity);
        activity.setAppWaitPackage(appPackage);
        activity.setAppWaitActivity(appActivity);
        android().startActivity(activity);
    }


    public void switchIOSApp(String currentAppBundleID, String switchAppBundleId) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("bundleId", currentAppBundleID);
        ios().executeScript("mobile: launchApp", args);
        hardWait(1000);
        args.put("bundleId", switchAppBundleId);
        ios().executeScript("mobile: activateApp", args);
        hardWait(1000);
        args.put("bundleId", currentAppBundleID);
        ios().executeScript("mobile: activateApp", args);
        hardWait(1000);
    }


    public void openNotificationAndroidApp() {
        hardWait(1000);
        android().openNotifications();
        hardWait(2000);
    }


    public void openNotificationIOSApp(String BundleId) {
        hardWait(1000);
        ios().terminateApp(BundleId);
        hardWait(2000);
        showNotifications();
        ios().findElement(By.xpath("//XCUIElementTypeCell[contains(@label, 'notification text')]"));
        hideNotifications();
        ios().activateApp(BundleId);
    }


    public void closeMobileApp() {
        if (android() != null) {
            android().closeApp();
        }
        if (ios() != null) {
            ios().closeApp();
        }
    }


    public void runMobileAppBackGround(long seconds) {
        if (android() != null) {
            android().runAppInBackground(Duration.ofSeconds(seconds));
        }
        if (ios() != null) {
            ios().runAppInBackground(Duration.ofSeconds(seconds));
        }
    }


    public List<Integer> getElementLocation(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        List<Integer> Coordinates = new ArrayList();
        RetryOnException retryHandler = new RetryOnException();
        RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);
        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
        By elementType = elementCapture(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);

        try {
            if (ios() != null) {
                while (true) {
                    try {
                        Point XY = ios().findElement(AppiumBy.iOSNsPredicateString(locatorArr[1])).getLocation();
                        int X = XY.x;
                        int Y = XY.y;
                        Coordinates.add(X);
                        Coordinates.add(Y);
                        logger.info("The element " + locatorString + " Co-Ordinates are " + X + " : " + Y);
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
            }

            if (android() != null) {
                while (true) {
                    try {
                        Point XY = android().findElement(elementType).getLocation();
                        int X = XY.x;
                        int Y = XY.y;
                        Coordinates.add(X);
                        Coordinates.add(Y);
                        logger.info("The element " + locatorString + " Co-Ordinates are " + X + " : " + Y);
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
            }
            if (browser() != null) {
                while (true) {
                    try {
                        Point XY = browser().findElement(elementType).getLocation();
                        int X = XY.x;
                        int Y = XY.y;
                        Coordinates.add(X);
                        Coordinates.add(Y);
                        logger.info("The element " + locatorString + " Co-Ordinates are " + X + " : " + Y);
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
            }
        } catch (InvalidArgumentException iae) {
            logger.error("The given element visibility is beyond screen range : Make sure you have taken correct element : " + locatorString);
        }
        return Coordinates;
    }


    public void hideMobileKeyBorad() {
        hideKeyBorad();
    }

    public void swipeRightUntilLogOutScreen() {
        do {
            swipeByDirection("right");
        } while (!isElementPresent(By.id("org.wordpress.android:id/me_login_logout_text_view")));
    }

    public boolean isElementPresent(By by) {
        Boolean retrunValue = false;
        if (ios() != null) {
            try {
                ios().findElement(by);
                retrunValue = true;
            } catch (NoSuchElementException e) {
                retrunValue = false;
            }
        }
        if (android() != null) {
            try {
                android().findElement(by);
                retrunValue = true;
            } catch (NoSuchElementException e) {
                retrunValue = false;
            }
        }
        return retrunValue;
    }

    public void swipeLeftUntilTextExists(String expected) {
        if (ios() != null) {
            do {
                swipeByDirection("left");
            } while (!ios().getPageSource().contains(expected));
        }
        if (android() != null) {
            do {
                swipeByDirection("left");
            } while (!android().getPageSource().contains(expected));
        }

    }

    public void horizontalSwipeToElement(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        horizontalSwipeToElement1(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);
    }

    public void swipeByDirection(String direction) {
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 0);
            Dimension size = tEnv().getDeviceDimension();
            if (browser() == null) {
                int start = (int) (size.width * 0.9);
                int end = (int) (size.width * 0.2);
                int anchorHorizontal = size.height / 2;
                int anchorVertical = size.width / 2;
                if(direction.equalsIgnoreCase("left")){
                    scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start, anchorHorizontal));
                    scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), end, anchorHorizontal));
                    scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }else if(direction.equalsIgnoreCase("right")){
                    scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), end, start));
                    scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), start, anchorHorizontal));
                    scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }
                else if(direction.equalsIgnoreCase("up")){
                    scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), anchorVertical, end));
                    scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), anchorVertical, start));
                    scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }
                else if(direction.equalsIgnoreCase("down")){
                    scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), anchorVertical, start));
                    scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), anchorVertical, end));
                    scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                }else{
                    logger.error("The given direction is not Valid!!!");
                }
                Objects.requireNonNullElse(android(), ios()).perform(List.of(scroll));
            }
        }catch (Exception e){
            logger.error("Swipe Right Failed!!!"+" : "+e);
        }

    }
}


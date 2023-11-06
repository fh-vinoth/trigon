package com.trigon.elements;

import com.google.gson.*;
import com.trigon.bean.ElementRepoPojo;
import com.trigon.exceptions.RetryOnException;
import com.trigon.exceptions.ThrowableTypeAdapter;
import com.trigon.reports.ReportManager;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import org.openqa.selenium.Dimension;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;
import java.util.Objects;

public class TestModelCore extends ReportManager {

    private static final Logger logger = LogManager.getLogger(TestModelCore.class);
    protected long startTime;
    protected long endTime;
    protected WebDriverWait webDriverWait;

    protected String locatorString(String s) {
        String locator = s;
        try {
            //s.startsWith("name")||s.startsWith("xpath")||s.startsWith("classname")||s.startsWith("partiallinktext")||s.startsWith("linktext")||s.startsWith("tagname")||s.startsWith("css")||s.startsWith("id")
            if (!s.contains("=")) {
                Gson pGson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
                JsonElement element1 = JsonParser.parseReader(new FileReader(tEnv().getPagesJsonFile()));
                ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);
                locator = eRepo.getElements().get(s).getAsJsonObject().get(tEnv().getElementLocator()).getAsString();
            }
        } catch (Exception e) {
            captureException(e);
        }
        return locator;
    }

    protected void locatorStringReplaceInJSON(String locatorObject, String newLocatorsMatched,String beforeXpath,String afterXpath) {
        try {
            if (!locatorObject.contains("=")) {
                Gson pGson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement element1 = JsonParser.parseReader(new FileReader(tEnv().getPagesJsonFile()));
                ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);
                if(tEnv().getElementLocator().equalsIgnoreCase("Web")){
                    eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty("Web_beforeElement",beforeXpath);
                    eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty("Web_afterElement",afterXpath);
                    eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty(tEnv().getElementLocator(),"xpath="+newLocatorsMatched);
                } else if(tEnv().getElementLocator().equalsIgnoreCase("Android")) {
                    eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty("App_beforeElement",beforeXpath);
                    eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty("App_afterElement",afterXpath);
                    eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty(tEnv().getElementLocator(),"xpath="+newLocatorsMatched);
                }
                JsonElement newJsonElement = JsonParser.parseString(eRepo.getElements().toString());
                element1.getAsJsonObject().add("elements",newJsonElement);
                try (PrintWriter out = new PrintWriter(new FileWriter(tEnv().getPagesJsonFile()))) {
                    out.write(new JSONObject(pGson.toJson(element1)).toString(4));
                    System.out.println("Modified object -> "+locatorObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected void locatorStringRemoveInJSON(String locatorObject, String newLocatorsMatched) {
        try {
            if (!locatorObject.contains("=")) {
                Gson pGson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement element1 = JsonParser.parseReader(new FileReader(tEnv().getPagesJsonFile()));
                ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);

                eRepo.getElements().get(locatorObject).getAsJsonObject().addProperty(tEnv().getElementLocator(),"xpath="+newLocatorsMatched);

                JsonElement newJsonElement = JsonParser.parseString(eRepo.getElements().toString());
                element1.getAsJsonObject().add("elements",newJsonElement);
                try (PrintWriter out = new PrintWriter(new FileWriter(tEnv().getPagesJsonFile()))) {
                    out.write(new JSONObject(pGson.toJson(element1)).toString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

    protected String[] fetchBeforeAfterLocators(String locatorObject) {
        String[] beforeAfter = new String[2];
        try {
            //s.startsWith("name")||s.startsWith("xpath")||s.startsWith("classname")||s.startsWith("partiallinktext")||s.startsWith("linktext")||s.startsWith("tagname")||s.startsWith("css")||s.startsWith("id")
            if (!locatorObject.contains("=")) {
                Gson pGson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement element1 = JsonParser.parseReader(new FileReader(tEnv().getPagesJsonFile()));
                ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);
                beforeAfter[0] = eRepo.getElements().get(locatorObject).getAsJsonObject().get("Web_beforeElement").getAsString();
                beforeAfter[1] = eRepo.getElements().get(locatorObject).getAsJsonObject().get("Web_afterElement").getAsString();
            }
        } catch (Exception e) {
            beforeAfter[0] = "";
            beforeAfter[1] = "";
        }
        return beforeAfter;
    }

    protected Set<String> scrapeXpaths(String typeTag)  {
        Set<String> xpaths = new LinkedHashSet<>();
        try {
           List<WebElement> tagElements = browser().findElements(By.tagName(typeTag));
           String xpath = "";
           for (WebElement currentElement : tagElements) {
               xpath = writeXpath(currentElement, typeTag);
               if (!xpath.isEmpty()) {
                   xpaths.add(xpath);
               }
           }
       }catch (StaleElementReferenceException e){
       }
        return xpaths;
    }

    protected Set<String> scrapeXpathsForAndroid() {
        Set<String> xpaths = new LinkedHashSet<>();
        try {
            List<WebElement> elements = android().findElements(By.xpath("//*"));
            for (WebElement currentElement : elements) {
                String elementText = currentElement.getAttribute("text");
                String elementID = currentElement.getAttribute("content-desc");

                if (elementID != null || (!elementText.equals("") && !StringUtils.containsAny(elementText, "<", ">"))) {
                    String xpath = "//*[ ]";
                    if (elementID != null) {
                        xpath = xpath.replace(" ", "@content-desc='" + elementID + "' ");
                        xpaths.add(xpath);
                        xpath = "//*[ ]";
                    }
                    if ((!elementText.equals("") && !StringUtils.containsAny(elementText, "<", ">"))) {
                        xpath = xpath.replace(" ", "@text='" + elementText + "' ");
                        xpaths.add(xpath);
                    }
                }
            }
        }catch (StaleElementReferenceException e){
        }
        return xpaths;
    }

    protected String writeXpath(WebElement element,String typeTag) {
        String xpath = "";
        String elementText = element.getAttribute("innerHTML");
        String elementID = element.getDomAttribute("id");
        String elementName = element.getDomAttribute("name");
        String elementPlaceholder = element.getDomAttribute("placeholder");
        String elementDataTestID1 = element.getDomAttribute("data-testid");
        String elementDataTestID2 = element.getDomAttribute("testID");

        if (elementID != null || elementName != null || elementPlaceholder != null
                ||elementDataTestID1 != null ||elementDataTestID2 != null ||
                (!elementText.equals("") && !StringUtils.containsAny(elementText, "<", ">"))) {
            xpath = "//" + typeTag + "[ ]";
            if (elementID != null) {
                xpath = xpath.replace(" ", "@id='" + elementID + "' ");
            }
            if (elementDataTestID1 != null) {
                xpath = xpath.replace(" ", "@data-testid='" + elementDataTestID1 + "' ");
            }
            if (elementDataTestID2 != null) {
                xpath = xpath.replace(" ", "@testID='" + elementDataTestID2 + "' ");
            }
            if (elementName != null) {
                xpath = xpath.replace(" ", "@name='" + elementName + "' ");
            }
            if (elementPlaceholder != null) {
                xpath = xpath.replace(" ", "@placeholder='" + elementPlaceholder + "' ");
            }
            if ((!elementText.equals("") && !StringUtils.containsAny(elementText, "<", ">"))) {
                xpath = xpath.replace(" ", "text()='" + elementText + "' ");
            }
            if ((StringUtils.countMatches(xpath, "@")) >= 2 ){
                xpath = StringUtils.replace(xpath, "'@", "' or @");
            }
            if ((xpath.contains("@") && xpath.contains("text()"))) {
                xpath = StringUtils.replace(xpath, "'text()", "' or text()");
            }
        }
        return xpath;
    }

    public String selfHeal(String locatorString, String compareString ,String action) {
        List<String> tags = readConfigFileForTags(action);
        List<String> selfHealXpaths = new ArrayList<>();
        String newLocatorFallbacks = "" , beforeXpath = "",afterXpath = "" ;
        if(tEnv().getElementLocator().equalsIgnoreCase("Web")){
            for (String tag : tags) {
                Set<String> xpaths = new LinkedHashSet<>();
                xpaths = scrapeXpaths(tag);
                String match = getClosestMatch(xpaths, compareString);
                if (match != null) {
                    selfHealXpaths.add(match);
                    System.out.println("New healing xpath for "+locatorString+"= with <" + tag + "> = " + match);
                }
            }
        }else if(tEnv().getElementLocator().equalsIgnoreCase("Android")){
            Set<String> xpaths = new LinkedHashSet<>();
            xpaths = scrapeXpathsForAndroid();
            String match = getClosestMatch(xpaths, compareString);
            if (match != null) {
                selfHealXpaths.add(match);
                System.out.println("New healing xpath for "+locatorString+" = " + match);
            }
        }

        if (selfHealXpaths.size()>0) {
            String primaryLocator = selfHealXpaths.get(0);
            try{
                List<WebElement> beforeElements = browser().findElements(By.xpath(primaryLocator + "//preceding::" + StringUtils.substringBetween(primaryLocator, "//", "=") + "]"));
                if(beforeElements.size()>1){
                    beforeXpath = writeXpath(beforeElements.get(beforeElements.size() - 1), beforeElements.get(beforeElements.size() - 1).getTagName());
                }if(beforeElements.size()==1){
                    beforeXpath = writeXpath(beforeElements.get(0), beforeElements.get(0).getTagName());
                }
            }catch(Exception ex){
                beforeXpath="";
            }

            try{
                WebElement afterElement = browser().findElement(By.xpath(primaryLocator + "//following::" + StringUtils.substringBetween(primaryLocator, "//", "=") + "]"));
                afterXpath = writeXpath(afterElement, afterElement.getTagName());
            }catch(Exception ex){
                afterXpath="";
            }

            if (selfHealXpaths.size() == 1) {
                newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(selfHealXpaths.get(0)).concat("}");
            }else if(selfHealXpaths.size() > 1) {
                for (int i = 0; i < selfHealXpaths.size(); i++) {
                    if (i == selfHealXpaths.size() - 1) {
                        newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(selfHealXpaths.get(i)).concat("}");
                    } else {
                        newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(selfHealXpaths.get(i)).concat("}, ");
                    }
                }
            }
            locatorStringReplaceInJSON(locatorString, newLocatorFallbacks,beforeXpath,afterXpath);
            System.out.println("New healing xpaths replaced for locator " + locatorString + " in JSON.");
        }
        return newLocatorFallbacks;
    }

    public void selfHealInConstructor(Set<String> xpaths, String locatorString) {
        String newLocatorFallbacks = "" , beforeXpath = "",afterXpath = "" ;
        List<String> selfHealXpaths = new ArrayList<>();
        String[] locatorArr = getLocatorTypeAndContent(locatorString);
        String compareString = "", oldValueString ="";
        try {
            if(locatorArr[1]!=null && !locatorArr[1].isEmpty()) {
                if (locatorArr[1].contains("'") || locatorArr[1].contains("\"")) {
                    compareString = !(StringUtils.substringBetween(locatorArr[1], "'", "'").isEmpty())
                            ? StringUtils.substringBetween(locatorArr[1], "'", "'")
                            : StringUtils.substringBetween(locatorArr[1], "\"", "\"");
                } else{
                    compareString = locatorArr[1];
                }
            }
            oldValueString = compareString;

            if (compareString != null && !compareString.isEmpty()) {
                List<String> nameSplit = Arrays.stream(compareString.replaceAll("[^A-Za-z0-9]", " ").trim().split(" ")).toList();
                compareString = "";
                for (String name : nameSplit) {
                    if (name.isEmpty()) {
                        continue;
                    } else {
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                        compareString = compareString.concat(name);
                    }
                }
                compareString = StringUtils.uncapitalize(compareString);
                String match = getClosestMatch(xpaths, compareString);
                if (match != null) {
                    selfHealXpaths.add(match);

                    if (selfHealXpaths.size() > 0) {
                        String primaryLocator = selfHealXpaths.get(0);
                        try {
                            List<WebElement> beforeElements = browser().findElements(By.xpath(primaryLocator + "//preceding::" + StringUtils.substringBetween(primaryLocator, "//", "=") + "]"));
                            if (beforeElements.size() > 1) {
                                beforeXpath = writeXpath(beforeElements.get(beforeElements.size() - 1), beforeElements.get(beforeElements.size() - 1).getTagName());
                            }
                            if (beforeElements.size() == 1) {
                                beforeXpath = writeXpath(beforeElements.get(0), beforeElements.get(0).getTagName());
                            }
                        } catch (Exception ex) {
                            beforeXpath = "";
                        }

                        try {
                            WebElement afterElement = browser().findElement(By.xpath(primaryLocator + "//following::" + StringUtils.substringBetween(primaryLocator, "//", "=") + "]"));
                            afterXpath = writeXpath(afterElement, afterElement.getTagName());
                        } catch (Exception ex) {
                            afterXpath = "";
                        }

                        if (selfHealXpaths.size() == 1) {
                            if(locatorArr[0].equalsIgnoreCase("accessibilityid")){
                                locatorArr[1]="//*[@content-desc='"+oldValueString+"']";
                            }
                            for (String healXpath: selfHealXpaths) {
                                if(locatorArr[1].replaceAll("\\s","").equals(healXpath.replaceAll("\\s",""))){
                                    locatorArr[1]="";
                                }
                            }
                            if(!locatorArr[1].isEmpty()) {
                                newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(locatorArr[1]).concat("}, ");
                            }
                            newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(selfHealXpaths.get(0)).concat("}");
                        } else if (selfHealXpaths.size() > 1) {
                            for (int i = 0; i < selfHealXpaths.size(); i++) {
                                if (i == selfHealXpaths.size() - 1) {
                                    newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(selfHealXpaths.get(i)).concat("}");
                                } else {
                                    newLocatorFallbacks = newLocatorFallbacks.concat("{").concat(selfHealXpaths.get(i)).concat("}, ");
                                }
                            }
                        }
                    }
                    locatorStringReplaceInJSON(locatorString, newLocatorFallbacks, beforeXpath, afterXpath);
                }
            }
        }catch (Exception ex){
            hardFail("Issue healing for compareString" + compareString);
        }
    }

    protected List<String> readConfigFileForTags(String action) {
        Properties prop = new Properties();
        List<String> tagsList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/Configuration/web_selfhealing.properties");
            prop.load(fis);
            String tags = prop.getProperty(action);
            tagsList = Arrays.stream(tags.split(",")).collect(Collectors.toList());
        } catch (Exception ex) {
        }
        return tagsList;
    }

    protected String getClosestMatch(Set<String> xpaths, String targetString) {
        String closest = null, closest1 = null, closest2 = null;
        double score1 = 0, score2 = 0;
        for (String xpath : xpaths) {
            double maxLength = Double.max(xpath.length(), targetString.length());
            if (maxLength > 0) {
                double newScore = (maxLength - StringUtils.getLevenshteinDistance((StringUtils.substringBetween(xpath,"'","'")).replaceAll("\\s","").replaceAll("[^a-zA-Z0-9]", "").toLowerCase(), targetString.toLowerCase())) / maxLength;
                if (newScore > score2) {
                    if(newScore > score1){
                        score2 = score1;
                        score1 = newScore;
                        closest2 = closest1;
                        closest1 = xpath;
                    }else{
                        score2 = newScore;
                        closest2 = xpath;
                    }
                }
            }
        }
        List<String> locatorStringMatchArray = Arrays.stream(targetString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")).collect(Collectors.toList());
        int closest1Count = 0, closest2Count = 0;
        for (String matchStr : locatorStringMatchArray) {
            if (score1 > 0.80 && (StringUtils.substringBetween(closest1,"'","'")).replaceAll("\\s", "").toLowerCase().contains(matchStr.toLowerCase())) {
                closest1Count++;
            }
            if (score2 > 0.80 && (StringUtils.substringBetween(closest2,"'","'")).replaceAll("\\s", "").toLowerCase().contains(matchStr.toLowerCase())) {
                closest2Count++;
            }
        }
        if (closest1Count == closest2Count) {
            closest = null;
        } else if (closest1Count > closest2Count) {
            closest = closest1;
        } else if (closest2Count > closest1Count) {
            closest = closest2;
        }
        if(closest!=null && !closest.isEmpty()){
            System.out.println("Healing locator Match Score - "+score1);
        }
        return closest;
    }

    protected String[] getLocatorTypeAndContent(String s, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        String[] locArray = new String[2];
        String rawLocator = locatorString(s);
        try {
            if (wait_logReport_isPresent_Up_Down_XpathValues.length == 0) {

                locArray[0] = rawLocator.substring(0, rawLocator.indexOf('='));
                if (!rawLocator.contains("{")) {
                    locArray[1] = rawLocator.substring(rawLocator.indexOf('=') + 1);
                } else {
                    locArray[1] = rawLocator.substring(rawLocator.indexOf("{"));
                }
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
                    if (!rawLocator.contains("{")) {
                        locArray[1] = rawLocator1.substring(rawLocator.indexOf('=') + 1);
                    } else {
                        locArray[1] = rawLocator1.substring(rawLocator.indexOf("{"));
                    }
                } else {
                    locArray[1] = rawLocator1.substring(rawLocator1.indexOf('=') + 1);
                }
            }
        } catch (Exception e){
            locArray[0] = "";
            locArray[1] = "";
        }
        return locArray;
    }

    protected void alertHandling(String expectedText, String alertAction, String textAction) {
        if (browser() != null) {
            webDriverWait = new WebDriverWait(browser(), Duration.ofSeconds(30));
        } else if (android() != null) {
            webDriverWait = new WebDriverWait(android(), Duration.ofSeconds(30));
        } else if (ios() != null) {
            webDriverWait = new WebDriverWait(ios(), Duration.ofSeconds(30));
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

    protected void textVerification(String actual, String expected, String textAction, String... description) {
        try {
            if ((actual != null) && (expected != null)) {
                switch (textAction.toLowerCase()) {
                    case "partial":
                        if (!actual.isEmpty()) {
                            customAssertPartialEquals(actual, expected, description);
                        } else {
                            if (expected.equals(actual)) {
                                customAssertPartialEquals(actual, expected, description);
                            } else {
                                logger.error("Actual or Expected value is empty");
                                logReport("FAIL", "Actual or Expected value is empty");
                            }
                        }
                        break;
                    case "notequal":
                        customAssertNotEquals(actual, expected, description);
                        break;

                    default:
                        customAssertEquals(actual, expected, description);
                        break;
                }

            } else {
                if (expected.equals(actual)) {
                    customAssertEquals(actual, expected);
                } else {
                    logger.error("Actual or Expected value is null");
                    logReport("FAIL", "Actual or Expected value is null");
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
                } else if (expected == null && actual == null) {
                    logReport("PASS", "Actual and Expected value is null");
                } else if (expected == null && actual != null) {
                    logger.error("Actual is [" + actual + "] but the Expected value is null");
                } else if (expected != null && actual == null) {
                    logger.error("Actual is null but the Expected value is [" + expected + "]");
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
            if (browser() == null) {
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence scroll = new Sequence(finger, 0);
                Dimension size= Objects.requireNonNullElse(android(), ios()).manage().window().getSize();

                int anchor = (int) (size.width * anchorPercentage);
                int startPoint = (int) (size.height * startPercentage);
                int endPoint = (int) (size.height * endPercentage);

                scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), anchor, startPoint));
                scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), anchor, endPoint));
                scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                Objects.requireNonNullElse(android(), ios()).perform(List.of(scroll));

            }
        } catch (InvalidArgumentException iae) {
            captureException(iae);
            logger.error("Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : " + startPercentage + " : " + endPercentage + " : " + anchorPercentage);
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
//            if (android() != null) {
//                Dimension screenSize = android().manage().window().getSize();
//                int yMargin = 5;
//                int xMid = screenSize.width / 2;
//                PointOption top = point(xMid, yMargin);
//                PointOption bottom = PointOption.point(xMid, screenSize.height - yMargin);
//
//                TouchAction action = new TouchAction(android());
//                if (show) {
//                    action.press(top);
//                } else {
//                    action.press(bottom);
//                }
//                action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
//                if (show) {
//                    action.moveTo(bottom);
//                } else {
//                    action.moveTo(top);
//                }
//                action.perform();
//            }
//            if (ios() != null) {
//                Dimension screenSize = ios().manage().window().getSize();
//                int yMargin = 5;
//                int xMid = screenSize.width / 2;
//                PointOption top = PointOption.point(xMid, yMargin);
//                PointOption bottom = PointOption.point(xMid, screenSize.height - yMargin);
//
//                TouchAction action = new TouchAction(ios());
//                if (show) {
//                    action.press(top);
//                } else {
//                    action.press(bottom);
//                }
//                action.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
//                if (show) {
//                    action.moveTo(bottom);
//                } else {
//                    action.moveTo(top);
//                }
//                action.perform();
//            }
        } catch (Exception e) {
            captureException(e);
        }
    }

}

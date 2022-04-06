package com.fh.adhoc;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class Yell extends TestLocalController {

    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "data")
    public void yellData(LinkedHashMap<String, Object> tData) {
        author_ScenarioName("Bhaskar", "Yell");
        addHeaderToCustomReport("SNO","region","Page","S.No", "SearchURL", "Source", "businessType", "businessName", "businessURL", "services", "businessWebsite", "businessPhone", "businessAddress", "businessHours", "ratings", "reviews");
        TestModels model = new TestModels();
        String businessType = tData.get("Name").toString();

        String searchURL = tData.get("URL").toString();
        model.navigateToUrl(searchURL);
        model.click("css=button[id=\"CookiePolicyClose\"]", "wait_0", "isPresent");
        do {
            List<WebElement> listOfElements = model.findElements("css=div[class='row businessCapsule--mainRow']");
            if (listOfElements.size() > 0) {
                for (int i = 1; i <= listOfElements.size(); i++) {
                    String businessName = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//a[contains(@class,'businessCapsule--title')]", "wait_0", "isPresent");
                    String services = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--classStrap')]", "wait_0", "isPresent");

                    String businessURL = model.getAttribute("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//a[contains(@class,'businessCapsule--title')]", "href", "wait_0", "isPresent");
                    String businessWebsite = model.getAttribute("xpath = ((//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--ctas')]/a)[2]", "href", "wait_0", "isPresent");
                    model.click("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--ctaItem')]", "wait_0", "isPresent");
                    String businessPhone = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'business--telephone')]//span[contains(@class,'business--telephoneNumber')]", "wait_0", "isPresent");
                    String businessAddress = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//span[@itemprop='address']", "wait_0", "isPresent");
                    String businessHours = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--openingHours') ]", "wait_0", "isPresent");

                    String ratings = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//span[contains(@class,'starRating--average')]", "wait_0", "isPresent");
                    String reviews = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//span[contains(@class,'starRating--total')]", "wait_0", "isPresent");
                    addRowToCustomReport(tData.get("SNO").toString(),tData.get("Region").toString(),tData.get("Page").toString(),String.valueOf(i), searchURL, "PDQ", businessType, businessName, businessURL, services, businessWebsite, businessPhone, businessAddress, businessHours, ratings, reviews);
                }
            }else{
                addRowToCustomReport(tData.get("SNO").toString(),tData.get("Region").toString(),tData.get("Page").toString(),"NA", searchURL, "PDQ", businessType, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA");
            }

            model.click("css=a[data-tracking='DISPLAY:PAGINATION:NEXT']","wait_0", "isPresent");
            hardWait(3000);
        } while (model.isPresent("css=a[data-tracking='DISPLAY:PAGINATION:NEXT']"));

        List<WebElement> listOfElements = model.findElements("css=div[class='row businessCapsule--mainRow']");
        if (listOfElements.size() > 0) {
            for (int i = 1; i <= listOfElements.size(); i++) {
                String businessName = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//a[contains(@class,'businessCapsule--title')]", "wait_0", "isPresent");
                String services = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--classStrap')]", "wait_0", "isPresent");

                String businessURL = model.getAttribute("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//a[contains(@class,'businessCapsule--title')]", "href", "wait_0", "isPresent");
                String businessWebsite = model.getAttribute("xpath = ((//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--ctas')]/a)[2]", "href", "wait_0", "isPresent");
                model.click("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--ctaItem')]", "wait_0", "isPresent");
                String businessPhone = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'business--telephone')]//span[contains(@class,'business--telephoneNumber')]", "wait_0", "isPresent");
                String businessAddress = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//span[@itemprop='address']", "wait_0", "isPresent");
                String businessHours = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//div[contains(@class,'businessCapsule--openingHours') ]", "wait_0", "isPresent");

                String ratings = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//span[contains(@class,'starRating--average')]", "wait_0", "isPresent");
                String reviews = model.getText("xpath=(//div[@class='row businessCapsule--mainRow'])[" + i + "]//span[contains(@class,'starRating--total')]", "wait_0", "isPresent");
                addRowToCustomReport(tData.get("SNO").toString(),tData.get("Region").toString(),tData.get("Page").toString(),String.valueOf(i), searchURL, "PDQ", businessType, businessName, businessURL, services, businessWebsite, businessPhone, businessAddress, businessHours, ratings, reviews);
            }
        }else{
            addRowToCustomReport(tData.get("SNO").toString(),tData.get("Region").toString(),tData.get("Page").toString(),"NA", searchURL, "PDQ", businessType, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA");
        }

    }

    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class, enabled = false)
    @ExcelSheet(name = "parallel")
    public void yellURL(LinkedHashMap<String, Object> tData) {
        author_ScenarioName("Bhaskar", "Yell");
        addHeaderToCustomReport("SNO", "Region", "Page", "Name", "URL");
        TestModels model = new TestModels();
        String region = tData.get("URL").toString();
        model.navigateToUrl(region);
        model.click("css=button[data-tracking=\"ACCEPTCOOKIES\"]", "wait_0", "isPresent");
        List<WebElement> data = model.findElements("css=a[data-tracking=\"SEARCHESINLOCATIONBYLETTER\"]", "wait_0", "isPresent");
        data.forEach(d -> {
            addRowToCustomReport(tData.get("SNO").toString(), region, "Page 1", d.getText(), d.getAttribute("href"));
        });

        List<WebElement> pages = model.findElements("css=a[data-tracking=\"DISPLAY:PAGINATION:NUMBER\"]", "wait_0", "isPresent");
        if (pages.size() > 0) {

            for (int i = 0; i < pages.size(); i++) {
                try {
                    System.out.println(i);
                    pages = model.findElements("css=a[data-tracking=\"DISPLAY:PAGINATION:NUMBER\"]", "wait_0", "isPresent");
                    System.out.println(pages.get(i).getText());
                    String page = pages.get(i).getAttribute("href");
                    System.out.println(pages.get(i).getAttribute("href"));
                    pages.get(i).click();
                    hardWait(3000);
                    List<WebElement> data1 = model.findElements("css=a[data-tracking=\"SEARCHESINLOCATIONBYLETTER\"]", "wait_0", "isPresent");
                    data1.forEach(d -> {
                        addRowToCustomReport(tData.get("SNO").toString(), region, page, d.getText(), d.getAttribute("href"));
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


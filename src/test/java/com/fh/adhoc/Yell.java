package com.fh.adhoc;

import autogenerated.core.TestModels;
import com.fh.core.TestLocalController;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class Yell extends TestLocalController {

    @Test(enabled = false)
    public void yellData() {
        author_ScenarioName("Bhaskar", "Yell");
        addHeaderToCustomReport("S.No", "SearchURL", "Source", "businessType","businessName", "businessURL","services", "businessWebsite", "businessPhone", "businessAddress", "businessHours","ratings","reviews");
        TestModels model = new TestModels();
        String businessType = "Car Wash";

        String searchURL = "https://www.yell.com/ucs/UcsSearchAction.do?scrambleSeed=634404639&keywords=car+wash&location=AA11AA";
        model.navigateToUrl(searchURL);

        List<WebElement> listOfElements = model.findElements("css=div[class='row businessCapsule--mainRow']");
        model.click("css=button[id=\"CookiePolicyClose\"]", "wait_0", "isPresent");
        //(//div[@class='row businessCapsule--mainRow'])[1]//div[contains(@class,'businessCapsule--titSpons')]

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
                addRowToCustomReport(String.valueOf(i), searchURL, "PDQ",businessType,businessName, businessURL,services, businessWebsite, businessPhone, businessAddress,businessHours,ratings,reviews);
            }
        }
    }

    @Test
    public void yellURL(){
        author_ScenarioName("Bhaskar", "Yell");
        addHeaderToCustomReport("Region", "Name", "URL" );
        TestModels model = new TestModels();
        String region = "https://www.yell.com/k/uk-c.html";
        model.navigateToUrl(region);
        model.click("css=button[data-tracking=\"ACCEPTCOOKIES\"]","wait_0","isPresent");
        List<WebElement> data = model.findElements("css=a[data-tracking=\"SEARCHESINLOCATIONBYLETTER\"]","wait_0","isPresent");
        data.forEach(d->{
            addRowToCustomReport(region,d.getText(),d.getAttribute("href"));
        });

        List<WebElement> pages = model.findElements("css=a[data-tracking=\"DISPLAY:PAGINATION:NUMBER\"]","wait_0","isPresent");
        if(pages.size()>0){
            System.out.println(pages.size());
            for (int i =0;i<pages.size();i++) {
                System.out.println(pages.get(i).getText());
                System.out.println(pages.get(i).getAttribute("href"));
                hardWait(2000);
                try{
                    pages.get(i).click();
                }catch (Exception e){
                    e.printStackTrace();
                }

                hardWait(6000);
                List<WebElement> data1 = model.findElements("css=a[data-tracking=\"SEARCHESINLOCATIONBYLETTER\"]", "wait_0", "isPresent");
                data1.forEach(d -> {
                    addRowToCustomReport(region, d.getText(), d.getAttribute("href"));
                });
            }
        }
    }
}

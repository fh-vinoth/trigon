package com.fh.adhoc;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class JustEats extends TestLocalController {

    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "Sheet3")

    public void jEatTest(LinkedHashMap<String, Object> tData) {

        author_ScenarioName("Bhaskar", "Just Eats");
        addHeaderToCustomReport("SNO", "URL", "takeAwayName", "takeAwayAddress", "rating", "reviews", "deliveryFee", "deliveryFeeMessage", "offers", "phone");
        TestModels model = new TestModels();
        String URL = mapKeyFinder(tData, "URL");
        String takeAwayName = "NA";
        String takeAwayAddress = "NA";
        String rating = "NA";
        String reviews = "NA";
        String deliveryFee = "NA";
        String deliveryFeeMessage = "NA";
        String phone = "NA";
        String offers = "NA";
        try {
            model.navigateToUrl(URL);
            //model.click("css=button[data-test-id='accept-all-cookies-button']","wait_0", "isPresent");
            try {
                takeAwayName = browser().findElement(By.cssSelector("h1[data-test-id='restaurant-heading']")).getText();
            } catch (Exception e) {
                takeAwayName = "NA";
            }

            if ( (!takeAwayName.equalsIgnoreCase("NA"))) {
                try {
                    takeAwayAddress = browser().findElement(By.cssSelector("span[data-test-id='header-restaurantAddress']")).getText();
                } catch (Exception e) {
                    takeAwayAddress = "NA";
                }
                try {
                    rating = browser().findElement(By.cssSelector("span[data-test-id='rating-description']")).getText();
                } catch (Exception e) {
                    rating = "NA";
                }
                try {

                    reviews = browser().findElement(By.cssSelector("strong[data-test-id='rating-count-description']")).getText();
                } catch (Exception e) {
                    reviews = "NA";
                }
                try {

                    deliveryFee = browser().findElement(By.cssSelector("p[data-test-id='restaurant-header-delivery-fee-area']")).getText();
                } catch (Exception e) {
                    deliveryFee = "NA";
                }
                try {

                    deliveryFeeMessage = browser().findElement(By.cssSelector("p[data-test-id='header-fees-message']")).getText();
                } catch (Exception e) {
                    deliveryFeeMessage = "NA";
                }
                try {
                    offers = browser().findElement(By.cssSelector("div[data-test-id='promotionsShowcase-item']")).getText();

                } catch (Exception e) {
                    offers = "NA";
                }
//                rating = tModels().getText("css=span[data-test-id='rating-description']", "wait_0", "isPresent");
//                reviews = tModels().getText("css=strong[data-test-id='rating-count-description']", "wait_0", "isPresent");
//                deliveryFee = tModels().getText("css=p[data-test-id='restaurant-header-delivery-fee-area']", "wait_2", "isPresent");
//                deliveryFeeMessage = tModels().getText("css=p[data-test-id='header-fees-message']", "wait_2", "isPresent");
//                offers = tModels().getText("css=div[data-test-id='promotionsShowcase-item']", "wait_0", "isPresent");
                try {
                    browser().findElement(By.xpath("(//span[@class='u-showAboveMid'])[2]")).click();
                    phone = browser().findElement(By.cssSelector("a[data-test-id='allergen-phone-link']")).getText();
                } catch (Exception e) {
                    phone = "NA";
                }
            }
        } catch (Exception e) {
        } finally {
            addRowToCustomReport(mapKeyFinder(tData, "SNO"), URL, takeAwayName, takeAwayAddress, rating, reviews, deliveryFee, deliveryFeeMessage, offers, phone);
            testTearDown();
        }
    }
}
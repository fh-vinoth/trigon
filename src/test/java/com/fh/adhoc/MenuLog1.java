package com.fh.adhoc;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;


public class MenuLog1 extends TestLocalController {

    @Test(dataProvider = "getDataFromExcelParallel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "Sheet3")
    public void menuLogdata(LinkedHashMap<String, Object> tData) {

        author_ScenarioName("Bhaskar", "Menu log");
        addHeaderToCustomReport("S.No", "URL", "takeAwayName", "takeAwayAddress", "rating", "reviews", "deliveryFee", "deliveryFeeMessage", "offers", "phone");
        String URL = tData.get("takeAwayURL").toString();
        String takeAwayName = "NA";
        String takeAwayAddress = "NA";
        String rating = "NA";
        String reviews = "NA";
        String deliveryFee = "NA";
        String deliveryFeeMessage = "NA";
        String phone = "NA";
        String offers = "NA";

        try {
            TestModels model = new TestModels();
            model.navigateToUrl(URL);

            takeAwayName = model.getText("css=h1[data-test-id='restaurant-heading']", "wait_0", "isPresent");
            if (takeAwayName != null) {
                takeAwayAddress = model.getText("css=span[data-test-id='header-restaurantAddress']", "wait_0", "isPresent");
                rating = model.getText("css=span[data-test-id='rating-description']", "wait_0", "isPresent");
                reviews = model.getText("css=strong[data-test-id='rating-count-description']", "wait_0", "isPresent");
                deliveryFee = model.getText("css=p[data-test-id='restaurant-header-delivery-fee-area']", "wait_2", "isPresent");
                deliveryFeeMessage = model.getText("css=p[data-test-id='header-fees-message']", "wait_2", "isPresent");
                offers = model.getText("css=div[data-test-id='promotionsShowcase-item']", "wait_0", "isPresent");

                model.click("xpath=(//span[@class='u-showAboveMid'])[2]", "wait_0", "isPresent");
                phone = model.getText("css=a[data-test-id='allergen-phone-link']");
            }

        } finally {
            addRowToCustomReport(tData.get("Sno").toString(), URL, takeAwayName, takeAwayAddress, rating, reviews, deliveryFee, deliveryFeeMessage, offers, phone);
            testTearDown();
        }
    }

}
    




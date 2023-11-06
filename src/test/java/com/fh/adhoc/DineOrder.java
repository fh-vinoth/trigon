package com.fh.adhoc;

import com.fh.core.FrameworkTestController;
import com.trigon.wrapper.TestModels;

import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class DineOrder extends FrameworkTestController {

    @Test
    public void getSubListOftakeAways() throws InterruptedException {
        //addHeaderToCustomReport("S.NO", "TakeAwayURL", "TakeAwayname", "takeAwayAddress", "takeAwayPhone","takeAwayCity","searchAddress","ratingStar","noOfRating","storeStatus","storeTime");
        TestModels model = new TestModels();
        model.navigateToUrl("https://sit-franchise-foodhub-uk.stage.t2sonline.com/");
        TimeUnit.SECONDS.sleep(10);

//        SCRAPPING - FOR NEW SCREENS -> scrapes the new locators when no fallbacks/Or conditions are set in xpaths
//        GenerateWebXpathsJSON newPageXpath = new GenerateWebXpathsJSON();
//        newPageXpath.findJSON();

//        SCRAPPING FOR EXISTING JSON BEFORE EACH RUN
        //tFH().btnLogIn_click();

//        AUTO-HEALING -> hardly happens only after some time period from previous scrapping process
       // tFH().btnFindTakeaway_click();
        tFH().btnLogIn_click("isPresent");

        TimeUnit.SECONDS.sleep(15);

    }

}

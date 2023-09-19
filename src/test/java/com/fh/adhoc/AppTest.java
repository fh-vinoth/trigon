package com.fh.adhoc;

import com.fh.core.AppFrameworkTestController;
import com.trigon.elements.GenerateWebXpathsJSON;
import com.trigon.wrapper.TestModels;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class AppTest extends AppFrameworkTestController {

    @Test
    public void appTest() throws InterruptedException {
        //addHeaderToCustomReport("S.NO", "TakeAwayURL", "TakeAwayname", "takeAwayAddress", "takeAwayPhone","takeAwayCity","searchAddress","ratingStar","noOfRating","storeStatus","storeTime");
        TestModels model = new TestModels();
        TimeUnit.SECONDS.sleep(5);

        tFHApp().btnPostcodeManual_click();

    }

}

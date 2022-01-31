package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class CReport extends TestLocalController {

    @Test(dataProvider = "getDataFromExcelParallel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "parallel")
    public void parallelTest(LinkedHashMap<String, Object> tData) {
        addHeaderToCustomReport("S.NO", "TakeAwayURL", "TakeAwayname", "takeAwayAddress", "takeAwayPhone");
        TestModels model = new TestModels();
        model.navigateToUrl(tData.get("URL").toString());
        String takeAwayname = null;
        String takeAwayAddress = null;
        String takeAwayPhone = null;
        try {
            takeAwayname = model.getText("css=h1[class^=styled__VenueName]", "wait_0", "isPresent");
            if (takeAwayname != null) {
                List<WebElement> listOfElements = model.findElements("css=div[class^=styled__ContentSection]");
                takeAwayAddress = listOfElements.get(0).getText();
                takeAwayPhone = listOfElements.get(1).getText();
            }

        } finally {
            addRowToCustomReport(tData.get("SNO").toString(), tData.get("URL").toString(), takeAwayname, takeAwayAddress, takeAwayPhone);
        }

    }


}

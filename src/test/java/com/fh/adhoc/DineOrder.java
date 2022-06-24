package com.fh.adhoc;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class DineOrder extends TestLocalController {

    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "dine")
    public void getSubListOftakeAways(LinkedHashMap<String, Object> tData) {
        addHeaderToCustomReport("S.NO", "TakeAwayURL", "TakeAwayname", "takeAwayAddress", "takeAwayPhone","takeAwayCity","searchAddress","ratingStar","noOfRating","storeStatus","storeTime");
        TestModels model = new TestModels();
        model.navigateToUrl(tData.get("SubCityURL").toString());
        hardWait(4000);
        String ratingStar= model.getText("xpath=(//div[contains(@class,'top-outside-wrapper-content-row-one')])[1]","isPresent","wait_0");
        String noOfRating= model.getText("xpath=(//div[contains(@class,'top-outside-wrapper-content-row-two')])[1]","isPresent","wait_0");
        String storeStatus= model.getText("xpath=(//div[contains(@class,'top-outside-wrapper-content-row-one')])[2]","isPresent","wait_0");
        String storeTime= model.getText("xpath=(//div[contains(@class,'top-outside-wrapper-content-row-two')])[2]","isPresent","wait_0");

        String takeAwayname = model.getText("xpath=//div[contains(@class,'top-outside-wrapper-name')]","isPresent","wait_5");;
        String takeAwayAddress = model.getText("xpath=//div[contains(@class,'top-outside-wrapper-address')]","isPresent","wait_5");;
        String takeAwayPhone = model.getText("xpath=//div[contains(@class,'top-outside-wrapper-telephone')]","isPresent","wait_5");;
        String takeAwayCity= model.getText("css=[class^=\"header-search-box-home-design\"]","isPresent","wait_0");
        String searchAddress= model.getText("css=[class^=\"header-search-box-home-address\"]","isPresent","wait_0");

        addRowToCustomReport(tData.get("SNO").toString(),tData.get("SubCityURL").toString(),takeAwayname,takeAwayAddress,takeAwayPhone,takeAwayCity,searchAddress,ratingStar,noOfRating,storeStatus,storeTime);

    }


}

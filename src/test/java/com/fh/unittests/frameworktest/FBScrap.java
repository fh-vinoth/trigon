package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FBScrap extends TestLocalController {

    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "fbScrap")
    public void scrapTest(LinkedHashMap<String,Object> tData){
        addHeaderToCustomReport("URL","TakeawayName","Slogan","Likes","Follow","Checkins","Address","temp","dir","Phone","temp","Website","temp2","temp3","temp4","temp5");
        String URL = tData.get("fbURL").toString();

        List<String> l = new ArrayList<>();
        try{
            browser().get(URL);
            l.add(URL);
            l.add(browser().findElement(By.cssSelector("h1[id='seo_h1_tag'] span")).getText());
            l.add(browser().findElementByCssSelector("h1[id='seo_h1_tag'] div").getText());
            browser().findElementsByCssSelector("div[class='clearfix _ikh']").forEach(a-> l.add(a.getText()));
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            addRowToCustomReport(l);
            hardWait(2000);
        }

    }
}

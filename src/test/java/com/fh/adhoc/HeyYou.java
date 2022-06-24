package com.fh.adhoc;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;

public class HeyYou extends TestLocalController {


    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "SubCities")
    public void getSubListOftakeAways(LinkedHashMap<String, Object> tData){
        addHeaderToCustomReport("S.NO", "TakeAwayURL","TakeAwayname","takeAwayAddress","takeAwayPhone");
        TestModels model = new TestModels();
        model.navigateToUrl(tData.get("SubCityURL").toString());
        String takeAwayname = null;
        String takeAwayAddress = null;
        String takeAwayPhone = null;

        try{
            takeAwayname = model.getText("css=h1[class^=styled__VenueName]","wait_0","isPresent");
            if(takeAwayname!=null){
                List<WebElement> listOfElements = model.findElements("css=div[class^=styled__ContentSection]");
                takeAwayAddress= listOfElements.get(0).getText();
                takeAwayPhone= listOfElements.get(1).getText();
            }

        }finally {
            addRowToCustomReport(tData.get("S.NO").toString(),tData.get("SubCityURL").toString(),takeAwayname,takeAwayAddress,takeAwayPhone);
        }

    }



    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class , enabled = false)
    @ExcelSheet(name = "ListOfCityUrls")
    public void getListOftakeAways(LinkedHashMap<String, Object> tData){
        addHeaderToCustomReport("S.NO", "TakeAwayURL","Sub City URL");
        TestModels model = new TestModels();
        model.navigateToUrl(tData.get("TakeAwayURL").toString());
        String loadMoreXpath = "xpath=//span[contains(text(),'Load more')]";

        while (model.isPresent(loadMoreXpath)){
            model.click(loadMoreXpath);
//                WebDriverWait wait = new WebDriverWait(browser(), 5000);
//                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Load more')]")));
//                WebElement searchButton = browser().findElement(By.xpath("//span[contains(text(),'Load more')]"));
//                clickElement(searchButton);
        }

        List<WebElement> listOfElements = model.findElements("css=a[class='block mt-12pt5 Text-ln26c3-0 Qwhnh']");
        listOfElements.forEach(a -> {
            addRowToCustomReport(tData.get("SNO").toString(),tData.get("TakeAwayURL").toString(), a.getAttribute("href"));
        });

    }

    // This script to get initial list of URLS
    @Test(enabled = false)
    public void heyYouTest() {
        addHeaderToCustomReport("City", "TakeAwayName", "TakeAwayURL");
        dataExtract("Sydney", "https://heyyou.com.au/sydney");
        dataExtract("melbourne", "https://heyyou.com.au/melbourne");
        dataExtract("brisbane", "https://heyyou.com.au/brisbane");
        dataExtract("perth", "https://heyyou.com.au/perth");
    }

    private void dataExtract(String city, String url) {
        TestModels model = new TestModels();

        model.navigateToUrl(url);
        List<WebElement> listOfElements = model.findElements("css=a[class='Suburbsstyled__LetterGroupItem-snrwtw-13 fIfKug']");
        listOfElements.forEach(a -> {
            addRowToCustomReport(city, a.getText(), a.getAttribute("href"));
        });
    }
}

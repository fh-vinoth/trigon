package com.fh.unittests.frameworktest;

import com.fh.core.TestLocalController;
import com.opencsv.CSVWriter;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import com.trigon.wrapper.TestModels;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class CReport extends TestLocalController {

int i = 0;
    @Test(dataProvider = "getDataFromExcelParallel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "parallel")
    public void parallelTest(LinkedHashMap<String, Object> tData){
        addHeaderToCustomReport("S.NO", "TakeAwayURL","TakeAwayname","takeAwayAddress","takeAwayPhone");
        addRowToCustomReport("1","gd","gfdh");
        TestModels model = new TestModels();
        model.navigateToUrl(tData.get("URL").toString());
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
            addRowToCustomReport(tData.get("SNO").toString(),tData.get("SubCityURL").toString(),takeAwayname,takeAwayAddress,takeAwayPhone);
        }

        csvWriter("takeaway.csv");
        try{
            CSVWriter csvWriter = new CSVWriter(new FileWriter("takeaway.csv",true));
            String[] data = new String[]{"a "+i+++"","b","c"};
            csvWriter.writeNext(data);
            csvWriter.close();

        }catch (Exception e){

        }
    }


    private void csvWriter(String filePath){
        if(!new File(filePath).exists()){
            try{
                CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
                String[] data = new String[]{"S.NO", "TakeAwayURL","TakeAwayname","takeAwayAddress","takeAwayPhone"};
                csvWriter.writeNext(data);
                csvWriter.close();

            }catch (Exception e){

            }
        }

    }
}

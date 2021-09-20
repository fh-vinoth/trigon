package com.fh.unittests;

import com.fh.core.TestLocalController;
import com.trigon.annotations.ExcelSheet;
import com.trigon.dataprovider.DataProviders;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class FileSize extends TestLocalController {

    int i =1;
    @Test(dataProvider = "getDataFromExcel", dataProviderClass = DataProviders.class)
    @ExcelSheet(name = "urlTest")
    public void fileSizeTest(LinkedHashMap<String, Object> tData){
        author_ScenarioName("Bhaskar","FileSize");
        addHeaderToCustomReport("S.No","Host",	"file",	"FileSize (KB)");

        long size = cUtils().getFileSizeFromURL(tData.get("file").toString());
        long fileToMB = size/1000;

        addRowToCustomReport(String.valueOf(i++),tData.get("Host").toString(),tData.get("file").toString(),String.valueOf(fileToMB));
        System.out.println(fileToMB);
    }
}

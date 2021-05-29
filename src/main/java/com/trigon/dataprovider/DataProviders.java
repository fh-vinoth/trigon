package com.trigon.dataprovider;

import com.trigon.dataprovider.excel.ExcelDataProvider;
import com.trigon.dataprovider.json.JSONDataProvider;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviders {

    @DataProvider(name = "getDataFromJson")
    public Object[][] jsonDataProvider(Method method) {
        JSONDataProvider jsonDataProvider = new JSONDataProvider();
        Object[][] dataProvider = jsonDataProvider.getJsonData(method.getName());
        return dataProvider;
    }

    @DataProvider(name = "getDataFromExcel")
    public Object[][] excelDataProvider(Method method) {
        ExcelDataProvider excelDataProvider = new ExcelDataProvider();
        Object[][] dataProvider = excelDataProvider.getExcelData(method);
        return dataProvider;
    }
}

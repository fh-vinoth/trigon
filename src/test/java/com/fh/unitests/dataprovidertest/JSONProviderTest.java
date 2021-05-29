package com.fh.unitests.dataprovidertest;


import com.trigon.dataprovider.DataProviders;
import com.trigon.testbase.TestController;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class JSONProviderTest extends TestController {

    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, priority = 1)
    public void amazonTest(LinkedHashMap<String, Object> map) {
        System.out.println(map);
    }

    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, priority = 1)
    public void amazonTest20(LinkedHashMap<String, Object> map) {
        System.out.println(map);
    }

    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, priority = 1)
    public void amazonTest3(LinkedHashMap<String, Object> map) {
        System.out.println(map);
    }
}

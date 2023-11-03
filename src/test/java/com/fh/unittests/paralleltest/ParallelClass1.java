package com.fh.unittests.paralleltest;

import com.trigon.dataprovider.DataProviders;
import com.trigon.testbase.TestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class ParallelClass1 extends TestController {

    private static final Logger logger = LogManager.getLogger(ParallelClass1.class);

    /*@Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, priority = 1)
    public void parallelClass1_Method1(LinkedHashMap<String, Object> map) {
        System.out.println(map);
        logger.error("Executing parallelClass1_Method1");
        logger.debug("Executing parallelClass1_Method1");
        author_ScenarioName("bhaskar_parallelClass1_Method1", "parallelClass1_Method1 scenario");
        logReport("PASS", "parallelClass1_Method1 PASSED");
        parallelClass1_parallelMethod1_SubMethod();
        Assert.fail("u r wrong");


    }*/

    public void parallelClass1_parallelMethod1_SubMethod() {
        logger.info("Executing parallelClass1_parallelMethod1_SubMethod");
        logReport("PASS", "parallelClass1_parallelMethod1_SubMethod FAILED");
    }
}

package com.fh.unittests.paralleltest;

import com.trigon.dataprovider.DataProviders;
import com.trigon.testbase.TestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class ParallelClass4 extends TestController {
    private static final Logger logger = LogManager.getLogger(ParallelClass4.class);


    @Test(groups = "future")
    public void parallelClass4_Method1() {
        logger.info("Executing parallelClass4_Method1");
        author_ScenarioName("bhaskar_parallelClass4_Method1", "parallelClass4_Method1 scenario");
        logReport("PASS", "parallelClass4_Method1 PASSED1");
        parallelClass4_parallelMethod1_SubMethod();
    }

    public void parallelClass4_parallelMethod1_SubMethod() {
        logger.info("Executing parallelClass4_parallelMethod1_SubMethod");
        logReport("PASS", "parallelClass4_parallelMethod1_SubMethod PASSED1");
    }

    @Test(groups = "future")
    public void parallelClass4_Method2() {
        logger.info("Executing parallelClass4_Method2");
        author_ScenarioName("bhaskar_parallelClass4_Method2", "parallelClass4_Method2 scenario");
        logReport("PASS", "parallelClass4_Method2 PASSED1");
        logReport("FAIL", "parallelClass4_Method2 FAILED1");
        logReport("PASS", "parallelClass4_Method2 PASSED2");
        logReport("FAIL", "parallelClass4_Method2 FAILED2");
        parallelClass4_parallelMethod2_SubMethod();
    }

    public void parallelClass4_parallelMethod2_SubMethod() {
        logger.info("Executing parallelClass4_parallelMethod2_SubMethod");
        logReport("FAIL", "parallelClass4_parallelMethod2_SubMethod FAILED1");
        logReport("FAIL", "parallelClass4_parallelMethod2_SubMethod FAILED2");
        logReport("PASS", "parallelClass4_parallelMethod2_SubMethod PASSED1");
        logReport("PASS", "parallelClass4_parallelMethod2_SubMethod PASSED2");
    }

    @Test(dataProvider = "getDataFromJson", dataProviderClass = DataProviders.class, priority = 1)
    public void parallelClass4_Method3(LinkedHashMap<String, Object> map) {
        System.out.println(map);
        logger.info("Executing parallelClass4_Method3");
        author_ScenarioName("bhaskar_parallelClass4_Method3", "parallelClass4_Method3 scenario");
        logReport("PASS", "parallelClass4_Method3 PASSED1");
        logReport("PASS", "parallelClass4_Method3 PASSED2");
        logReport("PASS", "parallelClass4_Method3 PASSED3");
        parallelClass4_parallelMethod3_SubMethod();
    }

    public void parallelClass4_parallelMethod3_SubMethod() {
        logger.info("Executing parallelClass4_parallelMethod3_SubMethod");
        logReport("PASS", "parallelClass4_parallelMethod3_SubMethod PASSED1");
        logReport("PASS", "parallelClass4_parallelMethod3_SubMethod PASSED2");
        logReport("PASS", "parallelClass4_parallelMethod3_SubMethod PASSED3");
    }

    @Test(groups = "Regression")
    public void parallelClass4_Method4() {
        logger.info("Executing parallelClass4_Method4");
        author_ScenarioName("bhaskar_parallelClass4_Method4", "parallelClass4_Method4 scenario");
        logReport("PASS", "parallelClass4_Method4 PASSED1");
        logReport("FAIL", "parallelClass4_Method4 FAILED1");
        logReport("FAIL", "parallelClass4_Method4 FAILED2");
        logReport("PASS", "parallelClass4_Method4 PASSED2");
        parallelClass4_parallelMethod4_SubMethod();
    }

    public void parallelClass4_parallelMethod4_SubMethod() {
        logger.info("Executing parallelClass4_parallelMethod4_SubMethod");
        logReport("PASS", "parallelClass4_parallelMethod4_SubMethod PASSED1");
        logReport("FAIL", "parallelClass4_parallelMethod4_SubMethod FAILED1");
        logReport("FAIL", "parallelClass4_parallelMethod4_SubMethod FAILED2");
        logReport("PASS", "parallelClass4_parallelMethod4_SubMethod PASSED2");
    }
}

package com.fh.unittests.paralleltest;

import com.trigon.testbase.TestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class ParallelClass2 extends TestController {

    private static final Logger logger = LogManager.getLogger(ParallelClass2.class);

    @Test(groups = {"future", "Regression"})
    public void parallelClass2_Method1() throws InterruptedException {
        logger.info("Executing parallelClass2_Method1");
        author_ScenarioName("bhaskar_parallelClass2_Method1", "parallelClass2_Method1 scenario");
        testStatus("PASS", "parallelClass2_Method1 PASSED");
        //Thread.sleep(10000);
        parallelClass2_parallelMethod1_SubMethod();
    }

    public void parallelClass2_parallelMethod1_SubMethod() {
        logger.info("Executing parallelClass2_parallelMethod1_SubMethod");
        testStatus("PASS", "parallelClass2_parallelMethod1_SubMethod FAILED");
    }

    @Test
    public void parallelClass2_Method2() {
        logger.info("Executing parallelClass2_Method2");
        author_ScenarioName("bhaskar_parallelClass2_Method2", "parallelClass2_Method2 scenario");
        testStatus("PASS", "parallelClass2_Method2 PASSED");
        parallelClass2_parallelMethod2_SubMethod();
    }

    public void parallelClass2_parallelMethod2_SubMethod() {
        logger.info("Executing parallelClass2_parallelMethod2_SubMethod");
        testStatus("PASS", "parallelClass2_parallelMethod2_SubMethod FAILED");
    }
}

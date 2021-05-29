package com.fh.unitests.paralleltest;

import com.trigon.testbase.TestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class ParallelClass3 extends TestController {
    private static final Logger logger = LogManager.getLogger(ParallelClass3.class);


    @Test
    public void parallelClass3_Method1() {
        logger.info("Executing parallelClass3_Method1");
        author_ScenarioName("bhaskar_parallelClass3_Method1", "parallelClass3_Method1 scenario");
        testStatus("FAIL", "parallelClass3_Method1 FAILED1");
        parallelClass3_parallelMethod1_SubMethod();
    }

    public void parallelClass3_parallelMethod1_SubMethod() {
        logger.info("Executing parallelClass3_parallelMethod1_SubMethod");
        testStatus("FAIL", "parallelClass3_parallelMethod1_SubMethod FAILED1");
    }

    @Test
    public void parallelClass3_Method2() {
        logger.info("Executing parallelClass3_Method2");
        author_ScenarioName("bhaskar_parallelClass3_Method2", "parallelClass3_Method2 scenario");
        testStatus("PASS", "parallelClass3_Method2 PASSED1");
        testStatus("PASS", "parallelClass3_Method2 PASSED2");
        parallelClass3_parallelMethod2_SubMethod();
    }

    public void parallelClass3_parallelMethod2_SubMethod() {
        logger.info("Executing parallelClass3_parallelMethod2_SubMethod");
        testStatus("PASS", "parallelClass3_parallelMethod2_SubMethod PASSED1");
        testStatus("PASS", "parallelClass3_parallelMethod2_SubMethod PASSED2");
    }

    @Test
    public void parallelClass3_Method3() {
        logger.info("Executing parallelClass3_Method3");
        author_ScenarioName("bhaskar_parallelClass3_Method3", "parallelClass3_Method3 scenario");
        testStatus("PASS", "parallelClass3_Method3 PASSED1");
        testStatus("FAIL", "parallelClass3_Method3 FAILED1");
        testStatus("FAIL", "parallelClass3_Method3 FAILED2");
        parallelClass3_parallelMethod3_SubMethod();
    }

    public void parallelClass3_parallelMethod3_SubMethod() {
        logger.info("Executing parallelClass3_parallelMethod3_SubMethod");
        testStatus("PASS", "parallelClass3_parallelMethod3_SubMethod PASSED1");
        testStatus("FAIL", "parallelClass3_parallelMethod3_SubMethod FAILED1");
    }

}

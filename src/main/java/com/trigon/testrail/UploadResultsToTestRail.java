package com.trigon.testrail;

import com.trigon.reports.ReportManager;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.trigon.testrail.TestRailTestStatusMap.checkHeapSize;

public class UploadResultsToTestRail {
    @Parameters({"TestRailStatusPath", "product"})
    @Test
    public void updateTestRailTestStatus(String testRailStatusPath, String product) {
        try {
            ReportManager r = new ReportManager();
            String testRunId = r.getTestRunId(product);
            r.uploadBulkTestResultToTestRail(testRunId, testRailStatusPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

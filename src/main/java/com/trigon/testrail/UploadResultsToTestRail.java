package com.trigon.testrail;

import com.trigon.reports.ReportManager;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UploadResultsToTestRail {
    @Parameters({"TestRailStatusPath", "product", "autoUpload"})
    @Test
    public void updateTestRailTestStatus(String testRailStatusPath, String product,String autoUpload) {
        try {
            ReportManager r = new ReportManager();
            String testRunId = r.getTestRunId(product);
            if (testRailStatusPath.contains("s3.amazonaws.com")) {
                String split = testRailStatusPath.split("s3.amazonaws.com/")[1];
                String bucketName = split.split("/")[0]+"/"+split.split("/")[1];
                String keyName = split.split(bucketName)[1].substring(1);
                testRailStatusPath = r.readS3BucketContent(bucketName,keyName);
                if(autoUpload.equalsIgnoreCase("true")){
                    r.uploadBulkTestResultToTestRail(testRunId, testRailStatusPath);
                }
            }else{
                r.uploadBulkTestResultToTestRail(testRunId, testRailStatusPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

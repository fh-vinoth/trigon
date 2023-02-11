package com.trigon.reports;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.trigon.security.AES;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ReportAnalyser {

    /***
     * @author -> Srikrishna
     * @param partialSuiteName -> Pass the suite name For Eg. MYT_Android_Regression_UK_Automation
     * @param date_DMMMYYYY -> DateFormat should be DD-MMM-YYYY, eg. 18-Jul-2022
     */
    public String getEmailReportOf(String date_DMMMYYYY, String partialSuiteName) {
        return listBucketObjects(date_DMMMYYYY, partialSuiteName);
    }


    private String listBucketObjects(String date_DMMMYYYY, String partialSuiteName) {
        String amazonaws_url = "https://s3.amazonaws.com/";
        String bucketName = "t2s-staging-automation";
        String prefix = "TestResults_2.8/" + date_DMMMYYYY;
        String reportUrl = amazonaws_url + bucketName + "/";
        List<String> reportUrlList = new LinkedList<>();
        try {
            AWSCredentials credentials = new BasicAWSCredentials(
                    AES.decrypt("W2ekXre8CE/HcVRqyloQgvx8gdNF7SukcZP/Gzx2aGY=", "t2sautomation"),
                    AES.decrypt("7vxMJLkwfL7VK8SksBb/ReNbhwbPtwjT9fHRCo1hutb6hXbOH/U/3c8Tad49ieBp", "t2sautomation")
            );
            final AmazonS3 s3 = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion("us-east-1")
                    .build();
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
                    .withPrefix(prefix + "/").withDelimiter("/");
            List<String> res = s3.listObjects(listObjectsRequest).getCommonPrefixes();
            res.forEach(v -> {
                if (v.contains(partialSuiteName)) {
                    reportUrlList.add(v);
                }
            });
            if (reportUrlList.size() < 1) {
                //throw new AmazonClientException("No reports available for the selected date " + date_DMMMYYYY + ". Please check the date format or the date again.");
                reportUrl = "no-report";
            }else {
                Collections.sort(reportUrlList);
                reportUrl = reportUrl + reportUrlList.get(reportUrlList.size() - 1) + "testSummary.json";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportUrl;
    }
}
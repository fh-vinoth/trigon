package com.trigon.testbase;

import org.testng.annotations.BeforeSuite;

import static com.trigon.reports.ReportManager.cUtils;

public class ConverterBase {

    @BeforeSuite
    public void beforeSuite() {
        cUtils().deleteFile("${sys:logFilename}");
        cUtils().deleteFile("${sys:loghtmlfile}");
    }

}


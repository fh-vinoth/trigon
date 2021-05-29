package com.trigon.testbase;

import org.testng.annotations.BeforeSuite;

public class ConverterBase {

    @BeforeSuite
    public void beforeSuite() {
        TestUtilities.cUtils().deleteFile("${sys:logFilename}");
        TestUtilities.cUtils().deleteFile("${sys:loghtmlfile}");
    }

}


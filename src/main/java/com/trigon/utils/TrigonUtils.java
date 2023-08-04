package com.trigon.utils;

import com.trigon.reports.ReportManager;
import com.trigon.wrapper.TestModels;

import java.util.Collection;
import java.util.HashMap;

public class TrigonUtils extends ReportManager {

    TestModels helper = new TestModels();

    public String mapKeyFinder(HashMap<String, Object> map, String KeyName) {
        return helper.mapKeyFinder(map, KeyName);
    }

    public void compareText(String actualText, String expectedText, String textAction,String... description) {
        helper.compareText(actualText, expectedText, textAction,description);
    }

    public void compareText(Collection<?> actualText, Collection<?> expectedText, String textAction) {
        helper.compareText(actualText, expectedText, textAction);
    }

    @Override
    public void logApiReport(String status, String message) {
        super.logApiReport(status, message);
    }

    @Override
    public void logStepAction(String message,String... testCaseID) {
        super.logStepAction(message,testCaseID);
    }

    @Override
    public void logReport(String status, String message, String... wait_logReport_isPresent_Up_Down_XpathValues) {
        super.logReport(status, message, wait_logReport_isPresent_Up_Down_XpathValues);
    }


}

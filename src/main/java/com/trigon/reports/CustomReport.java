package com.trigon.reports;

import org.testng.Assert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class CustomReport extends Initializers{

    public void addDataToHeader(String... values){
        if(!cReportPojo.isCustomReportStartFlag()){
            List<String> headerData = new ArrayList<>();
            if(values.length>0){
                for(int i=0;i<values.length;i++){
                    headerData.add(values[i]);
                }
            }
            cReportPojo.setCustomHeaderData(headerData);
        }
    }

    public void addHeaderToCustomReport(String... values) {
        if (!cReportPojo.isCustomReportStartFlag()) {
            initCustomReport("CustomReport",tEnv().getCurrentTestMethodName(),values);
        }
    }

    protected void initCustomReport(String reportName,String reportTask,String... values){
        try {
            cReportPojo.setCustomReport(new BufferedWriter(new FileWriter(trigonPaths.getTestResultsPath() + "/"+reportName+".html")));
            cReportPojo.getCustomReport().write("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Custom Email Report</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                    "    <tbody>\n" +
                    "    <tr style=\"background: #e0dbdb;height: 60px;\">\n" +
                    "        <th style=\"padding-left: 30px;text-align: left\"><img alt=\"FoodHub\" height=\"20\"\n" +
                    "                 src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\"\n" +
                    "                 width=\"114\"></th>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>\n" +
                    "            <div><div>Executed By : " + System.getProperty("user.name") + "</div>\n" +
                    "            <div>Executed OS : " + System.getProperty("os.name") + "</div>\n");

            if(cReportPojo.getCustomHeaderData().size()>0){
                for (String data : cReportPojo.getCustomHeaderData()) {
                    try {
                        cReportPojo.getCustomReport().write("            <div>" + data + "</div>\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            cReportPojo.getCustomReport().write("</div>\n        </td>\n" +
                    "    </tr>");

            if(values.length>0) {
                cReportPojo.getCustomReport().write("    <tr>\n" +
                        "        <td>\n" +
                        "            <div style=\"margin: 10px\">\n" +
                        "                <table style=\"background-color: #fcf8f8;border: 1px solid #EEEEEE;width: 98%;border-radius: 10px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                        "                    <tbody>\n" +
                        "                    <tr style=\"background: #e0dbdb;height: 40px\">\n" +
                        "                        <td colspan=\""+values.length+"\">\n" +
                        "                            Task Name : " + reportTask + "\n" +
                        "                        </td>\n" +
                        "                    </tr>\n");
                cReportPojo.setCustomReportHeaderSize(values.length);
            }

            cReportPojo.setCustomReportStartFlag(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(values.length>0){
            try{
                cReportPojo.getCustomReport().write(" <tr style=\"background: #F3F3F3;height: 40px;\">\n");
                for(int i=0;i<values.length;i++){
                    cReportPojo.getCustomReport().write("<td> "+values[i]+"</td>\n");
                }
                cReportPojo.getCustomReport().write(       "                    </tr>\n");
            }catch (Exception e){

            }

        }
        try {
            cReportPojo.getCustomReport().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRowToCustomReport(String... values) {

        if(cReportPojo.getCustomReport()!=null){
            if(values.length>0){
                try{
                    cReportPojo.getCustomReport().write(" <tr style=\"height: 40px;\">\n");
                    for(int i = 0; i< values.length; i++){
                        cReportPojo.getCustomReport().write("<td> "+ values[i]+"</td>\n");
                    }
                    cReportPojo.getCustomReport().write(       "                    </tr>\n");
                }catch (Exception e){
                    Assert.fail("Add Headers for Custom Report using addHeaderToCustomReport before you add Row to CustomReport");
                }

            }
            try {
                cReportPojo.getCustomReport().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private synchronized void compareCustomReportValues(List<String> status, String actual, String expected) {
        if (expected.equals(actual)) {
            status.add("PASS");
        } else {
            if (status.contains("<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>FAILED</strong></font></p>")) {
                status.add("EXP:" + expected + "<br/>" + "ACT :" + actual + "<br/>");
            } else {
                status.add("<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>FAILED</strong></font></p>");
                status.add("EXP:" + expected + "<br/>" + "ACT :" + actual + "<br/>");
            }

        }
    }

    private synchronized void compareCustomReportValues(List<String> status, Collection<?>
            actual, Collection<?> expected) {

        int actualValueSize = actual.size();
        int expectedValueSize = expected.size();

        List<Object> expectedlist = new ArrayList<>();
        for (Object expValue : expected) {
            expectedlist.add(expValue);
        }
        List<Object> actuallist = new ArrayList<>();
        for (Object actValue : actual) {
            actuallist.add(actValue);
        }

        if (expectedValueSize == actualValueSize) {

            if ((expectedlist.equals(actuallist))) {

                status.add("PASS");
            } else {

                actuallist.removeAll(expectedlist);
                expectedlist.removeAll(actuallist);
                if (status.contains("<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>FAILED</strong></font></p>")) {
                    status.add("EXP:" + expectedlist + "<br/>" + "ACT :" + actuallist + "<br/>");
                } else {
                    status.add("<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>FAILED</strong></font></p>");
                    status.add("EXP:" + expectedlist + "<br/>" + "ACT :" + actuallist + "<br/>");
                }

            }

        } else {

            actuallist.removeAll(expectedlist);
            expectedlist.removeAll(actuallist);
            if (status.contains("<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>FAILED</strong></font></p>")) {
                status.add("EXP:" + expectedlist + "<br/>" + "ACT :" + actuallist + "<br/>");
            } else {
                status.add("<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>FAILED</strong></font></p>");
                status.add("EXP:" + expectedlist + "<br/>" + "ACT :" + actuallist + "<br/>");
            }

        }


    }

    protected synchronized String addExpectedActualToCustomReport(String actual, String expected) {
        StringBuffer addToReport = new StringBuffer();
        if (expected.equals(actual)) {
            addToReport.append("EXP:" + expected);
            addToReport.append("<br/>");
            addToReport.append("ACT:" + actual);
        } else {
            addToReport.append("EXP:" + expected);
            addToReport.append("<br/>");
            addToReport.append("ACT:" + "<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>" + actual + "</strong></font></p>");
        }

        customReportStatusMap.put(actual, expected);
        return addToReport.toString();
    }

    protected synchronized String addExpectedActualToCustomReport(Collection<?> actual, Collection<?> expected) {
        StringBuffer addToReport = new StringBuffer();
        if (expected.equals(actual)) {
            addToReport.append("EXP:" + expected);
            addToReport.append("<br/>");
            addToReport.append("ACT:" + actual);
        } else {
            addToReport.append("EXP:" + expected);
            addToReport.append("<br/>");
            addToReport.append("ACT:" + "<p><font face=\"verdana\" size=\"2\" color=\"red\"><strong>" + actual + "</strong></font></p>");
        }

        customReportStatusMap1.put(actual, expected);
        return addToReport.toString();
    }

    private String finalStatusToCustomReport() {

        String finalStatus = "NA";

        return finalStatus;
    }

}

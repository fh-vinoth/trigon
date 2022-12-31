package com.trigon.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class TestRailReportNew extends Initializers {



    public void initTestRailReport() {
        try {
            ReportManager m = new ReportManager();
            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReportNew.html";
            String reportTask = "NA";
            if (tEnv() != null) {
                reportTask = tEnv().getCurrentTestMethodName();
            }
            if (!new File(htmlFile).exists()) {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile));
                int logoHeaderLength = 1;
                System.out.println(logoHeaderLength);
                 htmlWriter.write("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>FoodHub Custom Email Report</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<table style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                        "    <tbody>\n" +
                        "    <th style=\"padding-left: 30px;text-align: left;background: #e0dbdb;height: 60px;\"><img alt=\"FoodHub\" height=\"20\" src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                        "    <th colspan=\""+logoHeaderLength+"\" style=\"text-align: center;background: #e0dbdb;height: 60px;\"> Task Name : Tests Status</th>\n" +
                        "    <th style=\"text-align: left;background: #e0dbdb;height: 60px;\">\n" +
                        "        <div>Executed By : "+System.getProperty("user.name")+"</div>\n" +
                        "        <div>Executed OS : "+System.getProperty("os.name") +"</div>\n" +
                        "        <div>TestRail Link : <a href =\"https://www.testrail.com\"> Navigate to TestRail</a></div>\n" +
                        "    </th>\n" +
                        "<style> td {\n" +
                        "  border: 3px solid black;\n" +
                        "  border-color: #e0e4e4;\n" +
                        "} \n"+
                        "</style>\n"+
                        "    <tr style=\"background: #797575;height: 40px; font-weight: bold;color: #faf8f8\">\n");
                    htmlWriter.write("<td> ClassName </td>\n");
                    htmlWriter.write("<td> TestId </td>\n");
                    htmlWriter.write("<td> Status </td>\n");
                    //htmlWriter.write("<td> Skipped </td>\n");
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addRowToTestRailReport(String... values) {
        try {
            if (values.length > 0) {
                int value1Count  = values[1].split(",").length;
                int value2Count  = values[2].split(",").length;
                int value3Count  = values[3].split(",").length;
                int overalSpanCount = value1Count+value2Count+value3Count;
                String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReportNew.html";

                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));
                for (int i = 0; i < values.length; i++) {
                    if(i==0){
                        htmlWriter.write("<td rowspan=\""+(overalSpanCount)+"\"> " + values[i] + "</td>\n");
                    }else{
                        if(values[i].length()>0) {
                            String[] testId = values[i].split(",");
                            for (String testIds : testId) {
                                if(testIds.length()>0) {
                                    String testCaseId = testIds.split("_")[0];
                                    String status = testIds.split("_")[1];
                                    htmlWriter.write("  <tr><td>" + testCaseId + "</td><td>" + status + "</td></tr>\n");
                                }
                            }
                        }
                    }
                }
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void tearDownCustomReport() {
        String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReportNew.html";
        if (new File(htmlFile).exists()) {
            try {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));
                htmlWriter.write("    </tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>");
                htmlWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void initTestRailReportNew() {
        try {
            ReportManager m = new ReportManager();
            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReportNew.html";
            String reportTask = "NA";
            if (tEnv() != null) {
                reportTask = tEnv().getCurrentTestMethodName();
            }
            if (!new File(htmlFile).exists()) {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile));
                int logoHeaderLength = 1;
                System.out.println(logoHeaderLength);
                htmlWriter.write("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>FoodHub TestRail Email Report</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<table id = \"myTable\" style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                        "    <tbody>\n" +
                        "    <th style=\"padding-left: 30px;text-align: left;background: #e0dbdb;height: 60px;\"><img alt=\"FoodHub\" height=\"20\" src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                        "    <th colspan=\"" + logoHeaderLength + "\" style=\"text-align: center;background: #e0dbdb;height: 60px;\"> Task Name : Tests Status</th>\n" +
                        "    <th style=\"text-align: left;background: #e0dbdb;height: 60px;\" >\n" +
                        "        <div>Executed By : " + System.getProperty("user.name") + "</div>\n" +
                        "        <div>Executed OS : " + System.getProperty("os.name") + "</div>\n" +
                        "        <div>TestRail Link : <a href =\"https://www.testrail.com\"> Navigate to TestRail</a></div>\n" +
                        "    </th>\n" +
                        "<style> td {\n" +
                        "  border: 3px solid black;\n" +
                        "  border-color: #e0e4e4;\n" +
                        "} \n" +
                        "</style>\n" +
                        "    <tr style=\"background: #797575;height: 40px; font-weight: bold;color: #faf8f8\">\n");
                htmlWriter.write("<td> ClassName </td>\n");
                htmlWriter.write("<td> TestId </td>\n");
                htmlWriter.write("<th><input \n" +
                        "                    type=\"text\" \n" +
                        "                    id=\"myInput\" \n" +
                        "                    onkeyup=\"myFunction()\" \n" +
                        "                    placeholder=\"Status\" \n" +
                        "                    list=\"testStatus\" \n" +
                        "                   \n" +
                        "                    ></th>\n" +
                        "                    <datalist id=\"testStatus\">\n" +
                        "                        <option value=\"Passed\">\n" +
                        "                        <option value=\"Failed\">\n" +
                        "                        <option value=\"Skipped\">\n" +
                        "                      </datalist>\n" +
                        "                    </th>\n" +
                        "<script>\n" +
                        "            \n" +
                        "                    function myFunction(){\n" +
                        "                     \n" +
                        "                             const trs = document.querySelectorAll('#myTable tr:not(.header)')\n" +
                        "               const filter = document.querySelector('#myInput').value\n" +
                        "               const regex = new RegExp(filter, 'i')\n" +
                        "               const isFoundInTds = td => regex.test(td.innerHTML)\n" +
                        "               const isFound = childrenArr => childrenArr.some(isFoundInTds)\n" +
                        "               const setTrStyleDisplay = ({ style, children }) => {\n" +
                        "                 style.display = isFound([\n" +
                        "                   ...children \n" +
                        "                 ]) ? '' : 'none' \n" +
                        "                         }\n" +
                        "                         trs.forEach(setTrStyleDisplay)\n" +
                        "                     }\n" +
                        "                     </script>");
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

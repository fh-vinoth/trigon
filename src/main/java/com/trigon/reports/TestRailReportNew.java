package com.trigon.reports;

import com.aventstack.extentreports.ExtentReports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class TestRailReportNew extends Initializers {


    public void initTestRailReport() {
        try {
            ReportManager m = new ReportManager();
            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";
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
                        "    <th colspan=\"" + logoHeaderLength + "\" style=\"text-align: center;background: #e0dbdb;height: 60px;\"> Task Name : Tests Status</th>\n" +
                        "    <th style=\"text-align: left;background: #e0dbdb;height: 60px;\">\n" +
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
                htmlWriter.write("<td> Status </td>\n");
                /* htmlWriter.write("<td> TestRail Link </td>\n");*/
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addRowToTestRailReport(String... values) {
        try {
            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";

            BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));

            if (values.length > 0) {
                int value1Count=0,value2Count=0,value3Count=0;
                 if(values[1].length()>0){
                    value1Count = values[1].split(",").length;
                  }
                if(values[2].length()>0){
                    value2Count = values[2].split(",").length;
                }
                if(values[3].length()>0){
                    value3Count = values[3].split(",").length;
                }
                int overalSpanCount = value1Count + value2Count + value3Count;
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        htmlWriter.write("<td rowspan=\"" + (overalSpanCount+1)+ "\"> " + values[i] + "</td>\n");
                    } else {
                        if (values[i].length() > 0) {
                            String[] testId = values[i].split(",");
                            for (String testIds : testId) {
                                if (testIds.length() > 0) {
                                    String testCaseId = testIds.split("_")[0];
                                    String testCaseIdRC = "";
                                    if(testCaseId.contains(" ")){
                                        testCaseIdRC = testCaseId.substring(2);
                                    }else{
                                        testCaseIdRC = testCaseId.substring(1);
                                    }

                                    String status = testIds.split("_")[1];
                                    htmlWriter.write("  <tr><td>" + testCaseId + "</td>" +
                                            "<td>" + status.toUpperCase() + "</td>" +
                                            "<td><a href=\"https://touch2success.testrail.com/index.php?/cases/view/" + testCaseIdRC + "\"> Click here - " + testCaseId + "</a></td>" +
                                            "</tr>\n");
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
        String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";
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

    public void initTestRailReportNew(ExtentReports stats) {
        try {
            String  file  ="" ;
            String suiteWithTime = stats.getReport().getSystemEnvInfo().get(1).getValue();
            String localFile = trigonPaths.getTestResultsPath() + "TestStatus.json";
            Path path = Paths.get(localFile);
            boolean f = Files.exists(path);
            if(f){
                file = localFile;
            }else{
                file = "\"https://s3.amazonaws.com/t2s-staging-automation/TestResults_2.8/" + getSuiteExecutionDate + "/" + suiteWithTime + "/TestStatus.json\"";
            }

            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";
            if (!new File(htmlFile).exists()) {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile));
                int logoHeaderLength = 2;
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
                        "        <div>TestRail Link : <a href =\"#fileLink\">TestRail Upload Link</a></div>\n" +
                        "       <div class=\"myDiv\" id=\"fileLink\" style=\"display:none\">"+file+"</div>\n" +
                        "    </th>\n" +
                        "<style> td {\n" +
                        "  border: 3px solid black;\n" +
                        "  border-color: #e0e4e4;\n" +
                        "} \n" +
                        "::placeholder{\n" +
                        "  color: #faf8f8;\n" +
                        "}\n" +
                        "a{\n" +
                        "color: #00008B;\n" +
                        "}\n" +
                        ".myDiv:target{\n" +
                        "display:block !important;\n" +
                        "}\n" +
                        "</style>\n" +
                        "    <tr style=\"background: #797575;height: 40px; font-weight: bold;color: #faf8f8\">\n");
                htmlWriter.write("<td> Class_Method Name </td>\n");
                htmlWriter.write("<td> Testcase Id </td>\n");
                htmlWriter.write("<th><input \n" +
                        "                    type=\"text\" \n" +
                        "                    id=\"myInput\" \n" +
                        "                    onkeyup=\"myFunction()\" \n" +
                        "                    placeholder=\"Status\" \n" +
                        "                    list=\"testStatus\" \n" +
                        "                    style=\"background-color: #797575 ;outline: none;border-top-style: hidden;border-bottom-style: hidden;border-right-style: hidden;border-left-style: hidden; font-weight:bold;font-family: Roboto,sans-serif;font-size: 13px;text-align: center;\"" +
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
                htmlWriter.write("<td> TestRail Link </td>\n");
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

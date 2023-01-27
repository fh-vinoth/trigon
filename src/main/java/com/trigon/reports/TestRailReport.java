package com.trigon.reports;

import com.aventstack.extentreports.ExtentReports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class TestRailReport extends ReportManager {


    public void addRowToTestRailReport(String... values) {
        try {
            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";

            BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));

            if (values.length > 0) {
                int value1Count = 0, value2Count = 0, value3Count = 0;
                if (values[1].length() > 0) {
                    value1Count = values[1].split(",").length;
                }
                if (values[2].length() > 0) {
                    value2Count = values[2].split(",").length;
                }
                if (values[3].length() > 0) {
                    value3Count = values[3].split(",").length;
                }
                int overalSpanCount = value1Count + value2Count + value3Count;
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        htmlWriter.write("<td class=\"data-row\"  rowspan=\"" + (overalSpanCount + 1) + "\"> " + values[i] + "</td>\n");
                    } else {
                        if (values[i].length() > 0) {
                            String[] testId = values[i].split(",");
                            for (String testIds : testId) {
                                if (testIds.length() > 0) {
                                    String testCaseId = testIds.split("_")[0];
                                    String testCaseIdRC = "";
                                    if (testCaseId.contains(" ")) {
                                        testCaseIdRC = testCaseId.substring(2);
                                    } else {
                                        testCaseIdRC = testCaseId.substring(1);
                                    }

                                    String status = testIds.split("_")[1];
                                    htmlWriter.write("  <tr class=\"data-row\" data-option=\""+status.toUpperCase()+"\"><td><a href=\"https://touch2success.testrail.com/index.php?/cases/view/" + testCaseIdRC + "\" target=\"_blank\" rel=\"noopener noreferrer\">" + testCaseId + "</a></td>" +
                                            "<td>" + status.toUpperCase() + "</td>" +
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

    public void initTestRailReport(ExtentReports stats, String jenkinsExecution) {
        try {
            String file = "";
            if (jenkinsExecution.equalsIgnoreCase("true")) {
                String suiteWithTime = stats.getReport().getSystemEnvInfo().get(1).getValue();
                file = "\"https://s3.amazonaws.com/t2s-staging-automation/TestResults_2.8/" + getSuiteExecutionDate + "/" + suiteWithTime + "/TestStatus.json\"";
                System.out.println("Jenkins Execution");
                System.out.println("**** Jenkins Execution: " + file + " File");
            } else {
                file = trigonPaths.getTestResultsPath() + "TestStatus.json";
            }

            String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";
            if (!new File(htmlFile).exists()) {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile));
                int logoHeaderLength = 1;
                htmlWriter.write("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>FoodHub TestRail Email Report</title>\n" +
                        "</head>\n" +
                        "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\n" +
                        "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>" +
                        "<body>\n" +
                        "<table style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                        "    <tbody>\n" +
                        "    <th style=\"padding-left: 30px;text-align: left;background: #e0dbdb;height: 60px;\"><img alt=\"FoodHub\" height=\"20\" src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                        "    <th colspan=\"" + logoHeaderLength + "\" style=\"text-align: center;background: #e0dbdb;height: 60px;\"> Task Name : Tests Status</th>\n" +
                        "    <th style=\"text-align: center;background: #e0dbdb;height: 60px;\" >\n" +
                        "        <div>Executed By : " + System.getProperty("user.name") + "</div>\n" +
                        "        <div>Executed OS : " + System.getProperty("os.name") + "</div>\n" +
                        "        <div id=\"viewLink\"><a style=\"cursor:pointer;\">Copy TestRail Upload Link </a><img src = \"https://cdn-icons-png.flaticon.com/512/1620/1620767.png\" height=\"10\" width=\"10\"></div>\n" +
                        " <div id=\"copyLinkDiv\">" +
                        "    </th>\n" +
                        "  </tbody>\n" +
                        "</table>" +
                        "<table id=\"myTable\"\n" +
                        "       style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                        "    <tbody>" +
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
                htmlWriter.write("<td class=\"classHead\"> Class_Method Name </td>\n");
                htmlWriter.write("<td class=\"requiredHead\"> Testcase Id </td>\n");
                htmlWriter.write(" <td class=\"requiredHead\">" +
                        " <select id=\"table-filter\" style=\"background-color: #797575 ;outline: none;border-top-style: hidden;border-bottom-style: hidden;border-right-style: hidden;border-left-style: hidden; font-weight:bold;font-family: Roboto,sans-serif;font-size: 13px;text-align: center;color : #faf8f8; \">\n" +
                        "                <option value=\"STATUS\">Status</option>\n" +
                        "                <option value=\"PASSED\">Passed</option>\n" +
                        "                <option value=\"FAILED\">Failed</option>\n" +
                        "                <option value=\"SKIPPED\">Skipped</option>\n" +
                        "            </select>\n" +
                        "        </td></tr>");
                htmlWriter.write(
                        "<script>\n" +
                                "            \n" +
                                "   $(document).ready(function() {\n" +
                                "  $('#table-filter').change(function() {\n" +
                                "    var selectedOption = $(this).val();\n" +
                                "    $('.data-row').hide();\n" +
                                "    $('.classHead').hide();\n" +
                                "    $('.requiredHead').css('width', '55%');\n" +
                                "    $('.data-row[data-option=' + selectedOption + ']').show();\n" +
                                "  });\n" +
                                "});" +
                                "               document.getElementById(\"viewLink\").addEventListener(\"click\", getLink);\n" +
                                "                  function getLink(){\n" +
                                "                     document.execCommand(\"copy\");\n" +
                                "                     }\n" +
                                "                      document.getElementById(\"viewLink\").addEventListener(\"copy\", function(event) {\n" +
                                "  event.preventDefault();\n" +
                                "  if (event.clipboardData) {\n" +
                                "    event.clipboardData.setData(\"text/plain\",\"" + file + "\");" +
                                "  document.getElementById(\"viewLink\").innerHTML = \"Link Copied!!!\" \n" +
                                "  }\n" +
                                "});   " +

                                "                     </script>");
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

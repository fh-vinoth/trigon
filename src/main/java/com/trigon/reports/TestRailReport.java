package com.trigon.reports;

import org.testng.ITestContext;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class TestRailReport extends Initializers {



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
                int logoHeaderLength = 2;
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
                        "  border: 1px solid black;\n" +
                        "  border-color: #e0e4e4;\n" +
                        "} \n"+
                        "</style>\n"+
                        "    <tr style=\"background: #797575;height: 40px; font-weight: bold;color: #faf8f8\">\n");
                    htmlWriter.write("<td> ClassName </td>\n");
                    htmlWriter.write("<td> Passed </td>\n");
                    htmlWriter.write("<td> Failed </td>\n");
                    htmlWriter.write("<td> Skipped </td>\n");
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
                String htmlFile = trigonPaths.getTestResultsPath() + "/TestRailReport.html";

                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));
                htmlWriter.write(" <tr style=\"height: 40px;\">\n");
                for (int i = 0; i < values.length; i++) {
                    htmlWriter.write("<td> " + values[i] + "</td>\n");
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
}

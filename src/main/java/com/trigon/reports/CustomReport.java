package com.trigon.reports;

import com.google.gson.stream.JsonWriter;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.trigon.testbase.TestInitialization.trigonPaths;

public class CustomReport extends Initializers {

    public void addHeaderToCustomReport(String... headers) {
        initCustomReport(headers);
    }

    private void initCustomReport(String... headers) {
        try {
            String csvFile = trigonPaths.getTestResultsPath() + "/CustomReport.csv";
            String htmlFile = trigonPaths.getTestResultsPath() + "/CustomReport.html";
            String reportTask = "NA";
            if (tEnv() != null) {
                reportTask = tEnv().getCurrentTestMethodName();
            }
            if (!new File(csvFile).exists()) {
                try {
                    CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));
                    String[] headerLine = new String[]{reportTask,System.getProperty("user.name"),System.getProperty("os.name")};
                    csvWriter.writeNext(headerLine);
                    csvWriter.writeNext(headers);
                    csvWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!new File(htmlFile).exists()) {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile));
                int logoHeaderLength = 0;

                if(headers.length>3){
                    logoHeaderLength = headers.length-2;
                }
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
                        "    <th colspan=\""+logoHeaderLength+"\" style=\"text-align: center;background: #e0dbdb;height: 60px;\"> Task Name : "+reportTask+"</th>\n" +
                        "    <th style=\"text-align: left;background: #e0dbdb;height: 60px;\">\n" +
                        "        <div>Executed By : "+System.getProperty("user.name")+"</div>\n" +
                        "        <div>Executed OS : "+System.getProperty("os.name") +"</div>\n" +
                        "    </th>\n" +
                        "    <tr style=\"background: #797575;height: 40px; font-weight: bold;color: #faf8f8\">\n");

                for (int i = 0; i < headers.length; i++) {
                    htmlWriter.write("<td> " + headers[i] + "</td>\n");
                }
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addRowToCustomReport(String... values) {
        try {
            if (values.length > 0) {
                String csvFile = trigonPaths.getTestResultsPath() + "/CustomReport.csv";
                String htmlFile = trigonPaths.getTestResultsPath() + "/CustomReport.html";

                CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile, true));
                csvWriter.writeNext(values);
                csvWriter.close();

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

    public void addRowToCustomReportForFailure(String... values) {
        try {
            if (values.length > 0) {
                String csvFile = trigonPaths.getTestResultsPath() + "/CustomReport.csv";
                String htmlFile = trigonPaths.getTestResultsPath() + "/CustomReport.html";

                CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile, true));
                csvWriter.writeNext(values);
                csvWriter.close();

                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));
                htmlWriter.write(" <tr style=\"height: 40px;\">\n");
                for (int i = 0; i < values.length; i++) {
                    htmlWriter.write("<td style=color:red> " + values[i] + "</td>\n");
                }
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRowToCustomReport(List<String> values) {
        try {
            if (values.size() > 0) {
                String csvFile = trigonPaths.getTestResultsPath() + "/CustomReport.csv";
                String htmlFile = trigonPaths.getTestResultsPath() + "/CustomReport.html";

                String[] arrayValues = values.stream().toArray(String[]::new);
                CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile, true));
                csvWriter.writeNext(arrayValues);
                csvWriter.close();

                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));
                htmlWriter.write(" <tr style=\"height: 40px;\">\n");
                for (int i = 0; i < values.size(); i++) {
                    htmlWriter.write("<td> " + values.get(i) + "</td>\n");
                }
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void tearDownCustomReport(ITestContext iTestContext) {
        String htmlFile = trigonPaths.getTestResultsPath() + "/CustomReport.html";
        if (new File(htmlFile).exists()) {
            try {
                BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(htmlFile, true));
                htmlWriter.write("    </tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>");
                htmlWriter.close();
                JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(trigonPaths.getSupportFileHTMLPath() + "/" + "CustomReport.json")));
                writer.beginObject().name("subject").value(iTestContext.getSuite().getName());
                String customData = FileUtils.readFileToString(new File(htmlFile), StandardCharsets.UTF_8);
                String replaceWidth = customData.replace("90%", "100%");
                writer.name("customBody").value(replaceWidth);
                writer.endObject().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

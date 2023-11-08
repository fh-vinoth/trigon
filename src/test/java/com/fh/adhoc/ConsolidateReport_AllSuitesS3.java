package com.fh.adhoc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import com.trigon.email.ITriggerEmail;
import com.trigon.email.TriggerEmailImpl;
import com.trigon.reports.ReportAnalyser;
import com.trigon.testbase.TestController;
import com.trigon.testbase.TestInitialization;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class ConsolidateReport_AllSuitesS3 extends TestController {


    @AfterSuite(alwaysRun = true, dependsOnMethods = {"suiteClosure"})
    public void KibanaAutomationSendOfflineEmail() {
        try {
            ITriggerEmail emailObject = new TriggerEmailImpl();
            emailObject.triggerCustomEmail(TestInitialization.trigonPaths.getTestResultsPath().toString(), "sandhyarani.m@foodhub.com");
        } catch (Exception e) {
            hardFail(e);
        }
    }

    @Test()
    @Parameters({"suiteName"})
    public void triageFailureReport(String suiteName) throws IOException {
        try {


            initCustomReport("S.NO", "Module Name", "TotalTC", "Pass% ", "Fail%","Skip%", "PassedTC", "FailedTC", "SkippedTC", "Execution StartedAt", "Execution Time", "Report");
            int num = 1;
            String[] suitNames = suiteName.split(",");
            for (String suit : suitNames) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/d");
                String today = sdf.format(DateUtils.addDays(new Date(), 0));
                String yesterday = sdf.format(DateUtils.addDays(new Date(), -1));
//                if (today.split("-")[1].length() == 4) {
//                    today = today.replace(today.split("-")[1], today.split("-")[1].substring(0, 3));
//                }
//                if (yesterday.split("-")[1].length() == 4) {
//                    yesterday = yesterday.replace(yesterday.split("-")[1], yesterday.split("-")[1].substring(0, 3));
//                }
                com.trigon.reports.ReportAnalyser reportAnalyser = new ReportAnalyser();
                String partialSuiteName = suit;

                String TURL = reportAnalyser.getEmailReportOf(today, partialSuiteName);
                if (!TURL.equalsIgnoreCase("no-report")) {
//                    Map<String, String> tMap = capturingReportDetails(TURL);
                    Map<String, String> tMap = capturingReportDetails2(TURL);
                    addRowToCustomReportWithLink(String.valueOf(num), suit, tMap.get("TotalScenarios"), tMap.get("passPer"), tMap.get("failPer"),tMap.get("skipPer"), tMap.get("passedTc"), tMap.get("failedTc"), tMap.get("skippedTc"), tMap.get("ExecutionStartTime"), tMap.get("TestExecutionTime"), tMap.get("URL"));
                } else {
                    String YURL = reportAnalyser.getEmailReportOf(yesterday, partialSuiteName);
                    Map<String, String> yMap = capturingReportDetails2(YURL);
                    addRowToCustomReportWithLink(String.valueOf(num), suit, yMap.get("TotalScenarios"), yMap.get("passPer"), yMap.get("failPer"),yMap.get("skipPer") ,yMap.get("passedTc"), yMap.get("failedTc"), yMap.get("skippedTc"), yMap.get("ExecutionStartTime"), yMap.get("TestExecutionTime"), yMap.get("URL"));
                }
                num = num + 1;
            }
        } catch (Exception e) {
            hardFail(e);
        }

    }

    public Map<String, String> capturingReportDetails(String URL) {
        Map<String, String> failureTriageMap = new TreeMap<>();
        String resp = "";
        try {
            if (URL.equalsIgnoreCase("no-report")) {
                failureTriageMap.put("no-report", "no-report");
            } else {

                URL = URL.replace("s3.amazonaws.com/fh-qa-automation", "fh-qa-automation.s3.amazonaws.com");

                RestAssured.useRelaxedHTTPSValidation();
                RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
                resp = requestSpecification.get(URL).then().extract().response().getBody().prettyPrint();
                resp = StringUtils.substringBetween(resp, "{\n" +
                        "    ", ",\n" +
                        "    \"results\": [");
                resp = "{" + resp + "}";
                Gson gson = new Gson();
                URL = URL.replace("testSummary.json", "EmailReport.html");


                // Parse JSON String to JsonObject
                JsonObject jsonObject = gson.fromJson(resp, JsonObject.class);
                String pass_percentage = jsonObject.get("pass-percentage").getAsString();
                String fail_percentage = jsonObject.get("fail-percentage").getAsString();
                String skip_percentage = jsonObject.get("skip-percentage").getAsString();
                failureTriageMap.put("pass_percentage", pass_percentage);
                failureTriageMap.put("fail_percentage", fail_percentage);
                failureTriageMap.put("skip_percentage", skip_percentage);
                failureTriageMap.put("URL", URL);
            }
        } catch (Exception e) {
            System.out.println(e + resp);
        }
        return failureTriageMap;
    }

    public Map<String, String> capturingReportDetails2(String URL) {
        Map<String, String> failureTriageMap = new TreeMap<>();
        String resp = "";
        try {
            if (URL.equalsIgnoreCase("no-report")) {
                failureTriageMap.put("URL", "NA");
                failureTriageMap.put("passedTc", "NA");
                failureTriageMap.put("failedTc", "NA");
                failureTriageMap.put("skippedTc", "NA");
                failureTriageMap.put("TotalScenarios", "NA");
                failureTriageMap.put("passPer", "NA");
                failureTriageMap.put("failPer", "NA");
                failureTriageMap.put("skipPer", "NA");
                failureTriageMap.put("TestExecutionTime", "NA");
                failureTriageMap.put("ExecutionStartTime", "NA");
            } else {
                URL = URL.replace("testSummary.json", "SupportFiles/HTML/emailBody.json");
//                URL = URL + "SupportFiles/HTML/emailBody.json";
                RestAssured.useRelaxedHTTPSValidation();
                RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
                resp = requestSpecification.get(URL).then().extract().response().getBody().prettyPrint();
//                resp = StringUtils.substringBetween(resp, "20rem;\\\">" +
//                        "</div>: [");
//                resp = "{" + resp + "}";
//                System.out.println(resp);
//                Gson gson = new Gson();
                String passedTc = StringUtils.substringBetween(resp, "PASSED(", ")");
                String failedTc = StringUtils.substringBetween(resp, "FAILED/EXCEPTIONS(", ")");
                String skippedTc = StringUtils.substringBetween(resp, "SKIPPED(", ")");
                String TotalScenarios = String.valueOf(Integer.parseInt(passedTc) + Integer.parseInt(failedTc) + Integer.parseInt(skippedTc));
                String passPer = StringUtils.substringBetween(resp, "Pass : ", "% |");
                String failPer = StringUtils.substringBetween(resp, "Fail : ", "% |");
                URL = URL.replace("SupportFiles/HTML/emailBody.json", "testSummary.json");
                RestAssured.useRelaxedHTTPSValidation();
                requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
                resp = requestSpecification.get(URL).then().extract().response().getBody().prettyPrint();

                String TotalTimeExecution = StringUtils.substringBetween(resp, "\"total-time\": \"", "\",");
                String ExecutionStartTime = StringUtils.substringBetween(resp, "\"start-time\": \"", "\",");
                String skipPer = StringUtils.substringBetween(resp, "\"skip-percentage\": ",",");
                URL = URL.replace("testSummary.json", "EmailReport.html");
                failureTriageMap.put("URL", URL);
                failureTriageMap.put("passedTc", passedTc);
                failureTriageMap.put("failedTc", failedTc);
                failureTriageMap.put("skippedTc", skippedTc);
                failureTriageMap.put("TotalScenarios", TotalScenarios);
                failureTriageMap.put("passPer", passPer + "%");
                failureTriageMap.put("failPer", failPer + "%");
                failureTriageMap.put("skipPer", skipPer + "%");
                failureTriageMap.put("TestExecutionTime", TotalTimeExecution);
                failureTriageMap.put("ExecutionStartTime", ExecutionStartTime);
            }
        } catch (Exception e) {
            System.out.println(e + resp);
        }
        return failureTriageMap;
    }

    public void addHeaderToCustomReport(String... headers) {
        initCustomReport(headers);
    }

    private void initCustomReport(String... headers) {
        try {
            String csvFile = trigonPaths.getTestResultsPath() + "/CustomReport.csv";
            String htmlFile = trigonPaths.getTestResultsPath() + "/CustomReport.html";
            String reportTask = "Automation Summary Dash Board";
            if (tEnv() != null) {
                reportTask = "Automation Summary Dash Board";
            }
            if (!new File(csvFile).exists()) {
                try {
                    CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));
                    String[] headerLine = new String[]{reportTask, System.getProperty("user.name"), System.getProperty("os.name")};
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

                if (headers.length > 4) {
                    logoHeaderLength = headers.length - 3;
                }
                System.out.println(logoHeaderLength);
                htmlWriter.write("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>FoodHub Custom Email Report</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<table  style=\"width: 90%; border: 1px solid #000; font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: left;margin-left:auto;margin-right:auto;overflow: hidden;border-width: .5px;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                        "    <tbody style=\"height=\"30\" \">\n" +
                        "    <th colspan=2 style=\"text-align: left;background: #6CA6CD;height: 60px;\"><img alt=\"FoodHub\" height=\"20\" src=\"https://fh-qa-automation.s3.amazonaws.com/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                        "    <th colspan=\"" + logoHeaderLength + "\" style=\"text-align: center;background: #6CA6CD;color: black;height: 70px;\"> " + reportTask + "</th>\n" +
                        "    <th style=\"text-align: left;background: #6CA6CD;height: 60px;\">\n" +
//                        "        <div>Executed By : " + System.getProperty("user.name") + "</div>\n" +
//                        "        <div>Executed OS : " + System.getProperty("os.name") + "</div>\n" +
                        "    </th>\n" +
                        "    <tr style=\"background: #797575;height: 40px; font-weight: bold;color: #faf8f8\">\n");

                if (headers.length == 12) {
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:2%; background: #6CA6CD;\n\" > " + headers[0] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:7%;background: #6CA6CD;\n\" > " + headers[1] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black; width:3%;background: #6CA6CD; \n\" > " + headers[2] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:3%;background: #6CA6CD;\n\" > " + headers[3] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:4%;background: #6CA6CD;\n\" > " + headers[4] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:4%;background: #6CA6CD;\n\" > " + headers[5] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:4%;background: #6CA6CD;\n\" > " + headers[6] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:3%;background: #6CA6CD;\n\" > " + headers[7] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:20%;background:#6CA6CD;\n\" > " + headers[8] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:9%;background: #6CA6CD;\n\" > " + headers[9] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:5%;background: #6CA6CD;\n\" > " + headers[10] + "</td>\n");
                    htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 20px;padding-left: 20px;height: 40px; border-color: black;color: black;width:5%;background: #6CA6CD;\n\" > " + headers[11] + "</td>\n");

                }
//                for (int i = 0; i < headers.length; i++) {
//                    htmlWriter.write("<td style=\"padding-right: 30px;padding-left: 20px;height: 10px; border-color: black;  width:5%;  border-width: .1px;\" >" + headers[i] + "</td>\n");
//                }
                htmlWriter.write("                    </tr>\n");
                htmlWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addRowToCustomReportWithLink(String... values) {
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
                    if (i != values.length - 1) {
                        htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 30px;padding-left: 20px;height: 40px; border-color: black; background:#B0E2FF;color:black;font-weight:600;  border-width: .1px;\"  > " + values[i] + "</td>\n");
                    } else {
                        if (values[i].equals("NA")) {
                            htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 30px;padding-left: 20px;height: 40px; border-color: black;background:#B0E2FF; color:black;font-weight:600;border-width: .1px;\"   >NA</td>\n");
                        } else {
                            htmlWriter.write("<td style=\"border: 1px solid #000; padding-right: 30px;padding-left: 20px;height: 40px; border-color: black; background:#B0E2FF;color:black;font-weight:600; border-width: .1px;\"   ><a href='" + values[i] + "'>Report_link</a></td>\n");
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
}


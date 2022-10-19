package com.trigon.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.reports.model.TestSummary;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.time.DateUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TriageFailureReport {


    @Test()
    @Parameters({"suiteName"})
    public void triageFailureReport(String suiteName) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("d-MMM-yyyy");
        String today = sdf.format(DateUtils.addDays(new Date(), -2));
        String yesterday = sdf.format(DateUtils.addDays(new Date(), -3));
        String daybeforeyesterday = sdf.format(DateUtils.addDays(new Date(), -4));
        com.trigon.reports.ReportAnalyser reportAnalyser = new ReportAnalyser();
        String partialSuiteName = suiteName;


        //author_ScenarioName("Ganesh M", "Triage Failure Report");

        String DBYURL = reportAnalyser.getEmailReportOf(daybeforeyesterday, partialSuiteName);

        String YURL = reportAnalyser.getEmailReportOf(yesterday, partialSuiteName);

        String TURL = reportAnalyser.getEmailReportOf(today, partialSuiteName);


        Map<String, String> dbyMap = capturingFailures(DBYURL);
        Map<String, String> yMap = capturingFailures(YURL);
        Map<String, String> tMap = capturingFailures(TURL);

        //addHeaderToCustomReport("Scenario: Failure Reason", daybeforeyesterday, yesterday, today);


        File f = new File("src/test/resources/TestResults/Triage.html");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

        if (dbyMap.isEmpty() && yMap.isEmpty() && tMap.isEmpty()) {

            String passContent = "There is no Failures for past three days";
            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<title>FoodHub Failure Triage Report</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\"><thead>\n" +
                    "<style>\n" +
                    "  table, th, td {\n" +
                    "  border: 1px solid black;\n" +
                    "  border-collapse: collapse;\n" +
                    "}</style>\n" +
                    "<thead>\r\n" +
                    "<tr>\r\n" +
                    "<th style=\"padding-left: 30px;text-align: left;background: #e0dbdb;height: 60px;\"><img alt=\"FoodHub\" height=\"20\" src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                    "<th colspan=\"3\" style=\"text-align: center;background: #e0dbdb;height: 60px;\">Failure Triage Report</th>" +
                    "</tr>\r\n" +
                    "</thead>\r\n" +
                    "\r\n" +


                    "<thead>\r\n" +
                    "<tr>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">Module : Scenario</th>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">" + daybeforeyesterday + "</th>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">" + yesterday + "</th>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">" + today + "</th>\r\n" +
                    "</tr>\r\n" +
                    "</thead>\r\n" +
                    "<tbody border=\"0\">\r\n" +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%; colspan=\"4\"\">" + passContent + "</td>\r\n" +
                    "</tr>\r\n" +
                    "</tbody>\n" +
                    "</table>";

            bw.write(html);
            bw.close();


        } else {

            AtomicReference<String> html = new AtomicReference<>("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<title>FoodHub Failure Triage Report</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table style=\"width: 90%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\"><thead>\n" +
                    "<style>\n" +
                    "  table, th, td {\n" +
                    "  border: 1px solid black;\n" +
                    "  border-collapse: collapse;\n" +
                    "}</style>\n" +
                    "<thead>\r\n" +
                    "<tr>\r\n" +
                    "<th style=\"padding-left: 30px;text-align: left;background: #e0dbdb;height: 60px;\"><img alt=\"FoodHub\" height=\"20\" src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                    "<th colspan=\"3\" style=\"text-align: center;background: #e0dbdb;height: 60px;\">Failure Triage Report</th>" +
                    "</tr>\r\n" +
                    "</thead>\r\n" +
                    "\r\n" +


                    "<thead>\r\n" +
                    "<tr>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">Module : Scenario</th>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">" + daybeforeyesterday + "</th>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">" + yesterday + "</th>\r\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">" + today + "</th>\r\n" +
                    "</tr>\r\n" +
                    "</thead>\r\n" +
                    "<tbody border=\"0\">\r\n");

            AtomicReference<String> daybeforeYesterdayBGColor = new AtomicReference<>("background-color: #ccccccc4");
            AtomicReference<String> yesterdayBGColor = new AtomicReference<>("background-color: #ccccccc4");
            AtomicReference<String> todayBGColor = new AtomicReference<>("background-color: #ccccccc4");
            dbyMap.forEach((module, result) -> {
                if (result.equalsIgnoreCase(yMap.get(module)) && result.equalsIgnoreCase(tMap.get(module))) {
                    daybeforeYesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    yesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    todayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                } else if (yMap.get(module).equalsIgnoreCase(tMap.get(module))) {
                    yesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    todayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    //daybeforeYesterdayBGColor.set("background-color: #ccccccc4");
                } else if (result.equalsIgnoreCase(tMap.get(module))) {
                    daybeforeYesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    todayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    //yesterdayBGColor.set("background-color: #ccccccc4");
                } else{
                    daybeforeYesterdayBGColor.set("background-color: #ccccccc4");
                    yesterdayBGColor.set("background-color: #ccccccc4");
                    todayBGColor.set("background-color: #ccccccc4");
                }

                html.set(html +
                        "<tr>\r\n" +
                        "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%;\">" + module + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + daybeforeYesterdayBGColor + "; font-style:calibri;font-size:14px;\">" + result + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + yesterdayBGColor + ";font-style:calibri;font-size:14px;\">" + yMap.get(module) + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + todayBGColor + ";font-style:calibri;font-size:14px;\">" + tMap.get(module) + "</td>\r\n" +
                        "</tr>\r\n");
                yMap.remove(module);
                tMap.remove(module);
            });

            yMap.forEach((module, result) -> {
                AtomicReference<String> backgroundColor = new AtomicReference<>("");
                String empty = "---";
                if (result.equalsIgnoreCase(tMap.get(module))) {
                    backgroundColor.set("background-color:  #ccccccc4;color:#f74242;");
                } else {
                    backgroundColor.set("background-color:  #ccccccc4;");
                }
            html.set(html +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word width:25%;\">" + module+ "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + empty + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;" + backgroundColor + ";font-style:calibri;font-size:14px;\">" + result + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;" + backgroundColor + ";font-style:calibri;font-size:14px;\">" + tMap.get(module) + "</td>\r\n" +
                    "</tr>\r\n");

                tMap.remove(module);
            });

            tMap.forEach((module,result)->{
                String empty = "---";
                html.set(html +
                        "<tr>\r\n" +
                        "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word width:25%;\">" + module + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + empty + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + empty + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + result + "</td>\r\n" +
                        "</tr>\r\n");
            });

            html.set(html + "</tbody>\n" +
                    "</table>");

            bw.write(html.get());
            bw.close();
        }
    }

    public Map<String, String> capturingFailures(String URL) {
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
        String resp = requestSpecification.get(URL).then().extract().response().getBody().prettyPrint();

        Gson pGson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(resp);
        TestSummary summary = pGson.fromJson(jsonElement, TestSummary.class);
        Map<String, String> failureTriageMap = new TreeMap<>();
        summary.getResults().forEach(result -> {
            if ((result.get("status").getAsString().equalsIgnoreCase("fail"))) {
                String failureKey = result.get("class-name").toString() + "_" + result.get("method-name").toString();
                String failureValue = result.get("failure-reason").toString();
                failureTriageMap.put(failureKey, failureValue);
            }
        });

        return failureTriageMap;
    }
}
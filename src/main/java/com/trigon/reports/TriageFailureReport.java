package com.trigon.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.exceptions.ThrowableTypeAdapter;
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
                    "<style>\n" +
                    "table, th,\n" +
                    "td {border: 1px solid black;border-collapse: collapse;word-wrap: break-word;}\n" +
                    "table {display: block;}\n" +
                    "td, th {display: inline-block;width: 25%;}\n" +
                    "td {text-align: center !important;}\n" +
                    "th {text-align: center !important;}\n" +
                    "tbody {background-color: #ccccc4 !important;}\n" +
                    "</style>\n" +
                    "<body>\n" +
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<title>Failure Triage Report</title>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div>\n" +
                    "<div>\n" +
                    "<div style=\"display: flex; justify-content: center; align-items: center;\">\n" +
                    "<div style=\"flex: 1;\"><img alt=\"FoodHub\"\n" +
                    "src=\"https://fh-qa-automation.s3.amazonaws.com/Docs/foodhub_email_logo.png\"\n" +
                    "width=\"150px\">\n" +
                    "</div>\n" +
                    "<div style=\"text-align: center; background: #e0dbdb; height: 60px; flex: 1;\">Failure Triage\n" +
                    "Report</div>\n" +
                    "</div>\n" +
                    "<table style=\"margin-top: auto;\">\n" +
                    "<thead>" +
                    "<tr>\n" +
                    "<th\n" +
                    "style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;\">\n" +
                    "Module : Scenario</th>\n" +
                    "<th\n" +
                    "style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-\67p0-=-amily: calibri, arial, sans-serif\">\n" +
                    DBYURL +
                    "</th>\n" +
                    "<th\n" +
                    "style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">\n" +
                    YURL +
                    "</th>\n" +
                    "<th\n" +
                    "style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">\n" +
                    TURL +
                    "</th>\n" +
                    "</tr>\n" +
                    "</thead>\n" +

            "<tbody>\n" +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%; colspan=\"4\"\">" + passContent + "</td>\r\n" +
                    "</tr>\r\n" +
                    "</tbody>\n" +
                    "</table>\n" +
                    "</table>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "</body>\n" +
                    "</html>\n";
            bw.write(html);
            bw.close();


        }
        else if (dbyMap.containsKey("no-report") && yMap.containsKey("no-report") && tMap.containsKey("no-report")) {
            String noReportContent = "No Reports generated for the past 3 days";
            String html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<title>FoodHub Failure Triage Report</title>\n" +
                    "</head>\n" +
                    "<style>\n" +
                    "table, th, td {\n" +
                    "border: 1px solid black;\n" +
                    "border-collapse: collapse;\n" +
                    "}</style>\n" +
                    "<body>\n" +
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<title>Failure Triage Report</title>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div>\n" +
                    "<div class=\"table-responsive\">\n" +
                    "<table class=\"table\">\n" +
                    "<thead >\n" +
                    "<tr>\n" +
                    "<th><img alt=\"FoodHub\" src=\"https://fh-qa-automation.s3.amazonaws.com/Docs/foodhub_email_logo.png\" width=\"150px\"></th>\n" +
                    "<th width=\"75%\" style=\"text-align: center;background: #e0dbdb;height: 60px;\">Failure Triage Report</th></tr>\n" +
                    "</thead>\n" +
                    "<table class=\"table table-bordered\" style=\"margin-top: auto;\">\n" +
                    "<thead>\n" +
                    "<tr>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">Module : Scenario</th>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;text-align: center;\">"+daybeforeyesterday+"</th>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;text-align: center;\">"+yesterday+"</th>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;text-align: center;\">"+today+"</th>\n" +
                    "</tr>\n" +
                    "</thead>\n" +
                    "<tbody>\n" +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%; colspan=4\"></td>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%; colspan=4\">" + noReportContent + "</td>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%; colspan=4\">" + noReportContent + "</td>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%; colspan=4\">" + noReportContent + "</td>\r\n" +
                    "</tr>\r\n" +
                    "</tbody>\n" +
                    "</table>\n" +
                    "</table>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "</body>\n" +
                    "</html>\n";

            bw.write(html);
            bw.close();
        }
        else {
            AtomicReference<String> html = new AtomicReference<>("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<title>FoodHub Failure Triage Report</title>\n" +
                    "</head>\n" +
                    "<style>\n" +
                    "table, th, td {\n" +
                    "border: 1px solid black;\n" +
                    "border-collapse: collapse;\n" +
                    "}</style>\n" +
                    "<body>\n" +
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<title>Failure Triage Report</title>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div>\n" +
                    "<div class=\"table-responsive\">\n" +
                    "<table class=\"table\">\n" +
                    "<thead >\n" +
                    "<tr>\n" +
                    "<th><img alt=\"FoodHub\" src=\"https://fh-qa-automation.s3.amazonaws.com/Docs/foodhub_email_logo.png\" width=\"150px\"></th>\n" +
                    "<th width=\"75%\" style=\"text-align: center;background: #e0dbdb;height: 60px;\">Failure Triage Report</th></tr>\n" +
                    "</thead>\n" +
                    "<table class=\"table table-bordered\" style=\"margin-top: auto;\">\n" +
                    "<thead>\n" +
                    "<tr>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif\">Module : Scenario</th>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;text-align: center;\">"+daybeforeyesterday+"</th>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;text-align: center;\">"+yesterday+"</th>\n" +
                    "<th style=\"background-color: #1E7778;color:#fff;  font-size: 25px; font-family: calibri, arial, sans-serif;text-align: center;\">"+today+"</th>\n" +
                    "</tr>\n" +
                    "</thead>\n" +
                    "<tbody>\n");

            AtomicReference<String> daybeforeYesterdayBGColor = new AtomicReference<>("background-color: #ccccccc4");
            AtomicReference<String> yesterdayBGColor = new AtomicReference<>("background-color: #ccccccc4");
            AtomicReference<String> todayBGColor = new AtomicReference<>("background-color: #ccccccc4");
            dbyMap.forEach((module, result) -> {
                if (result.equalsIgnoreCase(String.valueOf(yMap.get(module))) && result.equalsIgnoreCase(String.valueOf(tMap.get(module)))) {
                    daybeforeYesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    yesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    todayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                } else if (String.valueOf(yMap.get(module)).equalsIgnoreCase(String.valueOf(tMap.get(module)))){
                    yesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    todayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                } else if (result.equalsIgnoreCase(String.valueOf(tMap.get(module)))) {
                    daybeforeYesterdayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                    todayBGColor.set("background-color: #ccccccc4;color:#f74242;");
                } else {
                    daybeforeYesterdayBGColor.set("background-color: #ccccccc4;");
                    yesterdayBGColor.set("background-color: #ccccccc4;");
                    todayBGColor.set("background-color: #ccccccc4;");
                }
                html.set(html +
                        "<tr>\r\n"+
                        "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%;\">"+module+"</td>\r\n"+
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;"+daybeforeYesterdayBGColor+"font-style:calibri;font-size:14px;\">"+result+"</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + yesterdayBGColor + "font-style:calibri;font-size:14px;\">"+ yMap.get(module) + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;"+todayBGColor+"font-style:calibri;font-size:14px;\">" +tMap.get(module) + "</td>\r\n" +
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
                        "<tr>\r\n"+
                        "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%;\">" + module  +"</td>\r\n"+
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;font-style:calibri;font-size:14px;\">"+empty+"</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + backgroundColor + "font-style:calibri;font-size:14px;\">"+ result + "</td>\r\n" +
                        "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;"+backgroundColor+"font-style:calibri;font-size:14px;\">" +tMap.get(module) + "</td>\r\n" +
                        "</tr>\r\n");

                tMap.remove(module);
            });

            tMap.forEach((module, result) -> {
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
                    "</table>\n" +
                    "</table>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "</body>\n" +
                    "</html>");

            bw.write(html.get());
            bw.close();
        }
    }

    public Map<String, String> capturingFailures(String URL) {
        Map<String, String> failureTriageMap = new TreeMap<>();
        if (URL.equalsIgnoreCase("no-report")) {
            failureTriageMap.put("no-report", "no-report");
        } else {
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            String resp = requestSpecification.get(URL).then().extract().response().getBody().prettyPrint();

            Gson pGson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
            JsonElement jsonElement = JsonParser.parseString(resp);
            TestSummary summary = pGson.fromJson(jsonElement, TestSummary.class);
            summary.getResults().forEach(result -> {
                if ((result.get("status").getAsString().equalsIgnoreCase("fail"))) {
                    String failureKey = result.get("class-name").getAsString() + "_" + result.get("method-name").getAsString();
                    String failureValue = result.get("failure-reason").getAsString();
                    failureTriageMap.put(failureKey, failureValue);
                }
            });
        }
        return failureTriageMap;
    }
}
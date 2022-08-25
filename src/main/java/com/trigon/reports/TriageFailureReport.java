package com.trigon.reports;

import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TriageFailureReport {

    @Test()
    @Parameters({"suiteName"})
    public void triageFailureReport(String suiteName) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("d-MMM-yyyy");
        String today = sdf.format(DateUtils.addDays(new Date(), 0));
        String yesterday = sdf.format(DateUtils.addDays(new Date(), -1));
        String daybeforeyesterday = sdf.format(DateUtils.addDays(new Date(), -2));
        ReportAnalyser reportAnalyser = new ReportAnalyser();
        String partialSuiteName = suiteName;


        //author_ScenarioName("Ganesh M", "Triage Failure Report");

        String DBYURL = reportAnalyser.getEmailReportOf(daybeforeyesterday, partialSuiteName);

        String YURL = reportAnalyser.getEmailReportOf(yesterday, partialSuiteName);

        String TURL = reportAnalyser.getEmailReportOf(today, partialSuiteName);

        List<String> myList1 = new ArrayList<String>();
        try {
            String url = DBYURL;

            Document document = Jsoup.connect(url).get();
            Element table = document.body().getElementsByTag("tbody").get(7);
            Elements rows = table.getElementsByTag("tr");
            for (int i = 2; i < rows.size(); i++) {
                myList1.add(rows.get(i).text());
            }
        } catch (Exception e) {
            System.out.println("err" + e);
        }

        Map<String, String> m1 = new HashMap<String, String>();
        for (String string : myList1) {
            String design1[] = string.split("Scenario :");
            String design2[] = design1[1].split("Failure Reason :");
            m1.put(design2[0], design2[1]);
        }

        List<String> myList2 = new ArrayList<String>();
        try {
            String url = YURL;
            Document document = Jsoup.connect(url).get();
            Element table = document.body().getElementsByTag("tbody").get(7);
            Elements rows = table.getElementsByTag("tr");
            for (int i = 2; i < rows.size(); i++) {
                myList2.add(rows.get(i).text());
            }
        } catch (Exception e) {
            System.out.println("err" + e);
        }

        Map<String, String> m2 = new HashMap<String, String>();

        for (String string : myList2) {

            String design1[] = string.split("Scenario :");
            String design2[] = design1[1].split("Failure Reason :");
            m2.put(design2[0], design2[1]);

        }

        List<String> myList3 = new ArrayList<String>();
        try {
            String url = TURL;
            Document document = Jsoup.connect(url).get();
            Element table = document.body().getElementsByTag("tbody").get(7);
            Elements rows = table.getElementsByTag("tr");
            for (int i = 2; i < rows.size(); i++) {
                myList3.add(rows.get(i).text());
            }
        } catch (Exception e) {
            System.out.println("err" + e);
        }
        Map<String, String> m3 = new HashMap<String, String>();

        for (String string : myList3) {
            String design1[] = string.split("Scenario :");
            String design2[] = design1[1].split("Failure Reason :");
            m3.put(design2[0], design2[1]);
        }


//        addHeaderToCustomReport("Scenario: Failure Reason", daybeforeyesterday, yesterday, today);


        File f = new File("src/test/resources/TestResults/Triage.html");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

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
                "}</style>\n"+
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
                "<tbody border=\"0\">\r\n";


        for (Map.Entry<String, String> entry : m1.entrySet()) {

            String backgroundColor1 = null, backgroundColor2 = null, backgroundColor3 = null;
            try {

                if ((entry.getValue().equalsIgnoreCase(m2.get(entry.getKey()))) && (entry.getValue().equalsIgnoreCase(m3.get(entry.getKey())))) {
                    backgroundColor1 = "background-color: #ccccccc4;color:#f74242;";
                    backgroundColor2 = "background-color: #ccccccc4;color:#f74242;";
                    backgroundColor3 = "background-color: #ccccccc4;color:#f74242;";


                } else if ((m2.get(entry.getKey()).equalsIgnoreCase(m3.get(entry.getKey())))) {
                    backgroundColor1 = "background-color: #ccccccc4";
                    backgroundColor2 = "background-color: #ccccccc4;color:#f74242;";
                    backgroundColor3 = "background-color: #ccccccc4;color:#f74242;";

                } else if (entry.getValue().equalsIgnoreCase(m3.get(entry.getKey()))) {
                    backgroundColor1 = "background-color: #ccccccc4;color:#f74242;";
                    backgroundColor2 = "background-color: #ccccccc4";
                    backgroundColor3 = "background-color: #ccccccc4;color:#f74242;";

                } else {
                    backgroundColor1 = "background-color: #ccccccc4";
                    backgroundColor2 = "background-color: #ccccccc4";
                    backgroundColor3 = "background-color: #ccccccc4";

                }
            } catch (Exception e) {
                System.out.println("Error Message: " + e);
            }

//            addRowToCustomReport(entry.getKey(), entry.getValue(), m2.get(entry.getKey()), m3.get(entry.getKey()));

            html = html +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word; width:25%;\">" + entry.getKey() + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + backgroundColor1 + "; font-style:calibri;font-size:14px;\">" + entry.getValue() + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + backgroundColor2 + ";font-style:calibri;font-size:14px;\">" + m2.get(entry.getKey()) + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word; width:25%;" + backgroundColor3 + ";font-style:calibri;font-size:14px;\">" + m3.get(entry.getKey()) + "</td>\r\n" +
                    "</tr>\r\n";

            System.out.print(entry.getKey() + "-->" + entry.getValue() + "  |   ");

            System.out.print(m2.get(entry.getKey()) + "  |  ");
            m2.remove(entry.getKey());

            System.out.println(m3.get(entry.getKey()));
            m3.remove(entry.getKey());
        }

        for (Map.Entry<String, String> entry2 : m2.entrySet()) {
            String empty = "---";
            String backgroundColor;
            if ((entry2.getValue().equalsIgnoreCase(m3.get(entry2.getKey())))) {
                backgroundColor = "background-color:  #ccccccc4;color:#f74242;";
            } else {
                backgroundColor = "background-color: #ccccccc4";
            }

//            addRowToCustomReport(entry2.getKey(), empty, entry2.getValue(), m3.get(entry2.getKey()));


            html = html +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word width:25%;\">" + entry2.getKey() + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + empty + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;" + backgroundColor + ";font-style:calibri;font-size:14px;\">" + entry2.getValue() + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;" + backgroundColor + ";font-style:calibri;font-size:14px;\">" + m3.get(entry2.getKey()) + "</td>\r\n" +
                    "</tr>\r\n";


            System.out.print(entry2.getKey() + "-->" + "   |   " + entry2.getValue() + "  |   ");

            System.out.println(m3.get(entry2.getKey()));
            m3.remove(entry2.getKey());
        }


        for (Map.Entry<String, String> entry3 : m3.entrySet()) {
            String empty = "---";

//            addRowToCustomReport(entry3.getKey(), empty, empty, entry3.getValue());


            html = html +
                    "<tr>\r\n" +
                    "<td style=\"background-color: #ccccccc4; white-space:pre-wrap; word-break:break-word width:25%;\">" + entry3.getKey() + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + empty + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + empty + "</td>\r\n" +
                    "<td style=\"white-space:pre-wrap; word-break:break-word width:25%;font-style:calibri;font-size:14px;\">" + entry3.getValue() + "</td>\r\n" +
                    "</tr>\r\n";

            System.out.println(entry3.getKey() + "-->" + "   |         |" + entry3.getValue());

        }
        html = html + "</tbody>\n" +
                "</table>";

        bw.write(html);
        bw.close();

    }

}
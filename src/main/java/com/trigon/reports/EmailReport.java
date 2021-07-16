package com.trigon.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static com.trigon.reports.ReportManager.cUtils;

public class EmailReport {
    public static void createEmailReport(String path, ExtentReports report){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(path+"/EmailReport.html"));
            StringBuffer bf = new StringBuffer();
            bf.append(header(report));
            bf.append(body(report));
            bf.append(footers());
            bw.write(bf.toString());
            bw.flush();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static String header(ExtentReports stats){

        String SuiteName = null;
        long passCount = stats.getStats().getGrandchild().get(Status.PASS);
        long failCount= stats.getStats().getGrandchild().get(Status.FAIL);
        long skipCount= stats.getStats().getGrandchild().get(Status.SKIP);

        long totalCount = passCount+failCount+skipCount;

        int passPercentage = stats.getStats().getGrandchildPercentage().get(Status.PASS).intValue();
        int failPercentage = stats.getStats().getGrandchildPercentage().get(Status.FAIL).intValue();
        int skipPercentage = stats.getStats().getGrandchildPercentage().get(Status.SKIP).intValue();

        String timeTaken = cUtils().getRunDuration(stats.getReport().timeTaken());


        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Automation Report</title>\n" +
                "    <link href=\"https://t2s-staging-automation.s3.amazonaws.com/report-libs/css/style.css\" rel=\"stylesheet\">\n" +
                "    <link href=\"https://s3.amazonaws.com/t2s-staging-automation/report-libs/css/icon-style.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<table style=\"width: 80%;font-family: Roboto,sans-serif;font-size: 13px;color: #555555;text-align: center;border-radius: 20px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse;background-color: #f5f2f2\">\n" +
                "    <tbody>\n" +
                "    <tr style=\"background: #e0dbdb;height: 60px;\">\n" +
                "        <th style=\"padding-left: 30px;text-align: left\"><img alt=\"FoodHub\" height=\"20\" src=\"https://s3.amazonaws.com/t2s-staging-automation/Docs/foodhub_email_logo.png\" width=\"114\"></th>\n" +
                "    </tr>\n" +
                "    <tr style=\"background: #ececec;height: 40px\">\n" +
                "        <td>\n" +
                "            <div style=\"margin: 10px\">\n" +
                "                <table style=\"background-color: #f1f1f1;border: 1px solid #EEEEEE;width: 98%;border-radius: 10px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                "                    <tbody>\n" +
                "                    <tr style=\"background: #e0dbdb;height: 40px\">\n" +
                "                        <td colspan=\"2\">\n" +
                "                            <table style=\"margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_Testsuite.png\" height=\"15\" width=\"15\" alt=\"Test Suite\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px\">"+stats.getReport().getSystemEnvInfo().get(1).getValue()+"</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <table style=\"margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_FrameWorkVersion.png\" height=\"15\" width=\"15\" alt=\"FrameWorkVersion\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px;text-align: left\">"+stats.getReport().getSystemEnvInfo().get(0).getValue()+"</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_ExecutedBy.png\" height=\"15\" width=\"15\" alt=\"Executed By\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px;text-align: left\">"+stats.getReport().getSystemEnvInfo().get(4).getValue()+"</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_OS.png\" height=\"15\" width=\"15\" alt=\"OS\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px;text-align: left\">"+stats.getReport().getSystemEnvInfo().get(5).getValue()+"</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                        <td>\n" +
                "                            <table style=\"margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_TotalTime.png\" height=\"15\" width=\"15\" alt=\"Icon_TotalTime\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px;text-align: left\">"+timeTaken+"</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_TestStartTime.png\" height=\"15\" width=\"15\" alt=\"TestStartTime\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px;text-align: left\">"+stats.getReport().getStartTime()+"</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/Icon_TestEndTime.png\" height=\"15\" width=\"15\" alt=\"TestEndTime\"></td>\n" +
                "                                    <td style=\"padding-bottom: 5px;padding-left: 10px;text-align: left\">"+stats.getReport().getEndTime()+"</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "    <tr style=\"background: #ececec;height: 40px\">\n" +
                "        <td>\n" +
                "            <div style=\"margin: 10px\">\n" +
                "                <table style=\"background-color: #f1f1f1;border: 1px solid #EEEEEE;width: 98%;border-radius: 10px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                "                    <tbody>\n" +
                "                    <tr style=\"background: #e0dbdb;height: 40px\">\n" +
                "                        <td>TOTAL TEST SCENARIOS</td>\n" +
                "                        <td>\n" +
                "                            <div style=\"padding: 4px;  display: flex;align-items: center;justify-content: center\">\n" +
                "                                <div><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/pass.png\" height=\"15\" width=\"15\" alt=\"pass\"></div>\n" +
                "                                <div style=\"padding-bottom: 5px;padding-left: 10px\">PASSED ("+passCount+")</div>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                        <td>\n" +
                "                            <div style=\"padding: 4px;  display: flex;align-items: center;justify-content: center\">\n" +
                "                                <div><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/fail.png\" height=\"15\" width=\"15\" alt=\"fail\"></div>\n" +
                "                                <div style=\"padding-bottom: 5px;padding-left: 10px\">FAILED & EXCEPTIONS ("+failCount+")</div>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                        <td>\n" +
                "                            <div style=\"padding: 4px;  display: flex;align-items: center;justify-content: center\">\n" +
                "                                <div><img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/skip.png\" height=\"15\" width=\"15\" alt=\"skip\"></div>\n" +
                "                                <div style=\"padding-bottom: 5px;padding-left: 10px\">SKIPPED ("+skipCount+")</div>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                        <td></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"font-size: 25px;padding: 2.45rem .20rem;\">"+totalCount+"</div>\n" +
                "                        </td>\n" +
                "                        <td>\n" +
                "                            <img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/Percentages_Black/P_"+passPercentage+".png\" alt=\""+passPercentage+"%\"></td>\n" +
                "                        <td>\n" +
                "                            <img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/Percentages_Black/R_"+failPercentage+".png\" alt=\""+failPercentage+"%\"></td>\n" +
                "                        <td>\n" +
                "                            <img src=\"https://t2s-staging-automation.s3.amazonaws.com/Docs/Percentages_Black/B_"+skipPercentage+".png\" alt=\""+skipPercentage+"%\"></td>\n" +
                "                        <td>\n" +
                "                            <div style=\"font-size: 25px;padding: 2.45rem .20rem;\"></div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "        </td>\n" +
                "    </tr>";
    }

    private static String body(ExtentReports stats){
        StringBuffer bf = new StringBuffer();

        // Fixed body Top
        bf.append("    <tr>\n" +
                "        <td>\n" +
                "            <div style=\"margin: 10px\">\n" +
                "                <table style=\"background-color: #fcf8f8;border: 1px solid #EEEEEE;width: 98%;border-radius: 10px;margin-left:auto;margin-right:auto;overflow: hidden;border-collapse: collapse\">\n" +
                "                    <tbody>\n");

        stats.getReport().getTestList().forEach(module->{
            String moduleName = module.getName();
            bf.append("<tr style=\"background: #e0dbdb;height: 40px\">\n" +
                    "                        <td colspan=\"3\">\n" +
                    "                            Test Environment for : "+moduleName+"\n" +
                    "                        </td>\n" +
                    "                    </tr>\n");


            module.getChildren().forEach(testClass->{
                String className = testClass.getName();
                bf.append("                    <tr style=\"background: #ececec;height: 40px\">\n" +
                        "                        <td colspan=\"3\">\n" +
                        "                            Test Features : "+className+"\n" +
                        "                        </td>\n" +
                        "                    </tr>\n");
                bf.append("                    <tr style=\"background: #F3F3F3;height: 40px;text-align: left\">\n" +
                        "                        <td style=\"width: 10%;padding-left: 30px\">Status</td>\n" +
                        "                        <td style=\"width: 40%\">Test Details</td>\n" +
                        "                        <td style=\"width: 50%;\">Test Features & Failures</td>\n" +
                        "                    </tr>\n");
                testClass.getChildren().forEach(method->{

                    String methodName = method.getName();
                    String description = method.getDescription();
                    String author = method.getAuthorSet().stream().iterator().next().getName();

                    String StatusImageURL = "https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/pass.png";

                    if(method.getStatus().getName().equals("Pass")){
                        StatusImageURL = "https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/pass.png";
                    }else if (method.getStatus().getName().equals("Fail")){
                        StatusImageURL = "https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/fail.png";
                    }else if (method.getStatus().getName().equals("Skip")){
                        StatusImageURL = "https://t2s-staging-automation.s3.amazonaws.com/Docs/report_result/skip.png";
                    }



                    bf.append("<tr style=\"text-align: left\">\n" +
                            "                        <td style=\"padding-top:10px;padding-left: 20px\">\n" +
                            "                            <div>\n" +
                            "                                <img src=\""+StatusImageURL+"\" height=\"50\" width=\"50\" alt=\"pass\">\n" +
                            "                            </div>\n" +
                            "                        </td>\n" +
                            "                        <td style=\"padding-top:10px\">\n" +
                            "                            <div>\n" +
                            "                                <div style=\"word-break:break-all\">"+methodName+"</div>\n" +
                            "                                <div style=\"padding-top: 5px;font-size: 11px;color: #928383\">\n" +
                            "                                <div style=\"word-break:break-all\"><b>Author : </b>"+author+"</div>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                        </td>\n" +
                            "                        <td>\n" +
                            "                            <div style=\"word-break:break-all\">\n" +
                            "                                <b>Scenario :</b> "+description+"\n" +
                            "                            </div>\n");
                    // If Test Fails
                    if(method.getStatus().getName().equals("Fail")){
                        bf.append("                            <div style=\"word-break:break-all;padding-top: 10px\"><b>Failure Reason :</b>");
                            method.getLogs().forEach(log->{
                            if(log.getStatus().getName().equalsIgnoreCase("Fail")){
                                if(!log.getDetails().startsWith("<details><summary>")){
                                    bf.append("<div>"+log.getDetails()+"</div>");
                                }
                            }
                        });
                            if(method.hasChildren()){
                                method.getChildren().forEach(child->{
                                    child.getLogs().forEach(log->{
                                        if(log.getStatus().getName().equalsIgnoreCase("Fail")){
                                            if(!log.getDetails().startsWith("<details><summary>")){
                                                bf.append("<div>"+log.getDetails()+"</div>");
                                            }
                                        }
                                    });
                                });
                            }
                        bf.append("                            </div>\n");
                    }
                });
            });
        });


        bf.append("                        </td>\n" +
                        "                    </tr>\n"+
                "                    </tbody>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "        </td>\n" +
                "    </tr>");

        return bf.toString();
    }

    private static String footers(){
        return "    <tr style=\"background: #e0dbdb;height: 40px;\">\n" +
                "        <td colspan=\"2\">© 2021 - Foodhub Automation Team</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
    }

}

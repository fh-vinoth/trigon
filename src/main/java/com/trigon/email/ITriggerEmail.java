package com.trigon.email;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface ITriggerEmail {
    void triggerEmail(String reportPath, String recipients, String uploadToAWS,String pipeline,String jenkins) throws IOException, ParseException;
    void triggerCustomEmail(String reportPath, String recipients) throws IOException, ParseException;

    void triggerEmail(String reportPath, String recipients, String uploadToAWS, String regenerateTestReport, String sendFailedReport, String pipeline);
    void triggerErrorEmail(String recipients);
}

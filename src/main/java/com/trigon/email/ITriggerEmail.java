package com.trigon.email;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface ITriggerEmail {
    void triggerEmail(String reportPath, String recipients, String uploadToAWS,String sendFailedReport)  ;
    void triggerCustomEmail(String reportPath, String recipients) throws IOException, ParseException;
}

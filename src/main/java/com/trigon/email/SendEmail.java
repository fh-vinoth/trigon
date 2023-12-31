package com.trigon.email;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SendEmail {
    ITriggerEmail email = new TriggerEmailImpl();

    @Test
    @Parameters({"reportPath", "recipients", "uploadToAWS", "sendFailedReport"})
    public void SendOfflineEmail(String reportPath, @Optional String recipients, @Optional String uploadToAWS, @Optional String sendFailedReport) {
        String[] splitPath = reportPath.split(",");
        for(String s:splitPath){
            email.triggerEmail(s, recipients, uploadToAWS, sendFailedReport);
        }
    }
}

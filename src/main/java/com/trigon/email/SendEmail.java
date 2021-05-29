package com.trigon.email;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SendEmail {
    ITriggerEmail email = new TriggerEmailImpl();

    @Test
    @Parameters({"reportPath", "recipients", "uploadToAWS", "regenerateTestReport", "sendFailedReport"})
    public void SendOfflineEmail(String reportPath, @Optional String recipients, @Optional String uploadToAWS, @Optional String regenerateTestReport, @Optional String sendFailedReport) {
        email.triggerEmail(reportPath, recipients, uploadToAWS, regenerateTestReport, sendFailedReport, "NA");
    }

    public void errorEmail(String recipients){
        email.triggerErrorEmail(recipients);
    }
}

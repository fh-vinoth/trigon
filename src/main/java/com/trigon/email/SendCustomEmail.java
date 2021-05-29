package com.trigon.email;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SendCustomEmail {
    ITriggerEmail email = new TriggerEmailImpl();

    @Test
    @Parameters({"recipients"})
    public void sendCustomEmail(@Optional String recipients) throws IOException, ParseException {
        File file2 = new File("reports-path.json");
        String reportPath = null;
        if (file2.exists()) {
            JsonElement element1 = JsonParser.parseReader(new FileReader("reports-path.json"));
            reportPath = element1.getAsJsonObject().get("path").getAsString();
        }

        if(reportPath != null){
            if(recipients!=null){
                email.triggerCustomEmail(reportPath, recipients);
            }else{
                email.triggerCustomEmail(reportPath, "bhaskar.marrikunta@foodhub.com");
            }
        }

    }
}

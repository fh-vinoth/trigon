package com.trigon.email;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SendJenkinsEmail {
    ITriggerEmail email = new TriggerEmailImpl();

    @Test
    public void sendJenkinsEmail() throws IOException, ParseException {
        File file2 = new File("reports-path.json");
        String reportPath = null;
        if (file2.exists()) {
            JsonElement element1 = JsonParser.parseReader(new FileReader("reports-path.json"));
            reportPath = element1.getAsJsonObject().get("path").getAsString();
        }

        if(reportPath != null){
            email.triggerEmail(reportPath, "automation@touch2success.com","true","NA","true");
        }

    }
}

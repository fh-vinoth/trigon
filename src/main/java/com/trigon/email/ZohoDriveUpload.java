package com.trigon.email;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trigon.security.AES;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ZohoDriveUpload extends ZohoCommonMethods {

    @Test
    @Parameters({"reportPath"})
    public void zohoTest(@Optional String reportPath){
        Map<String,Object> response = new HashMap<>();
        try {

            String refreshToken = AES.decrypt("nQwHnPBoPi0QTHgEDRk0m4Am08l20U7LiNIHEI+jwUipaeJbRXevcqADDN6AtDFu+g7uuSN816f04TJ7r9zLjHT9WdvYGyRbqy0LoJsD8bE=","t2sautomation");
            String clientSecretId = AES.decrypt("hRCEDxQzc+2SGRnUFwOpdUoeetvkM09VD1KYSF1sGV/urnBYuA1H+s6RVX+ue/J7","t2sautomation");
            String clientId = AES.decrypt("NUr66B38KAPWULbbwcEulTxJXgfN8BgSaphbSQrWqo3EL/r1WQKmmU+0A1fsGGW5","t2sautomation");

            /** Upload Process */

            // Convert folder to zip
            File file = new File(reportPath+".zip");
            if(! file.exists()){
                folderToZip(reportPath,reportPath+".zip");
            }

            // Access token generation for zoho
            response = getZohoAccessToken(refreshToken,clientSecretId,clientId);
            String accessToken = response.get("access_token").toString();
            response.clear();

            // Upload zip file to Zoho work drive
            response = UploadFileInZoho(reportPath+".zip",accessToken);
            String fileId = response.get("data[0].attributes.resource_id").toString();
            String parenFolderId = response.get("data[0].attributes.parent_id").toString();
            response.clear();

            // Unzip the file to folder
            String unzipFolder = AES.decrypt("+tDLW4T3xpLdSyRML4OCubhFrSh/TggEOwOaDgLaNALKUarvKFuaKS9GvwptSg4o","t2sautomation");
            response = unZipFileInZoho(unzipFolder,fileId,accessToken);
            String unZipId = response.get("data.id").toString();

            // Link to view the upload test results folder
            String replaceHtml = "https://drive.foodhub.com/folder/"+unZipId;
            System.out.println("\n" + "Drive Link for Test Report : "+replaceHtml);

            trashZohoFile(fileId,accessToken);

            /** Email Body generation */

            String[] splitPath = reportPath.split("/");
            String concatPath = splitPath[4] +"/"+ splitPath[5] +"/"+ splitPath[6] +"/"+ splitPath[7] +"/"+"TestRailReport.html";

            StringBuilder html = new StringBuilder();
            FileReader fr = new FileReader(reportPath+"/SupportFiles/HTML/emailBody.json");

            BufferedReader br = new BufferedReader(fr);
            String val;
            while ((val = br.readLine()) != null) {
                html.append(val);
            }
            String init = "https://fh-qa-automation.s3.amazonaws.com/TestResults_2.8/"+concatPath;
            String result = html.toString();
            String htm = result.replaceAll(init,replaceHtml);
            br.close();

            // Writing new JSON file
            Path path = Paths.get(reportPath+"/SupportFiles/HTML/ZohoEmailBody.json");
            byte[] arr = htm.getBytes();
            Files.write(path, arr);

            /** Send email to recepients */



        }
        catch (Exception e) {
            hardFail(e);
        } finally {
            File file = new File(reportPath+".zip");
            if (file.exists()) {
                file.delete();
                System.out.println("Deleted compressed report successfully"+"\n");
            }
        }
    }

}

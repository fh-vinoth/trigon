package com.trigon.email;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.google.gson.Gson;
import com.trigon.security.AES;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ZohoDriveUpload extends ZohoCommonMethods {

    @Test
    @Parameters({"reportPath", "recipients","sendFailedReport"})
    public void zohoTest(@Optional String reportPath,@Optional String recipients, @Optional String sendFailedReport){
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

            String from = "automation@foodhub.com";
            final String username = "automation@foodhub.com";
            final String password = AES.decrypt("tZD5Ep1YTtME9sinDOMcnQ==", "t2sautomation");

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtppro.zoho.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            AWSCredentials credentials = new BasicAWSCredentials(
                    AES.decrypt("OriNxlLJ6ngVCYi/qCBSy1kBwPag3XyxfDiGrXfUUUg=", "t2sautomation"),
                    AES.decrypt("hij44vD5DKQY+nlkxoB+BT/wXXofuDwJTNtl7eCMaaE8ZJVrkJ2exWcFBnVn9p/G", "t2sautomation")
            );

            AmazonS3 s3Client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion("eu-west-2")
                    .build();
            // Get the Session object.
            Session session = Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });


            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            Map<String, Object> obj = null;
            try {
                Gson gson = new Gson();
                obj = gson.fromJson(new FileReader(reportPath + "/SupportFiles/HTML/ZohoEmailBody.json"), Map.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if((String.valueOf(obj.get("testType")) == null) || String.valueOf(obj.get("testType")).isEmpty()){
                    message.setFrom(new InternetAddress(from, "FH AutomationReport"));
                }else{
                    message.setFrom(new InternetAddress(from, "FH "+obj.get("testType").toString()+" Report"));
                }

                StringBuffer sb = new StringBuffer(recipients);
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(sb.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    message.setSubject(String.valueOf(obj.get("subject")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BodyPart messageBodyPart = new MimeBodyPart();

                StringWriter writer = new StringWriter();
                // Create a multipart message
                Multipart multipart = new MimeMultipart();
                try {
                    multipart.addBodyPart(messageBodyPart);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if ((sendFailedReport != null) && (sendFailedReport.equalsIgnoreCase("true"))) {
                    writer.append(String.valueOf(obj.get("failedData")));
                } else {
                    writer.append(String.valueOf(obj.get("body")));
                }

                try {
                    ((MimeBodyPart) messageBodyPart).setText(writer.toString(), "UTF-8", "html");
                    message.setContent(multipart);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
                    String[] folderName = reportPath.split("/");
                    String folderPath = folderName[4] +"/" + folderName[5]+"/" + folderName[6]+"/" + folderName[7];
                    System.out.println("Uploading to S3 Bucket !! It takes a while depending on your network and depending on size of report !! Please, Wait.... ");
                    MultipleFileUpload xfer = xfer_mgr.uploadDirectory("fh-qa-automation/TestResults_2.8",folderPath, new File(reportPath), true);
                    XferMgrProgress.showTransferProgress(xfer);
                    XferMgrProgress.waitForCompletion(xfer);
                    System.out.println("Reports are Picked from " + reportPath);
                    System.out.println("Pushed Reports to S3 Bucket successfully....");
                    System.out.println("Started Sending Email to " + recipients + " !! Please wait.. It takes a while based on your Network");
                    Transport.send(message);
                    System.out.println("Email Triggered successfully to Recipients " + recipients);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }


        }
        catch (Exception e) {
            hardFail("The Zoho email creation process failed due to ",e);
        } finally {
            File file = new File(reportPath+".zip");
            if (file.exists()) {
                file.delete();
                System.out.println("Deleted compressed report successfully"+"\n");
            }
        }
    }

    @Test
    @Parameters({"reportPath"})
    public void zohoTestEmail(@Optional String reportPath){
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
            hardFail("The Zoho email creation process failed due to ",e);
        } finally {
            File file = new File(reportPath+".zip");
            if (file.exists()) {
                file.delete();
                System.out.println("Deleted compressed report successfully"+"\n");
            }
        }
    }


}

package com.trigon.email;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.trigon.security.AES;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;


public class TriggerEmailImpl implements ITriggerEmail {

    private void addAttachment(Multipart multipart, String filename) {
        DataSource source = new FileDataSource(filename);
        BodyPart messageBodyPart = new MimeBodyPart();
        try {
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("DetailedReport.html");
            multipart.addBodyPart(messageBodyPart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void triggerEmail(String reportPath, String recipients, String uploadToAWS, String sendFailedReport) {

        ZohoDriveUpload driveUpload = new ZohoDriveUpload();
        driveUpload.zohoTest(reportPath);

        String from = "automation@foodhub.com";
        final String username = "automation@foodhub.com";
        final String password = AES.decrypt("tZD5Ep1YTtME9sinDOMcnQ==", "t2sautomation");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtppro.zoho.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
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
            if ((uploadToAWS != null) && (uploadToAWS.equalsIgnoreCase("true"))) {
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
            } else {
                System.out.println("Alert!!!!! uploadToAWS is Configured as false!!!!");
                System.out.println("Started Sending Email to " + recipients + " !! Please wait.. It takes a while based on your Network");
                try {
                    Transport.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Email Triggered successfully to Recipients " + recipients);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void triggerCustomEmail(String reportPath, String recipients) throws IOException {
        String from = "automation@foodhub.com";
        final String username = "automation@foodhub.com";
        final String password = AES.decrypt("tZD5Ep1YTtME9sinDOMcnQ==", "t2sautomation");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtppro.zoho.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Get the Session object.
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });


        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);

        Map customEmail = null;

        try {
            Gson gson = new Gson();
            customEmail = gson.fromJson(new FileReader(reportPath + File.separator + "SupportFiles/HTML" + "/" + "CustomReport.json"),Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            message.setFrom(new InternetAddress(from, "FH TestAutomation"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            message.setSubject(String.valueOf(customEmail.get("subject")));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        BodyPart messageBodyPart = new MimeBodyPart();

        StringWriter writer = new StringWriter();
        // Create a multipar message
        Multipart multipart = new MimeMultipart();
        try {
            multipart.addBodyPart(messageBodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        writer.append(String.valueOf(customEmail.get("customBody")));

        try {
            ((MimeBodyPart) messageBodyPart).setText(writer.toString(), "UTF-8", "html");
            message.setContent(multipart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        String attachment = reportPath + "/CustomReport.html";
        System.out.println("Started Sending Email to " + recipients + " !! Please wait.. It takes a while based on your Network");
        File f = new File(attachment);
        long fileSize = f.length();
        if (fileSize <= 24000000) {
            addAttachment(multipart, attachment);
        } else {
            System.out.println("Detailed Report Size is greater than 24MB! Hence Skipping attachment");
        }
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Email Triggered successfully to Recipients " + recipients);

    }
}

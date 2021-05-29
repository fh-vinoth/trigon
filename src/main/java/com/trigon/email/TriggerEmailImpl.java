package com.trigon.email;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.trigon.reports.ReportGenerator;
import com.trigon.security.AES;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.trigon.testbase.TestUtilities.cUtils;

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

    private void addErrorAttachment(Multipart multipart, String filename) {
        DataSource source = new FileDataSource(filename);
        BodyPart messageBodyPart = new MimeBodyPart();
        try {
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("FWErrors.json");
            multipart.addBodyPart(messageBodyPart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void triggerEmail(String reportPath, String recipients, String uploadToAWS, String regenerateTestReport, String sendFailedReport, String pipeline) {
        String decryptedString = AES.decrypt("dxoS+CjoM/WctAD5Svfq/g==", "t2sautomation");

        if (regenerateTestReport.equalsIgnoreCase("true")) {
            ReportGenerator rg = new ReportGenerator();
            rg.generateHTMLReports(reportPath);
        }

        String from = "t2semailnotifications@gmail.com";
        final String username = "t2semailnotifications@gmail.com";
        final String password = "eeizrjqtltmogeix";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        AWSCredentials credentials = new BasicAWSCredentials(
                AES.decrypt("LBRGQ0dOh/cfKZtL09GVpwDdzdveukupzmqrY0uIhOE=", "t2sautomation"),
                AES.decrypt("Aw0HhbPVq78mUnu9AjHgkTn7IWTWPBRRZIeyUniNUeWMqVu1dydCYvT+1IRgTapE", "t2sautomation")
        );

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1")
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

        JSONParser parser = new JSONParser();
        Map<String, Object> obj = null;
        try {
            obj = (HashMap) parser.parse(new FileReader(reportPath + "/SupportFiles/HTML/emailBody.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = (JSONObject) obj;

        try {
            message.setFrom(new InternetAddress(from, "FH TestAutomation"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pipeline.equalsIgnoreCase("true")) {
            try {
                message.setSubject("Pipeline : " + jsonObject.get("subject").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                message.setSubject(String.valueOf(jsonObject.get("subject")));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        if (sendFailedReport.equalsIgnoreCase("true")) {
            writer.append(String.valueOf(jsonObject.get("failedData")));
        } else {
            writer.append(String.valueOf(jsonObject.get("body")));
        }

        try {
            ((MimeBodyPart) messageBodyPart).setText(writer.toString(), "UTF-8", "html");
            message.setContent(multipart);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (uploadToAWS.equalsIgnoreCase("true")) {
                TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
                String[] folderName = reportPath.split("/");
                int folderlength = folderName.length;
                if (pipeline.equalsIgnoreCase("NA")) {
                    System.out.println("Uploading to S3 Bucket !! It takes a while depending on your network and depending on size of report !! Please, Wait.... ");
                }
                MultipleFileUpload xfer = xfer_mgr.uploadDirectory("t2s-staging-automation/TestResults",
                        folderName[folderlength - 1], new File(reportPath), true);
                XferMgrProgress.showTransferProgress(xfer);
                XferMgrProgress.waitForCompletion(xfer);
                // String attachment = reportPath + "/APIDetailedReport.html";
                if (pipeline.equalsIgnoreCase("NA")) {
                    System.out.println("Reports are Picked from " + reportPath);
                    System.out.println("Pushed Reports to S3 Bucket successfully....");
                    // System.out.println("File adding to Attachment " + attachment);
                    System.out.println("Started Sending Email to " + recipients + " !! Please wait.. It takes a while based on your Network");
                }

//                File f = new File(attachment);
//                long fileSize = f.length();
//                if (fileSize <= 24000000) {
//                    addAttachment(multipart, attachment);
//                } else {
//                    System.out.println("Detailed Report Size is greater than 24MB! Hence Skipping attachment");
//                }

                Transport.send(message);
                if (pipeline.equalsIgnoreCase("NA")) {
                    System.out.println("Email Triggered successfully to Recipients " + recipients);
                }
            } else {
                System.out.println("Alert!!!!! uploadToAWS is Configured as false!!!!");
                System.out.println("Started Sending Email to " + recipients + " !! Please wait.. It takes a while based on your Network");

                //String attachment = reportPath + "/APIDetailedReport.html";
//                System.out.println("File adding to Attachment " + attachment);
//                File f = new File(attachment);
//                long fileSize = f.length();
//                if (fileSize <= 24000000) {
//                    addAttachment(multipart, attachment);
//                } else {
//                    System.out.println("Detailed Report Size is greater than 24MB! Hence Skipping attachment");
//                }

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

    @Override
    public void triggerEmail(String reportPath, String recipients, String uploadToAWS, String pipeline,String jenkins) throws IOException, ParseException {
        String from = "t2semailnotifications@gmail.com";
        final String username = "t2semailnotifications@gmail.com";
        final String password = "eeizrjqtltmogeix";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        AWSCredentials credentials = new BasicAWSCredentials(
                AES.decrypt("LBRGQ0dOh/cfKZtL09GVpwDdzdveukupzmqrY0uIhOE=", "t2sautomation"),
                AES.decrypt("Aw0HhbPVq78mUnu9AjHgkTn7IWTWPBRRZIeyUniNUeWMqVu1dydCYvT+1IRgTapE", "t2sautomation")
        );

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1")
                .build();
        // Get the Session object.
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });


        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);


        JSONParser parser = new JSONParser();
        Map<String, Object> obj = (HashMap) parser.parse(new FileReader(reportPath + File.separator + "SupportFiles" + "/" + "EmailBodyReport.json"));

        JSONObject jsonObject = (JSONObject) obj;


        try {

            message.setFrom(new InternetAddress(from, "FH TestAutomation"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        if(pipeline.equalsIgnoreCase("true")) {
            try {
                message.setSubject("Pipeline : "+jsonObject.get("subject").toString());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }       else if(jenkins.equalsIgnoreCase("true")) {
            try {
                message.setSubject("Jenkins : "+jsonObject.get("subject").toString());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                message.setSubject(String.valueOf(jsonObject.get("subject")));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
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

        if(uploadToAWS.equalsIgnoreCase("true")) {
            writer.append(String.valueOf(jsonObject.get("body")));
        }

        if (jsonObject.get("emailReport") != null) {

            writer.append(String.valueOf(jsonObject.get("emailReport")));
        }


        try {
            ((MimeBodyPart) messageBodyPart).setText(writer.toString(), "UTF-8", "html");
            message.setContent(multipart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            if(uploadToAWS.equalsIgnoreCase("true")){
                if ((jsonObject.get("emailReport") != null)){
                    TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
                    String[] folderName = reportPath.split("/");
                    int folderlength = folderName.length;
                    System.out.println("Uploading to S3 Bucket !! It takes a while depending on your network and depending on size of report !! Please, Wait.... ");

                    MultipleFileUpload xfer = xfer_mgr.uploadDirectory("t2s-staging-automation/TestResults",
                            folderName[folderlength - 1], new File(reportPath), true);
                    XferMgrProgress.showTransferProgress(xfer);
                    XferMgrProgress.waitForCompletion(xfer);
                    String attachment = reportPath +"/DetailedReport/index.html";
                    System.out.println("Reports are Picked from " + reportPath);
                    System.out.println("Pushed Reports to S3 Bucket successfully....");
                    System.out.println("File adding to Attachment "+attachment);
                    System.out.println("Started Sending Email to "+recipients+" !! Please wait.. It takes a while based on your Network");


                    File f = new File(attachment);
                    long fileSize = f.length();
                    if(fileSize<=24000000){
                        addAttachment(multipart,attachment);
                    }else{
                        System.out.println("Detailed Report Size is greater than 24MB! Hence Skipping attachment");
                    }


                    Transport.send(message);
                    System.out.println("Email Triggered successfully to Recipients " + recipients);
                }


            }else{
                if ((jsonObject.get("emailReport") != null)){
                    System.out.println("Alert!!!!! uploadToAWS is Configured as false!!!!");
                    String attachment = reportPath +"/DetailedReport/index.html";
                    System.out.println("File adding to Attachment "+attachment);
                    System.out.println("Started Sending Email to "+recipients+" !! Please wait.. It takes a while based on your Network");
                    File f = new File(attachment);
                    long fileSize = f.length();
                    if(fileSize<=24000000){
                        addAttachment(multipart,attachment);
                    }else{
                        System.out.println("Detailed Report Size is greater than 24MB! Hence Skipping attachment");
                    }
                    Transport.send(message);
                    System.out.println("Email Triggered successfully to Recipients " + recipients);
                }
            }

        } catch (AmazonServiceException | MessagingException e) {
            e.printStackTrace();

            System.exit(1);
        }


    }

    public void triggerCustomEmail(String reportPath, String recipients) throws IOException, ParseException {
        String from = "t2semailnotifications@gmail.com";
        final String username = "t2semailnotifications@gmail.com";
        final String password = "eeizrjqtltmogeix";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Get the Session object.
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });


        // Create a default MimeMessage object.
        Message message = new MimeMessage(session);


        JSONParser parser = new JSONParser();
        Map<String, Object> obj = (HashMap) parser.parse(new FileReader(reportPath + File.separator + "SupportFiles" + "/" + "CustomReport.json"));

        JSONObject jsonObject = (JSONObject) obj;


        try {

            message.setFrom(new InternetAddress(from, "FH TestAutomation"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            message.setSubject(String.valueOf(jsonObject.get("subject")));
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

        writer.append(String.valueOf(jsonObject.get("customBody")));

        try {
            ((MimeBodyPart) messageBodyPart).setText(writer.toString(), "UTF-8", "html");
            message.setContent(multipart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        String attachment = reportPath +"/CustomReport.html";
        System.out.println("Started Sending Email to "+recipients+" !! Please wait.. It takes a while based on your Network");
        File f = new File(attachment);
        long fileSize = f.length();
        if(fileSize<=24000000){
            addAttachment(multipart,attachment);
        }else{
            System.out.println("Detailed Report Size is greater than 24MB! Hence Skipping attachment");
        }
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Email Triggered successfully to Recipients " + recipients);


    }

    public void triggerErrorEmail(String recipients) {
        File f = new File("errors");
        long fileSize = f.length();
        if (fileSize > 1000) {
            String decryptedString = AES.decrypt("dxoS+CjoM/WctAD5Svfq/g==", "t2sautomation");

            String from = "t2semailnotifications@gmail.com";
            final String username = "t2semailnotifications@gmail.com";
            final String password = "eeizrjqtltmogeix";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });


            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);


            try {
                message.setSubject("Framework Error Message_"+System.getProperty("user.name") + " : "+System.getProperty("os.name")+" : " + cUtils().getCurrentTimeStamp());
                message.setFrom(new InternetAddress(from, "FH TestAutomation"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipients));
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

            try {
                ((MimeBodyPart) messageBodyPart).setText(writer.toString(), "UTF-8", "html");
                message.setContent(multipart);
            } catch (Exception e) {
                e.printStackTrace();
            }

            addErrorAttachment(multipart, "errors");

            try {

                Transport.send(message);
                cUtils().deleteFile("errors");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cUtils().deleteFile("errors");
        }

    }


    public void uploadErrorsToAWS(){
        String fileName = "errors_"+System.getProperty("user.name") + "_"+System.getProperty("os.name");
        String filePath = "errors/"+fileName+"";
        File f = new File(filePath);
        String timeStamp = cUtils().getDateTimeStamp();
        AWSCredentials credentials = new BasicAWSCredentials(
                AES.decrypt("LBRGQ0dOh/cfKZtL09GVpwDdzdveukupzmqrY0uIhOE=", "t2sautomation"),
                AES.decrypt("Aw0HhbPVq78mUnu9AjHgkTn7IWTWPBRRZIeyUniNUeWMqVu1dydCYvT+1IRgTapE", "t2sautomation")
        );
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1")
                .build();
        long fileSize = f.length();
        if (fileSize > 10000) {
            TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
            Upload xfer = xfer_mgr.upload("t2s-staging-automation/TestErrors",fileName+timeStamp+".json",f);
            //XferMgrProgress.showTransferProgress(xfer);
            XferMgrProgress.waitForCompletion(xfer);
            cUtils().deleteFile(filePath);
        }
    }

}

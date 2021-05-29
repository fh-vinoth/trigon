package com.fh.unitests.frameworktest;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class App {
    public static void main(String[] args) {

        String b = "https://api-cloud.browserstack.com/";
        HttpResponse<String> response = Unirest.post(""+b+"app-automate/upload").basicAuth("foodhubautomatio1","u9rrKTSK1KLy8hHrmAyx")
                .multiPartContent()
                .field("url", "https://appcenter-filemanagement-distrib3ede6f06e.azureedge.net/b9b2a373-f9ea-46bc-965a-00435b48a5e1/app-app-release.apk?sv=2019-02-02&sr=c&sig=cL6TD6KxfTmVi1BDbn%2FxgqHS0J5O7y8XqA9ehMEe9z0%3D&se=2020-12-21T10%3A20%3A21Z&sp=r").asString();

        System.out.println(response.getBody());
    }
}


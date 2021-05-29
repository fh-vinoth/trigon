package com.fh.unitests.appcenter;

import com.github.wnameless.json.flattener.JsonFlattener;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Appcenter {

    public static void main(String[] args) {
        String BrowserStackURI = "https://api-cloud.browserstack.com/";
        String appCenterURI = "https://api.appcenter.ms/v0.1/apps/Touch2Success/";

        Map<String, Object> headers = new HashMap<>();
        headers.put("X-Api-Token", "bcea0675ca8d60103febff0c0149e08a08160dfa");
        headers.put("Content-Type", "application/json");

        Map<String, Object> appCenterBuild = authRequest(appCenterURI, "MYT-Revamp-Android/branches", headers);
        Map<String, Object> appCenterBuildStatus = new LinkedHashMap<>();

        for (int i = 0; i < appCenterBuild.size(); i++) {
            if (appCenterBuild.get("[" + i + "].branch.name").equals("custom-changes-automation")) {
                appCenterBuildStatus.put("lastBuildID", appCenterBuild.get("[" + i + "].lastBuild.id"));
                appCenterBuildStatus.put("lastBuildNumber", appCenterBuild.get("[" + i + "].lastBuild.buildNumber"));
                appCenterBuildStatus.put("lastBuildStatus", appCenterBuild.get("[" + i + "].lastBuild.status"));
                appCenterBuildStatus.put("lastBuildResult", appCenterBuild.get("[" + i + "].lastBuild.result"));
                break;
            }
        }

        Map<String, Object> appCenterReleaseBuildDetails = authRequest(appCenterURI, "MYT-Revamp-Android/releases", headers);
        Map<String, Object> appCenterReleaseAutomationBuild = new LinkedHashMap<>();

        for (int i = 0; i < appCenterReleaseBuildDetails.size(); i++) {
            try{
                if((appCenterReleaseBuildDetails.get("[" + i + "].version") !=null)){
                    if (appCenterReleaseBuildDetails.get("[" + i + "].version").toString().equals(appCenterBuildStatus.get("lastBuildID").toString())) {
                        appCenterReleaseAutomationBuild.put("ReleaseBuildId",appCenterReleaseBuildDetails.get("[" + i + "].id"));
                        appCenterReleaseAutomationBuild.put("ReleaseBuildShortVersion",appCenterReleaseBuildDetails.get("[" + i + "].short_version"));
                        break;
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        int releaseId = Integer.valueOf(appCenterReleaseAutomationBuild.get("ReleaseBuildId").toString());

        Map<String, Object> downloadURL = authRequest(appCenterURI, "MYT-Revamp-Android/releases/"+releaseId+"", headers);


        Map<String, Object> recentBuilds = authRequest(BrowserStackURI, "app-automate/recent_group_apps", null);
        boolean uploadStatus = true;
        for (int i = 0; i < recentBuilds.size(); i++) {
            if(recentBuilds.get("[" + i + "].custom_id")!=null){
                if(recentBuilds.get("[" + i + "].custom_id").equals("MYT_Android_Test")){
                    System.out.println("Build already Uploaded : " + recentBuilds.get("[" + i + "].app_url"));
                    uploadStatus = false;
                    break;
                }
            }
        }

        if(uploadStatus){
            HttpResponse<String> response = Unirest.post("https://api-cloud.browserstack.com/app-automate/upload").basicAuth("foodhubautomatio1","u9rrKTSK1KLy8hHrmAyx")
                    .multiPartContent().field("data","{\"custom_id\": \"MYT_Android\"}")
                    .field("url", downloadURL.get("download_url").toString())
                    .asString();
            Map<String, Object> respMap3 = JsonFlattener.flattenAsMap(response.getBody());
            System.out.println(respMap3.get("app_url"));
        }
    }

    public static Map<String, Object> authRequest(String URI, String Endpoint, Map<String, Object> headers) {
        RestAssured.baseURI = URI;
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
        if (headers != null) {
            requestSpecification.headers(headers);
        }
        Response response = requestSpecification.auth().basic("foodhubautomatio1", "u9rrKTSK1KLy8hHrmAyx").get(Endpoint).then().extract().response();
        Map<String, Object> respMap = JsonFlattener.flattenAsMap(response.asString());
        return respMap;
    }

}

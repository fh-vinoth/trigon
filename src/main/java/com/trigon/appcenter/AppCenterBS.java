package com.trigon.appcenter;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.trigon.api.APICore;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppCenterBS extends APICore {

    private static final Logger logger = LogManager.getLogger(AppCenterBS.class);

    public HashMap<String,String> getBSAppURL(String projectName,String branch) {
        String app_url = null;
        HashMap<String,String> buildData = new HashMap<>();
        try{
            String BrowserStackURI = propertiesPojo.getBrowserStack_URI();
            String appCenterURI = propertiesPojo.getAppCenter_URI();
            Map<String, Object> headers = new HashMap<>();
            headers.put("X-Api-Token", propertiesPojo.getAppCenter_token());
            headers.put("Content-Type", "application/json");
            Map<String, Object> appCenterBuild = basicBSAuthRequest(appCenterURI, ""+projectName+"/branches", headers);
            Map<String, Object> appCenterBuildStatus = new LinkedHashMap<>();
            for (int i = 0; i < appCenterBuild.size(); i++) {
                if (appCenterBuild.get("[" + i + "].branch.name").equals(branch)) {
                    appCenterBuildStatus.put("lastBuildID", appCenterBuild.get("[" + i + "].lastBuild.id"));
                    appCenterBuildStatus.put("lastBuildNumber", appCenterBuild.get("[" + i + "].lastBuild.buildNumber"));
                    appCenterBuildStatus.put("lastBuildStatus", appCenterBuild.get("[" + i + "].lastBuild.status"));
                    appCenterBuildStatus.put("lastBuildResult", appCenterBuild.get("[" + i + "].lastBuild.result"));
                    break;
                }
            }

            Map<String, Object> appCenterReleaseBuildDetails = basicBSAuthRequest(appCenterURI, ""+projectName+"/releases", headers);
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
                    captureException(e);
                }

            }

            int releaseId = Integer.valueOf(appCenterReleaseAutomationBuild.get("ReleaseBuildId").toString());
            Map<String, Object> downloadURL = basicBSAuthRequest(appCenterURI, ""+projectName+"/releases/"+releaseId+"", headers);
            String identifyProjectName = projectName+"_"+releaseId+"_"+appCenterBuildStatus.get("lastBuildID").toString()+"_"+appCenterReleaseAutomationBuild.get("ReleaseBuildShortVersion");

            buildData.put("releaseId", identifyProjectName);
            Map<String, Object> recentBuilds = basicBSAuthRequest(BrowserStackURI, "app-automate/recent_group_apps", null);
            boolean uploadStatus = true;
            for (int i = 0; i < recentBuilds.size(); i++) {
                if(recentBuilds.get("[" + i + "].custom_id")!=null){
                    if(recentBuilds.get("[" + i + "].custom_id").equals(identifyProjectName)){
                        logger.info("Build was already Uploaded : " + recentBuilds.get("[" + i + "].app_url"));
                        app_url = recentBuilds.get("[" + i + "].app_url").toString();
                        uploadStatus = false;
                        break;
                    }
                }
            }
            if(uploadStatus){
                HttpResponse<String> response = Unirest.post(""+BrowserStackURI+"app-automate/upload").basicAuth(propertiesPojo.getBrowserStack_UserName(),propertiesPojo.getBrowserStack_Password())
                        .multiPartContent().field("data","{\"custom_id\": \""+identifyProjectName+"\"}")
                        .field("url", downloadURL.get("download_url").toString())
                        .asString();
                Map<String, Object> respMap3 = JsonFlattener.flattenAsMap(response.getBody());
                app_url = respMap3.get("app_url").toString();
                logger.info("New Build uploaded Successfully : " + app_url);
            }
            buildData.put("app_url",app_url);
        }catch (Exception e){
            logger.error("Check App center and Browser Stack configurations in properties file");
            captureException(e);
        }
        if(app_url == null){
            hardFail("Appcenter/Browser stack configuration issues, Check properties file");
        }
        return buildData;
    }

}

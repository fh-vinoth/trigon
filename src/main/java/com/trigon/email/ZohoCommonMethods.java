package com.trigon.email;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.trigon.api.APICore;
import com.trigon.security.AES;
import com.trigon.utils.TrigonUtils;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.collections.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZohoCommonMethods extends TrigonUtils {

    /**
     * @param: refresh_token
     * @param: client_secret
     * @param: client_id
     * @Author: Kanna
     * @Description: Get Zoho access token
     */
    public Map<String, Object> getZohoAccessToken(String refresh_token, String client_secret, String client_id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();
        try {
            queryParams.put("refresh_token", refresh_token);
            queryParams.put("client_secret", client_secret);
            queryParams.put("client_id", client_id);
            queryParams.put("grant_type", "refresh_token");

            Response resp = requestHandling("https://accounts.zoho.com/", "POST", "oauth/v2/token",null,null,queryParams,null,null,null,null);
            response = JsonFlattener.flattenAsMap(resp.asString());
        } catch (Exception e) {
            hardFail("Failed to get zoho access token" ,e);
        }
        return response;
    }


    /**
     * @param: filePath
     * @Author: Kanna
     * @Description: Get Zoho access token
     */
    public Map<String, Object> UploadFileInZoho(String filePath,String accessToken) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> multiFormMap = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> formParams = new HashMap<>();
        try {
            System.out.println("Uploading to ZOHO Drive !! It takes a while depending on your network and depending on size of report !! Please, Wait.... ");
//            String folderPath = AES.decrypt("NJII0Igp9JMEnGZv3gzX9PflbJQhsNhdRsYun3X89nV5uiE3eZlN2YXyBYRun+bo","t2sautomation");
            String folderPath = AES.decrypt("+tDLW4T3xpLdSyRML4OCubhFrSh/TggEOwOaDgLaNALKUarvKFuaKS9GvwptSg4o","t2sautomation");
            String[] file = filePath.split("/");
            int size = file.length;
            String filename = file[size-1];

            headers.put("Authorization","Zoho-oauthtoken "+accessToken);
            formParams.put("filename", filename);
            formParams.put("parent_id", folderPath);
            formParams.put("override-name-exist", "false");
            multiFormMap.put("content",filePath);

            Response resp = requestHandling("https://www.zohoapis.com", "POST", "workdrive/api/v1/upload",headers,null,null,formParams,null,null,multiFormMap);
            response = JsonFlattener.flattenAsMap(resp.asString());
            System.out.println("Test results Uploaded in ZOHO Drive");
            System.out.println("[########################################]: Completed");

        } catch (Exception e) {
            hardFail("Failed to upload file in zoho" ,e);
        }
        return response;
    }

    /**
     * @param: parentFolderId
     * @param: FileId
     * @param: accessToken
     * @Author: Kanna
     * @Description: Get Zoho access token
     */
    public Map<String, Object> unZipFileInZoho(String parentFolderId,String FileId,String accessToken) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        try {
            headers.put("Authorization","Zoho-oauthtoken "+accessToken);

            String requestBody = "{\n" +
                    "   data: {\n" +
                    "      attributes: {\n" +
                    "         resource_id: \""+FileId+"\",\n" +
                    "         selected_file_extraction: \"false\",\n" +
                    "         merge: \"yes\"\n" +
                    "      },\n" +
                    "      type: \"unzip\"\n" +
                    "   }\n" +
                    "}";


            Response resp = requestHandling("https://www.zohoapis.com", "POST", "workdrive/api/v1/files/"+parentFolderId+"/unzip",headers,null,null,null,null,requestBody,null);
            response = JsonFlattener.flattenAsMap(resp.asString());
            System.out.println("Test results Uploaded in ZOHO Drive");
            System.out.println("[########################################]: Completed");

        } catch (Exception e) {
            hardFail("Failed to unzip file in zoho" ,e);
        }
        return response;
    }

    /**
     * @Author: Kanna
     * @Description: Get files or folders
     */
    public Map<String, Object> getFileInZoho(String fileId, String accessToken) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        try {
            headers.put("Authorization","Zoho-oauthtoken "+accessToken);

            Response resp = requestHandling("https://www.zohoapis.com", "GET", "workdrive/api/v1/files/"+fileId,headers,null,null,null,null,null,null);
            response = JsonFlattener.flattenAsMap(resp.asString());

        } catch (Exception e) {
            hardFail("Failed to get file from zoho ",e);
        }
        return response;
    }

    /**
     * @Author: Kanna
     * @Description: Delete zoho file in drive
     */
    public Map<String, Object> trashZohoFile(String fileId, String accessToken) {
        APICore x = new APICore();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        try {

            headers.put("Authorization","Zoho-oauthtoken "+accessToken);

            String requestBody = "{\n" +
                    "   data: {\n" +
                    "      attributes: {\n" +
                    "         status: \"51\",\n" +
                    "      },\n" +
                    "      type: \"files\"\n" +
                    "   }\n" +
                    "}";

            Response resp = requestHandling("https://www.zohoapis.com", "PATCH", "workdrive/api/v1/files/"+fileId ,headers,null,null,null,null,requestBody,null);
            response = JsonFlattener.flattenAsMap(resp.asString());

        } catch (Exception e) {
            hardFail("Failed to delete file from zoho ",e);
        }
        return response;
    }


    /**
     * @Author: Kanna
     * @Description: Create zoho folder
     */
    public Map<String, Object> createZohoFolder(String parentFolderPath, String accessToken) {
        logStepAction("Create a folder in zoho in specific path");
        APICore x = new APICore();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();
        try {

            headers.put("Authorization","Zoho-oauthtoken "+accessToken);

            String requestBody = "{\n" +
                    "   data: {\n" +
                    "      attributes: {\n" +
                    "         name: \"Collection\",\n" +
                    "         parent_id: \""+parentFolderPath+"\",\n" +
                    "      },\n" +
                    "      type: \"files\"\n" +
                    "   }\n" +
                    "}";

            Response resp = requestHandling("https://www.zohoapis.com", "POST", "workdrive/api/v1/files" ,headers,null,null,null,null,requestBody,null);
            response = JsonFlattener.flattenAsMap(resp.asString());
        } catch (Exception e) {
            hardFail("Failed to create folder from zoho" ,e);
        }
        return response;
    }


    /**
     * @param: sourceDirPath
     * @param: zipFilePath
     * @Author: Kanna
     * @Description: Folder to zip file
     */
    public void folderToZip(String sourceDirPath, String zipFilePath) throws IOException {
        System.out.println("Entered folder to zip stage");
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
        catch (IOException e){
            System.out.println("Issue while making report ZIP process");
        }
    }


    private Response requestHandling(String URI, String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, Map<String, Object> multiPartMap) {
        RequestSpecification requestSpecification = null;
        try {
            RestAssured.baseURI = URI;
            RestAssured.useRelaxedHTTPSValidation();
            requestSpecification = RestAssured.given().request().urlEncodingEnabled(false);
            RestAssuredConfig restAssuredConfig = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig());
            requestSpecification.config(restAssuredConfig);
            requestPreparation(headers, cookies, queryParams, formParams, pathParams, requestBody, requestSpecification);
            if (CollectionUtils.hasElements(multiPartMap)) {
                for (Map.Entry<String, Object> entry : multiPartMap.entrySet()) {
                    String k = entry.getKey();
                    Object v = entry.getValue();
                    requestSpecification.multiPart(k, new File(v.toString()));
                }
            }
            if (requestBody != null) {
                if (CollectionUtils.hasElements(Collections.singleton(requestBody))) {
                    requestSpecification.body(requestBody);
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return executeAPIMethod(HttpMethod, Endpoint, requestSpecification);
    }


    private Response executeAPIMethod(String HttpMethod, String Endpoint, RequestSpecification requestSpecification) {
        Response response = null;
        try {
            try {
                switch (HttpMethod) {
                    case "GET":
                        response = requestSpecification.get(Endpoint).then().extract().response();
                        break;
                    case "POST":
                        response = requestSpecification.post(Endpoint).then().extract().response();
                        break;
                    case "PUT":
                        response = requestSpecification.put(Endpoint).then().extract().response();
                        break;
                    case "PATCH":
                        response = requestSpecification.patch(Endpoint).then().extract().response();
                        break;
                    case "DELETE":
                        response = requestSpecification.delete(Endpoint).then().extract().response();
                        break;
                    default:
                        logApiReport("FAIL", "Method " + HttpMethod + " is not yet implemented");
                        break;
                }
            } catch (Exception e) {

            }
        } catch (Exception e) {
            captureException(e);
        }
        return response;
    }

    private void requestPreparation(Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, RequestSpecification requestSpecification) {
        try {
            if (headers != null && headers.size() > 0) {
                requestSpecification.headers(new LinkedHashMap<>(headers));
            }
            if ((pathParams != null && pathParams.size() > 0)) {
                requestSpecification.pathParams(new LinkedHashMap<>(pathParams));
            }
            if ((queryParams != null && queryParams.size() > 0)) {
                requestSpecification.queryParams(new LinkedHashMap<>(queryParams));
            }
            if ((formParams != null && formParams.size() > 0)) {
                requestSpecification.formParams(new LinkedHashMap<>(formParams));
            }
            if ((cookies != null && cookies.size() > 0)) {
                requestSpecification.cookies(new LinkedHashMap<>(cookies));
            }
        } catch (Exception e) {
            captureException(e);
        }
    }

}

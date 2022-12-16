package com.trigon.testrail;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cases extends Client {

    /**
     * @param status
     * @param comment
     * @param runId
     * @param caseId
     * @return void
     * @throws IOException
     * @throws APIException
     */
    public void setTestCaseStatus(int status, String comment, String runId, String caseId)
            throws IOException, APIException {
        Map<String, java.io.Serializable> data = new HashMap<>();
        data.put("status_id", status);
        data.put("comment", comment);
        apiClient.sendPost(String.format("add_result_for_case/%s/%s", runId, caseId), data);
    }

    /**
     * @param caseId
     * @return String
     * @throws IOException
     * @throws APIException
     */
    public String getTestCaseTitle(String caseId) throws IOException, APIException {
        JsonObject c = (JsonObject) apiClient.sendGet("get_case/" + caseId);
        return c.get("title").toString();
    }

    /**
     * @param runId
     * @return JSONArray
     * @throws IOException
     * @throws APIException
     */
    public JsonArray getTestCasesOfRun(String runId) throws IOException, APIException {
        return (JsonArray) apiClient.sendGet("get_tests/" + runId);
    }
}

package com.trigon.testrail;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Runs extends Client {

    public JSONObject getRun(String runId) throws IOException, APIException {
        return (JSONObject) apiClient.sendGet("get_run/" + runId);
    }

    /**
     * @param projectId
     * @param runName
     * @return JSONObject containing full information and just created Test Run
     * @throws IOException
     * @throws APIException
     */
    public JSONObject createRunForProject(String projectId, String runName) throws IOException, APIException {
        Map<String, java.io.Serializable> data = new HashMap<>();
        data.put("name", runName);
        data.put("include_all", true);

        return (JSONObject) apiClient.sendPost("add_run/" + projectId, data);
    }

    public String createRunAndGetRunId(String projectId, String runName) throws IOException, APIException {
        Map<String, java.io.Serializable> data = new HashMap<>();
        data.put("name", runName);
        data.put("include_all", true);
        JSONObject obj = (JSONObject) apiClient.sendPost("add_run/" + projectId, data);
        return obj.get("id").toString();
    }

    /**
     * @param runId
     * @return void
     * @throws IOException
     * @throws APIException
     * @note should be used only in case that all modifications are already done
     * otherwise do not use close method cause after it even delete is not possible
     */
    public void closeRunById(String runId) throws IOException, APIException {
        apiClient.sendPost("close_run/" + runId, "");
    }

    /**
     * @param runId
     * @return void
     * @throws IOException
     * @throws APIException
     * @note Deleting runs should be used in cases when it is
     * created by mistake and can not be used anymore
     */
    public void deleteRunById(String runId) throws IOException, APIException {
        apiClient.sendPost("delete_run/" + runId, "");
    }
}

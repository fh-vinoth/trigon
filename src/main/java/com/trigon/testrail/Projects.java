package com.trigon.testrail;

import org.json.simple.JSONArray;

import java.io.IOException;

public class Projects extends Client {

    public JSONArray getProjects() throws IOException, APIException {
        return (JSONArray) apiClient.sendGet("get_projects");
    }

}

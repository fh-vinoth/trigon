package com.trigon.testrail;

import com.google.gson.JsonArray;

import java.io.IOException;

public class Projects extends Client {

    public JsonArray getProjects() throws IOException, APIException {
        return (JsonArray) apiClient.sendGet("get_projects");
    }

}

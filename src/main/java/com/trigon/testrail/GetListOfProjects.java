package com.trigon.testrail;



import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.testng.annotations.Test;

import java.util.HashMap;

public class GetListOfProjects {
    @Test
    public void getListOfTestRailProjects() {
        Projects p = new Projects();
        try {
            p.getProjects().forEach(name -> {

                try {
                    JsonObject obj = JsonParser.parseString(name.toString()).getAsJsonObject();
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("Project Name : " + obj.get("name"));
                    System.out.println("Project ID   : " + obj.get("id"));
                    System.out.println("Project URL  : " + obj.get("url"));
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("----------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

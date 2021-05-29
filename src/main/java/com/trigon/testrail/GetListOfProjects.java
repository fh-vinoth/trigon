package com.trigon.testrail;


import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.util.HashMap;

public class GetListOfProjects {
    @Test
    public void getListOfTestRailProjects() {
        Projects p = new Projects();
        try {
            p.getProjects().forEach(name -> {
                JSONParser parser = new JSONParser();
                try {
                    HashMap obj = (HashMap) parser.parse(name.toString());
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println("Project Name : " + obj.get("name"));
                    System.out.println("Project ID   : " + obj.get("id"));
                    System.out.println("Project URL  : " + obj.get("url"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("----------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

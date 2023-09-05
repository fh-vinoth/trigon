package com.fh.unittests.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.ElementRepoPojo;
import com.trigon.exceptions.ThrowableTypeAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSONReader {
    public static void main(String[] args) throws FileNotFoundException {
        Gson pGson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
        JsonElement element1 = JsonParser.parseReader(new FileReader("/Users/bhaskarreddy/Desktop/Data/Automation/Development/trigon/src/test/resources/ElementRepositories/Web/WebTest/SearchPage.json"));
        ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);
        System.out.println(eRepo.getElements().get("searchTextBox").getAsJsonObject().get("Web"));
    }
}

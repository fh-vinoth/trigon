package com.fh.unittests.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trigon.bean.testenv.TestEnvPojo;
import com.trigon.exceptions.ThrowableTypeAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestEnvParameters {

    public static void main(String[] args) throws FileNotFoundException {
        Gson pGson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
        JsonElement element1 = JsonParser.parseReader(new FileReader("test-env.json"));
        TestEnvPojo tse = pGson.fromJson(element1, TestEnvPojo.class);

        System.out.println(tse.equals("web"));


    }
}

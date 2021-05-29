package com.fh.unittests.Json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapToJSON {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();

        map.put("bhaskar", "Reddy");
        map.put("Dummy Boy", "No Not");
//        Gson pGson = new GsonBuilder().setPrettyPrinting().create();
//        pGson.toJson(map);

        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>() {
        }.getType();
        String gsonString = gson.toJson(map, gsonType);
        System.out.println(gsonString);


    }
}

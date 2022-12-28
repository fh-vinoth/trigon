package com.fh.unittests.Json;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.trigon.api.bean.APIInputs;
//
import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.util.HashMap;
//
public class APIJSONRead {
//
    public static void main(String[] args) throws FileNotFoundException {
//        Gson pGson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement element1 = JsonParser.parseReader(new FileReader("/Users/bhaskarreddy/Desktop/Data/Automation/Development/trigon/src/test/java/com/trigon/test/Json/apiTest.json"));
//        APIInputs eRepo = pGson.fromJson(element1, APIInputs.class);
//
//        System.out.println(eRepo.getAuthorHistory());
//        System.out.println(eRepo.getTestMethodData().get(0).getTestMethodName());
//        HashMap headers = eRepo.getTestMethodData().get(0).getApiInputData().get(0).getHeaders();
//
//        //System.out.println(headers.get("Store"));
//
////        if(headers.containsValue("Store")){
////            headers.replace("Store","235");
////
////        }
//
//        replaceTestEnvVariables(headers);
//
//        System.out.println(headers);
//
//
    }
//
//
//    private static HashMap replaceTestEnvVariables(HashMap map){
//
//
//        for (Object o : map.keySet()) {
//            if(map.get(o)!=null & map.get(o).equals("Store")){
//                map.replace(o,"Runner");
//            }
//        }
//
////        map.forEach((k,v)-> {
////            if(v.toString().equals("Store")){
////                v.toString().replaceAll("Store","921414");
////                System.out.println(v.toString());
////            }
////        });
//
//        return map;
//    }
}

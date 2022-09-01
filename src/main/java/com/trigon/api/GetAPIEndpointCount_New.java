package com.trigon.api;

import java.util.HashMap;
import java.util.Map;

public class GetAPIEndpointCount_New {
    public static Map<String,String> endpointList = new HashMap<>();
    public static Map<String,String> endpointCount = new HashMap<>();
    public static Map<String,Integer> allEndpointHitCount = new HashMap<>();
    public static Map<String,Integer> allHTTPHitCount = new HashMap<>();
    /**
     * @Author : Nisha A
     * @Description : Get all endpoints, Get all endpoint hit count and get all http method hit count
     * @param httpMethod
     * @param endpoint
     */
    public void getAPIEndpointCount(String httpMethod,String endpoint){
        if(endpoint.startsWith("/")){
            endpoint = endpoint.substring(1);
        }
        if(endpoint.contains("?")){
            int loc = endpoint.indexOf("?");
            endpoint = endpoint.substring(0,loc-1);
        }
        String endPointNew = endpoint.replaceAll("[0-9]","");
        String endPointNewKey = endPointNew+"_"+httpMethod;
        if(allEndpointHitCount.containsKey(endPointNewKey)){
            for(Map.Entry<String,Integer> s : allEndpointHitCount.entrySet()){
                String k = s.getKey();
                int v = s.getValue();
                if(k.equals(endPointNewKey)){
                    v = v+1;
                    allEndpointHitCount.put(endPointNewKey,v);
                }
            }
        }else{
            allEndpointHitCount.put(endPointNewKey,1);
        }
        if(allHTTPHitCount.containsKey(httpMethod)){
            for(Map.Entry<String,Integer> s : allHTTPHitCount.entrySet()){
                String k = s.getKey();
                int v = s.getValue();
                if(k.equals(httpMethod)){
                    v = v+1;
                    allHTTPHitCount.put(httpMethod,v);
                }
            }
        }else{
            allHTTPHitCount.put(httpMethod,1);
        }
    }

    /**
     * Description - This will return total number of endpoints
     * @return
     */
    public int getEndpointListCount(){
        int count = endpointCount.size();
        return count;
    }

    /**
     * Description - This will return total endpoints list with its hit count
     * @return
     */
    public Map<String,Integer> getEndpointsWithHitCount(){
        return allEndpointHitCount;
    }

    /**
     * Description - This will return total hit count for every http method
     * @return
     */
    public Map<String,Integer> getHttpHitCount(){
        return allHTTPHitCount;
    }
}

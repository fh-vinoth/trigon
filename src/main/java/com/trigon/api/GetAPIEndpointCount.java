package com.trigon.api;

import java.util.HashMap;
import java.util.Map;

public class GetAPIEndpointCount {
    public static Map<String,String> endpointList = new HashMap<>();
    public static Map<String,String> endpointCount = new HashMap<>();
    /**
     * @Author : Nisha A
     * @Description : Get API Endpoint Count
     * @param httpMethod
     * @param endpoint
     */
    public void getEndpointCount(String httpMethod,String endpoint){
        if(endpoint.startsWith("/")){
            endpoint = endpoint.substring(1);
        }
        if(endpoint.contains("?")){
            int loc = endpoint.indexOf("?");
            endpoint = endpoint.substring(0,loc-1);
        }

        String endPointNew = endpoint.replaceAll("[0-9]","");
        String endPointNewKey = endPointNew+"_"+httpMethod;
        if(endpointCount.containsKey(endPointNewKey)){
            String HttpMethod = endpointCount.get(endPointNewKey);
            if(!HttpMethod.equalsIgnoreCase(httpMethod)){
                endpointCount.put(endPointNewKey,httpMethod);
                endpointList.put(endpoint+"_"+httpMethod,httpMethod);
            }
        }else{
            endpointCount.put(endPointNewKey,httpMethod);
            endpointList.put(endpoint+"_"+httpMethod,httpMethod);
        }
    }
    public int getEndpointListCount(){
        int count = endpointCount.size();
        return count;
    }
    public Map<String,String> getEndPointLists(){
        return endpointList;
    }
}

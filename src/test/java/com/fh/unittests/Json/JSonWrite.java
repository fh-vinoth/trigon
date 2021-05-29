package com.fh.unittests.Json;

import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JSonWrite {
    public static void main(String[] args) throws IOException {
        ThreadLocal<JsonWriter> errorWriter = new ThreadLocal<>();
        errorWriter.set(new JsonWriter(new BufferedWriter(new FileWriter("errors",true))));
        errorWriter.get().setLenient(true);
        errorWriter.get().setIndent(" ");
        errorWriter.get().beginArray();
//        errorWriter.get().value("testType");
//        errorWriter.get().value("testType1");
//        errorWriter.get().value("testType2");
//        errorWriter.get().value("testType3");

        String a = null;

//        try{
//            if(a.contains("bc")){
//
//            }
//        }catch (Exception e){
//            errorWriter.get().value(ExceptionUtils.getStackTrace(e));
//        }
//
//        try {
//            List<String> l = Arrays.asList("A","B");
//            l.get(5);
//        }catch (Exception e){
//            errorWriter.get().value(ExceptionUtils.getStackTrace(e));
//        }



        errorWriter.get().endArray();
        errorWriter.get().flush();
        //SendEmail sendEmail = new SendEmail();
        //sendEmail.errorEmail("bhaskar.marrikunta@foodhub.com");

    }
}

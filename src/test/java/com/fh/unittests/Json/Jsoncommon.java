package com.fh.unittests.Json;

import com.google.gson.stream.JsonWriter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

public class Jsoncommon {
    public static JsonWriter writer;

    @BeforeSuite(alwaysRun = true)
    public void before() throws IOException {
        writer = new JsonWriter(new BufferedWriter(new FileWriter("TempResults.json")));
        writerr().beginArray();
    }

    @AfterSuite(alwaysRun = true)
    public void after() throws IOException {
        writerr().endArray();
        writerr().flush();
        writerr().close();
    }

    @BeforeTest
    public void BeforeMethod() throws InterruptedException {
        Thread.sleep(100);
    }

    @AfterMethod(alwaysRun = true)
    public void afterm(Method method) throws IOException {
        writerr().beginObject();
        writerr().name("method").value(method.getName());
        writerr().name("class").value(getClass().getSimpleName());
        writerr().endObject();
        writerr().flush();

    }

    public JsonWriter writerr() {
        JsonWriter writer1 = writer;
        return writer1;
    }
}
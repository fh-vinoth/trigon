package com.fh.unittests.Json;

import org.testng.annotations.Test;

import java.io.IOException;

public class GsonStreamApiWrite2 extends Jsoncommon {

    @Test
    public void gsonStreamApiWrite2() throws IOException {


        writerr().beginObject();
        writerr().name("name").value("Audi");
        writerr().name("model").value("2012");
        writerr().name("price").value(22000);

        writerr().name("colours");
        writerr().beginArray();
        writerr().value("gray");
        writerr().value("red");
        writerr().value("white");
        writerr().endArray();

        writerr().endObject();
        writerr().flush();
    }
}
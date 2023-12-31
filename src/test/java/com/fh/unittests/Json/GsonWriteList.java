package com.fh.unittests.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigon.exceptions.ThrowableTypeAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class Item {

    private final String name;
    private final int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}

public class GsonWriteList {

    public static void main(String[] args) throws IOException {

        String fileName = "src/test/resources/items1.json";

        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter isr = new OutputStreamWriter(fos,
                     StandardCharsets.UTF_8)) {

            Gson gson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();

            Item item1 = new Item("chair", 4);
            Item item2 = new Item("book", 5);
            Item item3 = new Item("pencil", 1);

            List<Item> items = new ArrayList<>();
            items.add(item1);
            items.add(item2);
            items.add(item3);

            gson.toJson(items, isr);
        }

        System.out.println("Items written to file");
    }
}
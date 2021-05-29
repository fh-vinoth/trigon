package com.fh.unitests.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class Car {

    private final String name;
    private final String model;
    private final int price;
    private final Item1 colours;


    Car(String name, String model, int price, Item1 colours) {
        this.name = name;
        this.model = model;
        this.price = price;
        this.colours = colours;
    }
}

class Item1 {

    private final String name;
    private final int quantity;

    public Item1(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}

public class GsonDataBindApiWrite {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Audi", "2012", 22000, new Item1("bhaskar", 50)));
        cars.add(new Car("Skoda", "2016", 14000, new Item1("bhaskar1", 51)));
        cars.add(new Car("Volvo", "2010", 19500, new Item1("bhaskar2", 52)));

        String fileName = "src/test/resources/cars.json";
        Path path = Paths.get(fileName);

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(cars, writer);
        }

        System.out.println("Cars written to file");
    }
}
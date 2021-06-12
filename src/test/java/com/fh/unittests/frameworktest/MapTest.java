package com.fh.unittests.frameworktest;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

    public static void main(String[] args) {
        Map <Integer,String> students = new HashMap<Integer,String>();
        students.put(100, "Swati Shashtri");
        students.put(101, "Gina George");
        students.put(102, "Puja Khanna");

        students.compute(100, (key,value) -> value+",Pune");
        System.out.println(students.get(100));
        students.compute(101, (key,value) -> value+",Bangalore");
        System.out.println(students.get(101));

        students.compute(103, (key,value) -> value+",Pune");
        System.out.println(students.get(103));
        students.computeIfPresent(104, (key,value) -> value+",Bangalore");
        System.out.println(students.get(104));

        Map<String, Integer> fruitsLengthMap = new HashMap<String, Integer>();
        fruitsLengthMap.put("apple", 5);
        fruitsLengthMap.put("strawberry", 10);

        String banana = "banana";

        fruitsLengthMap.computeIfAbsent(banana, key -> key.length());
        System.out.println(fruitsLengthMap.get(banana));
    }
}

package com.fh.unittests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Temp {
    public static void main(String[] args) {
        String line = null;
        int index = 0;
        try {
            FileReader fileReader = new FileReader("temp");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line.subSequence(0,line.length()-2));
            }
            index++;
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

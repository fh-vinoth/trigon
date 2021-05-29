package com.trigon.jsonutils;


import org.json.simple.JSONObject;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.trigon.testbase.TestUtilities.cUtils;


public class JavaToJSON {

    @Test
    @Parameters({"sourcePath", "destPath"})
    public void javaToJSON(String sourcePath, String destPath) throws IOException {

        final File FOLDERPATH = cUtils().createOrReadFile(null, "", sourcePath);

        Arrays.asList(FOLDERPATH.listFiles()).forEach(sourceFile -> {

            JSONObject master = new JSONObject();
            JSONObject Supermaster = new JSONObject();

            System.out.println("Executing file " + sourceFile.getName());

            if (sourceFile.getName().contains(".java")) {
                String fileName = sourcePath + sourceFile.getName();
                List<String> list = new ArrayList<>();

                try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                    list = stream.filter(line -> line.contains("~")).collect(Collectors.toList());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.forEach(item -> {
                            String[] getLocators = item.split("[~\";]");
                            String[] z = item.split("[\" \" =]");
                            List<String> listlocators = Arrays.asList(getLocators);
                            JSONObject child = new JSONObject();
                            JSONObject child1 = new JSONObject();


                            for (int i = 0; i < 4; i++) {

                                // System.out.println("Size: " +listlocators.size());
                                child1.put("locators", child);
                                //  child1.put("actionevent", listlocators.get(i));

                                if (listlocators.get(i).contains("xpath") || listlocators.get(i).contains("css") || listlocators.get(i).contains("classname") || listlocators.get(i).contains("id") || listlocators.get(i).contains("link") || listlocators.get(i).contains("partial") || listlocators.get(i).contains("name") || listlocators.get(i).contains("tagname")) {
                                    child.put("Web", "");
                                    child.put("Android", listlocators.get(i));
                                    i++;
                                    if (listlocators.get(i).contains("xpath") || listlocators.get(i).contains("css") || listlocators.get(i).contains("classname") || listlocators.get(i).contains("id") || listlocators.get(i).contains("link") || listlocators.get(i).contains("partial") || listlocators.get(i).contains("name") || listlocators.get(i).contains("tagname")) {
                                        child.put("IOS", listlocators.get(i));
                                        i++;
                                    }
                                    child1.put("actionevent", listlocators.get(i));


                                }

                            }

                            master.put(z[2], child1);
                        }
                );
            }


            String removeExtn = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.'));

            Supermaster.put("name", removeExtn);
            Supermaster.put("pageTitle", "");
            Supermaster.put("defaultlocator", "Android");
            Supermaster.put("elements", master);

            File DestPATH1 = cUtils().createOrReadFile(null, "", destPath);

            File DestPATH = cUtils().createOrReadFile(DestPATH1, "", removeExtn + ".json");
            boolean mkdirs = DestPATH.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(DestPATH)) {
                writer.write(Supermaster.toString().replaceAll("\\\\", ""));
                System.out.println(Supermaster.toString());

                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}




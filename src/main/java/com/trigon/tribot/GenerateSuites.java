package com.trigon.tribot;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.trigon.reports.ReportManager.cUtils;


public class GenerateSuites {

    @Test
    @Parameters({"path", "suiteName","includeGroups"})
    public void generateSuite(String path, String suiteName, @Optional String includeGroups) {
        suiteGenerator(path, suiteName, includeGroups);
    }

    private void suiteGenerator(String path, String suiteName, String includeGroups) {
        try {
//            String path = "/Users/bhaskarreddy/Desktop/Data/Automation/Development/trigon/src/test/java/com/fh/api";
//            String suiteName = "MYT_Android_Test";
//            String includeGroups = "Sanity";

            System.out.println("***************************************************************************************");
            System.out.println("Path      : "+path);
            System.out.println("suiteName : "+suiteName);
            System.out.println("Groups    : "+includeGroups);
            Stream<Path> paths = Files.walk(Paths.get(path));
            Stream<Path> listPaths = Files.list(Paths.get(path));
            BufferedWriter bw;
            String suitesPath = cUtils().createFolder("src/test/resources", "GeneratedSuites","");
            if(includeGroups!=null){
                 bw = new BufferedWriter(new FileWriter(""+suitesPath+"/" + suiteName+"_"+includeGroups + ".xml"));
            }else{
                 bw = new BufferedWriter(new FileWriter(""+suitesPath+"/" + suiteName + ".xml"));
            }

            StringBuffer suiteBuffer = new StringBuffer();
            suiteBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n");

            if(includeGroups!=null){
                suiteBuffer.append("<suite name=\"" + suiteName +"_"+includeGroups + "\" parallel=\"tests\" thread-count=\"5\">\n");
            }else{
                suiteBuffer.append("<suite name=\"" + suiteName + "\" parallel=\"tests\" thread-count=\"5\">\n");
            }


            suiteBuffer.append("\n" +
                    "    <parameter name=\"testEnvPath\" value=\"test-env.json\"></parameter>\n");


            LinkedList<String> testModList = new LinkedList<>();
            LinkedList<String> testClassList = new LinkedList<>();

            listPaths.sorted().forEachOrdered(a -> {
                List<String> dirPath = Arrays.asList(a.toString().split("/"));
                testModList.add(dirPath.get(dirPath.size() - 1));
            });


            paths.sorted().forEach(filePath -> {
                StringBuffer fPath = new StringBuffer();
                if (filePath.toFile().getName().contains(".java")) {
                    String removeExtn = filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.'));
                    List<String> pathEx = Arrays.asList(filePath.toString().split("/"));
                    int j = 0;
                    for (int i = 0; i < pathEx.size(); i++) {
                        if (pathEx.get(i).equalsIgnoreCase("com")) {
                            j = i;
                        }
                    }
                    for (int k = j; k < pathEx.size() - 1; k++) {
                        fPath.append(pathEx.get(k) + ".");
                    }
                    String s1 = fPath.toString() + removeExtn;
                    //System.out.println(s1);
                    testClassList.add(s1);
                }

            });

            testModList.forEach(m -> {
                if (!(m.contains("common") || m.contains("Common")|| m.contains("Util")|| m.contains("util")|| m.contains(".java"))) {

                    if (includeGroups != null) {
                        suiteBuffer.append("    <test name=\"" + m + "\">\n" +
                                "        <groups>\n" +
                                "            <run>\n" +
                                "                <include name=\"" + includeGroups + "\"/>\n" +
                                "            </run>\n" +
                                "        </groups>\n" +
                                "        <classes>\n");
                    } else {
                        suiteBuffer.append("    <test name=\"" + m + "\">\n" +
                                "        <classes>\n");
                    }


                    testClassList.forEach(c -> {
                        if (!(c.contains("common") || c.contains("Common"))) {
                            if (c.contains(m)) {
                                suiteBuffer.append("            <class name=\"" + c + "\"/>\n");
                            }
                        }
                    });
                    suiteBuffer.append("        </classes>\n" +
                            "    </test>\n");
                }
            });

            suiteBuffer.append("\n</suite>");
            bw.write(suiteBuffer.toString());
            bw.close();
            System.out.println("Successfully Created Suite File !! Refer your suite in Folder resources/GeneratedSuites");
            System.out.println("***************************************************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

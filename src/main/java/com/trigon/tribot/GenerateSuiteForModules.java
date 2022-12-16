package com.trigon.tribot;

import com.trigon.testbase.TestController;
import com.trigon.utils.TrigonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.TestNG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GenerateSuiteForModules extends TrigonUtils {

    HashMap<String, List<String>> classToExecute = new HashMap<>();
    HashMap<String, String> parameterToExecute = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(TestController.class);
    static ArrayList<String> moduleNameList = new ArrayList<>();
    static ArrayList<String> moduleClassList = new ArrayList<>();

    public void suiteGeneration(String[] moduleListToRun) {

        String moduleName = null;
        try {
            FileWriter fWriter = new FileWriter(
                    System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/ModuleExecution.xml");


            String suiteName = tEnv().getTestType();
            System.out.println("------------------------------------------------------------------------");
            System.out.println("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n" +
                    "<suite name=\"API_ModuleExecution\">\n" +
                    "    <parameter name=\"isJWT\" value=\"false\"></parameter>\n" +
                    "    <parameter name=\"endpointPrefix\" value=\"client/v1/\"></parameter>");

            fWriter.write("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n" +
                    "<suite name=\"");
            fWriter.write(suiteName.toUpperCase() + "_ModuleExecution");
            fWriter.write("\">\n" +
                    "    <parameter name=\"isJWT\" value=\"false\"></parameter>\n" +
                    "    <parameter name=\"endpointPrefix\" value=\"client/v1/\"></parameter>");

            if (suiteName.equalsIgnoreCase("FUSIONApp_Android")) {
                fWriter.write("<parameter name=\"device\" value=\"Samsung Galaxy Tab S8\"></parameter>\n" +
                        "        <parameter name=\"os\" value=\"12.0\"></parameter>");
            } else if (suiteName.equalsIgnoreCase("MYT") && tEnv().getAppType().equalsIgnoreCase("android")) {
                fWriter.write("<parameter name=\"os\" value=\"11.0\"/>\n" +
                        "        <parameter name=\"device\" value=\"One Plus 9\"></parameter>");
            } else if (suiteName.equalsIgnoreCase("MYT") && tEnv().getAppType().equalsIgnoreCase("ios")) {
                fWriter.write("<parameter name=\"os\" value=\"14.0\"/>\n" +
                        "        <parameter name=\"device\" value=\"iPhone XS\"></parameter>");
            } else if (suiteName.equalsIgnoreCase("MYTWeb") && tEnv().getApiEnvType().equalsIgnoreCase("sit")) {
                fWriter.write("<parameter name=\"url\" value=\"https://myt-sit.fhcdn.dev/\"></parameter>\n");
            } else if (suiteName.equalsIgnoreCase("MYTWeb") && tEnv().getApiEnvType().equalsIgnoreCase("prod")) {
                fWriter.write("<parameter name=\"url\" value=\"https://my.foodhub.com\"></parameter>\n");
            } else if (suiteName.equalsIgnoreCase("APOS")) {
                fWriter.write("<parameter name=\"os\" value=\"10.0\"/>\n" +
                        "    <parameter name=\"device\" value=\"onePlus 8\"/>");
            } else if (suiteName.equalsIgnoreCase("MYPOS")) {
                fWriter.write(" <parameter name=\"os\" value=\"10.0\"/>\n" +
                        "    <parameter name=\"device\" value=\"Samsung Galaxy Tab S7\"/>");
            }else if (suiteName.equalsIgnoreCase("MYPOS")) {
                fWriter.write(" <parameter name=\"os\" value=\"10.0\"/>\n" +
                        "    <parameter name=\"device\" value=\"Samsung Galaxy Tab S7\"/>");
            }
            for (int i = 0; i < moduleListToRun.length; i++) {
                moduleName = moduleListToRun[i];
                System.out.println("<test name=\"" + moduleListToRun[i] + "\">");
                fWriter.write("<test name=\"" + moduleListToRun[i] + "\">");

                System.out.println(parameterToExecute.get(moduleListToRun[i]));
                fWriter.write(parameterToExecute.get(moduleListToRun[i]));

                System.out.println("        <classes>\n");
                fWriter.write("        <classes>\n");

                List<String> reqList = classToExecute.get(moduleListToRun[i]);
                for (int j = 0; j < reqList.size(); j++) {
                    System.out.println(reqList.get(j));
                }

                System.out.println(" </classes>\n" +
                        "    </test>");

                fWriter.write(" </classes>\n" +
                        "    </test>");
            }

            System.out.println("</suite>");
            fWriter.write("</suite>");

            // Assigning the content of the file

            // Closing the file writing connection
            fWriter.close();


            TestNG runner = new TestNG();
            List<String> suitefiles = new ArrayList<String>();
            suitefiles.add(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/ModuleExecution.xml");
            runner.setTestSuites(suitefiles);
            runner.run();
//            System.exit(0);
        } catch (Exception e) {
            logger.error("Failed due to module name is not available in the suite " + moduleName);
            System.out.println(e.getMessage());
        }
    }

    public void fileReading() {
        try {
            System.out.println(System.getProperty("user.dir"));
            File file = null;
            String testType = tEnv().getTestType();
            if (testType.equalsIgnoreCase("api")) {
                file = new File(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/API_Regression.xml");
            } else if (testType.equalsIgnoreCase("FUSION") || testType.equalsIgnoreCase("MYT") || testType.equalsIgnoreCase("MYTWeb") || testType.equalsIgnoreCase("Apos") || testType.equalsIgnoreCase("mypos")) {
                file = new File(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/MYT_Regression.xml");
            } else if (testType.equalsIgnoreCase("fhapp") || testType.equalsIgnoreCase("fhweb") || testType.equalsIgnoreCase("eatappyapp") || testType.equalsIgnoreCase("bigfoodieapp") ) {
                file = new File(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/FH_Regression.xml");
            }else if (testType.equalsIgnoreCase("digitalboard") ) {
                file = new File(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/DB_Regression.xml");
            }else if (testType.equalsIgnoreCase("kdsapp") ) {
                file = new File(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/KDS_Regression.xml");
            }else if (testType.equalsIgnoreCase("d2s") ) {
                file = new File(System.getProperty("user.dir") + "/src/test/resources/TestSuites/moduleExecution/D2S_Regression.xml");
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String mainString = "";
            while ((st = br.readLine()) != null) {
                mainString = mainString + st;
            }


            String[] req_Name = mainString.split("<test name=");

            for (int i = 1; i < req_Name.length; i++) {
//                System.out.println(req_Name[i].split("\">")[0].replace("\"", ""));
                moduleNameList.add(req_Name[i].split("\">")[0].replace("\"", ""));
            }

            String[] testModule = mainString.split("</test>");
            for (int i = 0; i < testModule.length - 1; i++) {
//                System.out.println(testModule[i].split("<test name=")[1].split(moduleNameList.get(i)+"\">")[1]);
                moduleClassList.add(testModule[i].split("<test name=")[1].split(moduleNameList.get(i) + "\">")[1]);
            }


            for (int i = 0; i < moduleClassList.size(); i++) {
                ArrayList<String> moduleClassesList = new ArrayList<>();
//                System.out.println(moduleClassList.get(i).split("<classes>")[1].replace("</classes> ","").trim());
                String tempStr = moduleClassList.get(i).split("<classes>")[1].replace("</classes> ", "").trim();
                String[] temp = tempStr.split("\"/>");
                moduleClassesList.clear();
                for (int j = 0; j < temp.length; j++) {
                    moduleClassesList.add((temp[j] + "\"/>").trim());
                }
                classToExecute.put(moduleNameList.get(i), moduleClassesList);

                String tempParam = moduleClassList.get(i).split("<classes>")[0];
                String[] tempP = tempParam.split("<parameter ");
                String param = "";
                for (int k = 0; k < tempP.length; k++) {
                    if (!tempP[k].trim().equals("")) {
//                        System.out.println("<parameter "+tempP[k].trim());
                        param = param + "<parameter " + tempP[k].trim() + "\n";
                    }
                }
                parameterToExecute.put(moduleNameList.get(i), param);
            }


        } catch (Exception e) {
            System.out.println("Error");
        }
    }

}

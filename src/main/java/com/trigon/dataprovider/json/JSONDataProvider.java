package com.trigon.dataprovider.json;

import com.google.gson.*;
import com.trigon.dataprovider.json.bean.TestModules;
import com.trigon.exceptions.ThrowableTypeAdapter;
import com.trigon.reports.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;


public class JSONDataProvider extends ReportManager {
    private static final Logger logger = LogManager.getLogger(JSONDataProvider.class);
    //private List<File> fileList = new ArrayList<>();

    public Object[][] getJsonData(String methodName) {
        Object[][] dataProvider = null;
        try {
            if(tEnv().getJsonFilePath() == null){
                List<File> allFiles = cUtils().listAllFiles(new File(tEnv().getJsonDirectory()));
                for (File file : allFiles) {
                    if (!file.isDirectory()) {
                        if (file.getName().contains(".json")) {
                            dataProvider = searchForMethod(methodName, String.valueOf(file));
                            if (dataProvider != null) {
                                logger.info("Identified Method " + methodName + " in File " + file);
                                break;
                            }
                        }
                    }
                }
            }else{
                dataProvider = searchForMethod(methodName, tEnv().getJsonFilePath());
            }


        } catch (Exception e) {
            captureException(e);
        }

        if (dataProvider == null) {
            logger.error("Matching method name " + methodName + " is not found in JSON Files or Directory " + tEnv().getJsonDirectory());
            dataProvider = new Object[0][0];
        }
        return dataProvider;
    }


    private Object[][] searchForMethod(String methodName, String path) {

        Object[][] dataProvider = null;
        int counter = 0;
        Gson gson = new GsonBuilder().registerTypeAdapter(Throwable.class, new ThrowableTypeAdapter()).setPrettyPrinting().create();
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(path));
            TestModules bean = gson.fromJson(jsonElement, TestModules.class);
            JsonArray executableCases = new JsonArray();
            if (bean.getModules().size() > 0) {
                for (int i = 0; i < bean.getModules().size(); i++) {
                    if ((counter == 0) && bean.getModules().get(i).getTestdata().getAsJsonObject().has(methodName)) {
                        JsonArray validCases = bean.getModules().get(i).getTestdata().getAsJsonObject().getAsJsonArray(methodName);
                        for (int size = 0; size < validCases.size(); size++) {
                            if (validCases.get(size).getAsJsonObject().has("executeTest")) {
                                if (!(validCases.get(size).getAsJsonObject().get("executeTest").getAsString().equalsIgnoreCase("No"))) {
                                    executableCases.add(validCases.get(size));
                                }
                            } else {
                                executableCases.add(validCases.get(size));
                            }
                        }
                        counter++;
                        dataProvider = new Object[executableCases.size()][1];
                        for (int j = 0; j < executableCases.size(); j++) {
                            LinkedHashMap<String, Object> data = gson.fromJson(executableCases.get(j), LinkedHashMap.class);
                            dataProvider[j][0] = data;
                        }
                    } else if ((counter > 0) && bean.getModules().get(i).getTestdata().getAsJsonObject().has(methodName)) {
                        logger.error("Your json has two modules with the same methodName :: " + methodName + " , Please rename the specific testcase.");
                    }
                }
            }
        } catch (JsonSyntaxException | FileNotFoundException e) {
            captureException(e);
            logger.error("Invalid JSON Path or File");
        }

        return dataProvider;
    }


}

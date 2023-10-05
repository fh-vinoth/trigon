package com.trigon.tribot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.trigon.wrapper.TestModels;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.trigon.reports.ReportManager.cUtils;


public class TriBot {

    private static String JSON_MAIN_ELEMENT_NAME = "AuthorName";
    private static String JSON_MAIN_ELEMENT_NAME1 = "authorname";
    private static String JSON_PAGE_TITLE = "pageTitle";
    private static String ELEMENTS = "elements";
    private static String AUTOGEN_PATH = "../..//src/test/";
    //    private static String AUTOGEN_PATH = "src/test/";
    private static String CURRENT_FILE_NAME = "src/test/";

    private List<String> errorCollection = new ArrayList<>();

    private String currentFolderName;

    private static String pageTitleMethodCreation(String pageTitle) {
        String pagetitleMethod = "    public void pageTitle() {\n" +
                "        String title = \"" + pageTitle + "\";\n" +
                "        tmodel.verifyTitle(title, \"Exact\");\n" +
                "    }\n";
        return pagetitleMethod;
    }

    public String getCurrentFolderName() {
        return currentFolderName;
    }

    public void setCurrentFolderName(String currentFolderName) {
        this.currentFolderName = currentFolderName;
    }

    public void createJavaFile(String elemPath, String jsonFile, String originalname, String path, String folderName, BufferedWriter bw) throws IOException {

        Map<String, Object> obj = null;
        JsonObject jsonObject = null;
        try {
            Gson gson = new Gson();
            jsonObject = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
            obj = gson.fromJson(jsonObject, Map.class);
        } catch (JsonParseException e) {
            errorCollection.add("Please check your json structure.. look for missing commas,parameters at " + path + " and " + e.getMessage());
        }
        try {
            List<String> stringList = new ArrayList<>();
            List<String> aList = new ArrayList<>();

            obj.forEach((k, v) -> {
                stringList.add(k);
            });

            if (stringList.contains(JSON_MAIN_ELEMENT_NAME) || stringList.contains(JSON_MAIN_ELEMENT_NAME1)) {

            } else {
                System.err.println("Please add AuthorName Key to JSON File" + " : JSON FILE NAME :" + originalname);
                errorCollection.add("Please add AuthorName Key to JSON File" + " : JSON FILE NAME :" + originalname);
            }
            setCurrentFolderName(folderName);
            bw.write(packages());
            String classCreation = "public class" + " " + jsonFile + " extends TestModelCore {\n";
            String constructorCreation =
                    "    \n" + "    private TestModels tmodel;\n\n" +
                            "    //Page Constructor\n" +

                            "    public " + jsonFile + "() {\n" +
                            "        tmodel = new TestModels();\n" +
                            "        tEnv().setPagesJsonFile(new File(\"" + elemPath.replace("../../"+CURRENT_FILE_NAME+"/", "") + "/" + originalname + ".json" + "\"));\n" +
                            "Gson pGson = new GsonBuilder().setPrettyPrinting().create();\n" +
                            "        JsonElement element1 = null;\n" +
                            "        File jsonFilePath = tEnv().getPagesJsonFile();\n" +
                            "        try {\n" +
                            "            element1 = JsonParser.parseReader(new FileReader(jsonFilePath));\n" +
                            "        } catch (FileNotFoundException e) {\n" +
                            "            e.printStackTrace();\n" +
                            "        }\n" +
                            "        ElementRepoPojo eRepo = pGson.fromJson(element1, ElementRepoPojo.class);\n" +
                            "        boolean jsonVisited = eRepo.getElements().keySet().stream().anyMatch(keyStr -> (eRepo.getElements().get(keyStr).getAsJsonObject().has(\"Web_beforeElement\")\n" +
                            "                || eRepo.getElements().get(keyStr).getAsJsonObject().has(\"App_beforeElement\")));\n" +
                            "\n" +
                            "        if(!jsonVisited) {\n" +
                            "            System.out.println(\"Checking  -> \" + new File(jsonFilePath.getName()) + \" for setting Fallbacks to all elements.\");\n" +
                            "            List<String> tags = readConfigFileForTags(\"isEnabled\");\n" +
                            "            Set<String> xpaths = new LinkedHashSet<>();\n" +
                            "            if (tEnv().getElementLocator().equalsIgnoreCase(\"Web\")) {\n" +
                            "                for (String tag : tags) {\n" +
                            "                    xpaths = scrapeXpaths(tag);\n" +
                            "                }\n" +
                            "            } else if (tEnv().getElementLocator().equalsIgnoreCase(\"Android\")) {\n" +
                            "                xpaths = scrapeXpathsForAndroid();\n" +
                            "            }\n" +
                            "            Set<String> scrapedXpaths = xpaths;\n" +
                            "            eRepo.getElements().keySet().forEach(keyStr -> {\n" +
                            "                if (!keyStr.contains(\"XpathValues\")) {\n" +
                            "                    selfHealInConstructor(scrapedXpaths, keyStr);\n" +
                            "                }\n" +
                            "            });\n" +
                            "        }" +
                            "    }" +
                            "\n";

            bw.write(classCreation);
            bw.write(constructorCreation);

            if (stringList.contains(JSON_PAGE_TITLE)) {
                String pageTitle = obj.get(JSON_PAGE_TITLE).toString();
                bw.write("\n");

                String pageTitleMethod = pageTitleMethodCreation(pageTitle);
                bw.write(pageTitleMethod);
                bw.write("\n");
            } else {
                System.err.println("Please add pageTitle Key to JSON File" + " : JSON FILE NAME :" + originalname);
                errorCollection.add("Please add pageTitle Key to JSON File" + " : JSON FILE NAME :" + originalname);
            }

            if (stringList.contains(ELEMENTS)) {
                JsonObject elementsObject = jsonObject.get(ELEMENTS).getAsJsonObject();
                elementsObject.keySet().forEach(k -> aList.add(k));
                String createElementsMethod = createElementsMethod(aList, elementsObject, originalname);
                bw.write(createElementsMethod);

            } else {
                System.err.println("Please add elements Key to JSON File" + " : JSON FILE NAME :" + originalname);
                errorCollection.add("Please add elements Key to JSON File" + " : JSON FILE NAME :" + originalname);
            }
            bw.write("}");
            bw.close();
            //System.out.println("Total No of Locators present in JSON : " + aList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createElementsMethod(List<String> aList, JsonObject map, String jsonFilename) {
        StringBuffer stringBuffer = new StringBuffer();

        List<String> CommonActionAttribute = Arrays.asList("verifyAttribute");

        List<String> CommonActionInputLocatorAndMap = Arrays.asList("enterText", "getAttribute", "getValueFromListByIndex");
        List<String> CommonActionInputLocator = Arrays.asList("click", "clearText", "getText", "isEnabled", "isDisplayed", "isNotDisplayed", "isPresent", "isNotPresent", "verifyDisplayed",
                "isSelected", "getDropDownValues", "getSelectedDropDownValues", "switchToFrame", "tabOut", "scrollByVisibleElement", "getListOfElements", "scrollToElement", "horizontalSwipeToElement", "getElementLocation");
        List<String> CommonActionInputLocatorMapAndAction = Arrays.asList("verifyValueFromList", "dropDown", "verifyText");
        List<String> CommonActionInputStringAndAction = Arrays.asList("verifyAlert");
        List<String> CommonActionLocatorBy = Arrays.asList("getLocatorFromJson");
        List<String> CommonActionHelper = new ArrayList<>();

        Class aClass = TestModels.class;
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            Collections.addAll(CommonActionHelper, method.getName() + "##" + method.getReturnType().getSimpleName());
        }
        long t1 = System.currentTimeMillis();
        // Get the methods
        Gson gson = new Gson();
        /*for (String methodName : aList) {
            Map map1 = gson.fromJson(map.get(methodName), Map.class);*/
        aList.forEach(methodName -> {
            Map map1 = gson.fromJson(map.get(methodName), Map.class);
            AtomicBoolean actionflag = new AtomicBoolean(false);
            map1.forEach((key, value) -> {

                        if (key.toString().trim().contains("actionevent") && String.valueOf(value)!=null && !String.valueOf(value).isEmpty()) {
                            actionflag.set(true);
                            String actionValue = String.valueOf(value);
                            String[] jsonKeyValue = actionValue.split(",");

                            for (int a = 0; a < jsonKeyValue.length; a++) {
                                AtomicBoolean flag = new AtomicBoolean(false);
                                String methodNameLower = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);

                                methodGenerator(CommonActionHelper, CommonActionInputLocatorMapAndAction, jsonKeyValue[a].trim(), methodNameLower, stringBuffer, methodName, "(HashMap<String, Object> map, String textAction, ", "tmodel.mapKeyFinder(map, \"" + methodName + "\"), textAction, ", "(String inputValue, String textAction, ", "inputValue, textAction, ", flag);
                                methodGenerator(CommonActionHelper, CommonActionAttribute, jsonKeyValue[a].trim(), methodNameLower, stringBuffer, methodName, "(HashMap<String, Object> map, String textAction,", "tmodel.mapKeyFinder(map, \"" + methodName + "_attribute\"), tmodel.mapKeyFinder(map, \"" + methodName + "_attributeValue\"), textAction, ", "(String attribute, String attributeValue, String textAction, ", "attribute, attributeValue, textAction, ", flag);
                                methodGenerator(CommonActionHelper, CommonActionInputLocatorAndMap, jsonKeyValue[a].trim(), methodNameLower, stringBuffer, methodName, "(HashMap<String, Object> map,", "tmodel.mapKeyFinder(map, \"" + methodNameLower + "\"),", "(String inputValue, ", "inputValue, ", flag);
                                methodGenerator(CommonActionHelper, CommonActionInputLocator, jsonKeyValue[a].trim(), methodNameLower, stringBuffer, methodName, "(", "", "(", "", flag);
                                methodGenerator(CommonActionHelper, CommonActionInputStringAndAction, jsonKeyValue[a].trim(), methodNameLower, stringBuffer, methodName, "(HashMap<String, Object> map, String alertAction, String textAction, ", "tmodel.mapKeyFinder(map, \"" + methodName + "\"), alertAction, textAction, ", "(String inputValue, String alertAction, String textAction, ", "inputValue, alertAction, textAction, ", flag);
                                methodGenerator(CommonActionHelper, CommonActionLocatorBy, jsonKeyValue[a].trim(), methodNameLower, stringBuffer, methodName, "(", "", "(", "", flag);

                                if (!flag.get()) {
                                    //String methodNameLower = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
                                    String s = "    public void " + methodNameLower + "_" + jsonKeyValue[a] + "(HashMap<String, Object> map, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                                            "        // tmodel." + jsonKeyValue[a] + "(" + "\"" + methodName + "\"" + "," + "\"" + methodName + "\", wait_logReport_isPresent_Up_Down_XpathValues);  \n" +
                                            "        // This Method is not yet developed in CommonActionHelper. If it is intended method, please check actionevent  \n" +
                                            "        System.out.println(\"This Method is not yet developed in CommonActionHelper. If it is intended method, please check actionevent \"); \n" +
                                            "    }\n\n";
                                    stringBuffer.append(s);
                                    System.err.println("Actionevent " + jsonKeyValue[a] + " is not yet implemented for element : " + methodName + " : JSON FILE NAME :" + jsonFilename);
                                    errorCollection.add("Actionevent " + jsonKeyValue[a] + " is not yet implemented : " + methodName + " : JSON FILE NAME :" + jsonFilename);

                                }
                            }

                        } else {
                            if (key.toString().trim().equals("Web")) {
                                locatorValidation(value, methodName, "Web", jsonFilename);
                            } else if (key.toString().trim().equals("Android")) {
                                locatorValidation(value, methodName, "Android", jsonFilename);
                            } else if (key.toString().trim().equals("IOS")) {
                                locatorValidation(value, methodName, "IOS", jsonFilename);
                            } else if(key.toString().trim().equals("actionevent") || key.toString().trim().equals("Web_beforeElement") || key.toString().trim().equals("Web_afterElement") || key.toString().trim().equals("App_beforeElement") || key.toString().trim().equals("App_afterElement")){
                            } else {
                                System.err.println("Error in TestType!!Add testType Like Web/Android/IOS !! check any additional Spaces!!: " + methodName + " : JSON FILE NAME :" + jsonFilename);
                                errorCollection.add("Error in TestType!!Add testType Like Web/Android/IOS !! check any additional Spaces!!: " + methodName + " : JSON FILE NAME :" + jsonFilename);
                            }

                        }

                    }
            );
//            if (!actionflag.get()) {
//                System.err.println("Please add actionevent for element : " + methodName + " : JSON FILE NAME :" + jsonFilename);
//                errorCollection.add("Please add actionevent for element : " + methodName + " : JSON FILE NAME :" + jsonFilename);
//            }
        });
        long t2 = System.currentTimeMillis();
        //System.out.println("Filter Time Taken?= " + (t2 - t1) + "\n");

        return stringBuffer.toString();
    }

    ;

    @Test
    public void autoCodeGenerator() throws IOException {

        String directory = System.getProperty("user.dir");
        File projectFile = new File(directory).getParentFile();

        CURRENT_FILE_NAME = projectFile.getName();
        AUTOGEN_PATH = "../../" +CURRENT_FILE_NAME + "/src/test/";
        //AUTOGEN_PATH = directory + "/src/test/";

        String elementRepoPath = AUTOGEN_PATH + "resources/ElementRepositories/";
        String javaClassPath = AUTOGEN_PATH + "java/autogenerated/pages/";
        String objFactoryPath = AUTOGEN_PATH + "java/autogenerated/objrepo/";

        System.out.println("******************************************************************************************");
        System.out.println(" Please make sure your project or folder name is testautomation_UI or testautomation_API");
        System.out.println("******************************************************************************************");


        List<String> objImportList = new ArrayList<>();
        File ELEMENT_JSON = cUtils().createOrReadFile(null, "", elementRepoPath);
        LinkedHashMap<String, List<String>> objectsPaths = new LinkedHashMap<>();
        List<String> allObjList = new ArrayList<>();

        try {
            List<File> sourceFolders = Arrays.asList(ELEMENT_JSON.listFiles());
            sourceFolders.forEach(sourceFolder -> {
                if (sourceFolder.isDirectory()) {
//                System.out.println("######################################################################################");
//                System.out.println("                    Reading Folder  : " + sourceFolder.getName());
//                System.out.println("######################################################################################");
                    File ELEMENT_JSON_SUB = cUtils().createOrReadFile(null, "", elementRepoPath + sourceFolder.getName());
                    List<File> sourceSubFolders = Arrays.asList(ELEMENT_JSON_SUB.listFiles());
                    sourceSubFolders.forEach(sourceSubFolder -> {
                        List<String> objList = new ArrayList<>();
                        if (sourceSubFolder.isDirectory()) {

                            File ELEMENT_JSON_LIST = cUtils().createOrReadFile(null, "", elementRepoPath + sourceFolder.getName() + "/" + sourceSubFolder.getName());
                            List<File> sourceFiles = Arrays.asList(ELEMENT_JSON_LIST.listFiles());
                            if ((!sourceFiles.isEmpty())) {
//                            System.out.println("**************************************************************************************");
//                            System.out.println("                    Reading Sub Folder  : " + sourceSubFolder.getName());
//                            System.out.println("**************************************************************************************");
                                objImportList.add(sourceFolder.getName() + "." + sourceSubFolder.getName());
                                File AUTO_JAVA_PAGES_PATH = cUtils().createOrReadFile(null, "", javaClassPath + sourceFolder.getName() + "/" + sourceSubFolder.getName());

                                sourceFiles.forEach(sourceFile -> {

                                    try {
                                        if (sourceFile.isFile()) {
                                            if (sourceFile.getName().contains(".json")) {
                                                //System.out.println(sourceFile.getName());
                                                String removeExtn = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf('.'));
                                                String name = Character.toUpperCase(removeExtn.charAt(0)) + removeExtn.substring(1);
                                                objList.add(name);
                                                allObjList.add(name);
                                                File file = cUtils().createOrReadFile(AUTO_JAVA_PAGES_PATH, "",
                                                        name + ".java");
                                                file.getParentFile().mkdirs();
                                                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                                                createJavaFile(elementRepoPath + sourceFolder.getName() + "/" + sourceSubFolder.getName(), name, removeExtn, sourceFile.getAbsolutePath(), sourceFolder.getName() + "." + sourceSubFolder.getName(), bw);
                                            }
                                        }
                                        AUTO_JAVA_PAGES_PATH.deleteOnExit();
                                    } catch (Exception e) {
                                        System.err.println(e);
                                    }
                                });
                            }
                        }


                        objectsPaths.put(sourceFolder.getName() + "/" + sourceSubFolder.getName() + "##" + sourceSubFolder.getName(), objList);
                    });
                }
            });
        } catch (NullPointerException e) {
            Assert.fail("Please make sure your project or folder name is testautomation_UI or testautomation_API");
        }

        objectsPaths.forEach((k, v) -> {
            try {
                String[] getPathAndFile = k.split("##");
                String classFile = Character.toUpperCase(getPathAndFile[1].charAt(0)) + getPathAndFile[1].substring(1);
                objectRepo(v, objImportList, objFactoryPath, classFile, AUTOGEN_PATH + "java/com/fh/" + getPathAndFile[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//        createAPIController();
//        createTModel();
//        createLocalTestController(objectsPaths);

        objectRepo(null, null, objFactoryPath, "APIObjects", AUTOGEN_PATH + "java/com/fh/api");
//        objectRepo(allObjList, objImportList, objFactoryPath,"TestObjects",AUTOGEN_PATH + "java/com/fh");
        if (errorCollection.size() > 0) {
            System.out.println("\n" + "###################################### ERROR ALERT !!!!! ##### Please FIX BELOW ISSUES in JSON files ######################################\n");
            errorCollection.forEach(System.out::println);
            Assert.fail("###### ALERT !!!!! ###################################### Please fix Above errors ###############################");
        } else {
            locatorList();
            System.out.println("\n\n" + "####################################################################");
            System.out.println(" Congratulations!!! All Pages are generated successfully !!! ");
            System.out.println("####################################################################");
        }

    }

    public String packages() {

        return "/////////////////////////////////////////////////////////////////////////////////////\n" +
                "//\n" +
                "// This file was automatically generated by CODE GENERATOR.\n" +
                "// DO NOT MODIFY THIS FILE! All your modifications will be lost!\n" +
                "// Modify all your changes in your respective JSON Files only.\n" +
                "// You can regenerate this file by executing GenerateCode.xml\n" +
                "// You DON'T need to regenerate code if there is change in Locators.\n" +
                "// You must regenerate code, When ever you add new element or modify/add actionevent.\n" +
                "//\n" +
                "// Contact bhaskar.marrikunta@foodhub.com for any issues.\n" +
                "//\n" +
                "/////////////////////////////////////////////////////////////////////////////////////\n\n" +
                "package autogenerated.pages." + getCurrentFolderName() + ";\n" + "\n" +
                "import com.controllers.models.TestModels;\n" +
                "import com.trigon.elements.TestModelCore;\n" +
                "import static com.trigon.reports.ReportManager.tEnv;\n" +
                "\n" +
                "import com.google.gson.Gson;\n" +
                "import com.google.gson.GsonBuilder;\n" +
                "import com.google.gson.JsonElement;\n" +
                "import com.google.gson.JsonParser;\n" +
                "import com.trigon.bean.ElementRepoPojo;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.io.FileNotFoundException;\n" +
                "import java.io.FileReader;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.*;"+
                "\n";
    }

    public void objectRepo(List<String> objList, List<String> objImportList, String objFactoryPath, String className, String commonPath) throws IOException {

        if (!className.equals(".DS_Store")) {
            // System.out.println("Creating Java file for class : " + className);
            File AUTO_JAVA_OBJECTS_PATH = cUtils().createOrReadFile(null, "", objFactoryPath);

            File file = cUtils().createOrReadFile(AUTO_JAVA_OBJECTS_PATH, "",
                    className + ".java");
            boolean mkdirs = file.getParentFile().mkdirs();
            //System.out.println(mkdirs ? "Folders Created Exits" : "Folder Creation Unsuccessful or Already Exists");

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            StringBuffer objectBuffer = new StringBuffer();
            objectBuffer.append("/////////////////////////////////////////////////////////////////////////////////////\n" +
                    "//\n" +
                    "// This file was automatically generated by CODE GENERATOR.\n" +
                    "// DO NOT MODIFY THIS FILE! All your modifications will be lost!\n" +
                    "// Modify all your changes in your respective JSON Files only.\n" +
                    "// You can regenerate this file by executing GenerateCode.xml\n" +
                    "// You DON'T need to regenerate code if there is change in Locators.\n" +
                    "// You must regenerate code, When ever you add new element or modify/add actionevent.\n" +
                    "//\n" +
                    "// Contact qeautomation@foodhub.com for any issues.\n" +
                    "//\n" +
                    "/////////////////////////////////////////////////////////////////////////////////////\n\n");
            objectBuffer.append("package autogenerated." + file.getParentFile().getName().toLowerCase() + ";\n");

            identifyRequiredCommonPackages(objectBuffer, null, commonPath);
            //objectBuffer.append("import com.trigon.api.APICoreController;\n");
            if (objImportList != null) {
                objImportList.forEach(item -> {
                    objectBuffer.append("import autogenerated.pages." + item + ".*;\n");
                });
            }
            if (className.equalsIgnoreCase("APIObjects")) {
                objectBuffer.append("\n" +
                        "import java.util.HashMap;\n" +
                        "import java.util.Map;\n" +
                        "\n" +
                        "import static com.trigon.reports.ReportManager.tEnv;\n");
            }

            String content = "public class " + className + " {\n\n";
            objectBuffer.append(content);
            if (className.equalsIgnoreCase("APIObjects")) {
                objectBuffer.append("    public Map<String, Object> headers;\n" +
                        "    public Map<String, Object> queryparams;\n" +
                        "    public Map<String, Object> formparams;\n" +
                        "    public Map<String, Object> pathparams;\n" +
                        "    public Map<String, Object> expectedresponse;\n" +
                        "\n" +
                        "    public APIObjects() {\n" +
                        "        headers = new HashMap<>();\n" +
                        "        queryparams = new HashMap<>();\n" +
                        "        formparams = new HashMap<>();\n" +
                        "        pathparams = new HashMap<>();\n" +
                        "        expectedresponse = new HashMap<>();\n" +
                        "        headers.put(\"Content-Type\", \"application/x-www-form-urlencoded\");\n" +
                        "\n" +
                        "        if ((tEnv().getApiLocale() != null) && (tEnv().getApiRegion() != null)) {\n" +
                        "            headers.put(\"locale\", tEnv().getApiLocale());\n" +
                        "            headers.put(\"region\", tEnv().getApiRegion());\n" +
                        "        }\n" +
                        "        queryparams.put(\"api_token\", tEnv().getApiToken());\n" +
                        "        headers.put(\"store\", tEnv().getApiStore());\n" +
                        "        formparams.put(\"host\", tEnv().getApiHost());\n" +
                        "    }\n\n");
            }

            //objectBuffer.append("    public TestModels tModels() { \n        return new TestModels();\n    }\n\n");
            //objectBuffer.append("    public APICoreController api() { \n        return new APICoreController();\n    }\n\n");

            if (objList != null) {
                objList.forEach(item -> {

                    String methodNameLower = Character.toLowerCase(item.charAt(0)) + item.substring(1);

                    objectBuffer.append("    public " + item + " " + methodNameLower + "() {\n" +
                            "        return new " + item + "();\n" +
                            "    }\n\n");

                });
            }


            identifyRequiredCommonPackages(null, objectBuffer, commonPath);
            objectBuffer.append("}");
            bw.write(objectBuffer.toString());
            bw.flush();
            bw.close();
        } else {
            //System.out.println("File Ignored" +commonPath);
        }


    }


    private void methodVoidType(String method, String methodNameLower, String jsonKeyValue, StringBuffer stringBuffer, String methodName, String function, String body) {
        String s = "    public void " + methodNameLower + "_" + jsonKeyValue + "" + function + "String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        tmodel." + method + "(" + "\"" + methodName + "\"" + ", " + body + "wait_logReport_isPresent_Up_Down_XpathValues);  \n" +
                "    }\n\n";
        stringBuffer.append(s);

    }

    private void methodReturnType(String method, String returnType, String methodNameLower, String jsonKeyValue, StringBuffer stringBuffer, String methodName, String function, String body) {
        String s2 = "    public " + returnType + " " + methodNameLower + "_" + jsonKeyValue + "" + function + "String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return tmodel." + method + "(" + "\"" + methodName + "\"" + ", " + body + "wait_logReport_isPresent_Up_Down_XpathValues);  \n" +
                "    }\n\n";
        stringBuffer.append(s2);
    }

    private void methodGenerator(List<String> wrapperList, List<String> wrapperSubList, String jsonKey, String methodNameLower, StringBuffer stringBuffer, String methodName, String mapFunction, String mapBody, String regFunction, String regBody, AtomicBoolean flag) {
        wrapperList.parallelStream()
                .filter(e -> wrapperSubList.parallelStream().anyMatch(d -> d.equalsIgnoreCase(e.split("##")[0]))).filter(f -> f.contains(jsonKey)).forEach(item -> {
                    if (item.split("##")[1].equalsIgnoreCase("void")) {
                        if (mapFunction.equals("(")) {
                            methodVoidType(item.split("##")[0], methodNameLower, jsonKey, stringBuffer, methodName, regFunction, regBody);
                            flag.set(true);
                        } else {
                            methodVoidType(item.split("##")[0], methodNameLower, jsonKey, stringBuffer, methodName, mapFunction, mapBody);
                            methodVoidType(item.split("##")[0], methodNameLower, jsonKey, stringBuffer, methodName, regFunction, regBody);
                            flag.set(true);
                        }

                    } else {
                        if (mapFunction.equals("(")) {
                            methodReturnType(item.split("##")[0], item.split("##")[1], methodNameLower, jsonKey, stringBuffer, methodName, regFunction, regBody);
                            flag.set(true);
                        } else {
                            methodReturnType(item.split("##")[0], item.split("##")[1], methodNameLower, jsonKey, stringBuffer, methodName, mapFunction, mapBody);
                            methodReturnType(item.split("##")[0], item.split("##")[1], methodNameLower, jsonKey, stringBuffer, methodName, regFunction, regBody);
                            flag.set(true);
                        }
                    }
                });
    }

    private void locatorValidation(Object value, String methodName, String testType, String jsonFilename) {
        String actionValue = String.valueOf(value);
        if (!(actionValue.equals(""))) {
            if (actionValue.startsWith("xpath=") || actionValue.startsWith("classname=") || actionValue.startsWith("css=") || actionValue.startsWith("id=") || actionValue.startsWith("link=") || actionValue.startsWith("partial=") || actionValue.startsWith("name=") || actionValue.startsWith("tagname=") || actionValue.startsWith("predicate=") || actionValue.startsWith("classchain=") || actionValue.startsWith("accessibilityid=") ||
                    actionValue.startsWith("xpath =") || actionValue.startsWith("classname =") || actionValue.startsWith("css =") || actionValue.startsWith("id =") || actionValue.startsWith("link =") || actionValue.startsWith("partial =") || actionValue.startsWith("name =") || actionValue.startsWith("tagname =") || actionValue.startsWith("predicate =") || actionValue.startsWith("classchain =") || actionValue.startsWith("accessibilityid =")) {

            } else {
                System.err.println("Add locatorType like below for testType : " + testType + " : " + methodName + "  Current Value : " + actionValue + " : JSON FILE NAME :" + jsonFilename);
                errorCollection.add("Add locatorType like below for testType : " + testType + " : " + methodName + "  Current Value : " + actionValue + " : JSON FILE NAME :" + jsonFilename);
                locatorList();
            }
        }
    }

    private void locatorList() {

        System.out.println(" Please check ReadMe File or Confluence Pge for Locatiors and Matching Method names to add in JSON files");

//        System.out.println("\n\n*********** Please follow below Locators as  per priority ********");
//        System.out.println("==================================================================");
//        System.out.println("|   Priority    |       Web     |    Android     |       IOS     |");
//        System.out.println("==================================================================");
//        System.out.println("|       1       |	    id	    |accessibilityid |accessibilityid|");
//        System.out.println("|       2	    |       name	|       id	     |       id      |");
//        System.out.println("|       3	    |    classname  |   uiautomator  |	  predicate  |");
//        System.out.println("|       4	    |    linktext	|      name	     |      name     |");
//        System.out.println("|       5	    |       css	    |     xpath	     |      xpath    |");
//        System.out.println("|       6	    |       xpath	|    datamatcher |    classchain |");
//        System.out.println("|       7	    |partiallinktext|    classname	 |     classname |");
//        System.out.println("|       8	    |    tagname    |    viewtag     |      viewtag  |");
//        System.out.println("|       9	    |	            |     image      |	    image    |");
//        System.out.println("|       10	    |	            |     custom	 |      custom   |");
//        System.out.println("|       11	    |	            |partiallinktext |partiallinktext|");
//        System.out.println("|       12	    |	            |     linktext   |	    linktext |");
//        System.out.println("|       13	    |	            |      tagname   |	    tagname  |");
//        System.out.println("|       14  	|	            |        css     |	    css      |");
//        System.out.println("==================================================================");
//
//        System.out.println("\n*********** Please follow below Action Events ****************");
//        listOfActionEvents();
    }


    private void listOfActionEvents() {
        Class aClass = TestModels.class;
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("actionevent : " + method.getName());
        }
    }

    private void identifyRequiredCommonPackages(StringBuffer packageBuffer, StringBuffer methodBuffer, String path) {
        try {

            Stream<Path> paths = Files.walk(Paths.get(path));
            paths.forEach(filePath -> {
                StringBuffer fPath = new StringBuffer();
                if (!filePath.toFile().isDirectory()) {
                    List<String> pathEx;
                    if (System.getProperty("os.name").contains("Windows") || System.getProperty("os.name").contains("windows")) {
                        pathEx = Arrays.asList(filePath.toString().split("\\\\"));
                    } else {
                        pathEx = Arrays.asList(filePath.toString().split("/"));
                    }
                    int j = 0;
                    for (int i = 0; i < pathEx.size(); i++) {
                        if (pathEx.get(i).equalsIgnoreCase("com")) {
                            j = i;
                        }
                    }
                    for (int k = j; k < pathEx.size() - 1; k++) {
                        fPath.append(pathEx.get(k) + ".");
                    }
                    if ((filePath.toString().contains("common")) || (filePath.toString().contains("Common")) || (filePath.toString().contains("util")) || (filePath.toString().contains("Util"))) {
                        if (filePath.toFile().getName().contains(".java")) {
                            String removeExtn = filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.'));
                            //System.out.println("--------------------------------------------------------------------");
                            try {
                                String s1 = fPath + removeExtn;
                                //System.out.println(s1);
                                if (packageBuffer != null) {
                                    packageBuffer.append("import " + fPath + removeExtn + ";\n");
                                    //System.out.println("Identified Common Package Name: " + fPath.toString() + removeExtn);
                                }

                                //System.out.println("Identified Common Method to add package:" + removeExtn);
                                String methodNameLower = Character.toLowerCase(removeExtn.charAt(0)) + removeExtn.substring(1);
                                if (methodBuffer != null) {
                                    methodBuffer.append("    public " + removeExtn + " " + methodNameLower + "() {\n" +
                                            "        return new " + removeExtn + "();\n" +
                                            "    }\n\n");
                                }

//                            Commented below code to identify Constructor

//                            Class<?> c1 = Class.forName(s1);
//
//                            if (methodBuffer != null) {
//                                Constructor constructors[] = c1.getDeclaredConstructors();
//                                for (Constructor cons : constructors){
//                                    String k = cons.toString().replace("public " + fPath.toString() + "", "");
//                                    System.out.println("Identified Common Method to add package:" + k);
//                                    String methodNameLower = Character.toLowerCase(removeExtn.charAt(0)) + removeExtn.substring(1);
//
//                                    methodBuffer.append("    public " + removeExtn + " " + methodNameLower + "() {\n" +
//                                            "        return new " + k + ";\n" +
//                                            "    }\n\n");
//                                }
//                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Matching ElementRepositories Folder path " + path + " is not available in fh directory!! Ignore this error if you are not using the folder");
            //e.printStackTrace();
        }

    }

    private void createJavaFile(String path, String className, String Data) {
        try {
            File AUTO_JAVA_OBJECTS_PATH = cUtils().createOrReadFile(null, "", path);
            File file = cUtils().createOrReadFile(AUTO_JAVA_OBJECTS_PATH, "",
                    className + ".java");
            file.getParentFile().mkdirs();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(Data);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
package com.trigon.tribot;

import com.trigon.wrapper.TestModels;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

import static com.trigon.testbase.TestUtilities.cUtils;

public class TriBot {

    private static String JSON_MAIN_ELEMENT_NAME = "AuthorName";
    private static String JSON_MAIN_ELEMENT_NAME1 = "authorname";
    private static String JSON_PAGE_TITLE = "pageTitle";
    private static String ELEMENTS = "elements";
    //private static String AUTOGEN_PATH = "../../fh_trigon/src/test/";
    private static String AUTOGEN_PATH = "src/test/";
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

    public void createJavaFile(String elemPath, String jsonFile, String originalname, String path, String folderName, BufferedWriter bw) throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        Map<String, Object> obj = null;
        try {
            obj = (HashMap) parser.parse(new FileReader(path));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            List<String> stringList = new ArrayList<>();
            List<String> aList = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) obj;
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
            String classCreation = "public class" + " " + jsonFile + " {\n";
            String constructorCreation =
                    "    \n" + "    private TestModels tmodel;\n\n" +
                            "    //Page Constructor\n" +

                            "    public " + jsonFile + "() {\n" +
                            "        tmodel = new TestModels();\n" +
                            "        tEnv().setPagesJsonFile(new File(\"" + elemPath.replace("../../fh_trigon/", "") + "/" + originalname + ".json" + "\"));\n" +
                            "    }" +
                            "\n";

            bw.write(classCreation);
            bw.write(constructorCreation);

            if (stringList.contains(JSON_PAGE_TITLE)) {
                String pageTitle = (String) jsonObject.get(JSON_PAGE_TITLE);
                bw.write("\n");

                String pageTitleMethod = pageTitleMethodCreation(pageTitle);
                bw.write(pageTitleMethod);
                bw.write("\n");
            } else {
                System.err.println("Please add pageTitle Key to JSON File" + " : JSON FILE NAME :" + originalname);
                errorCollection.add("Please add pageTitle Key to JSON File" + " : JSON FILE NAME :" + originalname);
            }

            if (stringList.contains(ELEMENTS)) {
                Map map = (Map) jsonObject.get(ELEMENTS);
                map.forEach((key, value) -> {
                    aList.add(key.toString());
                });

                String createEwlementsMethod = createElementsMethod(aList, map, originalname);
                bw.write(createEwlementsMethod);

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

    private String createElementsMethod(List<String> aList, Map map, String jsonFilename) {
        StringBuffer stringBuffer = new StringBuffer();
        JSONObject jsonObject = (JSONObject) map;

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
        for (String methodName : aList) {
            Map map1 = (Map) jsonObject.get(methodName);
            AtomicBoolean actionflag = new AtomicBoolean(false);
            map1.forEach((key, value) -> {

                        if (key.toString().trim().contains("actionevent")) {
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
                            } else {
                                System.err.println("Error in TestType!!Add testType Like Web/Android/IOS !! check any additional Spaces!!: " + methodName + " : JSON FILE NAME :" + jsonFilename);
                                errorCollection.add("Error in TestType!!Add testType Like Web/Android/IOS !! check any additional Spaces!!: " + methodName + " : JSON FILE NAME :" + jsonFilename);
                            }

                        }

                    }
            );

            if (!actionflag.get()) {
                System.err.println("Please add actionevent for element : " + methodName + " : JSON FILE NAME :" + jsonFilename);
                errorCollection.add("Please add actionevent for element : " + methodName + " : JSON FILE NAME :" + jsonFilename);
            }

        }
        long t2 = System.currentTimeMillis();
        //System.out.println("Filter Time Taken?= " + (t2 - t1) + "\n");

        return stringBuffer.toString();
    }

    @Test
    public void autoCodeGenerator() throws IOException {
        String elementRepoPath = AUTOGEN_PATH + "resources/ElementRepositories/";
        String javaClassPath = AUTOGEN_PATH + "java/autogenerated/pages/";
        String objFactoryPath = AUTOGEN_PATH + "java/autogenerated/objrepo/";

        System.out.println("*********************************************************************");
        System.out.println(" Please make sure your project or folder name is fh_trigon");
        System.out.println("*********************************************************************");


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
            Assert.fail("Please make sure your project or folder name is fh_trigon");
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

        createAPIController();
        createLocalTestController(objectsPaths);

        objectRepo(null, null, objFactoryPath, "APIObjects", AUTOGEN_PATH + "java/com/fh/api");
        objectRepo(allObjList, objImportList, objFactoryPath,"TestObjects",AUTOGEN_PATH + "java/com/fh");
        if (errorCollection.size() > 0) {
            System.out.println("\n" + "###################################### ERROR ALERT !!!!! ##### Please FIX BELOW ISSUES in JSON files ######################################\n");
            errorCollection.forEach(item -> {
                System.out.println(item);
            });
            Assert.fail("###### ALERT !!!!! ###################################### Please fix Above errors ###############################");
        } else {
            locatorList();
            System.out.println("\n\n" + "####################################################################");
            System.out.println(" Congratulations!!! All Pages were generated successfully !!! ");
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
                "import com.trigon.wrapper.TestModels;\n" +
                "import static com.trigon.testbase.TestUtilities.tEnv;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.*;\n" +
                "\n";
    }

    public void objectRepo(List<String> objList, List<String> objImportList, String objFactoryPath, String className, String commonPath) throws IOException {

        if(!commonPath.contains(".")){
        System.out.println("Creating Java file for class : " + className);
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
                "// Contact bhaskar.marrikunta@foodhub.com for any issues.\n" +
                "//\n" +
                "/////////////////////////////////////////////////////////////////////////////////////\n\n");
        objectBuffer.append("package autogenerated." + file.getParentFile().getName().toLowerCase() + ";\n");

        identifyRequiredCommonPackages(objectBuffer, null, commonPath);
        //objectBuffer.append("import com.trigon.api.APIController;\n");
        if(objImportList!=null){
            objImportList.forEach(item -> {
                objectBuffer.append("import autogenerated.pages." + item + ".*;\n");
            });
        }

        String content =
                "import com.trigon.wrapper.TestModels;\n" +
                        "public class " + className + " {\n\n";

        objectBuffer.append(content);
        objectBuffer.append("    public TestModels tModels() { \n        return new TestModels();\n    }\n\n");
        //objectBuffer.append("    public APIController api() { \n        return new APIController();\n    }\n\n");

        if(objList!=null){
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
        }else{
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

        System.out.println("\n\n*********** Please follow below Locators as  per priority ********");
        System.out.println("==================================================================");
        System.out.println("|   Priority    |       Web     |    Android     |       IOS     |");
        System.out.println("==================================================================");
        System.out.println("|       1       |	    id	    |accessibilityid |accessibilityid|");
        System.out.println("|       2	    |       name	|       id	     |       id      |");
        System.out.println("|       3	    |    classname  |   uiautomator  |	  predicate  |");
        System.out.println("|       4	    |    linktext	|      name	     |      name     |");
        System.out.println("|       5	    |       css	    |     xpath	     |      xpath    |");
        System.out.println("|       6	    |       xpath	|    datamatcher |    classchain |");
        System.out.println("|       7	    |partiallinktext|    classname	 |     classname |");
        System.out.println("|       8	    |    tagname    |    viewtag     |      viewtag  |");
        System.out.println("|       9	    |	            |     image      |	    image    |");
        System.out.println("|       10	    |	            |     custom	 |      custom   |");
        System.out.println("|       11	    |	            |partiallinktext |partiallinktext|");
        System.out.println("|       12	    |	            |     linktext   |	    linktext |");
        System.out.println("|       13	    |	            |      tagname   |	    tagname  |");
        System.out.println("|       14  	|	            |        css     |	    css      |");
        System.out.println("==================================================================");

        System.out.println("\n*********** Please follow below Action Events ****************");
        listOfActionEvents();
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
                    if (filePath.toFile().getName().contains(".java") && ((filePath.toFile().getName().contains("common")) || (filePath.toFile().getName().contains("Common")) || (filePath.toFile().getName().contains("Util")) || (filePath.toFile().getName().contains("util")))) {
                        String removeExtn = filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.'));
                        System.out.println("--------------------------------------------------------------------");
                        try {
                            String s1 = fPath.toString() + removeExtn;
                            System.out.println(s1);
                            if (packageBuffer != null) {
                                packageBuffer.append("import " + fPath.toString() + removeExtn + ";\n");
                                System.out.println("Identified Common Package Name: " + fPath.toString() + removeExtn);
                            }

                            System.out.println("Identified Common Method to add package:" + removeExtn);
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
            });
        } catch (Exception e) {
            System.err.println("Matching ElementRepositories Folder path "+path+" is not available in fh directory!! Ignore this error if you are not using the folder");
            //e.printStackTrace();
        }

    }

    private void createAPIController() {
        String apiPath = AUTOGEN_PATH + "java/autogenerated/core/";
        String apiClassName = "LocalAPIController";

        String data = "package autogenerated.core;\n" +
                "\n" +
                "import com.trigon.api.APIController;\n" +
                "import io.restassured.response.Response;\n" +
                "\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class LocalAPIController extends APIController {\n" +
                "    /**\n" +
                "     *  Use this method when ever you are hitting endpoint in a Loop, This will help to avoid adding huge data to report\n" +
                "     * @param HttpMethod : You can pass parameter based on your request [POST,GET,PUT,DELETE,PATCH]\n" +
                "     * @param Endpoint : Pass your Endpoint, not the full URI\n" +
                "     * @param headers : Add all required headers in Map and Pass here, If no headers give it as null\n" +
                "     * @param cookies : Add all required cookies in Map and Pass here, If no cookies give it as null\n" +
                "     * @param queryParams : Add all required queryParams in Map and Pass here, If no queryParams give it as null\n" +
                "     * @param formParams : Add all required formParams in Map and Pass here, If no formParams give it as null\n" +
                "     * @param pathParams : Add all required pathParams in Map and Pass here, If no pathParams give it as null\n" +
                "     * @param requestBody : Prepare reqyest body as String\n" +
                "     *                    Usage:\n" +
                "     *                    String body = \"\"{\\n\" +\n" +
                "     *                 \"        \\\"consent_by\\\": \\\"CUSTOMER\\\",\\n\" +\n" +
                "     *                 \"        \\\"customer_id\\\": \\\"\" + custid + \"\\\",\\n\" +\n" +
                "     *                 \"        \\\"action\\\": \\\"\"+action+\"\\\",\\n\" +\n" +
                "     *                 \"        \\\"platform_id\\\": \"+platformid+\",\\n\" +\n" +
                "     *                 \"        \\\"device\\\": \\\"{\\\\\\\"os\\\\\\\":\\\\\\\"Mac\\\\\\\"}\\\"\\n\" +\n" +
                "     *                 \"    }\";\";\n" +
                "     * @param expectedStatusCode : Pass expected status code as String , Exp \"200\",\"201\",\"400\".. etc\n" +
                "     * @param expectedResponse : Add all required expectedResponse in Map and Pass here, This is Mandatory for all request verifications. However in some cases If no expectedResponse requured, in that case give it as null.\n" +
                "     *                         You Dont need to perform Assertions or if elses, framework will automatically performd the validation\n" +
                "     * @return : It returns entire response as Map\n" +
                "     */\n" +
                "    public Map<String, Object> sendRequestWithOutReport(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {\n" +
                "        return sendRequestWithOutReportImpl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Use this method to pick data directly from your Test Data\n" +
                "     * @param tData : Pass TestData Provider Map here\n" +
                "     * @param requiredNewMap : Pass required Map: For example, headers Map or expectedResponseMap\n" +
                "     * @param Key: Use the key which you need to pick from TestData\n" +
                "     */\n" +
                "    public void addDataToMapWithKey(HashMap<String, Object> tData, Map<String, Object> requiredNewMap, String Key) {\n" +
                "        addDataToMapWithKeyImpl(tData, requiredNewMap, Key);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     *\n" +
                "     * @param HttpMethod : You can pass parameter based on your request [POST,GET,PUT,DELETE,PATCH]\n" +
                "     * @param Endpoint : Pass your Endpoint, not the full URI\n" +
                "     * @param headers : Add all required headers in Map and Pass here, If no headers give it as null\n" +
                "     * @param cookies : Add all required cookies in Map and Pass here, If no cookies give it as null\n" +
                "     * @param queryParams : Add all required queryParams in Map and Pass here, If no queryParams give it as null\n" +
                "     * @param formParams : Add all required formParams in Map and Pass here, If no formParams give it as null\n" +
                "     * @param pathParams : Add all required pathParams in Map and Pass here, If no pathParams give it as null\n" +
                "     * @param requestBody : Prepare reqyest body as String\n" +
                "     *                    Usage:\n" +
                "     *                    String body = \"\"{\\n\" +\n" +
                "     *                 \"        \\\"consent_by\\\": \\\"CUSTOMER\\\",\\n\" +\n" +
                "     *                 \"        \\\"customer_id\\\": \\\"\" + custid + \"\\\",\\n\" +\n" +
                "     *                 \"        \\\"action\\\": \\\"\"+action+\"\\\",\\n\" +\n" +
                "     *                 \"        \\\"platform_id\\\": \"+platformid+\",\\n\" +\n" +
                "     *                 \"        \\\"device\\\": \\\"{\\\\\\\"os\\\\\\\":\\\\\\\"Mac\\\\\\\"}\\\"\\n\" +\n" +
                "     *                 \"    }\";\";\n" +
                "     * @param expectedStatusCode : Pass expected status code as String , Exp \"200\",\"201\",\"400\".. etc\n" +
                "     * @param expectedResponse : Add all required expectedResponse in Map and Pass here, This is Mandatory for all request verifications. However in some cases If no expectedResponse requured, in that case give it as null.\n" +
                "     *                         You Dont need to perform Assertions or if elses, framework will automatically performd the validation\n" +
                "     * @param multiPartMap : Put all your files in a map like below\n" +
                "     *   mutiFormMap.put(\"profile_image\", \"path/abc.png\");\n" +
                "     * @return\n" +
                "     */\n" +
                "    public Map<String, Object> validateMultiPartResponse(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse,Map<String, Object> multiPartMap) {\n" +
                "        return validateMultiPartResponseImpl(HttpMethod, Endpoint, headers,  cookies,  queryParams, formParams, pathParams,  requestBody, expectedStatusCode,expectedResponse, multiPartMap) ;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     *\n" +
                "     * @param HttpMethod : You can pass parameter based on your request [POST,GET,PUT,DELETE,PATCH]\n" +
                "     * @param Endpoint : Pass your Endpoint, not the full URI\n" +
                "     * @param headers : Add all required headers in Map and Pass here, If no headers give it as null\n" +
                "     * @param cookies : Add all required cookies in Map and Pass here, If no cookies give it as null\n" +
                "     * @param queryParams : Add all required queryParams in Map and Pass here, If no queryParams give it as null\n" +
                "     * @param formParams : Add all required formParams in Map and Pass here, If no formParams give it as null\n" +
                "     * @param pathParams : Add all required pathParams in Map and Pass here, If no pathParams give it as null\n" +
                "     * @param requestBody : Prepare reqyest body as String\n" +
                "     *                    Usage:\n" +
                "     *                    String body = \"\"{\\n\" +\n" +
                "     *                 \"        \\\"consent_by\\\": \\\"CUSTOMER\\\",\\n\" +\n" +
                "     *                 \"        \\\"customer_id\\\": \\\"\" + custid + \"\\\",\\n\" +\n" +
                "     *                 \"        \\\"action\\\": \\\"\"+action+\"\\\",\\n\" +\n" +
                "     *                 \"        \\\"platform_id\\\": \"+platformid+\",\\n\" +\n" +
                "     *                 \"        \\\"device\\\": \\\"{\\\\\\\"os\\\\\\\":\\\\\\\"Mac\\\\\\\"}\\\"\\n\" +\n" +
                "     *                 \"    }\";\";\n" +
                "     * @param expectedStatusCode : Pass expected status code as String , Exp \"200\",\"201\",\"400\".. etc\n" +
                "     * @param expectedResponse : Add all required expectedResponse in Map and Pass here, This is Mandatory for all request verifications. However in some cases If no expectedResponse requured, in that case give it as null.\n" +
                "     *                         You Dont need to perform Assertions or if elses, framework will automatically performd the validation\n" +
                "     * @return\n" +
                "     */\n" +
                "    public Map<String, Object> validateStaticResponse(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {\n" +
                "        return validateStaticResponseImpl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);\n" +
                "    }\n" +
                "    public Response validateStaticResponse() {\n" +
                "        return validateStaticRespImpl();\n" +
                "    }\n"+
                "\n" +
                "    /**\n" +
                "     *\n" +
                "     * @param HttpMethod : You can pass parameter based on your request [POST,GET,PUT,DELETE,PATCH]\n" +
                "     * @param Endpoint : Pass your Endpoint, not the full URI\n" +
                "     * @param headers : Add all required headers in Map and Pass here, If no headers give it as null\n" +
                "     * @param cookies : Add all required cookies in Map and Pass here, If no cookies give it as null\n" +
                "     * @param queryParams : Add all required queryParams in Map and Pass here, If no queryParams give it as null\n" +
                "     * @param formParams : Add all required formParams in Map and Pass here, If no formParams give it as null\n" +
                "     * @param pathParams : Add all required pathParams in Map and Pass here, If no pathParams give it as null\n" +
                "     * @param requestBody : Prepare reqyest body as String\n" +
                "     *                    Usage:\n" +
                "     *                    String body = \"\"{\\n\" +\n" +
                "     *                 \"        \\\"consent_by\\\": \\\"CUSTOMER\\\",\\n\" +\n" +
                "     *                 \"        \\\"customer_id\\\": \\\"\" + custid + \"\\\",\\n\" +\n" +
                "     *                 \"        \\\"action\\\": \\\"\"+action+\"\\\",\\n\" +\n" +
                "     *                 \"        \\\"platform_id\\\": \"+platformid+\",\\n\" +\n" +
                "     *                 \"        \\\"device\\\": \\\"{\\\\\\\"os\\\\\\\":\\\\\\\"Mac\\\\\\\"}\\\"\\n\" +\n" +
                "     *                 \"    }\";\";\n" +
                "     * @param expectedStatusCode : Pass expected status code as String , Exp \"200\",\"201\",\"400\".. etc\n" +
                "     * @param expectedResponse : Add all required expectedResponse in Map and Pass here, This is Mandatory for all request verifications. However in some cases If no expectedResponse requured, in that case give it as null.\n" +
                "     *                         You Dont need to perform Assertions or if elses, framework will automatically performd the validation\n" +
                "     * @return\n" +
                "     */\n" +
                "    public Map<String, Object> validateStaticResponseKeys(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {\n" +
                "        return validateStaticResponseKeysImpl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Use this method when ever there is a response body in array format and dats keep adding/changing\n" +
                "     *  Usage: if your response body is like below, use like knownKey is name and knownValue id \"TestCat1MBA\"\n" +
                "     *  {\n" +
                "     *   \"current_page\": 1,\n" +
                "     *   \"data\": [\n" +
                "     *     {\n" +
                "     *       \"id\": 1716759,\n" +
                "     *       \"host\": \"qa5.t2scdn.com\",\n" +
                "     *       \"name\": \"TestCat1MBA\",\n" +
                "     *       \"pos\": 0,\n" +
                "     *       \"pos_back\": 0,\n" +
                "     *      }]\n" +
                "     *   }\n" +
                "     *\n" +
                "     * @param HttpMethod : You can pass parameter based on your request [POST,GET,PUT,DELETE,PATCH]\n" +
                "     * @param Endpoint : Pass your Endpoint, not the full URI\n" +
                "     * @param headers : Add all required headers in Map and Pass here, If no headers give it as null\n" +
                "     * @param cookies : Add all required cookies in Map and Pass here, If no cookies give it as null\n" +
                "     * @param queryParams : Add all required queryParams in Map and Pass here, If no queryParams give it as null\n" +
                "     * @param formParams : Add all required formParams in Map and Pass here, If no formParams give it as null\n" +
                "     * @param pathParams : Add all required pathParams in Map and Pass here, If no pathParams give it as null\n" +
                "     * @param requestBody : Prepare reqyest body as String\n" +
                "     *                    Usage:\n" +
                "     *                    String body = \"\"{\\n\" +\n" +
                "     *                 \"        \\\"consent_by\\\": \\\"CUSTOMER\\\",\\n\" +\n" +
                "     *                 \"        \\\"customer_id\\\": \\\"\" + custid + \"\\\",\\n\" +\n" +
                "     *                 \"        \\\"action\\\": \\\"\"+action+\"\\\",\\n\" +\n" +
                "     *                 \"        \\\"platform_id\\\": \"+platformid+\",\\n\" +\n" +
                "     *                 \"        \\\"device\\\": \\\"{\\\\\\\"os\\\\\\\":\\\\\\\"Mac\\\\\\\"}\\\"\\n\" +\n" +
                "     *                 \"    }\";\";\n" +
                "     * @param knownKey : Identify the KnownKey from your response and pass here\n" +
                "     * @param knownValue :Identify the KnownValue from your response and pass here\n" +
                "     * @param expectedStatusCode : Pass expected status code as String , Exp \"200\",\"201\",\"400\".. etc\n" +
                "     * @param expectedResponse : Add all required expectedResponse in Map and Pass here, This is Mandatory for all request verifications. However in some cases If no expectedResponse requured, in that case give it as null.\n" +
                "     *                         You Dont need to perform Assertions or if elses, framework will automatically performd the validation\n" +
                "     * @return it returns all filtered data\n" +
                "     */\n" +
                "    public Map<String, Object> validateDynamicResponse(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String knownKey, String knownValue, String expectedStatusCode, Map<String, Object> expectedResponse) {\n" +
                "        return validateDynamicResponseImpl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, knownKey, knownValue, expectedStatusCode, expectedResponse);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     *\n" +
                "     * @param actualResponseMap : Pass Actual response Map\n" +
                "     * @param contains : Pass the key which you are looking, All matching keys along with values will add to map\n" +
                "     * @return : It returns all filered data from contains\n" +
                "     */\n" +
                "    public Map<String, Object> filterStaticDataFromActualResponse(Map<String, Object> actualResponseMap, String contains) {\n" +
                "        return filterStaticDataFromActualResponseImpl(actualResponseMap, contains);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Use this , whene ever there is dynamic response and you need to get the keys & values of matching known key and known value\n" +
                "     * @param actualResponseMap :Pass Actual response Map\n" +
                "     * @param knownKey : Pass Known Key\n" +
                "     * @param knownValue : Pass associated know value\n" +
                "     * @param requiredKeys : Add all required keys : example, \"Key1\",\"Key2\"\n" +
                "     * @return All matching keys from required keys will return\n" +
                "     */\n" +
                "    public Map<String, Object> filterDynamicDataFromActualResponse(Map<String, Object> actualResponseMap, String knownKey, String knownValue, String... requiredKeys) {\n" +
                "        return filterDynamicDataFromActualResponseImpl(actualResponseMap,knownKey, knownValue,requiredKeys);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Use this for any adhoc or sending ThirdParty requests\n" +
                "     * @param HttpMethod\n" +
                "     * @param URI\n" +
                "     * @param Endpoint\n" +
                "     * @param headers\n" +
                "     * @param cookies\n" +
                "     * @param queryParams\n" +
                "     * @param formParams\n" +
                "     * @param pathParams\n" +
                "     * @param requestBody\n" +
                "     * @param expectedStatusCode\n" +
                "     * @param expectedResponse\n" +
                "     * @return\n" +
                "     */\n" +
                "    public Map<String, Object> sendAndVerifyRequest(String HttpMethod, String URI, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {\n" +
                "        return sendRequestImpl(HttpMethod, URI, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * This method returns all matching keys mentioned in expectedKeyFromList\n" +
                "     * Example: If you need to return all list of ids from an JSONArray, Use as \"data.id\" DONOT use any indexes\n" +
                "     * @param HttpMethod\n" +
                "     * @param Endpoint\n" +
                "     * @param headers\n" +
                "     * @param cookies\n" +
                "     * @param queryParams\n" +
                "     * @param formParams\n" +
                "     * @param pathParams\n" +
                "     * @param requestBody\n" +
                "     * @param expectedStatusCode\n" +
                "     * @param expectedKeyFromList\n" +
                "     * @return\n" +
                "     */\n" +
                "    public List<Object> getStaticResponseList(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode,String expectedKeyFromList) {\n" +
                "        return getStaticResponseListImpl(HttpMethod,Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode,expectedKeyFromList);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Use this request for any adhoc tasks and sending any requests\n" +
                "     * @param HttpMethod\n" +
                "     * @param Endpoint\n" +
                "     * @param headers\n" +
                "     * @param cookies\n" +
                "     * @param queryParams\n" +
                "     * @param formParams\n" +
                "     * @param pathParams\n" +
                "     * @param requestBody\n" +
                "     * @param expectedStatusCode\n" +
                "     * @param expectedResponse\n" +
                "     * @return : It returns the entire Response, so that you can create your own parsings.\n" +
                "     */\n" +
                "    public Response sendRequest(String HttpMethod, String Endpoint, Map<String, Object> headers, Map<String, Object> cookies, Map<String, Object> queryParams, Map<String, Object> formParams, Map<String, Object> pathParams, String requestBody, String expectedStatusCode, Map<String, Object> expectedResponse) {\n" +
                "        return validateStaticRespImpl(HttpMethod, Endpoint, headers, cookies, queryParams, formParams, pathParams, requestBody, expectedStatusCode, expectedResponse);\n" +
                "    }\n" +
                "}\n";

        createJavaFile(apiPath, apiClassName, data);
    }

    private void createLocalTestController(LinkedHashMap<String, List<String>> moduleData) {

        String testPath = AUTOGEN_PATH + "java/autogenerated/core/";
        String testClassName = "TestLocalController";
        StringBuffer sb = new StringBuffer();
        sb.append("package autogenerated.core;");
        sb.append("\n" +
                "import autogenerated.objrepo.*;\n"+
                "import autogenerated.objrepo.TestObjects;\n" +
                "import com.trigon.testbase.TestController;\n" +
                "import org.testng.ITestContext;\n" +
                "import org.testng.annotations.*;\n" +
                "import org.testng.xml.XmlTest;\n" +
                "\n" +
                "import java.lang.reflect.Method;\n" +
                "\n");
        sb.append("public class TestLocalController extends TestController {\n" +
                "    public static TestObjects tObj() {\n" +
                "        return new TestObjects();\n" +
                "    }\n" +
                "    public static LocalAPIController api() {\n" +
                "        return new LocalAPIController();\n" +
                "    }\n" +
                "    public static APIObjects tApi() {\n" +
                "        return new APIObjects();\n" +
                "    }\n");

        if(moduleData!=null){
            moduleData.forEach((mod,data)->{
                String[] getPathAndFile = mod.split("##");
                String classFile = Character.toUpperCase(getPathAndFile[1].charAt(0)) + getPathAndFile[1].substring(1);
                if(!classFile.startsWith(".")){
                    sb.append("    public static "+classFile+" t"+classFile+"() {\n" +
                            "        return new "+classFile+"();\n" +
                            "    }\n");
                }

            });
        }


        sb.append("    @BeforeTest(alwaysRun = true)\n" +
                "    @Parameters({\"testEnvPath\", \"excelFilePath\", \"jsonFilePath\", \"jsonDirectory\", \"applicationType\", \"browser\", \"browser_version\", \"device\", \"os_version\", \"URI\", \"version\", \"token\", \"store\", \"host\", \"locale\", \"region\", \"country\", \"currency\", \"timezone\", \"phoneNumber\", \"emailId\"})\n" +
                "    public void moduleInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,\n" +
                "                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String browser,\n" +
                "                           @Optional String browserVersion, @Optional String device, @Optional String os_version,\n" +
                "                           @Optional String URI, @Optional String version, @Optional String token,\n" +
                "                           @Optional String store, @Optional String host, @Optional String locale,\n" +
                "                           @Optional String region, @Optional String country, @Optional String currency,\n" +
                "                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId) {\n" +
                "        moduleInitilalization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId);\n" +
                "    }\n" +
                "\n" +
                "    @BeforeClass(alwaysRun = true)\n" +
                "    @Parameters({\"testEnvPath\", \"excelFilePath\", \"jsonFilePath\", \"jsonDirectory\", \"applicationType\", \"browser\", \"browser_version\", \"device\", \"os_version\", \"URI\", \"version\", \"token\", \"store\", \"host\", \"locale\", \"region\", \"country\", \"currency\", \"timezone\", \"phoneNumber\", \"emailId\"})\n" +
                "    public void classInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,\n" +
                "                          @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String browser,\n" +
                "                          @Optional String browserVersion, @Optional String device, @Optional String os_version,\n" +
                "                          @Optional String URI, @Optional String version, @Optional String token,\n" +
                "                          @Optional String store, @Optional String host, @Optional String locale,\n" +
                "                          @Optional String region, @Optional String country, @Optional String currency,\n" +
                "                          @Optional String timezone, @Optional String phoneNumber, @Optional String emailId) {\n" +
                "        classInitialization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId);\n" +
                "    }\n" +
                "\n" +
                "    @BeforeMethod(alwaysRun = true)\n" +
                "    @Parameters({\"testEnvPath\", \"excelFilePath\", \"jsonFilePath\", \"jsonDirectory\", \"applicationType\", \"browser\", \"browser_version\", \"device\", \"os_version\", \"URI\", \"version\", \"token\", \"store\", \"host\", \"locale\", \"region\", \"country\", \"currency\", \"timezone\", \"phoneNumber\", \"emailId\"})\n" +
                "    public void methodInit(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,\n" +
                "                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String browser,\n" +
                "                           @Optional String browserVersion, @Optional String device, @Optional String os_version,\n" +
                "                           @Optional String URI, @Optional String version, @Optional String token,\n" +
                "                           @Optional String store, @Optional String host, @Optional String locale,\n" +
                "                           @Optional String region, @Optional String country, @Optional String currency,\n" +
                "                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId) {\n" +
                "        setUp(context, xmlTest, method, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId);\n" +
                "    }\n" +
                "}");


        createJavaFile(testPath, testClassName, sb.toString());
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
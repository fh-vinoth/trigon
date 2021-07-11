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

import static com.trigon.reports.ReportManager.cUtils;


public class TriBot {

    private static String JSON_MAIN_ELEMENT_NAME = "AuthorName";
    private static String JSON_MAIN_ELEMENT_NAME1 = "authorname";
    private static String JSON_PAGE_TITLE = "pageTitle";
    private static String ELEMENTS = "elements";
    private static String AUTOGEN_PATH = "../../fh_trigon/src/test/";
    //private static String AUTOGEN_PATH = "src/test/";
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

//        createAPIController();
//        createTModel();
//        createLocalTestController(objectsPaths);

        objectRepo(null, null, objFactoryPath, "APIObjects", AUTOGEN_PATH + "java/com/fh/api");
//        objectRepo(allObjList, objImportList, objFactoryPath,"TestObjects",AUTOGEN_PATH + "java/com/fh");
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
                "import com.controllers.models.TestModels;\n" +
                "import static com.trigon.reports.ReportManager.tEnv;\n" +
                "\n" +
                "import java.io.File;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.*;\n" +
                "\n";
    }

    public void objectRepo(List<String> objList, List<String> objImportList, String objFactoryPath, String className, String commonPath) throws IOException {

        if(!className.equals(".DS_Store")){
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
                "// Contact bhaskar.marrikunta@foodhub.com for any issues.\n" +
                "//\n" +
                "/////////////////////////////////////////////////////////////////////////////////////\n\n");
        objectBuffer.append("package autogenerated." + file.getParentFile().getName().toLowerCase() + ";\n");

        identifyRequiredCommonPackages(objectBuffer, null, commonPath);
        //objectBuffer.append("import com.trigon.api.APICoreController;\n");
        if(objImportList!=null){
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
                    if((filePath.toString().contains("common"))||(filePath.toString().contains("Common"))||(filePath.toString().contains("util"))||(filePath.toString().contains("Util"))){
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
            System.err.println("Matching ElementRepositories Folder path "+path+" is not available in fh directory!! Ignore this error if you are not using the folder");
            //e.printStackTrace();
        }

    }

    private void createAPIController() {
        String apiPath = AUTOGEN_PATH + "java/autogenerated/core/";
        String apiClassName = "LocalAPIController";

        String data = "package autogenerated.core;\n" +
                "\n" +
                "import com.trigon.api.APICoreController;\n" +
                "import io.restassured.response.Response;\n" +
                "\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class LocalAPIController extends APICoreController {\n" +
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
                "import com.fh.api.actions.APIObjects;\n"+
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
                "    }\n" +
                "public TestModels tModels() { return new TestModels(); }\n");

        if(moduleData!=null){
            moduleData.forEach((mod,data)->{
                String[] getPathAndFile = mod.split("##");
                String classFile = Character.toUpperCase(getPathAndFile[1].charAt(0)) + getPathAndFile[1].substring(1);
                if(!classFile.equals(".DS_Store")){
                    sb.append("    public static "+classFile+" t"+classFile+"() {\n" +
                            "        return new "+classFile+"();\n" +
                            "    }\n");
                }

            });
        }


        sb.append("    @BeforeTest(alwaysRun = true)\n" +
                "    @Parameters({\"testEnvPath\", \"excelFilePath\", \"jsonFilePath\", \"jsonDirectory\", \"applicationType\",\"url\", \"browser\", \"browser_version\", \"device\", \"os_version\", \"URI\", \"version\", \"token\", \"store\", \"host\", \"locale\", \"region\", \"country\", \"currency\", \"timezone\", \"phoneNumber\", \"emailId\",\"test_region\"})\n" +
                "    public void moduleInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,\n" +
                "                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType, @Optional String url,@Optional String browser,\n" +
                "                           @Optional String browserVersion, @Optional String device, @Optional String os_version,\n" +
                "                           @Optional String URI, @Optional String version, @Optional String token,\n" +
                "                           @Optional String store, @Optional String host, @Optional String locale,\n" +
                "                           @Optional String region, @Optional String country, @Optional String currency,\n" +
                "                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region) {\n" +
                "        moduleInitilalization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region);\n" +
                "    }\n" +
                "\n" +
                "    @BeforeClass(alwaysRun = true)\n" +
                "    @Parameters({\"testEnvPath\", \"excelFilePath\", \"jsonFilePath\", \"jsonDirectory\", \"applicationType\", \"url\",\"browser\", \"browser_version\", \"device\", \"os_version\", \"URI\", \"version\", \"token\", \"store\", \"host\", \"locale\", \"region\", \"country\", \"currency\", \"timezone\", \"phoneNumber\", \"emailId\",\"test_region\"})\n" +
                "    public void classInit(ITestContext context, XmlTest xmlTest, @Optional String testEnvPath, @Optional String excelFilePath,\n" +
                "                          @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String url, @Optional String browser,\n" +
                "                          @Optional String browserVersion, @Optional String device, @Optional String os_version,\n" +
                "                          @Optional String URI, @Optional String version, @Optional String token,\n" +
                "                          @Optional String store, @Optional String host, @Optional String locale,\n" +
                "                          @Optional String region, @Optional String country, @Optional String currency,\n" +
                "                          @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region) {\n" +
                "        classInitialization(context, xmlTest, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region);\n" +
                "    }\n" +
                "\n" +
                "    @BeforeMethod(alwaysRun = true)\n" +
                "    @Parameters({\"testEnvPath\", \"excelFilePath\", \"jsonFilePath\", \"jsonDirectory\", \"applicationType\",\"url\", \"browser\", \"browser_version\", \"device\", \"os_version\", \"URI\", \"version\", \"token\", \"store\", \"host\", \"locale\", \"region\", \"country\", \"currency\", \"timezone\", \"phoneNumber\", \"emailId\",\"test_region\"})\n" +
                "    public void methodInit(ITestContext context, XmlTest xmlTest, Method method, @Optional String testEnvPath, @Optional String excelFilePath,\n" +
                "                           @Optional String jsonFilePath, @Optional String jsonDirectory, @Optional String applicationType,@Optional String url, @Optional String browser,\n" +
                "                           @Optional String browserVersion, @Optional String device, @Optional String os_version,\n" +
                "                           @Optional String URI, @Optional String version, @Optional String token,\n" +
                "                           @Optional String store, @Optional String host, @Optional String locale,\n" +
                "                           @Optional String region, @Optional String country, @Optional String currency,\n" +
                "                           @Optional String timezone, @Optional String phoneNumber, @Optional String emailId,@Optional String test_region) {\n" +
                "        setUp(context, xmlTest, method, testEnvPath, excelFilePath, jsonFilePath, jsonDirectory, applicationType,url, browser, browserVersion, device, os_version, URI, version, token, store, host, locale, region, country, currency, timezone, phoneNumber, emailId,test_region);\n" +
                "    }\n" +
                "}");


        createJavaFile(testPath, testClassName, sb.toString());
    }

    private void createTModel(){

        String modelPath = AUTOGEN_PATH + "java/autogenerated/core/";
        String modelClassName = "TestModels";

        String data = "package autogenerated.core;\n" +
                "\n" +
                "import com.trigon.elements.PerformElementAction;\n" +
                "import com.trigon.exceptions.RetryOnException;\n" +
                "import io.appium.java_client.MobileBy;\n" +
                "import io.appium.java_client.MobileElement;\n" +
                "import io.appium.java_client.MultiTouchAction;\n" +
                "import io.appium.java_client.TouchAction;\n" +
                "import io.appium.java_client.android.Activity;\n" +
                "import io.appium.java_client.touch.WaitOptions;\n" +
                "import io.appium.java_client.touch.offset.PointOption;\n" +
                "import org.apache.logging.log4j.LogManager;\n" +
                "import org.apache.logging.log4j.Logger;\n" +
                "import org.openqa.selenium.NoSuchElementException;\n" +
                "import org.openqa.selenium.*;\n" +
                "import org.openqa.selenium.interactions.touch.TouchActions;\n" +
                "import org.testng.Assert;\n" +
                "\n" +
                "import java.time.Duration;\n" +
                "import java.util.*;\n" +
                "import java.util.concurrent.TimeUnit;\n" +
                "\n" +
                "import static io.appium.java_client.touch.WaitOptions.waitOptions;\n" +
                "import static io.appium.java_client.touch.offset.ElementOption.element;\n" +
                "import static io.appium.java_client.touch.offset.PointOption.point;\n" +
                "import static java.time.Duration.ofMillis;\n" +
                "import static java.time.Duration.ofSeconds;\n" +
                "\n" +
                "public class TestModels extends PerformElementAction {\n" +
                "\n" +
                "    private static final Logger logger = LogManager.getLogger(TestModels.class);\n" +
                "\n" +
                "\n" +
                "    public void click(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        performElementAction(locatorString, \"click\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void clearText(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        performElementAction(locatorString, \"clearText\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void enterText(String locatorString, String text, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        performElementAction(locatorString, \"enterText\", text, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getText(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementAction(locatorString, \"getText\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getAttribute(String locatorString, String attribute, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementAction(locatorString, \"getAttribute\", attribute, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean isEnabled(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementActionWithPresent(locatorString, \"isEnabled\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void verifyDisplayed(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        performElementAction(locatorString, \"verifyDisplayed\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean isSelected(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementActionWithPresent(locatorString, \"isSelected\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean isPresent(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementActionWithPresent(locatorString, \"isPresent\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean isNotPresent(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementActionWithPresent(locatorString, \"isNotPresent\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean isNotDisplayed(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementActionWithPresent(locatorString, \"isNotDisplayed\", \"\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List<String> getDropDownValues(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return dropDownHandling(locatorString, \"\", \"GetAllOptions\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List<String> getSelectedDropDownValues(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return dropDownHandling(locatorString, \"\", \"GetSelectedOptions\", wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void verifyValueFromList(String locatorString, String expected, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        performElementsAction(locatorString, \"lisofvalues\", expected, textAction, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List<String> getListOfElements(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementsAction(locatorString, \"lisofvalues\", \"NA\", null, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List<String> getValueFromListByIndex(String locatorString, String i, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return performElementsAction(locatorString, \"lisofvalues\", \"NA\", null, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List<String> dropDown(String locatorString, String value, String dropDownAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        return dropDownHandling(locatorString, value, dropDownAction, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * ########################################################################################################\n" +
                "     * START OF VERIFICATION POINTS\n" +
                "     * ########################################################################################################\n" +
                "     */\n" +
                "\n" +
                "    public void verifyAlert(String locatorString, String expectedText, String alertAction, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        alertHandling(expectedText, alertAction, textAction);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void verifyText(String locatorString, String expectedText, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        textVerificationWithScreenShot(getText(locatorString, wait_logReport_isPresent_Up_Down_XpathValues), expectedText, textAction);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void compareText(String actualText, String expectedText, String textAction) {\n" +
                "        textVerification(actualText, expectedText, textAction);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void compareText(Collection<?> actualText, Collection<?> expectedText, String textAction) {\n" +
                "        textVerification(actualText, expectedText, textAction);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void verifyAttribute(String locatorString, String attribute, String value, String textAction, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        textVerificationWithScreenShot(getAttribute(locatorString, attribute, wait_logReport_isPresent_Up_Down_XpathValues), value, textAction);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void verifyTitle(String expectedTitle, String textAction) {\n" +
                "        if (browser() != null) {\n" +
                "            String screenTitle = browser().getTitle();\n" +
                "            logger.info(\"The title is \" + screenTitle);\n" +
                "            textVerificationWithScreenShot(screenTitle, expectedTitle, textAction);\n" +
                "        }\n" +
                "    }\n" +
                "    /**\n" +
                "     * ########################################################################################################\n" +
                "     * END OF OF VERIFICATION POINTS\n" +
                "     * ########################################################################################################\n" +
                "     */\n" +
                "\n" +
                "    /**\n" +
                "     * ########################################################################################################\n" +
                "     * START OF SWITCHING/ NAVIGATING ELEMENTS\n" +
                "     * ########################################################################################################\n" +
                "     */\n" +
                "\n" +
                "    public void switchToWindow(int index) {\n" +
                "        if (browser() != null) {\n" +
                "            try {\n" +
                "                Set<String> allWindowHandles = browser().getWindowHandles();\n" +
                "                List<String> allHandles = new ArrayList<>();\n" +
                "                allHandles.addAll(allWindowHandles);\n" +
                "                browser().switchTo().window(allHandles.get(index));\n" +
                "            } catch (NoSuchWindowException e) {\n" +
                "                logReport(\"FAIL\", \"The browser() could not move to the given window by index \" + index);\n" +
                "            } catch (WebDriverException e) {\n" +
                "                logReport(\"FAIL\", \"WebDriverException : \" + e.getMessage());\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public void switchToWindow_VerifyTitle_SwitchBack(int index, String expecteTitle, String textAction) {\n" +
                "        if (browser() != null) {\n" +
                "            try {\n" +
                "                String currentWindow = browser().getWindowHandle();\n" +
                "                for (String window : browser().getWindowHandles()) {\n" +
                "                    if (!window.equals(currentWindow)) {\n" +
                "                        browser().switchTo().window(window);\n" +
                "                        browser().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);\n" +
                "                        compareText(browser().getTitle(), expecteTitle, textAction);\n" +
                "                        browser().close();\n" +
                "                    }\n" +
                "                }\n" +
                "                browser().switchTo().defaultContent();\n" +
                "\n" +
                "            } catch (NoSuchWindowException e) {\n" +
                "                logReport(\"FAIL\", \"No Windows present\");\n" +
                "            } catch (WebDriverException e) {\n" +
                "                logReport(\"FAIL\", \"WebDriverException : \" + e.getMessage());\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void switchToFrame(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "\n" +
                "        RetryOnException retryHandler = new RetryOnException();\n" +
                "        RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 500);\n" +
                "        By elementType = elementCapture(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        startTime = System.currentTimeMillis();\n" +
                "\n" +
                "        if (browser() != null) {\n" +
                "            hardWait(2000);\n" +
                "            try {\n" +
                "                while (true) {\n" +
                "                    try {\n" +
                "                        if (browser() != null) {\n" +
                "                            WebElement element = browser().findElement(elementType);\n" +
                "                            browser().switchTo().frame(element);\n" +
                "                        }\n" +
                "                        logReport(\"PASS\", \"switch In to the Frame \");\n" +
                "                        break;\n" +
                "                    } catch (WebDriverException e) {\n" +
                "                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {\n" +
                "                            if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        } else {\n" +
                "                            if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            } catch (NoSuchFrameException e) {\n" +
                "                logReport(\"FAIL\", \"WebDriverException : \" + e.getMessage());\n" +
                "            } catch (WebDriverException e) {\n" +
                "                logReport(\"FAIL\", \"WebDriverException : \" + e.getMessage());\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String getCurrentPageURL() {\n" +
                "        String appURL = null;\n" +
                "        if (browser() != null) {\n" +
                "            logger.info(\"Get the current page url\");\n" +
                "            appURL = browser().getCurrentUrl();\n" +
                "            logReportWithScreenShot(\"PASS\",\n" +
                "                    \"Captured current page url : \" + appURL);\n" +
                "        }\n" +
                "        return appURL;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String navigateToUrl(String appURL) {\n" +
                "        if (browser() != null) {\n" +
                "            logger.info(\"Set Current Page url\");\n" +
                "            browser().get(appURL);\n" +
                "            browser().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);\n" +
                "            logReportWithScreenShot(\"PASS\",\n" +
                "                    \"Set page URL to : \" + appURL);\n" +
                "        }\n" +
                "        return appURL;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void tabOut(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        if (browser() != null) {\n" +
                "            WebElement element = null; //relook\n" +
                "            logger.info(\"Press the TAB\");\n" +
                "            element.sendKeys(Keys.TAB);\n" +
                "            logReport(\"PASS\", \"Pressed on keyboard tab successfully\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void refreshBrowser() {\n" +
                "        if (browser() != null) {\n" +
                "            logger.info(\"Browser refreshed\");\n" +
                "            browser().navigate().refresh();\n" +
                "            logReport(\"PASS\", \"Browser Refreshed\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void backNavigation() {\n" +
                "\n" +
                "        try {\n" +
                "            if (android() != null) {\n" +
                "                logger.info(\"Mobile App Navigated Back\");\n" +
                "                android().navigate().back();\n" +
                "                logReport(\"PASS\", \"Mobile App Navigated Back\");\n" +
                "            }\n" +
                "            if (ios() != null) {\n" +
                "                logger.info(\"Mobile App Navigated Back\");\n" +
                "                ios().navigate().back();\n" +
                "                logReport(\"PASS\", \"Mobile App Navigated Back\");\n" +
                "            }\n" +
                "            if (browser() != null) {\n" +
                "                logger.info(\"Browser Back\");\n" +
                "                browser().navigate().back();\n" +
                "                logReport(\"PASS\", \"Browser Navigated Back\");\n" +
                "            }\n" +
                "        } catch (WebDriverException we) {\n" +
                "            hardFail(\"Failed to perform Back navigation!! Application Crashed\", \"\");\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void forwardNavigation() {\n" +
                "        if (android() != null) {\n" +
                "            logger.info(\"Mobile App Forwarded\");\n" +
                "            android().navigate().forward();\n" +
                "            logReport(\"PASS\", \"Mobile App Forwarded\");\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            logger.info(\"Mobile App Forwarded\");\n" +
                "            ios().navigate().forward();\n" +
                "            logReport(\"PASS\", \"Mobile App Forwarded\");\n" +
                "        }\n" +
                "        if (browser() != null) {\n" +
                "            logger.info(\"Browser Forwarded\");\n" +
                "            browser().navigate().forward();\n" +
                "            logReport(\"PASS\", \"Browser Forwarded\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * ########################################################################################################\n" +
                "     * END OF SWITCHING/ NAVIGATING ELEMENTS\n" +
                "     * ########################################################################################################\n" +
                "     */\n" +
                "\n" +
                "    /**\n" +
                "     * ########################################################################################################\n" +
                "     * START OF WEB SCROLLS\n" +
                "     * ########################################################################################################\n" +
                "     */\n" +
                "\n" +
                "    //To scroll down the web page by pixel.\n" +
                "    //x-pixels is the number at x-axis, it moves to the left if number is positive and it move to the right if number is negative .y-pixels is the number at y-axis, it moves to the down if number is positive and it move to the up if number is in negative\n" +
                "    public void scrollByPixel(int x, int y) {\n" +
                "        scrollbypixel(x, y);\n" +
                "    }\n" +
                "\n" +
                "    //To scroll down the web page by the visibility of the element.\n" +
                "\n" +
                "    public void scrollByVisibleElement(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "\n" +
                "        hardWait(2000);\n" +
                "        if (browser() != null) {\n" +
                "            WebElement element = null; //relook\n" +
                "            JavascriptExecutor js = (JavascriptExecutor) browser();\n" +
                "            js.executeScript(\"arguments[0].scrollIntoView();\", element);\n" +
                "        }\n" +
                "        hardWait(3000);\n" +
                "    }\n" +
                "\n" +
                "    //To scroll down the web page at the bottom of the page.\n" +
                "\n" +
                "    public void scrollToBottomPage() {\n" +
                "        hardWait(2000);\n" +
                "        JavascriptExecutor js = (JavascriptExecutor) browser();\n" +
                "        js.executeScript(\"window.scrollTo(0, document.body.scrollHeight)\");\n" +
                "        hardWait(3000);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void keyBoardActions(String pressKey) {\n" +
                "        keyBoard(pressKey);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * ########################################################################################################\n" +
                "     * END OF WEB SCROLLS\n" +
                "     * ########################################################################################################\n" +
                "     */\n" +
                "\n" +
                "\n" +
                "    public By getLocatorFromJson(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "\n" +
                "        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        //By elementType = getObjectLocatorBy(locatorArr[0], locatorArr[1]);\n" +
                "        By elementType = null;\n" +
                "        return elementType;\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    // Started for Mobile Actions\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * method to set the context to required view.\n" +
                "     * <p>\n" +
                "     * Views are NATIVE_APP , WEBVIEW_1\n" +
                "     *\n" +
                "     * @param context view to be set\n" +
                "     */\n" +
                "\n" +
                "    public void setContext(String context) {\n" +
                "        hardWait(4000);\n" +
                "        Set<String> contextNames = android().getContextHandles();\n" +
                "        logger.info(\"Context Names : \" + contextNames);\n" +
                "        if (context.contains(\"NATIVE\")) {\n" +
                "            android().context((String) contextNames.toArray()[0]);\n" +
                "        } else if (context.contains(\"WEBVIEW\")) {\n" +
                "            android().context((String) contextNames.toArray()[1]);\n" +
                "        }\n" +
                "        logger.info(\"Current context\" + android().getContext());\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * method scroll to visible text in a list and it will click on that element\n" +
                "     *\n" +
                "     * @param elementName\n" +
                "     */\n" +
                "\n" +
                "    public void androidScrollToVisibleTextInListAndClick(String elementName) {\n" +
                "        MobileElement element = android()\n" +
                "                .findElementByAndroidUIAutomator(\"new UiScrollable(new UiSelector()\"\n" +
                "                        // +\".resourceId(\\\"android:id/list\\\")).scrollIntoView(\"\n" +
                "                        + \".className(\\\"android.widget.ListView\\\")).scrollIntoView(\"\n" +
                "                        + \"new UiSelector().text(\\\"\" + elementName + \"\\\"));\");\n" +
                "        element.click();\n" +
                "        logReport(\"INFO\", \"Scrolled to element in list succesfully\");\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * Generic scroll using send keys Pass in values to be selected as a String\n" +
                "     * array to the list parameter Method will loop through looking for scroll\n" +
                "     * wheels based on the number of values you supply For instance Month, Day,\n" +
                "     * Year for a birthday would have this loop 3 times dynamically selecting\n" +
                "     * each scroll wheel\n" +
                "     *\n" +
                "     * @param list Example : {\"Apr\",\"27\",\"1999\"}\n" +
                "     */\n" +
                "\n" +
                "    public void setDateInAndroid(String[] list) {\n" +
                "        for (int i = 0; i < list.length; i++) {\n" +
                "            By meX = By.xpath(\"//android.widget.NumberPicker[\" + (i + 1)\n" +
                "                    + \"]/android.widget.EditText[1]\");\n" +
                "            MobileElement me = android().findElement(meX);\n" +
                "            TouchAction touchAction6 = new TouchAction(android());\n" +
                "            // touchAction6.longPress().release();\n" +
                "            android().performTouchAction(touchAction6);\n" +
                "            android().getKeyboard().pressKey(getAndroidMonthName(list[i]) + \"\");\n" +
                "        }\n" +
                "        logReport(\"INFO\",\n" +
                "                \"Date setted succesfully\");\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * Generic scroll using send keys Pass in values to be selected as a String\n" +
                "     * array to the list parameter Method will loop through looking for scroll\n" +
                "     * wheels based on the number of values you supply For instance Month, Day,\n" +
                "     * Year for a birthday would have this loop 3 times dynamically selecting\n" +
                "     * each scroll wheel\n" +
                "     *\n" +
                "     * @param list Example : {\"Apr\",\"27\",\"1999\"}\n" +
                "     */\n" +
                "\n" +
                "    public void setDateOrTimeInIos(String[] list) {\n" +
                "        for (int i = 0; i < list.length; i++) {\n" +
                "            MobileElement me = ios()\n" +
                "                    .findElementByXPath(\"//UIAPickerWheel[\" + (i + 1) + \"]\");\n" +
                "            me.sendKeys(list[i]);\n" +
                "        }\n" +
                "        logReport(\"INFO\",\n" +
                "                \"IOS Date setted succesfully to : \" + list);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * method to scroll to the text and clicks on the text\n" +
                "     *\n" +
                "     * @param text\n" +
                "     * @return true if it clicked successfully\n" +
                "     */\n" +
                "\n" +
                "    public boolean androidScrollToTextAndClick(String text) {\n" +
                "        try {\n" +
                "            MobileElement el = android()\n" +
                "                    .findElement(MobileBy\n" +
                "                            .AndroidUIAutomator(\"new UiScrollable(new UiSelector()).scrollIntoView(\"\n" +
                "                                    + \"new UiSelector().text(\\\"\"\n" +
                "                                    + text\n" +
                "                                    + \"\\\"));\"));\n" +
                "            el.click();\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(e);\n" +
                "            return false;\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean iOSScrollToTextAndClick(String text) {\n" +
                "        try {\n" +
                "            MobileElement el = ios()\n" +
                "                    .findElement(MobileBy.iOSNsPredicateString(text));\n" +
                "            el.click();\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(e);\n" +
                "            return false;\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public boolean iOSScrollToText(String text) {\n" +
                "        try {\n" +
                "            MobileElement el = ios()\n" +
                "                    .findElement(MobileBy.iOSNsPredicateString(text));\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(e);\n" +
                "            return false;\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * method to scroll to the text in the page\n" +
                "     *\n" +
                "     * @param text\n" +
                "     * @return true if it scroll to text successfully\n" +
                "     */\n" +
                "\n" +
                "    public boolean androidScrollToText(String text) {\n" +
                "        try {\n" +
                "            android()\n" +
                "                    .findElement(MobileBy\n" +
                "                            .AndroidUIAutomator(\"new UiScrollable(new UiSelector()).scrollIntoView(\"\n" +
                "                                    + \"new UiSelector().text(\\\"\"\n" +
                "                                    + text\n" +
                "                                    + \"\\\"));\"));\n" +
                "        } catch (Exception e) {\n" +
                "            logger.error(e);\n" +
                "            return false;\n" +
                "        }\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * method to scroll to Text and return an element\n" +
                "     *\n" +
                "     * @param text\n" +
                "     * @return element\n" +
                "     */\n" +
                "\n" +
                "    public MobileElement androidScrollToTextAndGetElement(String text) {\n" +
                "        return android()\n" +
                "                .findElement(MobileBy\n" +
                "                        .AndroidUIAutomator(\"new UiScrollable(new UiSelector()).scrollIntoView(\"\n" +
                "                                + \"new UiSelector().text(\\\"\" + text + \"\\\"));\"));\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * converts the full string of the month to Android short form name\n" +
                "     *\n" +
                "     * @param month\n" +
                "     * @return\n" +
                "     */\n" +
                "\n" +
                "    public String getAndroidMonthName(String month) {\n" +
                "        if (month != null && !month.isEmpty())\n" +
                "            return month.substring(0, 3);\n" +
                "        return month;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    //Tap to an element for 250 milliseconds\n" +
                "//    public void tapByElement(MobileElement element) {\n" +
                "//        new TouchAction(android())\n" +
                "//                .tap(tapOptions().withElement(element(element)))\n" +
                "//                .waitAction(waitOptions(ofMillis(250))).perform();\n" +
                "//    }\n" +
                "\n" +
                "    //Tap by coordinates\n" +
                "\n" +
                "    public void tapByCoordinates(int x, int y) {\n" +
                "        try {\n" +
                "            if (android() != null) {\n" +
                "                new TouchAction(android())\n" +
                "                        .tap(point(x, y))\n" +
                "                        .waitAction(waitOptions(ofMillis(250))).perform();\n" +
                "            }\n" +
                "            if (ios() != null) {\n" +
                "                new TouchAction(ios())\n" +
                "                        .tap(point(x, y))\n" +
                "                        .waitAction(waitOptions(ofMillis(250))).perform();\n" +
                "            }\n" +
                "        } catch (InvalidArgumentException iae) {\n" +
                "            logger.error(\"Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : \" + x + \" : \" + y);\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    //Press by element\n" +
                "\n" +
                "    public void pressByElement(MobileElement element, long seconds) {\n" +
                "        if (android() != null) {\n" +
                "            new TouchAction(android())\n" +
                "                    .press(element(element))\n" +
                "                    .waitAction(waitOptions(ofSeconds(seconds)))\n" +
                "                    .release()\n" +
                "                    .perform();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            new TouchAction(ios())\n" +
                "                    .press(element(element))\n" +
                "                    .waitAction(waitOptions(ofSeconds(seconds)))\n" +
                "                    .release()\n" +
                "                    .perform();\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    //Press by coordinates\n" +
                "\n" +
                "    public void pressByCoordinates(int x, int y, long seconds) {\n" +
                "        try {\n" +
                "            if (android() != null) {\n" +
                "                new TouchAction(android())\n" +
                "                        .press(point(x, y))\n" +
                "                        .waitAction(waitOptions(ofSeconds(seconds)))\n" +
                "                        .release()\n" +
                "                        .perform();\n" +
                "            }\n" +
                "            if (ios() != null) {\n" +
                "                new TouchAction(ios())\n" +
                "                        .press(point(x, y))\n" +
                "                        .waitAction(waitOptions(ofSeconds(seconds)))\n" +
                "                        .release()\n" +
                "                        .perform();\n" +
                "            }\n" +
                "        } catch (InvalidArgumentException iae) {\n" +
                "            logger.error(\"Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : \" + x + \" : \" + y);\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    //Horizontal Swipe by percentages\n" +
                "\n" +
                "    public void horizontalSwipeByPercentage(double startPercentage, double endPercentage, double anchorPercentage) {\n" +
                "        try {\n" +
                "            if (android() != null) {\n" +
                "                Dimension size = android().manage().window().getSize();\n" +
                "                int anchor = (int) (size.height * anchorPercentage);\n" +
                "                int startPoint = (int) (size.width * startPercentage);\n" +
                "                int endPoint = (int) (size.width * endPercentage);\n" +
                "\n" +
                "                new TouchAction(android())\n" +
                "                        .press(point(startPoint, anchor))\n" +
                "                        .waitAction(waitOptions(ofMillis(1000)))\n" +
                "                        .moveTo(point(endPoint, anchor))\n" +
                "                        .release().perform();\n" +
                "            }\n" +
                "            if (ios() != null) {\n" +
                "                Dimension size = ios().manage().window().getSize();\n" +
                "                int anchor = (int) (size.height * anchorPercentage);\n" +
                "                int startPoint = (int) (size.width * startPercentage);\n" +
                "                int endPoint = (int) (size.width * endPercentage);\n" +
                "\n" +
                "                new TouchAction(ios())\n" +
                "                        .press(point(startPoint, anchor))\n" +
                "                        .waitAction(waitOptions(ofMillis(1000)))\n" +
                "                        .moveTo(point(endPoint, anchor))\n" +
                "                        .release().perform();\n" +
                "            }\n" +
                "        } catch (InvalidArgumentException iae) {\n" +
                "            logger.error(\"Provide Co-Ordinates with in range. The given Co-Ordinates crossed beyond screen range : \" + startPercentage + \" : \" + endPercentage + \" : \" + anchorPercentage);\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    //Vertical Swipe by percentages\n" +
                "\n" +
                "    public void verticalSwipeByPercentages(double startPercentage, double endPercentage, double anchorPercentage) {\n" +
                "        verticalSwipe(startPercentage, endPercentage, anchorPercentage);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void swipeToMobileElement(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        System.out.println(\"Under Construction\");\n" +
                "\n" +
                "//        MobileElement element = null; //relook\n" +
                "//        int numberOfTimes = 10;\n" +
                "//        for (int i = 0; i < numberOfTimes; i++) {\n" +
                "//            verticalSwipeByPercentages(0.6, 0.3, 0.5);\n" +
                "//            if (element.isDisplayed()) {\n" +
                "//                break;\n" +
                "//            }\n" +
                "//        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    //Swipe by elements\n" +
                "\n" +
                "    public void swipeByElements(MobileElement startElement, MobileElement endElement) {\n" +
                "        int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);\n" +
                "        int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);\n" +
                "\n" +
                "        int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);\n" +
                "        int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);\n" +
                "\n" +
                "        try {\n" +
                "            if (android() != null) {\n" +
                "                new TouchAction(android())\n" +
                "                        .press(point(startX, startY))\n" +
                "                        .waitAction(waitOptions(ofMillis(1000)))\n" +
                "                        .moveTo(point(endX, endY))\n" +
                "                        .release().perform();\n" +
                "            }\n" +
                "            if (ios() != null) {\n" +
                "                new TouchAction(ios())\n" +
                "                        .press(point(startX, startY))\n" +
                "                        .waitAction(waitOptions(ofMillis(1000)))\n" +
                "                        .moveTo(point(endX, endY))\n" +
                "                        .release().perform();\n" +
                "            }\n" +
                "        } catch (InvalidArgumentException iae) {\n" +
                "            logger.error(\"The given Element Co-Ordinates crossed beyond screen range : Check you have picked correct elements in range \");\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    //Multitouch action by using an android element\n" +
                "\n" +
                "    public void multiTouchByElement(MobileElement element) {\n" +
                "        if (android() != null) {\n" +
                "            TouchAction press = new TouchAction(android())\n" +
                "                    .press(element(element))\n" +
                "                    .waitAction(waitOptions(ofSeconds(1)))\n" +
                "                    .release();\n" +
                "\n" +
                "            new MultiTouchAction(android())\n" +
                "                    .add(press)\n" +
                "                    .perform();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            TouchAction press = new TouchAction(ios())\n" +
                "                    .press(element(element))\n" +
                "                    .waitAction(waitOptions(ofSeconds(1)))\n" +
                "                    .release();\n" +
                "\n" +
                "            new MultiTouchAction(ios())\n" +
                "                    .add(press)\n" +
                "                    .perform();\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void singleTap(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        if (android() != null) {\n" +
                "            MobileElement element = android().findElement(By.xpath(locatorArr[1]));\n" +
                "            TouchActions action = new TouchActions(android());\n" +
                "            action.singleTap(element);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            MobileElement element = ios().findElementByIosNsPredicate(locatorArr[1]);\n" +
                "            TouchActions action = new TouchActions(ios());\n" +
                "            action.singleTap(element);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void doubleTap(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        if (android() != null) {\n" +
                "            MobileElement element = android().findElement(By.xpath(locatorArr[1]));\n" +
                "            TouchActions action = new TouchActions(android());\n" +
                "            action.doubleTap(element);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            MobileElement element = ios().findElementByIosNsPredicate(locatorArr[1]);\n" +
                "            TouchActions action = new TouchActions(ios());\n" +
                "            action.doubleTap(element);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void longPress(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        if (android() != null) {\n" +
                "            MobileElement element = android().findElement(By.xpath(locatorArr[1]));\n" +
                "            TouchActions action = new TouchActions(android());\n" +
                "            action.longPress(element);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            MobileElement element = ios().findElementByIosNsPredicate(locatorArr[1]);\n" +
                "            TouchActions action = new TouchActions(ios());\n" +
                "            action.longPress(element);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void scroll(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        if (android() != null) {\n" +
                "            MobileElement element = android().findElement(By.xpath(locatorArr[1]));\n" +
                "            TouchActions action = new TouchActions(android());\n" +
                "            action.scroll(element, 10, 100);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            MobileElement element = ios().findElementByIosNsPredicate(locatorArr[1]);\n" +
                "            TouchActions action = new TouchActions(ios());\n" +
                "            action.scroll(element, 10, 100);\n" +
                "            action.perform();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List<String> getElementsFromGroup(String className1, String className2, String className3, String SearchText) {\n" +
                "        MobileElement superParent = android().findElement(By.className(className1));\n" +
                "        List<MobileElement> parent = superParent.findElements(By.className(className2));\n" +
                "        List<String> returnList = new ArrayList<>();\n" +
                "        int i;\n" +
                "        int j;\n" +
                "        int k = 0;\n" +
                "        for (i = 0; i < parent.size(); i++) {\n" +
                "            List<MobileElement> child = parent.get(i).findElements(By.className(className3));\n" +
                "            for (j = 0; j < child.size(); j++) {\n" +
                "                if (child.get(j).getText().contains(SearchText)) {\n" +
                "                    for (k = 0; k < i + 2; k++) {\n" +
                "                        String y = parent.get(i).findElements(By.className(className3)).get(k).getText();\n" +
                "                        System.out.println(y);\n" +
                "                        returnList.add(y);\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return returnList;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public String mapKeyFinder(HashMap<String, Object> map, String KeyName) {\n" +
                "        Object KeyName1 = map.get(KeyName);\n" +
                "        String returnValue = \"\";\n" +
                "        if (KeyName1 != null) {\n" +
                "            returnValue = String.valueOf(KeyName1);\n" +
                "        } else {\n" +
                "            Assert.fail(\" \" + KeyName + \" Key is Not Found in Map: Check your map values or Excel header keys !!!\");\n" +
                "            logReport(\"FAIL\", \" \" + KeyName + \" Key is Not Found in Map: Check your map values or Excel header keys !!!\");\n" +
                "        }\n" +
                "        return returnValue;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void switchAndroidApp(String appPackage, String appActivity) {\n" +
                "        Activity activity = new Activity(appPackage, appActivity);\n" +
                "        activity.setAppWaitPackage(appPackage);\n" +
                "        activity.setAppWaitActivity(appActivity);\n" +
                "        android().startActivity(activity);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void switchIOSApp(String currentAppBundleID, String switchAppBundleId) {\n" +
                "        HashMap<String, Object> args = new HashMap<>();\n" +
                "        args.put(\"bundleId\", currentAppBundleID);\n" +
                "        ios().executeScript(\"mobile: launchApp\", args);\n" +
                "        hardWait(1000);\n" +
                "        args.put(\"bundleId\", switchAppBundleId);\n" +
                "        ios().executeScript(\"mobile: activateApp\", args);\n" +
                "        hardWait(1000);\n" +
                "        args.put(\"bundleId\", currentAppBundleID);\n" +
                "        ios().executeScript(\"mobile: activateApp\", args);\n" +
                "        hardWait(1000);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void openNotificationAndroidApp() {\n" +
                "        hardWait(1000);\n" +
                "        android().openNotifications();\n" +
                "        hardWait(2000);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void openNotificationIOSApp(String BundleId) {\n" +
                "        hardWait(1000);\n" +
                "        ios().terminateApp(BundleId);\n" +
                "        hardWait(2000);\n" +
                "        showNotifications();\n" +
                "        ios().findElement(By.xpath(\"//XCUIElementTypeCell[contains(@label, 'notification text')]\"));\n" +
                "        hideNotifications();\n" +
                "        ios().activateApp(BundleId);\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void closeMobileApp() {\n" +
                "        if (android() != null) {\n" +
                "            android().closeApp();\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            ios().closeApp();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void runMobileAppBackGround(long seconds) {\n" +
                "        if (android() != null) {\n" +
                "            android().runAppInBackground(Duration.ofSeconds(seconds));\n" +
                "        }\n" +
                "        if (ios() != null) {\n" +
                "            ios().runAppInBackground(Duration.ofSeconds(seconds));\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "    public List<Integer> getElementLocation(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        List<Integer> Coordinates = new ArrayList();\n" +
                "        RetryOnException retryHandler = new RetryOnException();\n" +
                "        RetryOnException retryHandler1 = new RetryOnException(elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues), 200);\n" +
                "        String[] locatorArr = getLocatorTypeAndContent(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "        By elementType = elementCapture(locatorString, wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "\n" +
                "        try {\n" +
                "            if (ios() != null) {\n" +
                "                while (true) {\n" +
                "                    try {\n" +
                "                        Point XY = ios().findElementByIosNsPredicate(locatorArr[1]).getLocation();\n" +
                "                        int X = XY.x;\n" +
                "                        int Y = XY.y;\n" +
                "                        Coordinates.add(X);\n" +
                "                        Coordinates.add(Y);\n" +
                "                        logger.info(\"The element \" + locatorString + \" Co-Ordinates are \" + X + \" : \" + Y);\n" +
                "                        break;\n" +
                "                    } catch (WebDriverException e) {\n" +
                "                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {\n" +
                "                            if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        } else {\n" +
                "                            if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            if (android() != null) {\n" +
                "                while (true) {\n" +
                "                    try {\n" +
                "                        Point XY = android().findElement(elementType).getLocation();\n" +
                "                        int X = XY.x;\n" +
                "                        int Y = XY.y;\n" +
                "                        Coordinates.add(X);\n" +
                "                        Coordinates.add(Y);\n" +
                "                        logger.info(\"The element \" + locatorString + \" Co-Ordinates are \" + X + \" : \" + Y);\n" +
                "                        break;\n" +
                "                    } catch (WebDriverException e) {\n" +
                "                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {\n" +
                "                            if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        } else {\n" +
                "                            if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "            if (browser() != null) {\n" +
                "                while (true) {\n" +
                "                    try {\n" +
                "                        Point XY = browser().findElement(elementType).getLocation();\n" +
                "                        int X = XY.x;\n" +
                "                        int Y = XY.y;\n" +
                "                        Coordinates.add(X);\n" +
                "                        Coordinates.add(Y);\n" +
                "                        logger.info(\"The element \" + locatorString + \" Co-Ordinates are \" + X + \" : \" + Y);\n" +
                "                        break;\n" +
                "                    } catch (WebDriverException e) {\n" +
                "                        if (elementWaitCheck(wait_logReport_isPresent_Up_Down_XpathValues) > 0) {\n" +
                "                            if (retryHandler1.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        } else {\n" +
                "                            if (retryHandler.exceptionOccurred(locatorString, wait_logReport_isPresent_Up_Down_XpathValues)) {\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        } catch (InvalidArgumentException iae) {\n" +
                "            logger.error(\"The given element visibility is beyond screen range : Make sure you have taken correct element : \" + locatorString);\n" +
                "        }\n" +
                "        return Coordinates;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public void hideMobileKeyBorad() {\n" +
                "        hideKeyBorad();\n" +
                "    }\n" +
                "\n" +
                "    public void swipeRightUntilLogOutScreen() {\n" +
                "        do {\n" +
                "            swipeRight();\n" +
                "        } while (!isElementPresent(By.id(\"org.wordpress.android:id/me_login_logout_text_view\")));\n" +
                "    }\n" +
                "\n" +
                "    public boolean isElementPresent(By by) {\n" +
                "        Boolean retrunValue = false;\n" +
                "        if (ios() != null) {\n" +
                "            try {\n" +
                "                ios().findElement(by);\n" +
                "                retrunValue = true;\n" +
                "            } catch (NoSuchElementException e) {\n" +
                "                retrunValue = false;\n" +
                "            }\n" +
                "        }\n" +
                "        if (android() != null) {\n" +
                "            try {\n" +
                "                android().findElement(by);\n" +
                "                retrunValue = true;\n" +
                "            } catch (NoSuchElementException e) {\n" +
                "                retrunValue = false;\n" +
                "            }\n" +
                "        }\n" +
                "        return retrunValue;\n" +
                "    }\n" +
                "\n" +
                "    public void swipeLeftUntilTextExists(String expected) {\n" +
                "        if (ios() != null) {\n" +
                "            do {\n" +
                "                swipeLeft();\n" +
                "            } while (!ios().getPageSource().contains(expected));\n" +
                "        }\n" +
                "        if (android() != null) {\n" +
                "            do {\n" +
                "                swipeLeft();\n" +
                "            } while (!android().getPageSource().contains(expected));\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    public void horizontalSwipeToElement(String locatorString, String... wait_logReport_isPresent_Up_Down_XpathValues) {\n" +
                "        horizontalSwipeToElement1(locatorString,wait_logReport_isPresent_Up_Down_XpathValues);\n" +
                "    }\n" +
                "\n" +
                "    public void swipeRight() {\n" +
                "        if (ios() != null) {\n" +
                "            Dimension size = ios().manage().window().getSize();\n" +
                "            int startx = (int) (size.width * 0.9);\n" +
                "            int endx = (int) (size.width * 0.20);\n" +
                "            int starty = size.height / 2;\n" +
                "            new TouchAction(ios()).press(PointOption.point(startx, starty))\n" +
                "                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))\n" +
                "                    .moveTo(PointOption.point(endx, starty)).release().perform();\n" +
                "        }\n" +
                "        if (android() != null) {\n" +
                "            Dimension size = ios().manage().window().getSize();\n" +
                "            int startx = (int) (size.width * 0.9);\n" +
                "            int endx = (int) (size.width * 0.20);\n" +
                "            int starty = size.height / 2;\n" +
                "            new TouchAction(ios()).press(PointOption.point(startx, starty))\n" +
                "                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))\n" +
                "                    .moveTo(PointOption.point(endx, starty)).release().perform();\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    public void swipeLeft() {\n" +
                "        if (ios() != null) {\n" +
                "            Dimension size = ios().manage().window().getSize();\n" +
                "            int startx = (int) (size.width * 0.8);\n" +
                "            int endx = (int) (size.width * 0.20);\n" +
                "            int starty = size.height / 2;\n" +
                "            new TouchAction(ios()).press(PointOption.point(startx, starty))\n" +
                "                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))\n" +
                "                    .moveTo(PointOption.point(endx, starty)).release();\n" +
                "        }\n" +
                "        if (android() != null) {\n" +
                "            Dimension size = android().manage().window().getSize();\n" +
                "            int startx = (int) (size.width * 0.8);\n" +
                "            int endx = (int) (size.width * 0.20);\n" +
                "            int starty = size.height / 2;\n" +
                "            new TouchAction(android()).press(PointOption.point(startx, starty))\n" +
                "                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))\n" +
                "                    .moveTo(PointOption.point(endx, starty)).release();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n";
        createJavaFile(modelPath, modelClassName, data);

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
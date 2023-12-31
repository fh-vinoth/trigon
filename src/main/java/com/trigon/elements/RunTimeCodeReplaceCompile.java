package com.trigon.elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trigon.reports.ReportManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static com.trigon.reports.Initializers.browser;

public class RunTimeCodeReplaceCompile extends ReportManager {

    public boolean searchFile(File folder, String fileName) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        boolean fileFoundInSubfolder = searchFile(file, fileName);
                        if (fileFoundInSubfolder) {
                            return true;
                        }
                    } else {
                        if (file.getName().equals(fileName)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void replacelocators(File file,String searchString, String replacementString) throws IOException {
        try{
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        replacelocators(f,searchString, replacementString);
                    }
                }
            } else if (file.isFile()) {
                if(file.getName().endsWith(".json")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(new File(file.getAbsolutePath()));
                    List<String> nodeNames = findNodeNames(rootNode, searchString);

                    if (nodeNames.size() > 1) {
                        Set<String> actions = new HashSet<>();
                        ObjectNode newNode = null;
                        for (String nodeName : nodeNames) {
                            actions.addAll(Arrays.stream(rootNode.get("elements").get(StringUtils.substringBetween(nodeName, "elements.", ".Web")).get("actionevent").toString().replaceAll("\"","").split(",")).collect(Collectors.toSet()));
                            newNode  = (ObjectNode) rootNode.get("elements").get(StringUtils.substringBetween(nodeName, "elements.", ".Web"));
                            ((ObjectNode) rootNode.get("elements")).remove(StringUtils.substringBetween(nodeName, "elements.", ".Web"));
                        }
                        newNode.put("actionevent", actions.stream()
                                .collect(Collectors.joining(", ")));
                        ((ObjectNode) rootNode.get("elements")).set(replacementString, newNode);
                        try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
                            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, rootNode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        for (String nodeName : nodeNames) {
                            //replace the locator name in corresponding Autogen file
                            if (file.getName() != null) {
                                String directoryPath = "src/test/java/autogenerated/pages/";
                                String newFileName = file.getName().substring(0, 1).toUpperCase() + file.getName().substring(1).replace(".json", ".java");
                                String finalOldName = StringUtils.substringBetween(nodeName, "elements.", ".Web");
                                Files.walk(Paths.get(directoryPath))
                                        .filter(path -> path.toFile().isFile())
                                        .filter(path -> path.getFileName().toString().equals(newFileName))
                                        .forEach(path -> {
                                            try {
                                                replacelocatorIn_autogeneratedAndClassFiles(new File(path.toString()), finalOldName, replacementString);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }
                        }
                    } else if (nodeNames.size() == 1) {

                        List<String> jsonLines = Files.readAllLines(file.toPath());
                        for (int j = 0; j < jsonLines.size(); j++) {
                            String jsonLine = jsonLines.get(j);
                            if (jsonLine.contains("\""+StringUtils.substringBetween(nodeNames.get(0), "elements.", ".Web")+"\"")) {
                                jsonLines.set(j, jsonLine.replace(StringUtils.substringBetween(nodeNames.get(0), "elements.", ".Web"), replacementString));
                                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                                for (String newline : jsonLines) {
                                    writer.write(newline);
                                    writer.newLine();
                                }
                                writer.close();
                            }
                        }

                        if (file.getName() != null) {
                            String directoryPath = "src/test/java/autogenerated/pages/";
                            String newFileName = file.getName().substring(0, 1).toUpperCase() + file.getName().substring(1).replace(".json", ".java");
                            String finalOldName = StringUtils.substringBetween(nodeNames.get(0), "elements.", ".Web");
                            Files.walk(Paths.get(directoryPath))
                                    .filter(path -> path.toFile().isFile())
                                    .filter(path -> path.getFileName().toString().equals(newFileName))
                                    .forEach(path -> {
                                        try {
                                            replacelocatorIn_autogeneratedAndClassFiles(new File(path.toString()), finalOldName, replacementString);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }
                    }
                }
            }
        }catch(Exception ex){
            System.out.println("Failed replacing " + searchString );
        }
    }

    public static List<String> findNodeNames(JsonNode rootNode, String searchString) {
        List<String> nodeNames = new ArrayList<>();
        findNodeNamesRecursive("", rootNode, searchString, nodeNames);
        return nodeNames;
    }


    public static void findNodeNamesRecursive(String currentPath, JsonNode node, String searchString, List<String> nodeNames) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String nodeName = entry.getKey();
                JsonNode childNode = entry.getValue();
                String nodePath = currentPath.isEmpty() ? nodeName : currentPath + "." + nodeName;
                if (childNode.isObject() || childNode.isArray()) {
                    findNodeNamesRecursive(nodePath, childNode, searchString, nodeNames);
                } else if (childNode.isTextual()) {
                    String compareText = StringUtils.substringBetween(childNode.asText().replaceAll("'", "").replaceAll("\"", "").replaceAll(" ", ""),"[","]");
                    if(compareText!=null && compareText.equals(StringUtils.substringBetween(searchString,"[","]"))){
                        nodeNames.add(nodePath);
                    }
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                JsonNode arrayNode = node.get(i);
                findNodeNamesRecursive(currentPath + "[" + i + "]", arrayNode, searchString, nodeNames);
            }
        }
    }

    public void replacelocatorIn_autogeneratedAndClassFiles(File file, String searchString, String replacementString) throws IOException {
        String repoName="";
        if (file.isFile()) {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().startsWith("tEnv().setPagesJsonFile")) {
                    repoName =StringUtils.substringBetween(line,"/",".json\"));");
                }
                String oldMethodRenamed =StringUtils.uncapitalize(searchString) +"_" + StringUtils.substringBetween(line, "_", "(");
                String newMethodRenamed = replacementString  +"_" + StringUtils.substringBetween(line, "_", "(");
                if (line.contains(oldMethodRenamed) || line.contains("\""+searchString+"\"")) {
                    String oldSearchString = searchString;
                    String oldReplacementString = replacementString;
                    if (line.trim().startsWith("public")) {
                        searchString = oldMethodRenamed;
                        replacementString = newMethodRenamed;
                        //replace the locator name in ALL corresponding Class files
                        String directoryPath = "src/test/java/com/fh/";
                        String finalRepoName = repoName;
                        Files.walk(Paths.get(directoryPath))
                                .filter(path -> path.toFile().isFile())
                                .forEach(path -> {
                                    try {
                                        replacelocatorIn_classFiles(finalRepoName,new File(path.toString()), oldMethodRenamed, newMethodRenamed);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }

                    lines.set(i, line.replace(searchString, replacementString));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    for (String newline : lines) {
                        writer.write(newline);
                        writer.newLine();
                    }
                    writer.close();
                    searchString=oldSearchString;
                    replacementString=oldReplacementString;
                }

            }
        }
    }

    public void replacelocatorIn_classFiles(String repoName,File file, String searchString, String replacementString) throws IOException, ClassNotFoundException, InterruptedException {
        String directory = System.getProperty("user.dir");
        if (file.isFile() && file.getName().endsWith(".java")) {
            List<String> classLines = Files.readAllLines(file.toPath());
            for (int j = 0; j < classLines.size(); j++) {
                String classLine = classLines.get(j);
                String repoCheck = repoName.substring(repoName.lastIndexOf("/")).replace("/", "");
                if (classLine.contains(StringUtils.uncapitalize(repoCheck) +"()."+searchString)) {
                    classLines.set(j, classLine.replace(searchString, replacementString));
                    BufferedWriter writer = new BufferedWriter( new FileWriter(file));
                    for (String newline : classLines) {
                        writer.write(newline);
                        writer.newLine();
                    }
                    writer.close();
                    RunTimeCodeReplaceCompile runTimeCodeReplaceCompile = new RunTimeCodeReplaceCompile();
                    runTimeCodeReplaceCompile.runtimeCompiler(directory+"/src/test/java/com/fh/adhoc/"+file.getName(),"build/classes/java/test/com/fh/adhoc/");
                }
            }
        }
    }

    protected Set<String> scrapeXpaths(String typeTag) {
        List<WebElement> tagElements = browser().findElements(By.tagName(typeTag));
        Set<String> xpaths = new LinkedHashSet<>();
        for (WebElement currentElement : tagElements) {
            String elementText = currentElement.getAttribute("innerHTML");
            String elementID = currentElement.getDomAttribute("id");
            String elementName = currentElement.getDomAttribute("name");
            String elementPlaceholder = currentElement.getDomAttribute("placeholder");
            String elementDataTestID1 = currentElement.getDomAttribute("data-testid");
            String elementDataTestID2 = currentElement.getDomAttribute("testID");

            if (elementID != null || elementName != null || elementPlaceholder != null
                    ||elementDataTestID1 != null ||elementDataTestID2 != null ||
                    (!elementText.equals("") && !StringUtils.containsAny(elementText, "<", ">"))) {
                String xpath = "//" + typeTag + "[ ]";
                if (elementID != null) {
                    xpath = xpath.replace(" ", "@id='" + elementID + "' ");
                    xpaths.add(xpath);
                    xpath = "//" + typeTag + "[ ]";
                }
                if (elementDataTestID1 != null) {
                    xpath = xpath.replace(" ", "@data-testid='" + elementDataTestID1 + "' ");
                    xpaths.add(xpath);
                    xpath = "//" + typeTag + "[ ]";
                }
                if (elementDataTestID2 != null) {
                    xpath = xpath.replace(" ", "@testID='" + elementDataTestID2 + "' ");
                    xpaths.add(xpath);
                    xpath = "//" + typeTag + "[ ]";
                }
                if (elementName != null) {
                    xpath = xpath.replace(" ", "@name='" + elementName + "' ");
                    xpaths.add(xpath);
                    xpath = "//" + typeTag + "[ ]";
                }
                if (elementPlaceholder != null) {
                    xpath = xpath.replace(" ", "@placeholder='" + elementPlaceholder + "' ");
                    xpaths.add(xpath);
                    xpath = "//" + typeTag + "[ ]";
                }
                if ((!elementText.equals("") && !StringUtils.containsAny(elementText, "<", ">"))) {
                    xpath = xpath.replace(" ", "text()='" + elementText + "' ");
                    xpaths.add(xpath);
                }
            }
        }
        return xpaths;
    }

    protected static Map<String, Set<String>> readConfigFileForActionTags() {
        Properties prop = new Properties();
        List<String> tagsList = new ArrayList<>();
        Map<String, Set<String>> tagActions = new HashMap<>();
        Set<String> allTags = new HashSet<>();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/Configuration/web_selfhealing.properties");
            prop.load(fis);
            //get all unique tags from prop file
            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                String tags = entry.getValue().toString();
                allTags.addAll(Arrays.stream(tags.split(",")).collect(Collectors.toList()));
            }

            //generate a mapping for a Tag->all relevant actions
            for (String tag : allTags) {
                Set<String> actionsForTag = new HashSet<>();
                for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                    if (Arrays.stream(entry.getValue().toString().split(",")).anyMatch(s1 -> s1.equals(tag))) {
                        actionsForTag.add(entry.getKey().toString());
                    }
                }
                tagActions.put(tag, actionsForTag);
            }
        } catch (Exception ex) {
        }
        return tagActions;
    }

    public void replaceLocatorNameInAutogenFiles(File jsonFilePath,String searchString, String replacementString ){
        if (jsonFilePath.getName() != null) {
            String directoryPath = "src/test/java/autogenerated/pages/";
            String fileName = jsonFilePath.getName().substring(0, 1).toUpperCase() + jsonFilePath.getName().substring(1).replace(".json", ".java");
            try {
                Files.walk(Paths.get(directoryPath))
                        .filter(path -> path.toFile().isFile())
                        .filter(path -> path.getFileName().toString().equals(fileName))
                        .forEach(path -> {
                            try {
                                replacelocatorIn_autogeneratedAndClassFiles(new File(path.toString()), searchString, replacementString);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void runtimeCompiler(String classFile, String destinationFolder)  {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int compilationResult = compiler.run(null, null, null, classFile);

            if (compilationResult == 0) {
                System.out.println("Compilation successful");
                Path sourcePath = Paths.get(classFile.replace(".java",".class"));
                Path destinationDirectory =Paths.get(destinationFolder);
                try {
                    Files.copy(sourcePath, destinationDirectory.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println("Class files could not be moved.");
                }
            } else {
                hardFail("Compilation failed");
            }
    }
    public void runtimeProjectRebuild()  {
        try {
            //String projectDirectory = System.getProperty("user.dir");
            String projectDirectory =System.getProperty("user.dir")+"/";
            ProcessBuilder processBuilder = new ProcessBuilder("gradle", "clean", "build");
            processBuilder.directory(new File(projectDirectory));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Project build successful.");
            } else {
                System.err.println("Project build failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

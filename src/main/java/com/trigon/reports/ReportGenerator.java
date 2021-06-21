package com.trigon.reports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.trigon.reports.bean.TestClassData;
import com.trigon.reports.bean.TestFailedSummary;
import com.trigon.reports.bean.TestModuleData;
import com.trigon.reports.bean.TestSuiteData;
import freemarker.core.InvalidReferenceException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {
    private static final Logger logger = LogManager.getLogger(ReportGenerator.class);

    @Test
    @Parameters({"path"})
    public void generateHTMLReports(String path) {
        createHTMLReports(path);
    }

    private void createHTMLReports(String path) {
        String Path = path;
        Path reportPath = Paths.get(Path + "/SupportFiles/TestResultJSON");
        Gson pGson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, String> lisOfModulesPath = new HashMap<>();
        List<String> lisOfClassPath = new ArrayList<>();
        List<String> passed = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        List<TestFailedSummary> failedSummary = new ArrayList<>();
        List<String> totalEndpoints = new ArrayList<>();
        List<File> sourceFiles = Arrays.asList(reportPath.toFile().listFiles());
        final String[] buildNumber = {"NA"};
        identifyFiles(lisOfModulesPath, lisOfClassPath, sourceFiles);

        HashMap<String, Map<TestModuleData, List<TestClassData>>> classMap = new HashMap<>();

        processJSONFiles(reportPath, pGson, lisOfModulesPath, lisOfClassPath, buildNumber, classMap);

        try {
            processData(Path, pGson, passed, failed, skipped, failedSummary, totalEndpoints, buildNumber, classMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedTemplateNameException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            BufferedWriter writer = null;
            String textToAppend = "                                   ,\n" +
                    "  \"testEndTime\": \"NA\",\n" +
                    "  \"timeTaken\": \"NA\"\n" +
                    "}";
            try {
                writer = new BufferedWriter(
                        new FileWriter(Path + "/SupportFiles/TestResultJSON/TestSuite.json", true));
                writer.write(textToAppend);
                writer.close();

                processData(Path, pGson, passed, failed, skipped, failedSummary, totalEndpoints, buildNumber, classMap);
                logger.info("Successfully Generated HTML Reports Refer to the path:" + path);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

    }

    private void processJSONFiles(Path reportPath, Gson pGson, Map<String, String> lisOfModulesPath, List<String> lisOfClassPath, String[] buildNumber, HashMap<String, Map<TestModuleData, List<TestClassData>>> classMap) {
        lisOfClassPath.stream().forEach(classPath -> {
            List<String> getStatusForRename = new ArrayList<>();
            try {
                JsonElement element = JsonParser.parseReader(new FileReader(reportPath + "/" + classPath));
                TestClassData tcd = pGson.fromJson(element, TestClassData.class);

                classMap.computeIfAbsent(tcd.getTestModuleName(), key -> {
                    TestModuleData tmd = new TestModuleData();
                    List<TestClassData> ltcd = new ArrayList<>();
                    Map<TestModuleData, List<TestClassData>> tmMap = new HashMap<>();
                    tmMap.put(tmd, ltcd);
                    return tmMap;
                });
                classMap.computeIfPresent(tcd.getTestModuleName(), (key, value) -> {

                    TestModuleData tmd1 = null;
                    List<TestClassData> ltcd1 = null;
                    for (Map.Entry<TestModuleData, List<TestClassData>> modEntry : value.entrySet()) {
                        tmd1 = modEntry.getKey();
                        ltcd1 = modEntry.getValue();
                    }
                    if (!Optional.ofNullable(tmd1.getTestEnvFile()).isPresent()) {
                        String pathOfModule = lisOfModulesPath.get(tcd.getTestModuleName());
                        try {
                            JsonElement element1 = JsonParser.parseReader(new FileReader(pathOfModule));
                            tmd1 = pGson.fromJson(element1, TestModuleData.class);
                            buildNumber[0] = tmd1.getTestApiVersion();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ltcd1.add(tcd);
                    Map<TestModuleData, List<TestClassData>> tmMap = new HashMap<>();
                    tmMap.put(tmd1, ltcd1);
                    tcd.getTestMethodData().forEach(status -> {
                        getStatusForRename.add(status.getTestMethodStatus());
                    });
                    return tmMap;
                });

                String modifiedClassFile = classPath.replace("PASS-", "").replace("FAIL-", "").replace("SKIP-", "");
                File f = new File(reportPath + "/" + classPath);
                if (getStatusForRename.contains("SKIPPED")) {
                    f.renameTo(new File(reportPath + "/" + "SKIP-" + modifiedClassFile));
                } else if (getStatusForRename.contains("FAILED")) {
                    f.renameTo(new File(reportPath + "/" + "FAIL-" + modifiedClassFile));
                } else {
                    f.renameTo(new File(reportPath + "/" + "PASS-" + modifiedClassFile));
                }
                getStatusForRename.clear();
            } catch (Exception e) {
                System.out.println(" Issue with Class file "+ classPath);
                e.printStackTrace();
            }
        });
    }

    private void identifyFiles(Map<String, String> lisOfModulesPath, List<String> lisOfClassPath, List<File> sourceFiles) {
        sourceFiles.stream().forEach(subFile -> {
            if (!subFile.getName().startsWith("TestSuite")) {
                String[] s = subFile.getName().split("-");
                if (s[1].equalsIgnoreCase("TestModule")) {
                    lisOfModulesPath.put(s[2].substring(0, s[2].lastIndexOf('.')), subFile.getAbsolutePath());
                }
                if (s[0].equalsIgnoreCase("TestModule")) {
                    lisOfModulesPath.put(s[1].substring(0, s[1].lastIndexOf('.')), subFile.getAbsolutePath());
                }

                if (s[0].equalsIgnoreCase("TestClass") || s[1].equalsIgnoreCase("TestClass")) {
                    lisOfClassPath.add(subFile.getName());
                }
            }
        });
    }

    private void processData(String path, Gson pGson, List<String> passed, List<String> failed, List<String> skipped, List<TestFailedSummary> failedSummary, List<String> totalEndpoints, String[] buildNumber, HashMap<String, Map<TestModuleData, List<TestClassData>>> classMap) throws IOException, TemplateException {
        JsonElement element2 = JsonParser.parseReader(new FileReader(path + "/SupportFiles/TestResultJSON/TestSuite.json"));
        TestSuiteData tsd = pGson.fromJson(element2, TestSuiteData.class);
        List<TestModuleData> collect = classMap.values().parallelStream().map(data -> {
            TestModuleData tmd2 = null;
            for (Map.Entry<TestModuleData, List<TestClassData>> modEntry : data.entrySet()) {
                tmd2 = modEntry.getKey();
                tmd2.setClassDataList(modEntry.getValue());
                modEntry.getValue().forEach(tmd1 -> {
                    tmd1.getTestMethodData().forEach(tmd3 -> {
                        TestFailedSummary tfail = new TestFailedSummary();
                        if (tmd3.getTestMethodStatus().equals("PASSED")) {
                            passed.add("PASSED");
                        } else if (tmd3.getTestMethodStatus().equals("FAILED")) {
                            failed.add("FAILED");
                            tfail.setTestStatus("FAILED");
                            tfail.setTestMethod(tmd3.getTestMethodName());
                            tfail.setTestAnalysis(tmd3.getTestMethodAnalysis());
                            tfail.setTestModule(tmd1.getTestModuleName());
                            failedSummary.add(tfail);
                        } else if (tmd3.getTestMethodStatus().equals("SKIPPED")) {
                            failed.add("SKIPPED");
                            tfail.setTestStatus("SKIPPED");
                            tfail.setTestMethod(tmd3.getTestMethodName());
                            tfail.setTestAnalysis(tmd3.getTestMethodAnalysis());
                            tfail.setTestModule(tmd1.getTestModuleName());
                            failedSummary.add(tfail);
                        }
                        tmd3.getApiTableData().forEach(apiData -> {
                            totalEndpoints.add("dependency");
                        });
                    });
                });
            }
            return tmd2;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        tsd.setModuleData(collect);

        tsd.setTotalEndPoints(totalEndpoints.size());
        tsd.setPassed(passed.size());
        tsd.setFailed(failed.size());
        tsd.setSkipped(skipped.size());
        int total = passed.size() + failed.size() + skipped.size();
        tsd.setTotalTests(total);
        if (total > 0) {
            tsd.setPassPercentage(passed.size() * 100 / total);
            tsd.setFailPercentage(failed.size() * 100 / total);
            tsd.setSkipPercentage(skipped.size() * 100 / total);
        }

        if (failed.size() > 0) {
            tsd.setCheckFailStatus(true);
        }

        if (tsd.getTestType().equalsIgnoreCase("api")) {
            tsd.setApiVersion(buildNumber[0]);
            BufferedWriter bw = null;
            try{
                Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
                Template template;
                cfg.setClassForTemplateLoading(ReportGenerator.class, "/templates");
                cfg.setDefaultEncoding("UTF-8");
                generateEmailBody(path, failed, failedSummary, tsd, "api_email.ftl", "api_failed_summary.ftl", "APIEmailReport", "API_Fail_Summary_Report");
                template = cfg.getTemplate("api.ftl");
                Writer fileWriter = new FileWriter(new File(path + "/APIDetailedReport.html"));
                bw = new BufferedWriter(fileWriter);
                template.process(tsd, bw);
                bw.flush();

            }catch (Exception e){
                bw.flush();
                //e.printStackTrace();
            }
         }
        if (tsd.getTestType().equalsIgnoreCase("web") || tsd.getTestType().equalsIgnoreCase("FHWeb") || tsd.getTestType().equalsIgnoreCase("FHNative")) {
            generateEmailBody(path, failed, failedSummary, tsd, "web_email.ftl", "web_failed_summary.ftl", "WebEmailReport", "Web_Fail_Summary_Report");
        }
        if (tsd.getTestType().equalsIgnoreCase("MOBILE") || tsd.getTestType().equalsIgnoreCase("MYT") || tsd.getTestType().equalsIgnoreCase("D2S") || tsd.getTestType().equalsIgnoreCase("FHAPP")||tsd.getTestType().equalsIgnoreCase("fusionapp")||tsd.getTestType().equalsIgnoreCase("mypos")||tsd.getTestType().equalsIgnoreCase("apos")||tsd.getTestType().equalsIgnoreCase("digitalboard")) {
            generateEmailBody(path, failed, failedSummary, tsd, "mobile_email.ftl", "mobile_failed_summary.ftl", "MobileEmailReport", "Mobile_Fail_Summary_Report");
        }

    }

    private static void generateEmailBody(String path, List<String> failed, List<TestFailedSummary> failedSummary, TestSuiteData tsd, String emailTemp1, String emailTemp2, String htmlName1, String htmlName2) throws IOException, TemplateException {
        tsd.setFailSummary(failedSummary);
        BufferedWriter bw1=null;
        BufferedWriter bw2 = null;
        try{
            Configuration cfg1 = new Configuration(Configuration.VERSION_2_3_30);
            Template template1;
            cfg1.setClassForTemplateLoading(ReportGenerator.class, "/templates");
            cfg1.setDefaultEncoding("UTF-8");
            template1 = cfg1.getTemplate(emailTemp1);
            Writer fileWriter1 = new FileWriter(new File(path + "/" + htmlName1 + ".html"));
            bw1 = new BufferedWriter(fileWriter1);
            template1.process(tsd, bw1);
            bw1.flush();
        }catch (InvalidReferenceException ir){
            bw1.flush();
        }

        LinkedHashMap<String, Object> emailData = new LinkedHashMap<>();
        emailData.put("subject", tsd.getTestSuiteName() + " | Passed : " + tsd.getPassPercentage() + "%" + " | Failed : " + tsd.getFailPercentage() + "% | " + tsd.getTimeTaken());
        String content = new String(Files.readAllBytes(Paths.get(path + "/" + htmlName1 + ".html")));
        emailData.put("body", content);
        try{
            if (failed.size() > 0) {
                Configuration cfg2 = new Configuration(Configuration.VERSION_2_3_30);
                Template template2;
                cfg2.setClassForTemplateLoading(ReportGenerator.class, "/templates");
                cfg2.setDefaultEncoding("UTF-8");
                template2 = cfg2.getTemplate(emailTemp2);
                Writer fileWriter2 = new FileWriter(new File(path + "/" + htmlName2 + ".html"));
                bw2 = new BufferedWriter(fileWriter2);
                template2.process(tsd, bw2);
                bw2.flush();
                String content2 = new String(Files.readAllBytes(Paths.get(path + "/" + htmlName2 + ".html")));
                emailData.put("failedData", content2);
            }

            Gson pGson1 = new GsonBuilder().setPrettyPrinting().create();
            String data = pGson1.toJson(emailData);
            JsonWriter jw = new JsonWriter(new BufferedWriter(new FileWriter(path + "/SupportFiles/HTML/emailBody.json")));
            jw.jsonValue(data).flush();
        }catch (Exception ir){
            bw2.flush();
            String content2 = new String(Files.readAllBytes(Paths.get(path + "/" + htmlName2 + ".html")));
            emailData.put("failedData", content2);
            Gson pGson1 = new GsonBuilder().setPrettyPrinting().create();
            String data = pGson1.toJson(emailData);
            JsonWriter jw = new JsonWriter(new BufferedWriter(new FileWriter(path + "/SupportFiles/HTML/emailBody.json")));
            jw.jsonValue(data).flush();
        }

    }
}

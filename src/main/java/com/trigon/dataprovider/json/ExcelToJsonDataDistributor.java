package com.trigon.dataprovider.json;

import com.google.gson.stream.JsonWriter;
import com.trigon.exceptions.TrigonAsserts;
import com.trigon.testbase.ConverterBase;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelToJsonDataDistributor extends ConverterBase {

    TrigonAsserts sAssert = new TrigonAsserts();

    @Test
    @Parameters({"path","distributeCount","testType","classNameWithPackage"})
    public void excelToJson(String path,String distributeCount,String testType,String classNameWithPackage) throws IOException {
        if (path == null) {
            Assert.fail("Please provide valid path Parameter");
        } else {

//            String distributeCount = "9";
//            String testType = "NA";
//            String className="NA";


            File filePath = new File(path);
            if (filePath.isFile()) {
                if (filePath.getName().contains(".xlsx")) {
                    System.out.println("--------------------------------------------------------------------");
                    System.out.println("Conversion Started for File " + filePath.getName());
                    String removeExtn = filePath.getName().substring(0, filePath.getName().lastIndexOf('.'));
                    String removeFileExtn = filePath.getPath().substring(0, filePath.getPath().lastIndexOf('.'));
                    jsonConverter(filePath, removeExtn, removeFileExtn, distributeCount,testType,classNameWithPackage);
                }
            }
        }


        sAssert.assertAll();
    }

    private void jsonConverter(File filePath, String fileName, String convertedFilePath, String distributeCount,String testType,String className) {
        ExcelToJsonDataDistributor.GetSheetBean getSheetBean = new ExcelToJsonDataDistributor.GetSheetBean();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            int totalSheets = wb.getNumberOfSheets();


            for (int s = 0; s < totalSheets; s++) {
                String suiteName = "Automation";
                String jsonFilePathForSuite = null;
                List<String> testList = new ArrayList<>();
                XSSFSheet sheet = wb.getSheet(wb.getSheetAt(s).getSheetName());
                System.out.println("Processing For Sheet             : " + wb.getSheetAt(s).getSheetName());
                suiteName = testType + "_"+wb.getSheetAt(s).getSheetName()+"_TestSuite";
                getSheetBean.setSheetName(wb.getSheetAt(s).getSheetName());
                if (sheet != null) {
                    int findIterations = sheet.getLastRowNum() / Integer.valueOf(distributeCount);
                    int reminder = sheet.getLastRowNum() % Integer.valueOf(distributeCount);
                    if (reminder != 0) {
                        findIterations++;
                    }
                    int headerRow = 0;
                    int k = 1;
                    int l = Integer.valueOf(distributeCount);

                    int nextRow = 0;
                    for (int j = 1; j <= findIterations; j++) {

                        String jsonFilePath = convertedFilePath + "_" + j;
                        testList.add(fileName+ "_" + j);

                        JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(""+jsonFilePath+".json")));
                        jsonWriter.setLenient(true);
                        jsonWriter.setIndent(" ");
                        jsonWriter.beginObject().name("modules").beginArray();
                        jsonWriter.beginObject().name("modulename").value(wb.getSheetAt(s).getSheetName());
                        jsonWriter.name("authorname").value("Automation");
                        jsonWriter.name("testdata").beginObject();
                        DataFormatter formatter = new DataFormatter();
                        int colNum;
                        if (formatter.formatCellValue(sheet.getRow(0).getCell(0)).trim().equalsIgnoreCase("TestName")) {
                            jsonWriter.name(sheet.getRow(1).getCell(0).getStringCellValue()).beginArray().flush();
                            headerRow = 0;
                            nextRow++;
                        }else{
                            Assert.fail("Start your sheet with TestName");
                        }

                        if (l > sheet.getLastRowNum()) {
                            l = sheet.getLastRowNum()+1;
                        }

                        for (int i = k; i <= l; i++) {
                            colNum = 0;
                            jsonWriter.beginObject().flush();
                            for (Cell cell : sheet.getRow(i)) {
                                if (!formatter.formatCellValue(sheet.getRow(headerRow).getCell(colNum)).trim().isEmpty()) {
                                    String key = formatter.formatCellValue(sheet.getRow(headerRow).getCell(colNum)).trim();
                                    String value = formatter.formatCellValue(sheet.getRow(i).getCell(colNum)).trim();
                                    if (!key.equalsIgnoreCase("TestName")) {
                                        if (key.equalsIgnoreCase("ExecuteTest")) {
                                            key = "executeTest";
                                        }
                                        jsonWriter.name(key).value(value).flush();
                                    }
                                    colNum++;
                                }
                            }
                            jsonWriter.endObject().flush();
                            nextRow++;
                            int finalRow = nextRow - 1;
                            if (finalRow == l) {
                                jsonWriter.endArray().flush();
                                nextRow--;
                                i = l + 1;
                            }
                        }
                        k = k + Integer.valueOf(distributeCount);
                        nextRow = k;
                        l = k + Integer.valueOf(distributeCount);
                        jsonWriter.endObject().endObject().flush();
                        jsonWriter.endArray().endObject().flush();
                        jsonWriter.close();
                        System.out.println("Successfully Created JSON File " + className +"_"+j + ".json");
                        System.out.println("--------------------------------------------------------------------");
                    }
                    suiteCreator(suiteName,findIterations,testList,convertedFilePath,className);

                }
            }
        } catch (IOException | NullPointerException | EmptyFileException | IllegalStateException io) {
            System.out.println("#### FAILED##### File " + filePath + " is not converted Due to below Errors");
            System.out.println("1. Check TestName Field in Sheet !! Ensure TestName Field in First Column");
            System.out.println("2. Check any rows added with blank/Junk data and remove them");
            sAssert.fail("File " + filePath + " with sheet " + getSheetBean.getSheetName() + "");
            io.printStackTrace();
        }
    }


    public class GetSheetBean {
        private String sheetName;

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }
    }

    private void suiteCreator(String suiteName,int findIterations,List<String> testList, String jsonFilePathForSuite,String className){

        StringBuffer b = new StringBuffer();

        b.append("<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\">\n" +
                "<suite name=\""+suiteName+"\" parallel=\"tests\" thread-count=\""+findIterations+"\">\n");

        testList.forEach(l->{
            b.append("    <test name=\"TestModule-"+l+"\">\n" +
                    "        <parameter name=\"jsonFilePath\" value=\""+jsonFilePathForSuite+"/"+l+".json\"></parameter>\n"+
                    "        <classes>\n");
            b.append("            <class name=\""+className+"\"/>\n");

            b.append("        </classes>\n" +
                    "    </test>\n");
        });

        b.append("</suite>");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(jsonFilePathForSuite+".xml"));
            bw.write(b.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

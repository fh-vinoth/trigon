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
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ExcelToJsonConverter extends ConverterBase {

    TrigonAsserts sAssert = new TrigonAsserts();

    @Test
    @Parameters({"path"})
    public void excelToJson(@Optional String path) throws IOException {
        if (path == null) {
            Assert.fail("Please provide valid path Parameter");
        } else {
            Stream<Path> paths = Files.walk(Paths.get(path));
            paths.forEach(filePath -> {
                if (filePath.toFile().isDirectory()) {
                    if (filePath.toFile().isFile()) {
                        if (filePath.toFile().getName().contains(".xlsx")) {
                            System.out.println("--------------------------------------------------------------------");
                            System.out.println("Conversion Started for File " + filePath.toFile().getName());
                            String removeExtn = filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.'));
                            jsonConverter(filePath.toFile(), removeExtn, String.valueOf(filePath.toFile()));
                        }
                    }

                } else {
                    if (filePath.toFile().getName().contains(".xlsx")) {
                        String removeExtn = filePath.toFile().getName().substring(0, filePath.toFile().getName().lastIndexOf('.'));
                        System.out.println("--------------------------------------------------------------------");
                        System.out.println("Conversion Started for File " + filePath.toFile().getName());
                        jsonConverter(filePath.toFile(), removeExtn, filePath.toFile().getParent());

                    }
                }

            });
        }
        sAssert.assertAll();
    }

    private void jsonConverter(File filePath, String fileName, String convertedFilePath) {
        ExcelToJsonConverter.GetSheetBean getSheetBean = new ExcelToJsonConverter.GetSheetBean();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(convertedFilePath + "/" + fileName + ".json")));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            int totalSheets = wb.getNumberOfSheets();
            jsonWriter.setLenient(true);
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject().name("modules").beginArray();

            for (int s = 0; s < totalSheets; s++) {
                XSSFSheet sheet = wb.getSheet(wb.getSheetAt(s).getSheetName());
                System.out.println("Processing For Sheet             : " + wb.getSheetAt(s).getSheetName());
                getSheetBean.setSheetName(wb.getSheetAt(s).getSheetName());
                if (sheet != null) {
                    int colNum;
                    int headerRow = 0;
                    int nextRow = 0;
                    jsonWriter.beginObject().name("modulename").value(wb.getSheetAt(s).getSheetName());
                    jsonWriter.name("authorname").value("Automation");
                    jsonWriter.name("testdata").beginObject();
                    DataFormatter formatter = new DataFormatter();
                    for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                        colNum = 0;
                        if (formatter.formatCellValue(sheet.getRow(i).getCell(colNum)).trim().equalsIgnoreCase("TestName")) {
                            jsonWriter.name(sheet.getRow(i + 1).getCell(colNum).getStringCellValue()).beginArray();
                            headerRow = i;
                            nextRow++;
                        } else {
                            jsonWriter.beginObject();
                            for (Cell cell : sheet.getRow(i)) {
                                if (!formatter.formatCellValue(sheet.getRow(headerRow).getCell(colNum)).trim().isEmpty()) {
                                    String key = formatter.formatCellValue(sheet.getRow(headerRow).getCell(colNum)).trim();
                                    String value = formatter.formatCellValue(sheet.getRow(i).getCell(colNum)).trim();
                                    if (!key.equalsIgnoreCase("TestName")) {
                                        if (key.equalsIgnoreCase("ExecuteTest")) {
                                            key = "executeTest";
                                        }
                                        jsonWriter.name(key).value(value);
                                    }
                                    colNum++;
                                }
                            }
                            jsonWriter.endObject();
                            nextRow++;
                            int finalRow = nextRow - 1;
                            if (finalRow == sheet.getLastRowNum()) {
                                jsonWriter.endArray();
                                nextRow--;
                            }
                            if (formatter.formatCellValue(sheet.getRow(nextRow).getCell(0)).trim().equalsIgnoreCase("TestName")) {
                                jsonWriter.endArray();
                            }
                        }
                    }
                }
                jsonWriter.endObject().endObject().flush();
                System.out.println("Successfully Completed For Sheet : " + wb.getSheetAt(s).getSheetName());
            }
            jsonWriter.endArray().endObject().flush();
            jsonWriter.close();
            System.out.println("Successfully Converted to " + fileName + ".json");
            System.out.println("--------------------------------------------------------------------");
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

}

package com.trigon.dataprovider.excel;


import com.trigon.annotations.ExcelSheet;
import com.trigon.reports.ReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.TestException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;



public class ExcelDataProvider extends ReportManager {
    private static final Logger logger = LogManager.getLogger(ExcelDataProvider.class);


    /**
     * Getting the excelSheetName value from @ExcelSheetName
     *
     * @param method
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public String getExcelSheetName(Method method) {
        String excelsheetname = null;
        try {
            Annotation[] annotation = method.getAnnotations();
            for (int i = 0; i < annotation.length; i++) {
                if (annotation[i].annotationType() == ExcelSheet.class) {
                    Class<?>[] classes = method.getParameterTypes();
                    if (!(classes.length > 0)) {
                        excelsheetname = method.getDeclaringClass().getDeclaredMethod(method.getName()).getAnnotation(ExcelSheet.class).name();
                    } else {
                        excelsheetname = method.getDeclaringClass().getDeclaredMethod(method.getName(), classes).getAnnotation(ExcelSheet.class).name();
                    }
                }
            }
        } catch (Exception e) {
            captureException(e);
        }
        return excelsheetname;
    }


    /**
     * Retrieves excel data by key
     *
     * @param method
     * @return
     * @throws Exception
     */


    public Object[][] getExcelData(Method method) {
        int iterationcount = 0;
        Object[][] data = null;
        try {
            if (!(tEnv().getExcelFilePath() == null)) {
                try {
                    //for reading initialization of Test Data sheet

                    FileInputStream fis = new FileInputStream(tEnv().getExcelFilePath());
                    XSSFWorkbook wb = new XSSFWorkbook(fis);
                    XSSFSheet sheet = wb.getSheet(getExcelSheetName(method));
                    if (sheet != null) {
                        int fullRowCount = sheet.getLastRowNum();
                        int fullColumnCount = 0;
                        String name = method.getName();
                        for (int i = 0; i <= fullRowCount; i++) {
                            String testName = sheet.getRow(i).getCell(0).getStringCellValue();
                            if (name.equals(testName)) {
                                fullColumnCount = sheet.getRow(i - 1).getLastCellNum();
                                break;
                            }
                        }
                        //System.out.println("Column Count: " + fullColumnCount);
                        // To count number of iterations
                        for (int i = 0; i <= fullRowCount; i++) {
                            String testName = sheet.getRow(i).getCell(0).getStringCellValue();
                            if (name.equals(testName)) {
                                String testCondition = sheet.getRow(i).getCell(1).getStringCellValue();
                                if (testCondition.equalsIgnoreCase("Yes")) {
                                    iterationcount = iterationcount + 1;
                                }
                            }
                        }
                        // System.out.println("No of Iterations :" + iterationcount);
                        data = new Object[iterationcount][1];
                        // for generating keys
                        ArrayList<Object> list = new ArrayList<Object>();
                        for (int i = 0; i <= fullRowCount; i++) {
                            String testName = sheet.getRow(i).getCell(0).getStringCellValue();
                            if (name.equals(testName)) {
                                String testCondition = sheet.getRow(i).getCell(1).getStringCellValue();
                                if ((testCondition.equalsIgnoreCase("Yes") || (testCondition.equalsIgnoreCase("No")))) {
                                    for (int j = 2; j < fullColumnCount; j++) {
                                        String columnName = sheet.getRow(i - 1).getCell(j).getStringCellValue().trim();
                                        list.add(columnName);
                                    }
                                    break;
                                }
                            }
                        }
                        int limiter = 0;
                        boolean nameflag = false;
                        // loading values to map from excel

                        DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
                        Map<Object, Object> map = null;
                        for (int i = 0; i <= fullRowCount; i++) {
                            String testName = sheet.getRow(i).getCell(0).getStringCellValue();
                            map = new LinkedHashMap<>();
                            if (name.equals(testName)) {
                                nameflag = true;
                                String testCondition = sheet.getRow(i).getCell(1).getStringCellValue();
                                if (testCondition.equalsIgnoreCase("Yes")) {
                                    for (int j = 2; j < fullColumnCount; j++) {
                                        try {
                                            Cell cell = sheet.getRow(i).getCell(j);
                                            String cellData = formatter.formatCellValue(cell);
                                            if (cellData.contains("##")) {
                                                String regEx = "\\d+";
                                                String sub = cellData.substring(0, 3);
                                                if (sub.matches(regEx)) {
                                                    long number = cUtils().generateRandomNumber();
                                                    cellData = sub.concat(Long.toString((number)));
                                                    //	System.out.println(cellData);
                                                } else {
                                                    int len = cellData.substring(2, cellData.length()).length();
                                                    String alpha = cUtils().generateRandomString(len);
                                                    cellData = cellData.substring(0, 2).concat(alpha);
                                                    //	System.out.println(cellData);
                                                }
                                            } else if (cellData.contains("$$@")) {
                                                int len = cellData.substring(0, cellData.lastIndexOf('$') + 1).length();

                                                cellData = cUtils().emailidGenerator(len).toLowerCase();
                                                //  System.out.println(cellData);

                                            } else if (cellData.contains("&&")) {
                                                cellData.length();
                                                String datenum = cellData.substring(cellData.length() - 1, cellData.length());
                                                String date = cUtils().futureDateGenerator(Integer.parseInt(datenum));
                                                cellData = date;

                                            }

                                            map.put(list.get(j - 2), cellData);
                                        } catch (NullPointerException e) {
                                            // TODO Auto-generated catch block
                                            logger.error("unable to read the data from rownumber " + i + " and Column Number " + j);
                                        }
                                    }
                                    //assigning values to data provider
                                    data[limiter][0] = map;
                                    limiter = limiter + 1;
                                }
                            }

                        }

                        if (map.size() == 0) {
                            // logger.error("For Test " + name + " You have chosen to Read TestData From Excel, But No TestData Found or Change ExecuteTest to Yes to your Test");
                            //logger.error("ALERT !!! Alternatively you can Remove dataProvider from your Test ::  " + name + "!!!! ");
                        }

                        if (!nameflag) {
                            throw new IllegalArgumentException("Method Name " + name + " Not Found in Spread Sheet");
                        }

                    } else {

                        logger.error("Unable to Find the Sheet " + getExcelSheetName(method) + " in SpreadSheet");
                        // throw new TestException("Unable to Find the Sheet "+getExcelSheetName(method)+" in SpreadSheet");

                    }
                    wb.close();

                } catch (FileNotFoundException e) {
                    logger.error("Unable to Find the specified file in the path");
                } catch (IOException e) {
                    logger.error("Error in reading the data");
                } catch (NullPointerException n) {
                    logger.error("You might have performed Deleted Rows in Excel, It causes Excel crash, Please create Fresh sheet and copy modified data to that sheet");
                } catch (Exception e) {
                    captureException(e);
                    logger.error("Unable to fetch data");
                }
                if (data == null) {
                    throw new IllegalArgumentException("No TestData Found or You might have performed Deleted Rows in Excel, It causes Excel crash, Please create Fresh sheet and copy modified data to that sheet");
                }
                
            } else {
                throw new TestException("Please provide Excel Name in Test Suite");
            }
        } catch (Exception e) {
            captureException(e);
        }
        return data;
    }
}



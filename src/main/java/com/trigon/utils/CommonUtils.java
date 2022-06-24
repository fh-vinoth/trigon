package com.trigon.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.collections.Lists;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtils {

    private static final Logger logger = LogManager.getLogger(CommonUtils.class);
    public String CurrentWorkingDir = System.getProperty("user.dir");

    public CommonUtils() {
    }


    /**
     * @param value
     * @return
     */
    public Map<String, Object> convertStringToHashMap(String value) {
        logger.info("Converting value to HashMap...");
        value = value.substring(1, value.length() - 1); // remove curly brackets
        String[] keyValuePairs = value.split(","); // split the string to creat
        // key-value pairs
        Map<String, Object> map = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":"); // split the pairs to get key and
            // value
            map.put(entry[0].trim(), entry[1].trim()); // add them to the
            // hashmap and trim
            // whitespaces
        }
        logger.info("Returning HashMap...");
        return map;
    }

    /**
     * @param fileName
     * @return
     */
    public String removeNumberFromFileName(String fileName) {
        if (Character.isDigit(fileName.charAt(0))) {
            logger.info("Removing Sequence Number from File Name...");
            return fileName.split("_", 2)[1];
        }
        return null;
    }

    public File createOrReadFile(File parentFile, String folderName, String fileNameWithExtension) {

        if (parentFile == null) {
            parentFile = new File(fileNameWithExtension);
            return parentFile;
        }
        if (!folderName.isEmpty())
            parentFile = new File(parentFile, FilenameUtils.getName(folderName));
        if (!fileNameWithExtension.isEmpty())
            parentFile = new File(parentFile, FilenameUtils.getName(fileNameWithExtension));

        return parentFile;

    }

    public String getCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public String getCurrentDate() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-MMM-yyyy");
        LocalDate localDate = LocalDate.now();
        String currentDate = dtf.format(localDate);
        return currentDate;

    }

    public String getCurrentTimeinMilliSeconds() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");// time generated in format hhmmss
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    public String getCurrentTimeinMilliSecondsWithColon() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");// time generated in format hhmmss
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    public String createFolder(String filepath, String foldername, String subFolder) {
        String resultsPath = "src/test/resources";
        try {
            File file = new File(filepath + File.separator + foldername);
            if (!file.exists()) {
                file.mkdir();
            }
            if (subFolder != null) {
                File subFile = new File(file.getPath() + File.separator + subFolder);
                if (!subFile.exists()) {
                    subFile.mkdir();
                }
                resultsPath = file.getPath() + File.separator + subFolder;
            } else {
                resultsPath = filepath + File.separator + foldername;
            }

        } catch (Exception e) {
            System.out.println("Failed To create Directory: - " + filepath + File.separator + foldername);
        }
        return resultsPath;
    }

    public String generateRandomStringbasedonsize(int size) {
        String generatedString = RandomStringUtils.randomAlphabetic(size);
        return generatedString;
    }

    public String getCurrentDateIndoubleDigitFormat() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now();
        String currentDate = dtf.format(localDate);
        return currentDate;
    }

    public String getCurrentDateInAlpMonth() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        LocalDate localDate = LocalDate.now();
        String currentDate = dtf.format(localDate);
        return currentDate;

    }

    public String getDateTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getRunDuration(long startTime, long endTime) {
        long duration = (endTime - startTime);  //Total execution time in milli seconds
        long secs = duration / 1000L;
        long millis = duration % 1000L;
        long mins = secs / 60L;
        secs %= 60L;
        long hours = mins / 60L;
        mins %= 60L;
        return hours + "h " + mins + "m " + secs + "s+" + millis + "ms";
    }

    public String getRunDuration(long duration) {
        long secs = duration / 1000L;
        long millis = duration % 1000L;
        long mins = secs / 60L;
        secs %= 60L;
        long hours = mins / 60L;
        mins %= 60L;
        return hours + "h " + mins + "m " + secs + "s+" + millis + "ms";
    }

    public void writefile(String filename, List<String> valuetoType) {
        String filepath = CurrentWorkingDir + File.separator + "src" + File.separator + "WorkDir" + File.separator + filename + ".txt";
        String newline = System.getProperty("line.separator");

        FileWriter fileWriter = null;

        try {

            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
                fileWriter = new FileWriter(file);
            } else {
                fileWriter = new FileWriter(file);
            }
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < valuetoType.size(); i++) {
                //bufferedWriter.write(""+valuetoType.get(i));

                if (i != valuetoType.size()) {
                    bufferedWriter.write(newline + valuetoType.get(i));
                }
            }

            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + filename + "'");

        }
    }

    public void writefile(String filename, String valuetoType) {
        createFolder(CurrentWorkingDir + File.separator + "src", "WorkDir", null);
        String filepath = CurrentWorkingDir + File.separator + "src" + File.separator + "WorkDir" + File.separator + filename + ".txt";

        FileWriter fileWriter = null;

        try {

            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
                fileWriter = new FileWriter(file);
            } else {
                fileWriter = new FileWriter(file);
            }


            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("" + valuetoType);
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + filename + "'");

        }
    }

    public void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
        }
    }

    public int getRandomNumber(int min, int max)  {
        if (min > max) {
            logger.error("Enter minimum number less than maximum number: Hence setting Maximum number");
            min = max-1;
        }

        int random = 0;
        Random r = new Random();
        max = max - min + 1;
        random = r.nextInt(max) + min;
        return random;
    }

    public List<String> readFile(String filename) throws Exception {
        List<String> Stringlist = new ArrayList<String>();
        String line = null;
        int index = 0;
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                Stringlist.add(index, line);
            }
            index++;
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");
        }
        return Stringlist;
    }

    public boolean isAlpha(String value) {
        if (StringUtils.isAlpha(value)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isAlphawithSpace(String value) {
        if (StringUtils.isAlphaSpace(value)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isAlphaNumeric(String value) {
        if (StringUtils.isAlphanumeric(value)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean containsSpecialCharacter(String value) {
        if (value.matches("^[a-zA-Z][0-9]")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNotEmpty(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return true;
        } else {
            return false;
        }

    }

    public String generatecharactersbasedonLength(int length) throws Exception {

        StringBuilder sb = new StringBuilder("A");
        String character = "";

        if (length > 0) {
            for (int appendchar = 0; appendchar < length - 1; appendchar++) {
                sb.append("a");
            }
        } else {
            throw new Exception(" Length should be greater than zero !!");
        }

        character = sb.toString();

        return character;
    }

    public void writefile(String filename, String[] valuetoType) {
        createFolder(CurrentWorkingDir + File.separator + "src", "WorkDir", null);
        String filepath = CurrentWorkingDir + File.separator + "src" + File.separator + "WorkDir" + File.separator + filename + ".txt";
        FileWriter fileWriter = null;
        try {

            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
                fileWriter = new FileWriter(file);
            } else {
                fileWriter = new FileWriter(file);
            }

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int i = 0; i < valuetoType.length; i++) {
                bufferedWriter.write("" + valuetoType[i]);

                if (i != valuetoType.length - 1) {
                    bufferedWriter.write(",");
                }
            }
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + filename + "'");
        }
    }

    public String getCurrentDate(String format) {
        String currentDate = "";
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            LocalDate localDate = LocalDate.now();
            currentDate = dtf.format(localDate);

        } catch (Exception e) {
            System.out.println("Exception Caught on the date format " + format);
        }

        return currentDate;
    }

    public static void fileOrFolderDelete(File dir) {
        //logger.info("Entering FileorFolderDelete...");
        boolean deleted;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    //logger.info("File " + aFile.getName() + " is deleted");
                    fileOrFolderDelete(aFile);
                }
            }
            deleted = dir.delete();
          //  logger.info(deleted ? "Folder Deleted Successfully" : "Folder Delete was unsuccessful");
        } else {
            deleted = dir.delete();
           // logger.info(deleted ? "File Deleted Successfully" : "File Delete was unsuccessful");

        }
    }

    public String getRandomNumberWithDoublePrecision(int min, int max) throws Exception {
        if (min > max) {
            throw new Exception("Enter minimum number less than maximum number");
        }

        double random = 0.0;
        Random r = new Random();
        max = max - min + 1;
        random = r.nextInt(max) + min;

        String doublePrecision = String.format("%.2f", random);

        return doublePrecision;
    }

    /**
     * @param pattern  = EEEE-HH-MM-a-yyyy
     * @param locale   = Locale.UK
     * @param timeZone = Europe/London
     * @return
     */

    public List<Object> getRegionCalendar(String pattern, Locale locale, String timeZone) {

        List<Object> calendar = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        Calendar cal = Calendar.getInstance(tz);
        String date = dateFormat.format(cal.getTime());
        int Todaysdate = cal.get(Calendar.DATE);
        System.out.println(Todaysdate);
        String[] curDayAndTime = date.split("-");
        String month = curDayAndTime[2];
        String year = curDayAndTime[4];
        String day = curDayAndTime[0];
        int time = Integer.parseInt(curDayAndTime[1]);
        int min = cal.get(Calendar.MINUTE);
        String amOrPM = curDayAndTime[3];

        System.out.println(day);
        System.out.println(time);
        System.out.println(min);
        System.out.println(amOrPM);

        calendar.add(time);
        calendar.add(min);
        calendar.add(amOrPM);
        calendar.add(day);

        String Todaysday;
        String nextday;

        switch (day) {
            case "Monday":
                Todaysday = "MON";
                nextday = "TUE";
                break;
            case "Tuesday":
                Todaysday = "TUE";
                nextday = "WED";
                break;
            case "Wednesday":
                Todaysday = "WED";
                nextday = "THU";
                break;
            case "Thursday":
                Todaysday = "THU";
                nextday = "FRI";
                break;
            case "Friday":
                Todaysday = "FRI";
                nextday = "SAT";
                break;
            case "Saturday":
                Todaysday = "SAT";
                nextday = "SUN";
                break;
            case "Sunday":
                Todaysday = "SUN";
                nextday = "MON";
                break;
            default:
                Todaysday = "invalid day";
                nextday = "invalid day";
                break;
        }
        calendar.add(Todaysday);
        calendar.add(nextday);
        calendar.add(year);
        calendar.add(Todaysdate);
        calendar.add(month);
        return calendar;
    }

    public String generateRandomString(int size) {

        String generatedString = RandomStringUtils.randomAlphabetic(size);

        return generatedString;
    }

    public long generateRandomNumber() {
        long number = (long) Math.floor(Math.random() * 100000000) + 10000000;
        return number;
    }

    public String emailidGenerator(int size) {
        String userName = generateRandomString(size);
        String emailId = userName.concat("@gmail.com");
        return emailId;
    }

    public String futureDateGenerator(int num) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, num);
        Date currentDatePlusOne = c.getTime();
        String convertedDate = dateFormat.format(currentDatePlusOne);
        return convertedDate;
    }

    public String mapKeyFinder(HashMap<String, Object> map, String KeyName) {

        Object KeyName1 = map.get(KeyName);
        String returnValue = "";
        if (KeyName1 != null) {
            returnValue = String.valueOf(KeyName1);
        } else {
            Assert.fail(" " + KeyName + " Key is Not Found in Map: Check your map values or Excel header keys !!!");
        }

        return returnValue;
    }

    public List<File> listAllFiles(File folder) {
        List<File> fileList = new ArrayList<>();
        if (folder.isDirectory()) {
            File[] fileNames = folder.listFiles();
            for (File file : fileNames) {
                fileList.add(file);
                if (file.isDirectory()) {
                    listAllFiles(file);
                }
            }
        } else {
            fileList.add(folder);
        }
        return fileList;
    }

    public long getFileSizeFromURL(String url){
        URL url1;
        URLConnection conn;
        long size = 0;
        try {
            url1 = new URL(url);
            conn = url1.openConnection();
            size = conn.getContentLength();
            conn.getInputStream().close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String escapeCharacters(String input) {
        List<String> specialCharacters = Lists.newArrayList("\\");
        return Arrays.stream(input.split("")).map((c) -> {
            if (specialCharacters.contains(c)) return "\\" + c;
            else return c;
        }).collect(Collectors.joining());
    }
}

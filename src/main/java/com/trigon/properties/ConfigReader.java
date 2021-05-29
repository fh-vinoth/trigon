package com.trigon.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    private String propFilePath;
    private Properties prop;
    private FileInputStream fis;

    public ConfigReader(String filePath) {
        this.propFilePath = filePath;
        loadConfig();
    }

    /**
     * This method returns the value of propFilePath and returns the filename.
     *
     * @param //takes propFilePath of type String.
     * @return returns the filename.
     */
    private String getFilename() {
        String[] tmpArray = propFilePath.split("/");
        return tmpArray[tmpArray.length - 1];
    }

    /**
     * This method returns the value associated with a particular key.
     *
     * @param //takes a key of type String.
     * @return returns the value associated with the key.
     */
    public String getProperty(String key) {

        String value = prop.getProperty(key);
        if (value != null) {
            //logger.info("Property value for key: " + key + " is " + value);
        } else {
            logger.error("The key " + key + " is not found in " + getFilename());
        }
        return value.trim();
    }

    public boolean containsKey(String value) {
        return prop.containsKey(value);
    }

    public void setProperty(String key, String value) {
        prop.setProperty(key, value);
    }

    /**
     * the sib loads the property file
     */
    public void loadConfig() {
        try {
            fis = new FileInputStream(propFilePath);
            prop = new Properties();
            prop.load(fis);
            logger.info("Loaded file: " + propFilePath + " successfully.");
        } catch (FileNotFoundException e) {
            logger.error(propFilePath + " file Not Found, check the fileName or path" + e);
            System.exit(0);
        } catch (IOException e) {
            logger.error("Error reading file " + propFilePath + e);
            System.exit(0);
        }
    }

}

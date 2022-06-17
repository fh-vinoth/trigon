package com.fh.unittests.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.trigon.mobile.AvailablePorts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class NewDBTest {
    private static final Logger logger = LogManager.getLogger(NewDBTest.class);
    private AvailablePorts ap = new AvailablePorts();
    private static Connection connection = null;
    private static Session session = null;
    private int connectionPort = 3306;
    private String connectionUrl = null;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("Before Suite");
        try {
            MysqlDataSource dataSource = new MysqlDataSource();

            String SshKeyFilepath = null;
            try {
                if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    SshKeyFilepath = "/Users/" + System.getProperty("user.name") + "/.ssh/id_rsa";
                } else {
                    SshKeyFilepath = "/home/" + System.getProperty("user.name") + "/.ssh/id_rsa";
                }
            } catch (Exception e) {
                logger.error("Add SSH File path to Execution.properties file Key DB_SSH_KEY_FILE_PATH ");
            }

            int remotePort = 3306;
            Properties config = new Properties();
            JSch jsch = new JSch();
            session = jsch.getSession("qa", "us-east-1-bastion.stage.t2sonline.com", 22);
            jsch.addIdentity(SshKeyFilepath);
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "2");
            session.setConfig(config);
            session.connect();
            int localPort = ap.getPort();
            session.setPortForwardingL(localPort, "nonprod-rds-writer.foodhub.local", remotePort);
            Class.forName("com.mysql.cj.jdbc.Driver");
            dataSource.setServerName("localhost");
            dataSource.setPortNumber(localPort);
            dataSource.setUser("ssostag");
            dataSource.setAllowMultiQueries(true);
            dataSource.setPassword("sskDwm8X");
            dataSource.setDatabaseName("sit_foxy_project56");
            connection = dataSource.getConnection();
            logger.info("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void abcTest() {
        System.out.println("Test abc in progress");

        Statement stmt = null;
        try {

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from config where host = 'automation-uk1.t2scdn.com'");
            System.out.println(rs.getRow());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    logger.info("Closing Statement");
                    System.out.println("closed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void xyzTest() {
        System.out.println("Test xyz in progress");
        Statement stmt = null;
        try {

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from config where host = 'automation-uk1.t2scdn.com'");
            System.out.println(rs.getRow());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    logger.info("Closing Statement");
                    System.out.println("closed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("After Suite");
    }
}

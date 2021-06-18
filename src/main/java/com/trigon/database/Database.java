package com.trigon.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.trigon.mobile.AvailablePorts;
import com.trigon.utils.TrigonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


public class Database extends TrigonUtils {
    private static Connection connection = null;
    private static Session session = null;
    private static final Logger logger = LogManager.getLogger(Database.class);
    private int connectionPort = 3306;
    private String connectionUrl = null;
    private AvailablePorts ap = new AvailablePorts();

    private Database() {
        try {
            String sshHost = tEnv().getDbSSHHost();
            String sshuser = tEnv().getDbSSHUser();
            String SshKeyFilepath;
            int localPort = ap.getPort();
            if (tEnv().getJenkins_execution().equalsIgnoreCase("false") && tEnv().getPipeline_execution().equalsIgnoreCase("false")) {

                try {
                    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                        SshKeyFilepath = "/Users/" + System.getProperty("user.name") + "/.ssh/id_rsa";
                    } else {
                        SshKeyFilepath = "/home/" + System.getProperty("user.name") + "/.ssh/id_rsa";
                    }
                } catch (Exception e) {
                    logger.error("Add SSH File path to Execution.properties file Key DB_SSH_KEY_FILE_PATH ");
                    captureException(e);
                    SshKeyFilepath = tEnv().getDbSSHFilePath();
                }
                if (SshKeyFilepath == null) {
                    logger.info("OS is identified as " + System.getProperty("os.name") + " Picking SSH File path from Execution.properties file Key DB_SSH_KEY_FILE_PATH ");
                    SshKeyFilepath = tEnv().getDbSSHFilePath();
                }

                String remoteHost =  tEnv().getDbHost();
                int remotePort = 3306;
                Properties config = new Properties();
                JSch jsch = new JSch();
                session = jsch.getSession(sshuser, sshHost, 22);
                jsch.addIdentity(SshKeyFilepath);
                config.put("StrictHostKeyChecking", "no");
                config.put("ConnectionAttempts", "2");
                session.setConfig(config);
                session.connect();
                int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);
                logger.info("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
                logger.info("Port Forward Successful");
            }

            if (tEnv().getJenkins_execution().equalsIgnoreCase("true") || tEnv().getPipeline_execution().equalsIgnoreCase("true")) {
                System.out.println("Connecting to DBHost : " + tEnv().getDbHost());
                connectionUrl = tEnv().getDbHost();
                connectionPort = 3306;
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            String localSSHUrl = "localhost";
            MysqlDataSource dataSource = new MysqlDataSource();
            if(tEnv().getJenkins_execution().equalsIgnoreCase("false")){
                dataSource.setServerName(localSSHUrl);
                dataSource.setPortNumber(localPort);
            }

            if(tEnv().getJenkins_execution().equalsIgnoreCase("true")||tEnv().getPipeline_execution().equalsIgnoreCase("true")){
                System.out.println("Connection URL is : "+connectionUrl);
                dataSource.setServerName(connectionUrl);
                dataSource.setPortNumber(connectionPort);
            }

            dataSource.setUser(tEnv().getDbUserName());
            dataSource.setAllowMultiQueries(true);
            dataSource.setPassword(tEnv().getDbPassword());
            dataSource.setDatabaseName(tEnv().getDbName());
            connection = dataSource.getConnection();
            System.out.print("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) new Database();
        return connection;
    }

    public static synchronized  String connecttodatabaseString(String query, String value) {
        String resultArray = "null";
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);
            logger.info("The Result Set is  " + rs.toString());
            while (rs.next()) {
                resultArray = rs.getString(value);
            }
            logger.info("The Result Array is  " + resultArray);
        } catch (Exception var6) {
            var6.printStackTrace();

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    logger.info("Closing Statement");
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    logger.info("Closing DB Connection");
                }
                if (session != null && session.isConnected()) {
                    logger.info("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {

            }
        }
        return resultArray;
    }


    private static synchronized ResultSet executeMyQuery(String query) {
        ResultSet resultSet = null;
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);
        } catch (Exception var6) {
            var6.printStackTrace();

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    logger.info("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {

            }
        }
        return resultSet;
    }

    public static synchronized ArrayList<String> connecttodatabase(String query, String value) {
        ArrayList resultArray = new ArrayList();

        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);

            while (rs.next()) {
                resultArray.add(rs.getString(value));
            }

        } catch (Exception var6) {
            var6.printStackTrace();

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    logger.info("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {

            }
        }

        return resultArray;
    }


    public static synchronized String UpdateTableInDB(String query) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            int rs = stmt.executeUpdate(query);
            logger.info("Executed Query Statement " + query);
            logger.info(rs);

        } catch (Exception var6) {
            var6.printStackTrace();

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    logger.info("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {

            }
        }

        return query;
    }

    public static synchronized String SelectTableInDB(String query) {
        String rs1 = null;
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);
            if (rs.next()) {
                rs1 = rs.getString(1);
            }

        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    logger.info("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {

            }
        }
        return rs1;
    }

    public static synchronized int GetDBvalues(String query) {
        int id = 0;
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);

            while (rs.next()) {
                id = rs.getInt("id");
                logger.info(id);
            }
        } catch (Exception var6) {
            var6.printStackTrace();

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    logger.info("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {

            }
        }
        return id;
    }


}

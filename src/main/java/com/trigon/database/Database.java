package com.trigon.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.trigon.mobile.AvailablePorts;
import com.trigon.utils.TrigonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;


public class Database extends TrigonUtils {
    private static Connection connection = null;
    private static Session session = null;
    private static final Logger logger = LogManager.getLogger(Database.class);
    private int connectionPort = 3306;
    private String connectionUrl = null;
    private AvailablePorts ap = new AvailablePorts();

    private Database() {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            if (tEnv().getJenkins_execution().equalsIgnoreCase("false") && tEnv().getPipeline_execution().equalsIgnoreCase("false")) {
                String sshHost = tEnv().getDbSSHHost();
                String sshuser = tEnv().getDbSSHUser();
                String SshKeyFilepath;
                int localPort = ap.getPort();
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

                String remoteHost = tEnv().getDbHost();
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
                Class.forName("com.mysql.cj.jdbc.Driver");
                String localSSHUrl = "localhost";
                dataSource.setServerName(localSSHUrl);
                dataSource.setPortNumber(localPort);
            } else {
                connectionUrl = tEnv().getDbHost();
                System.out.println("Connection URL is : " + connectionUrl);
                Class.forName("com.mysql.cj.jdbc.Driver");
                dataSource.setServerName(connectionUrl);
                dataSource.setPortNumber(connectionPort);
            }
            dataSource.setUser(tEnv().getDbUserName());
            dataSource.setAllowMultiQueries(true);
            dataSource.setPassword(tEnv().getDbPassword());
            dataSource.setDatabaseName(tEnv().getDbName());
            connection = dataSource.getConnection();
            logger.info("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) new Database();
        return connection;
    }

    public static synchronized String sendQuery(String query, String columnLabel) {
        String resultArray = null;
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);
            while (rs.next()) {
                resultArray = rs.getString(columnLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();

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
                e.printStackTrace();
            }
        }
        return resultArray;
    }

    public static synchronized ArrayList<String> sendQueryReturnList(String query, String value) {
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

        } catch (Exception e) {
            e.printStackTrace();

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

    public static synchronized String updateQuery(String query) {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
            logger.info("Executed Query Statement " + query);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static synchronized String sendQuery(String query,int columnIndex) {
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
                rs1 = rs.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static synchronized Map<String, Object> sendQueryReturnMap(String query) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Object> map = new HashMap<>();
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            String columnName = null;
            while (rs.next()) {
                for (int i = 1; i <= count; i++) {
                    columnName = rsMetaData.getColumnName(i);
                    map.put(columnName, rs.getString(columnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return map;
    }

    public static synchronized List<Map<String, Object>> sendQueryReturnTableAsMap(String query) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> table = new ArrayList<>();
        try {
            connection = connect();
            logger.info("Database connection success");
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            String columnName = null;
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    columnName = rsMetaData.getColumnName(i);
                    map.put(columnName, rs.getString(columnName));
                }
                table.add(map);
                System.out.println(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        return table;
    }

}

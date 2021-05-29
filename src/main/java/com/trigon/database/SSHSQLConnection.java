package com.trigon.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.trigon.utils.TrigonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import static com.trigon.testbase.TestUtilities.tEnv;

public class SSHSQLConnection extends TrigonUtils {
    private static Connection connection = null;
    private static Session session = null;
    private static final Logger logger = LogManager.getLogger(SSHSQLConnection.class);

    private void connectToServer(String dataBaseName) {
        connectSSH();
        connectToDataBase(dataBaseName);
    }

    private void connectSSH() {
        String sshHost = tEnv().getDbSSHHost();
        String sshuser = tEnv().getDbSSHUser();
        String SshKeyFilepath;

        try {
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                SshKeyFilepath = "/Users/" + System.getProperty("user.name") + "/.ssh/id_rsa";
            } else {
                SshKeyFilepath = "/home/" + System.getProperty("user.name") + "/.ssh/id_rsa";
            }
        } catch (Exception e) {
            logger.error("Add SSH File path to Execution.propertied file Key DB_SSH_KEY_FILE_PATH ");
            captureException(e);
            SshKeyFilepath = tEnv().getDbSSHFilePath();
        }
        if (SshKeyFilepath == null) {
            logger.info("OS is identified as " + System.getProperty("os.name") + " Picking SSH File path from Execution.propertied file Key DB_SSH_KEY_FILE_PATH ");
            SshKeyFilepath = tEnv().getDbSSHFilePath();
        }

        int localPort = 8740;

        String remoteHost = tEnv().getDbHost();
        int remotePort = 3306;
        String driverName = "com.mysql.cj.jdbc.Driver";

        try {
            Properties config = new Properties();
            JSch jsch = new JSch();
            session = jsch.getSession(sshuser, sshHost, 22);
            jsch.addIdentity(SshKeyFilepath);
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "2");
            session.setConfig(config);
            session.connect();
            logger.info("SSH Connected");
            Class.forName(driverName).newInstance();
            session.setPortForwardingL(localPort,remoteHost, remotePort);

        } catch (Exception e) {
            captureException(e);
            hardFail("Failed to Connect to DB !! \n Check your DB connections or Network Access or Your SSH Key format !!\n Try Rerun by connecting to VPN \n Regenerae SSH key by navigating to Terminal : ssh-keygen -t rsa -m PEM");
        }

    }

    private void connectToDataBase(String dataBaseName) {
        String dbuserName = tEnv().getDbUserName();
        String dbpassword = tEnv().getDbPassword();
        int localPort = 8740;
        String localSSHUrl = "localhost";

        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(localPort);
            dataSource.setUser(dbuserName);
            dataSource.setAllowMultiQueries(true);
            dataSource.setPassword(dbpassword);
            dataSource.setDatabaseName(dataBaseName);
            connection = dataSource.getConnection();
            System.out.print("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
            captureException(e);
            hardFail("Failed to Connect to DB !! \n Check your DB connections or Network Access or Your SSH Key format !!\n Try Rerun by connecting to VPN \n Regenerae SSH key by navigating to Terminal : ssh-keygen -t rsa -m PEM");
        }
    }

    private void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
    }

    private void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                logger.info("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

    private void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            logger.info("Closing SSH Connection");
            session.disconnect();
        }

    }

    private ResultSet executeMyQuery(String query) {
        ResultSet resultSet = null;
        try {
            connectToServer(tEnv().getDbName());
            logger.info("Database connection success");
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);

        } catch (SQLException var4) {
            var4.printStackTrace();
            hardFail("Unable to Fetch Data from DB with statement : " + query);
        } finally {
            closeConnections();
        }
        return resultSet;
    }

    public ArrayList<String> connecttodatabase(String query, String value) {
        ArrayList resultArray = new ArrayList();

        try {
            connectToServer(tEnv().getDbName());
            logger.info("Database connection success");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);

            while (rs.next()) {
                resultArray.add(rs.getString(value));
            }

        } catch (Exception var6) {
            //var6.printStackTrace();
        } finally {
            closeConnections();
        }

        return resultArray;
    }

    public String connecttodatabaseString(String query, String value) {
        String resultArray = "null";
        try {
            connectToServer(tEnv().getDbName());
            logger.info("Database connection success");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executed Query Statement " + query);

            while (rs.next()) {
                resultArray = rs.getString(value);
            }
        } catch (Exception var6) {
            logger.info(var6);
            //var6.printStackTrace();
            closeConnections();
        } finally {
            closeConnections();
        }
        return resultArray;
    }

    public String UpdateTableInDB(String query) {
        try {
            connectToServer(tEnv().getDbName());
            logger.info("Database connection success");
            Statement stmt = connection.createStatement();
            int rs = stmt.executeUpdate(query);
            logger.info("Executed Query Statement " + query);
            logger.info(rs);
            closeConnections();
        } catch (Exception var4) {

           // var4.printStackTrace();

        } finally {
            closeConnections();
        }

        return query;
    }

    public String SelectTableInDB(String query) {
        String rs1 = null;

        try {
            connectToServer(tEnv().getDbName());
            logger.info("Database connection success");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                rs1 = rs.getString(1);
            }

            closeConnections();
        } catch (Exception var5) {
            //var5.printStackTrace();
            logger.info(var5);
            closeConnections();
        } finally {
            closeConnections();
        }

        return rs1;
    }

    public int GetDBvalues(String query) {
        int id = 0;
        try {
            connectToServer(tEnv().getDbName());
            logger.info("Database connection success");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt("id");
                logger.info(id);
            }
            closeConnections();
        } catch (Exception var5) {
            logger.info(var5);
            closeConnections();
        } finally {
            closeConnections();
        }
        return id;
    }


}

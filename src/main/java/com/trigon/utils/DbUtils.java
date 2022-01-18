package com.trigon.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.trigon.database.DbConnectionPool;
import com.trigon.mobile.AvailablePorts;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.util.Properties;

public class DbUtils extends TrigonUtils {

    private static final Logger logger = LogManager.getLogger(DbConnectionPool.class);
    private static Session session = null;
    private static AvailablePorts ap = new AvailablePorts();
    private static BasicDataSource dataSource;
    private static int connectionPort = 3306;
    private static final int openConnections = 10;
    private static final int idleConnections = 3;
    private static final long connectionWaitTime = -1;

    static {
        dataSource = new BasicDataSource();
        try {
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
                    //captureException(e);
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
                System.out.println("Port Forward Successful");
                String localSSHUrl = "localhost";
                String connectionUrl = "jdbc:mysql://" + localSSHUrl + ":" + localPort + "/" + tEnv().getDbName();
                System.out.println("Connection URL is : " + connectionUrl);
                dataSource.setUrl(connectionUrl);
            } else {
                String remoteConnectionUrl = tEnv().getDbHost();
                String connectionUrl = "jdbc:mysql://" + remoteConnectionUrl + ":" + connectionPort + "/" + tEnv().getDbName();
                System.out.println("Connection URL is : " + connectionUrl);
                dataSource.setUrl(connectionUrl);
            }

            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername(tEnv().getDbUserName());
            dataSource.setPassword(tEnv().getDbPassword());
            dataSource.setMaxTotal(openConnections);
            dataSource.setMaxIdle(idleConnections);
            dataSource.setMaxWaitMillis(connectionWaitTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}


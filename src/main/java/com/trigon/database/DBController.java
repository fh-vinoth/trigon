package com.trigon.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.trigon.mobile.AvailablePorts;
import com.trigon.security.AES;
import com.trigon.utils.TrigonUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.util.Properties;

public class DBController extends TrigonUtils {

    private static final Logger logger = LogManager.getLogger(DBController.class);
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

            String connectionUrl="jdbc:mysql://" + tEnv().getDbHost() + ":" + connectionPort + "/" + tEnv().getDbName();
            dataSource.setUrl(connectionUrl);
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername(AES.decrypt(tEnv().getDbUserName(),"t2sautomation"));
            dataSource.setPassword(AES.decrypt(tEnv().getDbPassword(),"t2sautomation"));
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


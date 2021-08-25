package com.fh.unittests.database;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Queries {

    @Test
    public void queries() {
        Connection connection = null;
        Statement stmt = null;
        Session session=null;
        try{
            MysqlDataSource dataSource = new MysqlDataSource();
            JSch jsch = new JSch();
            session = jsch.getSession("qa", "us-east-1-bastion.stage.t2sonline.com");
            jsch.addIdentity("~/.ssh/id_rsa");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "10");
            session.setConfig(config);
            session.connect();
            int port = session.setPortForwardingL( "localhost",5390,"nonprod-rds-writer.foodhub.local",3306);
            Class.forName("com.mysql.cj.jdbc.Driver");


            System.out.println(port);
            dataSource.setUser("ssostag");
            dataSource.setPassword("sskDwm8X");
            dataSource.setDatabaseName("sit_foxy_project56");
            dataSource.setServerName("localhost");
            dataSource.setPort(port);
            dataSource.setPortNumber(port);
            dataSource.setSocksProxyPort(port);
            dataSource.setAllowLoadLocalInfile(true);
            dataSource.setAllowPublicKeyRetrieval(true);
            dataSource.setAllowSlaveDownConnections(true);
            dataSource.setAutoReconnect(true);
            dataSource.setCreateDatabaseIfNotExist(true);
            dataSource.setRequireSSL(false);
            dataSource.setAutoReconnectForPools(true);

            System.out.println("SSH Tunnel is OK");
            connection = dataSource.getConnection();

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from config where host = 'automation-uk1.t2scdn.com'");
            System.out.println("Executed Query Statement ");
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    System.out.println("Closing SSH Connection");
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

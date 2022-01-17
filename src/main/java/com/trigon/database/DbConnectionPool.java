package com.trigon.database;
import com.jcraft.jsch.Session;
import com.trigon.mobile.AvailablePorts;
import com.trigon.utils.DbUtils;
import com.trigon.utils.TrigonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;


public class DbConnectionPool extends TrigonUtils {
    private static Connection connection = null;
    private static Session session = null;
    private static final Logger logger = LogManager.getLogger(DbConnectionPool.class);

    public synchronized static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed())
            try {
                connection = DbUtils.getDataSource().getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return connection;
    }

    public static synchronized String sendQuery(String query, String columnLabel) {
        String resultArray = null;
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

    public static synchronized String sendQuery(String query, int columnIndex) {
        String rs1 = null;
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

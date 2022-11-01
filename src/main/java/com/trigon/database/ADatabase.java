package com.trigon.database;

import com.jcraft.jsch.Session;
import com.trigon.utils.TrigonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ADatabase extends TrigonUtils {
    private static Connection connection = null;
    private static Session session = null;
    private static final Logger logger = LogManager.getLogger(ADatabase.class);

    public static Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) try {
            connection = ADBController.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertData(Method method, XmlTest xmlTest, ITestContext context, ITestResult result, String status) {
        Statement stmt = null;
        try {
            connection = connect();
            stmt = connection.createStatement();
            String timestamp = cUtils().getCurrentTimeStamp();
            String methodId = cUtils().getRunId();

            String query1 = "INSERT INTO trigon.test_methods" +
                    "(`timestamp`, `run_id`, `method_id`, `module_name`, `class_name`, `method_name`, author, browser, platform, `android_device`, `ios_device`, status)" +
                    "VALUES('"+timestamp+"', '"+suiteRunId+"', '"+methodId+"', '"+xmlTest.getName()+"', '"+tEnv().getCurrentTestClassName()+"', '"+tEnv().getCurrentTestMethodName()+"', '', '"+tEnv().getWebBrowser()+"', '"+platformType+"', '"+tEnv().getAndroidDevice()+"', '"+tEnv().getIosDevice()+"', '"+status+"');";

            stmt.executeUpdate(query1);
        } catch (SQLException excep) {
           // excep.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                if (session != null && session.isConnected()) {
                    session.disconnect();
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public String sendQuery(String query, String columnLabel) {
        String resultArray = null;
        Statement stmt = null;
        try {
            connection = connect();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                resultArray = rs.getString(columnLabel);
            }
            logDBData(query, resultArray);
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
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultArray;
    }

    public ArrayList<String> sendQueryReturnList(String query, String value) {
        ArrayList resultArray = new ArrayList();

        Statement stmt = null;
        try {
            connection = connect();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                resultArray.add(rs.getString(value));
            }
            logDBData(query, String.valueOf(rs));
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
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultArray;
    }

    public String updateQuery(String query) {
        Statement stmt = null;
        try {
            connection = connect();
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
            logDBData(query, "Updated in DB");
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
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return query;
    }

    public String sendQuery(String query, int columnIndex) {
        String rs1 = null;
        Statement stmt = null;
        try {
            connection = connect();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                rs1 = rs.getString(columnIndex);
            }
            logDBData(query, rs1);
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
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rs1;
    }

    public Map<String, Object> sendQueryReturnMap(String query) {
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Object> map = new HashMap<>();
        try {
            connection = connect();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            String columnName = null;
            while (rs.next()) {
                for (int i = 1; i <= count; i++) {
                    columnName = rsMetaData.getColumnName(i);
                    map.put(columnName, rs.getString(columnName));
                }
            }
            logDBData(query, String.valueOf(map));
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
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public List<Map<String, Object>> sendQueryReturnTableAsMap(String query) {
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> table = new ArrayList<>();
        try {
            connection = connect();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
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
            }
            logDBData(query, String.valueOf(table));
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
                    session.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return table;
    }

}

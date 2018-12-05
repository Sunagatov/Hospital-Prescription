package com.zufar.testtask.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection connection = null;

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            final String user = "SA";
            final String password = "";
            final String url = "jdbc:hsqldb:file:db";
            final String driver = "org.hsqldb.jdbc.JDBCDriver";
            try {
                Class.forName(driver);
            } catch (Exception e) {
                throw new SQLException("Problems with getting a connection to the database object!");
            }
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
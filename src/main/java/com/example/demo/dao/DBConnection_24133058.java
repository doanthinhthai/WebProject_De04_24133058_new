package com.example.demo.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection_24133058 {
    private final String serverName = "localhost";
    private final String dbName = "WebDB_De04";
    private final String portNumber = "1433";
    private final String userID = "sa";
    private final String password = "123456";

    public Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://" + serverName + ":" + portNumber 
                + ";databaseName=" + dbName 
                + ";encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, userID, password);
    }
}
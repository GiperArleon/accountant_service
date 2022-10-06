package com.accountant.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionMaker {
    private static ConnectionMaker instance;
    private final Connection con;
    private static final Properties props = PropertiesMaker.getProps("db.properties");

    private ConnectionMaker() throws SQLException, ClassNotFoundException {
        Class.forName(props.getProperty("connection.driver"));
        this.con = DriverManager.getConnection(
                props.getProperty("connection.url"),
                props.getProperty("connection.username"),
                props.getProperty("connection.password"));
    }

    public Connection getConnection() {
        return con;
    }

    public static ConnectionMaker getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new ConnectionMaker();
        }
        return instance;
    }
}

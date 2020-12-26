package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class JDBC {
    public static Vector<Connection> connectionPool = new Vector<Connection>();

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            for (int i = 0; i < 3; i++) {

                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/business_administration", "root", "root");
                connectionPool.add(connection);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection getConnection() {
        Connection connection = connectionPool.get(0);
        connectionPool.remove(0);
        return connection;
    }

    public synchronized static void returnConnection(Connection connection) {
        connectionPool.add(connection);
    }
}

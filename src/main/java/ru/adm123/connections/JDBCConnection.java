package ru.adm123.connections;

import java.sql.*;

public class JDBCConnection {

    private static final JDBCConnection instance = new JDBCConnection();
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/coto-chat",
                    "postgres",
                    "root"
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JDBCConnection() {}

    public static JDBCConnection getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}

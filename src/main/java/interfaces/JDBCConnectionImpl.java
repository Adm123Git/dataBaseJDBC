package interfaces;

import java.sql.*;
import java.util.Objects;

public class JDBCConnectionImpl implements JDBCConnection {

    private static final JDBCConnection instance = new JDBCConnectionImpl();
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

    private JDBCConnectionImpl() {}

    public static JDBCConnection getInstance() {
        return instance;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

}

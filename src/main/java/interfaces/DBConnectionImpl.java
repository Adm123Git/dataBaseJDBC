package interfaces;

import java.sql.*;
import java.util.Objects;

public class DBConnectionImpl implements DBConnection {

    private static final DBConnection instance = new DBConnectionImpl();
    private Connection connection = null;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DBConnectionImpl() {}

    public static DBConnection getInstance() {
        return instance;
    }

    private void createConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/coto-chat",
                "postgres",
                "root"
        );
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (Objects.isNull(connection)) {
            createConnection();
        }
        return connection;
    }

}

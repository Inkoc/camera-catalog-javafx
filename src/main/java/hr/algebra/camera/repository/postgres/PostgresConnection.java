package hr.algebra.camera.repository.postgres;

import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.utils.ConfigurationManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static PostgresConnection INSTANCE;

    private final String url;
    private final String username;
    private final String password;

    private PostgresConnection() {
        this.url = ConfigurationManager.getDbUrl();
        this.username = ConfigurationManager.getDbUsername();
        this.password = ConfigurationManager.getDbPassword();
    }

    public static synchronized PostgresConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostgresConnection();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to open database connection", e);
        }
    }
}

package hr.algebra.camera.repository.postgres;

import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.utils.ConfigurationManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    private static PostgresConnection instance;

    private final String url;
    private final String username;
    private final String password;

    private PostgresConnection() {
        this.url = ConfigurationManager.getDbUrl();
        this.username = ConfigurationManager.getDbUsername();
        this.password = ConfigurationManager.getDbPassword();
    }

    public static synchronized PostgresConnection getInstance() {
        if (instance == null) {
            instance = new PostgresConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to open database connection", e);
        }
    }
}

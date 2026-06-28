package hr.algebra.camera;

import hr.algebra.camera.auth.PasswordHasher;
import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.exception.DatabaseOperationException;
import hr.algebra.camera.repository.postgres.PostgresConnection;
import hr.algebra.camera.utils.ConfigurationManager;
import hr.algebra.camera.utils.ThreadManager;
import hr.algebra.camera.utils.ViewManager;
import hr.algebra.camera.utils.XmlActionLogger;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CameraApplication extends Application {
    @Override
    public void start(Stage stage) {
        ConfigurationManager.load();
        EventBus.getInstance().subscribe(XmlActionLogger.getInstance());
        seedDefaultAdmin();
        ViewManager.init(stage);
        ViewManager.switchTo("login.fxml", ConfigurationManager.getWindowTitle());
    }

    @Override
    public void stop() throws Exception {
        ThreadManager.shutdown();
    }

    private void seedDefaultAdmin() {
        String hash = PasswordHasher.hash("admin");
        try (Connection connection = PostgresConnection.getInstance().getConnection();
             CallableStatement stmt = connection.prepareCall("CALL create_admin(?, ?)")) {
            stmt.setString(1, "admin");
            stmt.setString(2, hash);
            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to seed default admin", e);
        }
    }
}

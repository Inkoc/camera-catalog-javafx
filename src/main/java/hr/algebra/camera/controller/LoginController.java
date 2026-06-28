package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.exception.AuthenticationException;
import hr.algebra.camera.model.User;
import hr.algebra.camera.service.XmlImportService;
import hr.algebra.camera.service.interfaces.IAuthService;
import hr.algebra.camera.utils.ConfigurationManager;
import hr.algebra.camera.utils.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final IAuthService authService;

    public LoginController(IAuthService authService) {
        this.authService = authService;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = authService.login(username, password);
            EventBus.getInstance().publish(new DataChangedEvent(user.getId(), "AUTH", "LOGIN"));
            LOGGER.info(() -> "Successful login: " + username);

            ViewManager.switchTo(
                    "main.fxml",
                    ConfigurationManager.getWindowTitle()
            );

        } catch (AuthenticationException e) {
            LOGGER.info(() -> "Failed login attempt for user: " + username);
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackToRegister(ActionEvent actionEvent) {
        ViewManager.switchTo("register.fxml", ConfigurationManager.getWindowTitle());
    }
}

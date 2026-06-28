package hr.algebra.camera.controller;

import hr.algebra.camera.exception.AuthenticationException;
import hr.algebra.camera.service.interfaces.IAuthService;
import hr.algebra.camera.utils.ConfigurationManager;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class RegisterController {
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());

    @FXML public PasswordField confirmPasswordField;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public Label errorLabel;

    private final IAuthService authService;

    public RegisterController(IAuthService authService, IAuthService authService1) {
        this.authService = authService1;
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (password == null || !password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }
        try {
            authService.register(username, password);
            LOGGER.info(() -> "Registered new user: " + username);
            DialogUtils.info("Success", "Account created. You can now log in.");
            ViewManager.switchTo("login.fxml", ConfigurationManager.getWindowTitle());
        } catch (AuthenticationException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleBackToLogin(ActionEvent actionEvent) {
        ViewManager.switchTo("login.fxml", ConfigurationManager.getWindowTitle());
    }
}

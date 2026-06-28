package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.exception.AuthenticationException;
import hr.algebra.camera.model.User;
import hr.algebra.camera.service.interfaces.IAuthService;
import hr.algebra.camera.utils.ConfigurationManager;
import hr.algebra.camera.utils.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final IAuthService authService;

    public LoginController(IAuthService authService) {
        this.authService = authService;
    }

    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = authService.login(username, password);
            EventBus.getInstance().publish(new DataChangedEvent(user.getId(), "AUTH", "LOGIN"));
            System.out.println("Successful login: " + user.getName());

            ViewManager.switchTo(
                    "main.fxml",
                    ConfigurationManager.getWindowTitle()
            );

        } catch (AuthenticationException e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

}

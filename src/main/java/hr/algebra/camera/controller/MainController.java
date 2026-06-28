package hr.algebra.camera.controller;

import hr.algebra.camera.auth.SessionManager;
import hr.algebra.camera.utils.ConfigurationManager;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane rootPane;

    public MainController() { }

    @FXML
    public void handleCameras(ActionEvent actionEvent) {
        rootPane.setCenter(ViewManager.load("camera.fxml"));
    }

    @FXML
    public void handleLenses(ActionEvent actionEvent) {
        rootPane.setCenter(ViewManager.load("lens.fxml"));
    }

    @FXML
    public void handleAdmin(ActionEvent actionEvent) {
        if (!SessionManager.getInstance().isAdmin()) {
            DialogUtils.warn("Forbidden", "Only administrators can access this view.");
            return;
        }
        rootPane.setCenter(ViewManager.load("admin.fxml"));
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        SessionManager.getInstance().logout();

        ViewManager.switchTo(
                "login.fxml",
                ConfigurationManager.getWindowTitle()
        );
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

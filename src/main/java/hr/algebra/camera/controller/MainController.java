package hr.algebra.camera.controller;

import hr.algebra.camera.auth.SessionManager;
import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.ActionType;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.event.events.EntityType;
import hr.algebra.camera.utils.ConfigurationManager;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane rootPane;

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
        EventBus.getInstance().publish(new DataChangedEvent(0, EntityType.AUTH, ActionType.LOGOUT));
        ViewManager.switchTo(
                "login.fxml",
                ConfigurationManager.getWindowTitle()
        );
    }
}

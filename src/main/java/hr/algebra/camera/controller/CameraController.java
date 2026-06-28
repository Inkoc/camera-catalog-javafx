package hr.algebra.camera.controller;

import hr.algebra.camera.model.Camera;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.utils.TableColumnFactory;
import hr.algebra.camera.utils.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class CameraController {
    @FXML private TableView<Camera> cameraTable;

    private final ICameraService cameraService;
    private final ObservableList<Camera> cameraList = FXCollections.observableArrayList();

    public CameraController(ICameraService cameraService) {
        this.cameraService = cameraService;
    }

    @FXML
    public void initialize() {
        setupColumns();
        loadCameras();
    }

    private void setupColumns() {
        cameraTable.getColumns().setAll(
                TableColumnFactory.create("Name", 220, Camera::getName),
                TableColumnFactory.create("Type", 130, c -> c.getCameraType().getDisplayName()),
                TableColumnFactory.create("Brand", 140, c -> c.getBrand() != null ? c.getBrand().getName() : "-"),
                TableColumnFactory.create("Release Year", 100, Camera::getReleaseYear),
                TableColumnFactory.create("Price (€)", 100, Camera::getPrice)
        );
    }

    private void loadCameras() {
        cameraList.setAll(cameraService.findAll());
        cameraTable.setItems(cameraList);
    }

    public void handleAddCamera(ActionEvent actionEvent) {
        openForm(null);
    }

    public void handleEditCamera(ActionEvent actionEvent) {
        Camera selected = cameraTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a camera from the table to edit.");
            return;
        }

        openForm(selected);
    }

    public void handleDeleteCamera(ActionEvent actionEvent) {
        Camera selected = cameraTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a camera from the table to delete.");
            return;
        }

        try {
            cameraService.deleteById(selected.getId());
            loadCameras();
        } catch (Exception e) {
            showAlert("Error", "Could not delete camera: " + e.getMessage());
        }
    }

    private void openForm(Camera camera) {
        CameraFormController formController = ViewManager.openModal(
                "camera-form.fxml",
                camera == null ? "Add Camera" : "Edit Camera",
                (CameraFormController c) -> c.setCamera(camera)
        );

        if (formController.isSaved()) {
            loadCameras();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleAttachLens(ActionEvent actionEvent) {
        Camera selected = cameraTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a camera to attach lenses to.");
            return;
        }
        ViewManager.openModal(
                "lens-attach.fxml",
                "Attach Lenses",
                (LensAttachController c) -> c.setCamera(selected)
        );
    }
}

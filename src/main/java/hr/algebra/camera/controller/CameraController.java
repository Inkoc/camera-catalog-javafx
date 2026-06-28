package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.EventListener;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.enums.CameraType;
import hr.algebra.camera.model.enums.Purpose;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.utils.ImageStorage;
import hr.algebra.camera.utils.TableColumnFactory;
import hr.algebra.camera.utils.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.function.Predicate;

public class CameraController {
    @FXML private TextField searchField;
    @FXML private ComboBox<CameraType> typeFilter;
    @FXML private ComboBox<Purpose> purposeFilter;
    @FXML private ImageView imagePreview;
    @FXML private TableView<Camera> cameraTable;

    private final ICameraService cameraService;
    private final ObservableList<Camera> cameraList = FXCollections.observableArrayList();

    public CameraController(ICameraService cameraService) {
        this.cameraService = cameraService;
    }

    @FXML
    public void initialize() {
        setupColumns();
        typeFilter.getItems().setAll(CameraType.values());
        purposeFilter.getItems().setAll(Purpose.values());

        searchField.textProperty().addListener((o, a, b) -> applyFilter());
        typeFilter.valueProperty().addListener((o, a, b) -> applyFilter());
        purposeFilter.valueProperty().addListener((o, a, b) -> applyFilter());
        applyFilter();

        cameraTable.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) ->
                imagePreview.setImage(sel == null ? null : ImageStorage.load(sel.getImagePath())));

        EventListener eventListener = dataChangedEvent -> {
            if ("CAMERA".equals(dataChangedEvent.getEntityType())) applyFilter();
        };
        EventBus.getInstance().subscribe(eventListener);

        cameraTable.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene == null) EventBus.getInstance().unsubscribe(eventListener);
        });
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

    private void applyFilter() {
        String q = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        CameraType type = typeFilter.getValue();
        Purpose purpose = purposeFilter.getValue();
        Predicate<Camera> predicate = c ->
                (q.isEmpty() || c.getName().toLowerCase().contains(q))
                        && (type == null || c.getCameraType() == type)
                        && (purpose == null || c.getPurpose() == purpose);
        cameraList.setAll(cameraService.filterCameras(predicate));
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
            ImageStorage.delete(selected.getImagePath());
            EventBus.getInstance().publish(new DataChangedEvent(selected.getId(), "CAMERA", "DELETE"));
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
            EventBus.getInstance().publish(new DataChangedEvent(0, "CAMERA", "SAVE"));
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

    public void handleClearFilters(ActionEvent actionEvent) {
        searchField.clear();
        typeFilter.setValue(null);
        purposeFilter.setValue(null);
        applyFilter();
    }
}

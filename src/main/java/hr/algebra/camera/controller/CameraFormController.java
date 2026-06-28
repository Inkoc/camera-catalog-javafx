package hr.algebra.camera.controller;

import hr.algebra.camera.model.Brand;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.enums.CameraType;
import hr.algebra.camera.model.enums.Purpose;
import hr.algebra.camera.service.interfaces.IBrandService;
import hr.algebra.camera.service.interfaces.ICameraService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class CameraFormController {

    @FXML private TextField nameField;
    @FXML private TextField releaseYearField;
    @FXML private TextField megapixelsField;
    @FXML private TextField sensorTypeField;
    @FXML private TextField isoRangeField;
    @FXML private TextField maxShootingSpeedField;
    @FXML private TextField priceField;
    @FXML private TextField imagePathField;
    @FXML private ComboBox<Brand> brandComboBox;
    @FXML private ComboBox<CameraType> typeComboBox;
    @FXML private ComboBox<Purpose> purposeComboBox;
    @FXML private Label errorLabel;

    private final ICameraService cameraService;
    private final IBrandService brandService;

    private Camera editing;
    private boolean saved = false;

    public CameraFormController(ICameraService cameraService, IBrandService brandService) {
        this.cameraService = cameraService;
        this.brandService = brandService;
    }

    @FXML
    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(CameraType.values()));
        purposeComboBox.setItems(FXCollections.observableArrayList(Purpose.values()));
        brandComboBox.setItems(FXCollections.observableArrayList(brandService.findAll()));
        brandComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Brand brand) {
                return brand == null ? "" : brand.getName();
            }

            @Override
            public Brand fromString(String text) {
                return null;
            }
        });
    }

    public void setCamera(Camera camera) {
        this.editing = camera;
        if (camera == null) {
            return;
        }
        nameField.setText(camera.getName());
        releaseYearField.setText(String.valueOf(camera.getReleaseYear()));
        megapixelsField.setText(String.valueOf(camera.getMegapixels()));
        sensorTypeField.setText(camera.getSensorType());
        isoRangeField.setText(camera.getIsoRange());
        maxShootingSpeedField.setText(String.valueOf(camera.getMaxShootingSpeed()));
        priceField.setText(String.valueOf(camera.getPrice()));
        imagePathField.setText(camera.getImagePath());
        brandComboBox.setValue(camera.getBrand());
        typeComboBox.setValue(camera.getCameraType());
        purposeComboBox.setValue(camera.getPurpose());
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    public void handleSave() {
        if (typeComboBox.getValue() == null || purposeComboBox.getValue() == null) {
            errorLabel.setText("Type and purpose are required.");
            return;
        }
        try {
            Camera camera = buildFromForm();
            if (editing == null) {
                cameraService.save(camera);
            } else {
                cameraService.update(camera);
            }
            saved = true;
            close();
        } catch (NumberFormatException e) {
            errorLabel.setText("Year, megapixels, speed and price must be valid numbers.");
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        close();
    }

    private Camera buildFromForm() {
        int id = (editing == null) ? 0 : editing.getId();
        return new Camera(
                id,
                nameField.getText(),
                Integer.parseInt(releaseYearField.getText().trim()),
                Double.parseDouble(megapixelsField.getText().trim()),
                sensorTypeField.getText(),
                isoRangeField.getText(),
                Integer.parseInt(maxShootingSpeedField.getText().trim()),
                Double.parseDouble(priceField.getText().trim()),
                imagePathField.getText(),
                brandComboBox.getValue(),
                typeComboBox.getValue(),
                purposeComboBox.getValue(),
                (editing == null) ? new ArrayList<>() : editing.getCompatibleLenses()
        );
    }

    private void close() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}

package hr.algebra.camera.controller;

import hr.algebra.camera.model.Brand;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.enums.CameraType;
import hr.algebra.camera.model.enums.Purpose;
import hr.algebra.camera.service.interfaces.IBrandService;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.utils.ImageStorage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CameraFormController {
    private static final Logger LOGGER = Logger.getLogger(CameraFormController.class.getName());

    @FXML private TextField nameField;
    @FXML private TextField releaseYearField;
    @FXML private TextField megapixelsField;
    @FXML private TextField sensorTypeField;
    @FXML private TextField isoRangeField;
    @FXML private TextField maxShootingSpeedField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Brand> brandComboBox;
    @FXML private ComboBox<CameraType> typeComboBox;
    @FXML private ComboBox<Purpose> purposeComboBox;
    @FXML private Label errorLabel;
    @FXML private Label imageNameLabel;
    @FXML private ImageView imagePreview;

    private final ICameraService cameraService;
    private final IBrandService brandService;

    private Camera editing;
    private boolean saved = false;
    private String imagePath;
    private File pickedImage;

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
        brandComboBox.setValue(camera.getBrand());
        typeComboBox.setValue(camera.getCameraType());
        purposeComboBox.setValue(camera.getPurpose());
        imagePath = camera.getImagePath();
        imagePreview.setImage(ImageStorage.load(imagePath));
        imageNameLabel.setText(imagePath == null ? "(none)" : "stored image"); //TODO check
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
            boolean isNew = (editing == null);
            int id = isNew ? cameraService.save(camera) : editing.getId();

            if (pickedImage != null) {
                ImageStorage.delete(camera.getImagePath());
                String filename = ImageStorage.store(pickedImage, id);
                camera.setId(id);
                camera.setImagePath(filename);
                cameraService.update(camera);
            } else if (!isNew) {
                cameraService.update(camera);
            }
            saved = true;
            close();
        } catch (NumberFormatException e) {
            errorLabel.setText("Year, megapixels, speed and price must be valid numbers.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to save camera", e);
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
                imagePath,
                brandComboBox.getValue(),
                typeComboBox.getValue(),
                purposeComboBox.getValue(),
                (editing == null) ? new ArrayList<>() : editing.getCompatibleLenses()
        );
    }

    private void close() {
        ((Stage) nameField.getScene().getWindow()).close();
    }

    @FXML
    public void handleChooseImage(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fc.showOpenDialog(nameField.getScene().getWindow());
        if (file != null) {
            pickedImage = file;
            imageNameLabel.setText(file.getName());
            imagePreview.setImage(new Image(file.toURI().toString()));
        }
    }
}

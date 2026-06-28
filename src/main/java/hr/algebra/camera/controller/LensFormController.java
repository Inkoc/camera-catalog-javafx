package hr.algebra.camera.controller;

import hr.algebra.camera.model.Lens;
import hr.algebra.camera.service.interfaces.ILensService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LensFormController {
    private static final Logger LOGGER = Logger.getLogger(LensFormController.class.getName());

    @FXML private TextField nameField;
    @FXML private TextField focalLengthField;
    @FXML private TextField apertureField;
    @FXML private TextField mountTypeField;
    @FXML private TextField priceField;
    @FXML private Label errorLabel;

    private final ILensService lensService;
    private Lens editing;
    private boolean saved = false;

    public LensFormController(ILensService lensService) {
        this.lensService = lensService;
    }

    public void setLens(Lens lens) {
        this.editing = lens;

        if (lens == null) {
            return;
        }

        nameField.setText(lens.getName());
        focalLengthField.setText(String.valueOf(lens.getFocalLength()));
        apertureField.setText(String.valueOf(lens.getAperture()));
        mountTypeField.setText(lens.getMountType());
        priceField.setText(String.valueOf(lens.getPrice()));
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    public void handleSave() {
        try {
            Lens lens = buildFromForm();
            if (editing == null) {
                lensService.save(lens);
            } else {
                lensService.update(lens);
            }
            saved = true;
            close();
        } catch (NumberFormatException e) {
            errorLabel.setText("Focal length, aperture and price must be valid numbers.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to save lens", e);
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        close();
    }

    private Lens buildFromForm() {
        int id = (editing == null) ? 0 : editing.getId();
        return new Lens(
                id,
                nameField.getText(),
                Integer.parseInt(focalLengthField.getText().trim()),
                Double.parseDouble(apertureField.getText().trim()),
                mountTypeField.getText(),
                Double.parseDouble(priceField.getText().trim())
        );
    }

    private void close() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}

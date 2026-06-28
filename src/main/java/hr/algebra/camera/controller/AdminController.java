package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.ActionType;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.event.events.EntityType;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.service.interfaces.IXmlImportService;
import hr.algebra.camera.service.interfaces.IXmlExportService;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.ImageStorage;
import hr.algebra.camera.utils.ThreadManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {
    private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());

    @FXML private Label statusLabel;
    @FXML private Button importButton;
    @FXML public Button exportButton;

    private final IXmlImportService importService;
    private final IXmlExportService exportService;
    private final ICameraService cameraService;

    public AdminController(IXmlImportService importService, IXmlExportService exportService, ICameraService cameraService, ICameraService cameraService1) {
        this.importService = importService;
        this.exportService = exportService;
        this.cameraService = cameraService1;
    }

    @FXML
    private void handleImport(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File file = fc.showOpenDialog(statusLabel.getScene().getWindow());
        if (file == null) return;

        importButton.setDisable(true);
        statusLabel.setText("Importing…");

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                return importService.importFromFile(file.toPath());
            }
        };

        task.setOnSucceeded(e -> {
            statusLabel.setText("Imported " + task.getValue() + " new cameras.");
            importButton.setDisable(false);
            EventBus.getInstance().publish(new DataChangedEvent(0, EntityType.CAMERA, ActionType.IMPORT));
            LOGGER.info(() -> "Imported " + task.getValue() + " cameras");
        });
        task.setOnFailed(e -> {
            statusLabel.setText("Import failed.");
            importButton.setDisable(false);
            LOGGER.log(Level.SEVERE, "Import failed", task.getException());
        });

        ThreadManager.run(task);
    }

    @FXML
    public void handleExport(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("camera-catalog.xml");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File file = fc.showSaveDialog(statusLabel.getScene().getWindow());
        if (file == null) return;

        statusLabel.setText("Exporting…");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                exportService.exportCatalog(file.toPath());
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            statusLabel.setText("Exported to " + file.getName());
            LOGGER.info(() -> "Exported catalog to " + file.getName());
        });
        task.setOnFailed(e -> {
            statusLabel.setText("Export failed.");
            LOGGER.log(Level.SEVERE, "Export failed", task.getException());
        });

        ThreadManager.run(task);
    }

    @FXML
    private void handleClear() {
        boolean confirmed = DialogUtils.confirm(
                "Clear catalog",
                "This permanently deletes all cameras, lenses and brands. Continue?"
        );
        if (!confirmed) return;
        statusLabel.setText("Clearing…");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                cameraService.clearCatalog();
                ImageStorage.clearAll();
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            statusLabel.setText("Catalog cleared.");
            EventBus.getInstance().publish(new DataChangedEvent(0, EntityType.CAMERA, ActionType.CLEAR));
        });
        task.setOnFailed(e -> {
            statusLabel.setText("Clear failed.");
            DialogUtils.error("Clear failed", String.valueOf(task.getException().getMessage()));
        });

        ThreadManager.run(task);
    }
}

package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.service.interfaces.IDataImportService;
import hr.algebra.camera.service.interfaces.IXmlExportService;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.ThreadManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminController {
    // TODO: temp solution, implement input field
    private static final String CATALOG_URL = "https://raw.githubusercontent.com/USER/REPO/main/catalog.xml";

    @FXML private Label statusLabel;
    @FXML private Button importButton;

    private final IDataImportService importService;
    private final IXmlExportService exportService;

    public AdminController(IDataImportService importService, IXmlExportService exportService) {
        this.importService = importService;
        this.exportService = exportService;
    }

    @FXML
    private void handleImport() {
        importButton.setDisable(true);
        statusLabel.setText("Importing…");

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                return importService.importFromUrl(CATALOG_URL);
            }
        };
        task.setOnSucceeded(e -> {
            statusLabel.setText("Imported " + task.getValue() + " new cameras.");
            importButton.setDisable(false);
            EventBus.getInstance().publish(new DataChangedEvent(0, "CAMERA", "IMPORT"));
        });
        task.setOnFailed(e -> {
            statusLabel.setText("Import failed.");
            importButton.setDisable(false);
            DialogUtils.error("Import failed", String.valueOf(task.getException().getMessage()));
        });

        ThreadManager.run(task);
    }

    public void handleExport(ActionEvent actionEvent) {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setInitialFileName("camera-catalog.xml");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("XML", "*.xml"));
        java.io.File file = fc.showSaveDialog(statusLabel.getScene().getWindow());
        if (file == null) return;
        statusLabel.setText("Exporting…");
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                exportService.exportCatalog(file.toPath());
                return null;
            }
        };
        task.setOnSucceeded(e -> statusLabel.setText("Exported to " + file.getName()));
        task.setOnFailed(e -> {
            statusLabel.setText("Export failed.");
            DialogUtils.error("Export failed", String.valueOf(task.getException().getMessage()));
        });
        ThreadManager.run(task);
    }
}

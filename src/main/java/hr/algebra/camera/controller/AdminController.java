package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.service.interfaces.IDataImportService;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.ThreadManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminController {
    // TODO: temp solution, implement input field
    private static final String CATALOG_URL = "https://raw.githubusercontent.com/USER/REPO/main/catalog.xml";

    @FXML private Label statusLabel;
    @FXML private Button importButton;
    private final IDataImportService importService;

    public AdminController(IDataImportService importService) {
        this.importService = importService;
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
}

package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.EventListener;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.service.interfaces.ILensService;
import hr.algebra.camera.utils.DialogUtils;
import hr.algebra.camera.utils.TableColumnFactory;
import hr.algebra.camera.utils.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LensController {
    private static final Logger LOGGER = Logger.getLogger(LensController.class.getName());

    @FXML private TableView<Lens> lensTable;

    private final ILensService lensService;
    private final ObservableList<Lens> lensList = FXCollections.observableArrayList();

    public LensController(ILensService lensService) {
        this.lensService = lensService;
    }

    @FXML
    public void initialize() {
        setupColumns();
        loadLenses();

        EventListener eventListener = dataChangedEvent -> {
            if ("LENS".equals(dataChangedEvent.getEntityType())) loadLenses();
        };
        EventBus.getInstance().subscribe(eventListener);

        lensTable.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene == null) EventBus.getInstance().unsubscribe(eventListener);
        });
    }

    private void setupColumns() {
        lensTable.getColumns().setAll(
                TableColumnFactory.create("Name", 220, Lens::getName),
                TableColumnFactory.create("Focal Length (mm)", 130, Lens::getFocalLength),
                TableColumnFactory.create("Aperture (f/)", 110, Lens::getAperture),
                TableColumnFactory.create("Mount", 100, Lens::getMountType),
                TableColumnFactory.create("Price (€)", 100, Lens::getPrice)
        );
    }

    private void loadLenses() {
        lensList.setAll(lensService.findAll());
        lensTable.setItems(lensList);
    }

    @FXML
    public void handleAddLens(ActionEvent actionEvent) {
        openForm(null);
    }

    @FXML
    public void handleEditLens(ActionEvent actionEvent) {
        Lens selected = lensTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            DialogUtils.info("No Selection", "Please select lens from the table to edit.");
            return;
        }

        openForm(selected);
    }

    @FXML
    public void handleDeleteLens(ActionEvent actionEvent) {
        Lens selected = lensTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            DialogUtils.info("No Selection", "Please select lens from the table to delete.");
            return;
        }

        try {
            lensService.deleteById(selected.getId());
            EventBus.getInstance().publish(new DataChangedEvent(selected.getId(), "LENS", "DELETE"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete lens", e);
            DialogUtils.error("Error", "Could not delete lens: " + e.getMessage());
        }
    }
    private void openForm(Lens lens) {
        LensFormController formController = ViewManager.openModal(
                "lens-form.fxml",
                lens == null ? "Add Lens" : "Edit Lens",
                (LensFormController c) -> c.setLens(lens)
        );

        if (formController.isSaved()) {
            EventBus.getInstance().publish(new DataChangedEvent(0, "LENS", "SAVE"));
        }
    }
}

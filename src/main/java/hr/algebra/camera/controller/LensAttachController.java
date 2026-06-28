package hr.algebra.camera.controller;

import hr.algebra.camera.event.EventBus;
import hr.algebra.camera.event.events.DataChangedEvent;
import hr.algebra.camera.model.Camera;
import hr.algebra.camera.model.Lens;
import hr.algebra.camera.service.interfaces.ICameraService;
import hr.algebra.camera.service.interfaces.ILensService;
import hr.algebra.camera.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LensAttachController {
    private static final Logger LOGGER = Logger.getLogger(LensAttachController.class.getName());

    @FXML public Label titleLabel;
    @FXML public ListView availableList;
    @FXML public ListView attachedList;

    private Camera camera;
    private final ICameraService cameraService;
    private final ILensService lensService;
    private final ObservableList<Lens> available = FXCollections.observableArrayList();
    private final ObservableList<Lens> attached  = FXCollections.observableArrayList();

    public LensAttachController(ICameraService cameraService, ILensService lensService) {
        this.cameraService = cameraService;
        this.lensService = lensService;
    }

    @FXML
    public void initialize() {
        availableList.setItems(available);
        attachedList.setItems(attached);

        configureDragSource(availableList);
        configureDragSource(attachedList);

        configureDropTarget(attachedList, true);
        configureDropTarget(availableList, false);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;

        titleLabel.setText("Lenses for: " + camera.getName());
        reload();
    }

    private void reload() {
        List<Lens> attachedLenses = cameraService.findLensesForCamera(camera.getId());

        Set<Integer> attachedIds = attachedLenses.stream()
                .map(Lens::getId)
                .collect(Collectors.toSet());

        attached.setAll(attachedLenses);

        available.setAll(lensService.findAll().stream()
                .filter(l -> !attachedIds.contains(l.getId()))
                .toList());
    }

    private void configureDragSource(ListView<Lens> list) {
        list.setCellFactory(lv -> {
            ListCell<Lens> cell = new ListCell<>() {
                @Override
                protected void updateItem(Lens lens, boolean empty) {
                    super.updateItem(lens, empty);
                    setText(empty || lens == null ? null : lens.getName());
                }
            };

            cell.setOnDragDetected(e -> {
                if (cell.getItem() == null) return;
                Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(cell.getItem().getId()));
                db.setContent(content);
                e.consume();
            });

            return cell;
        });
    }

    private void configureDropTarget(ListView<Lens> target, boolean attach) {
        target.setOnDragOver(event -> {
            boolean fromOtherList = event.getGestureSource() instanceof ListCell<?> c
                    && c.getListView() != target;
            if (fromOtherList && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        target.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int lensId = Integer.parseInt(db.getString());
                try {
                    if (attach) {
                        cameraService.attachLens(camera.getId(), lensId);
                    } else {
                        cameraService.detachLens(camera.getId(), lensId);
                    }
                    reload();
                    EventBus.getInstance().publish(new DataChangedEvent(camera.getId(), "CAMERA", attach ? "ATTACH" : "DETACH"));
                    success = true;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to attach/detach lens", e);
                    DialogUtils.error("Error", e.getMessage());
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    public void handleClose() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }
}

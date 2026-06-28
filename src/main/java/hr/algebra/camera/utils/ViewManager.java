package hr.algebra.camera.utils;

import hr.algebra.camera.controller.ControllerFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public final class ViewManager {
    private static final String VIEWS_PATH = "/hr/algebra/camera/view/";
    private static final ControllerFactory FACTORY = new ControllerFactory();
    private static Stage rootStage;

    private ViewManager() {}

    public static void init(Stage stage) {
        rootStage = stage;
    }

    public static void switchTo(String fxmlName, String title) {
        ensureInitialized();

        Scene scene = new Scene(
                load(fxmlName),
                ConfigurationManager.getWindowWidth(),
                ConfigurationManager.getWindowHeight()
        );

        rootStage.setTitle(title);
        rootStage.setScene(scene);
        rootStage.show();
    }

    public static <C> C openModal(String fxmlName, String title) {
        return openModal(fxmlName, title, null);
    }

    public static <C> C openModal(String fxmlName, String title, Consumer<C> beforeShow) {
        ensureInitialized();

        FXMLLoader loader = newLoader(fxmlName);
        try {
            Parent root = loader.load();
            C controller = loader.getController();

            if (beforeShow != null) {
                beforeShow.accept(controller);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(rootStage);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            return controller;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load view: " + fxmlName, e);
        }
    }

    public static Parent load(String fxmlName) {
        ensureInitialized();

        try {
            return newLoader(fxmlName).load();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load view: " + fxmlName, e);
        }
    }

    private static FXMLLoader newLoader(String fxmlName) {
        URL url = ViewManager.class.getResource(VIEWS_PATH + fxmlName);
        if (url == null) {
            throw new IllegalStateException("FXML not found: " + VIEWS_PATH + fxmlName);
        }

        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(FACTORY);

        return loader;
    }

    private static void ensureInitialized() {
        if (rootStage == null) {
            throw new IllegalStateException("ViewManager was never initialized");
        }
    }




}

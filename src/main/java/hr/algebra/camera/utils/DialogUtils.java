package hr.algebra.camera.utils;

import javafx.scene.control.Alert;

public final class DialogUtils {
    private DialogUtils() {}

    public static void info(String title, String msg) {
        show(Alert.AlertType.INFORMATION, title, msg);
    }

    public static void warn(String title, String msg) {
        show(Alert.AlertType.WARNING, title, msg);
    }

    public static void error(String title, String msg) {
        show(Alert.AlertType.ERROR, title, msg);
    }

    private static void show(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

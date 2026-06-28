package hr.algebra.camera.utils;


import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;

import java.util.function.Function;

public final class TableColumnFactory {
    private  TableColumnFactory() {}

    public static <S, T> TableColumn<S, T> create(String title, Function<S, T> extractor) {
        TableColumn<S, T> column = new TableColumn<>(title);

        column.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(extractor.apply(cell.getValue())));

        return column;
    }

    public static <S, T> TableColumn<S, T> create(String title, double prefWidth, Function<S, T> extractor) {
        TableColumn<S, T> column = create(title, extractor);

        column.setPrefWidth(prefWidth);

        return column;
    }
}
